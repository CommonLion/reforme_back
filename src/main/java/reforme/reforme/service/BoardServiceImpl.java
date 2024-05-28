package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import reforme.reforme.dto.ResponseBody;
import reforme.reforme.entity.MeCategory;
import reforme.reforme.entity.YouCategory;
import reforme.reforme.entity.Image;
import reforme.reforme.entity.User;
import reforme.reforme.entity.board.Board;
import reforme.reforme.dto.BoardCreateDto;
import reforme.reforme.dto.BoardUpdateDto;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
import reforme.reforme.repository.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final UserRepository userRepository;

    private final ReforyouRepository reforyouRepository;
    private final ReformeRepository reformeRepository;
    private final ImageRepository imageRepository;

    @Override
    public ResponseBody<?> createBoard(BoardCreateDto boardCreateDto, MultipartFile[] images, String repositoryType) {
        //필요하면 매개변수에 Authentication auth를 추가해서 로그인 정보를 받아옴녀 좋을 것 같은데..
        //auth.getUserId 혹은 auth.getId해서 가져오면 될듯
        //단, 이렇게 할라면 프론트가 확보되어야 확인 할 수 있을듯. postman 요청으로 로그인 정보를 보낼 수 없음.
        //그리고 내가 따로 auth를 추가적으로 구현해야하는 부분이 있는지 알아봐야함
        String userId = boardCreateDto.getUserId();
        if (userId == null || userId.trim().isEmpty()) {
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "UserId가 누락되었습니다");
        }
        List<User> user = userRepository.findById(boardCreateDto.getUserId());
        //이부분. 오류 날수 도 있음.
        if (user.isEmpty()) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다");
        }

        //이부분도 오류 날 수 있음.
        User userEntity = user.get(0);
        List<Image> savedImages = saveImages(images);

        //캐스팅하면 오류 많이 생긴다고해서 그냥 이렇게 따로 만듬. 뭔가 구리긴함.
        try {
            if (repositoryType.equals("reforyou")) {
                Reforyou reforyouBoard = createReforyou(boardCreateDto, userEntity, savedImages);
                reforyouRepository.save(reforyouBoard);
            } else if (repositoryType.equals("reforme")) {
                Reforme reformeBoard = createReforme(boardCreateDto, userEntity, savedImages);
                reformeRepository.save(reformeBoard);
            } else {
                return new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Invalid repository type");
            }
            return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            return new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "게시글 생성 중 오류가 발생하였습니다");
        }
    }

    private Reforyou createReforyou(BoardCreateDto boardCreateDto, User user, List<Image> images) {
        Reforyou reforyouBoard = new Reforyou();
        reforyouBoard.setTitle(boardCreateDto.getTitle());
        reforyouBoard.setBody(boardCreateDto.getBody());
        reforyouBoard.setCreatedDateTime(LocalDateTime.now());
        reforyouBoard.setModifiedDateTime(LocalDateTime.now());
        reforyouBoard.setUser(user);
        reforyouBoard.setCategory(YouCategory.valueOf(boardCreateDto.getCategory()));
        reforyouBoard.setImages(images);
        return reforyouBoard;
    }

    private Reforme createReforme(BoardCreateDto boardCreateDto, User user, List<Image> images) {
        Reforme reformeBoard = new Reforme();
        reformeBoard.setTitle(boardCreateDto.getTitle());
        reformeBoard.setBody(boardCreateDto.getBody());
        reformeBoard.setCreatedDateTime(LocalDateTime.now());
        reformeBoard.setModifiedDateTime(LocalDateTime.now());
        reformeBoard.setUser(user);
        reformeBoard.setCategory(MeCategory.valueOf(boardCreateDto.getCategory()));
        reformeBoard.setImages(images);
        return reformeBoard;
    }

    //게시판 업데이트
    @Override
    public ResponseBody<?> updateBoard(Long boardId, BoardUpdateDto updateDto, MultipartFile[] images, String repositoryType) {
        // 게시글 정보 업데이트
        Board existingBoard = null;
        try {
            if (repositoryType.equals("reforyou")) {
                existingBoard = reforyouRepository.findById(boardId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
            } else if (repositoryType.equals("reforme")) {
                existingBoard = reformeRepository.findById(boardId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
            }
        }catch (Exception e) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

        existingBoard.setTitle(updateDto.getTitle());
        existingBoard.setBody(updateDto.getBody());
        existingBoard.setModifiedDateTime(LocalDateTime.now());

        // 기존 이미지 삭제 로직
        if (existingBoard.getImages() != null) {
            existingBoard.getImages().forEach(image -> {
                try {
                    Path fileToDelete = Paths.get(image.getImagePath());
                    Files.deleteIfExists(fileToDelete);
                    imageRepository.delete(image); // 이미지 데이터베이스에서도 삭제
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            existingBoard.getImages().clear(); // 이미지 리스트 초기화
        }
        // 새로운 이미지 추가
        List<Image> savedImages = saveImages(images);
        existingBoard.setImages(savedImages);

        //전체 저장.
        if (repositoryType.equals("reforyou")) {
            Reforyou reforyouBoard = (Reforyou) existingBoard;
            reforyouBoard.setCategory(YouCategory.valueOf(updateDto.getCategory())); //기능에 맞는 카테고리 추가
            reforyouRepository.save(reforyouBoard);
        } else if (repositoryType.equals("reforme")) {
            Reforme reformeBoard = (Reforme) existingBoard;
            reformeBoard.setCategory(MeCategory.valueOf(updateDto.getCategory()));
            reformeRepository.save(reformeBoard);
        }

        return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 업데이트되었습니다.");
    }

    //게시글 삭제
    @Override
    public ResponseBody<?> deleteBoard(Long boardId, String repositoryType) {
        Optional<Board> board = Optional.empty();
        if (repositoryType.equals("reforyou")) {
            board = reforyouRepository.findById(boardId);
        } else if (repositoryType.equals("reforme")) {
            board = reformeRepository.findById(boardId);
        } else {
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Invalid repository type");

        }
        if (!board.isPresent()) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "게시글이 존재하지 않습니다");
        }

        try {
            // 파일 시스템에서 이미지 파일들을 삭제
            board.get().getImages().forEach(image -> {
                try {
                    Path fileToDelete = Paths.get(image.getImagePath());
                    Files.deleteIfExists(fileToDelete);
                    imageRepository.delete(image); //이게 이미지 레포에서 이미지를 삭제해주는거겠지?
                } catch (IOException e) {
                    e.printStackTrace(); // 로그 남기기 또는 에러 처리
                }
            });

            if (repositoryType.equals("reforyou")) {
                reforyouRepository.delete(board.get());
            } else if (repositoryType.equals("reforme")) {
                reformeRepository.delete(board.get());
            }
            return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 삭제되었습니다");
        } catch (Exception e) {
            // 로깅 또는 다른 에러 처리
            return new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 삭제 중 오류 발생");
        }
    }
    //이미지 업로드 기능(완)
    private List<Image> saveImages(MultipartFile[] files) {
        List<Image> images = new ArrayList<>();
        String directoryPath = "C:/reforme/images";  // 이미지를 저장할 디렉토리 경로

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 파일 경로 생성 //저장되는 이미지의 자체에 중복이 없게 랜덤 UUID 삽입
                    String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(directoryPath + "/" + uniqueFileName);
                    // 파일을 디스크에 저장 // 이미 같은 이름의 파일 존재 할때 예외 처리
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    // 이미지 엔티티 생성 및 정보 설정
                    Image image = Image.builder()
                            .imagePath(filePath.toString())
                            .origImageName(file.getOriginalFilename())
                            .build();

                    images.add(image);
                    imageRepository.save(image);
                } catch (IOException e) {
                    e.printStackTrace();
                    
                }
            }
        }
        return images;
    }
}