package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reforme.reforme.dto.BoardEditDto;
import reforme.reforme.dto.BoardDto;
import reforme.reforme.dto.ResponseBody;
import reforme.reforme.entity.*;
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
@RequiredArgsConstructor
public class BoardService {

    private final ReformeRepository reformeRepository;
    private final ReforyouRepository reforyouRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    //-----------------------게시글 수정, 검색, 삭제-----------------------//
    public ResponseBody<?> createReformeBoard(BoardEditDto boardEditDto, MultipartFile[] images, Authentication auth) {
        //필요하면 매개변수에 Authentication auth를 추가해서 로그인 정보를 받아옴녀 좋을 것 같은데..
        //auth.getUserId 혹은 auth.getId해서 가져오면 될듯
        //단, 이렇게 할라면 프론트가 확보되어야 확인 할 수 있을듯. postman 요청으로 로그인 정보를 보낼 수 없음.
        //그리고 내가 따로 auth를 추가적으로 구현해야하는 부분이 있는지 알아봐야함
        String userId = auth.getName();
        if (userId == null || userId.trim().isEmpty()) {
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "UserId가 누락되었습니다");
        }
        List<User> user = userRepository.findById(userId);
        //이부분. 오류 날수 도 있음.
        if (user.isEmpty()) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다");
        }

        //이부분도 오류 날 수 있음.
        User userEntity = user.get(0);
        List<Image> savedImages = saveImages(images);

        //캐스팅하면 오류 많이 생긴다고해서 그냥 이렇게 따로 만듬. 뭔가 구리긴함.
        try {
            Reforme reformeBoard = createReforme(boardEditDto, userEntity, savedImages);
            reformeRepository.save(reformeBoard);
            return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            return new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "게시글 생성 중 오류가 발생하였습니다");
        }
    }

    public ResponseBody<?> createReforyouBoard(BoardEditDto boardEditDto, MultipartFile[] images, Authentication auth) {
        //필요하면 매개변수에 Authentication auth를 추가해서 로그인 정보를 받아옴녀 좋을 것 같은데..
        //auth.getUserId 혹은 auth.getId해서 가져오면 될듯
        //단, 이렇게 할라면 프론트가 확보되어야 확인 할 수 있을듯. postman 요청으로 로그인 정보를 보낼 수 없음.
        //그리고 내가 따로 auth를 추가적으로 구현해야하는 부분이 있는지 알아봐야함
        String userId = auth.getName();
        if (userId == null || userId.trim().isEmpty()) {
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "UserId가 누락되었습니다");
        }
        List<User> user = userRepository.findById(userId);
        //이부분. 오류 날수 도 있음.
        if (user.isEmpty()) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "사용자를 찾을 수 없습니다");
        }


        //이부분도 오류 날 수 있음.
        User userEntity = user.get(0);
        List<Image> savedImages = saveImages(images);

        //캐스팅하면 오류 많이 생긴다고해서 그냥 이렇게 따로 만듬. 뭔가 구리긴함.
        try {
            Reforyou reforyouBoard = createReforyou(boardEditDto, userEntity, savedImages);
            reforyouRepository.save(reforyouBoard);
            return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 생성되었습니다.");
        } catch (Exception e) {
            return new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "게시글 생성 중 오류가 발생하였습니다");
        }
    }

    private Reforyou createReforyou(BoardEditDto boardEditDto, User user, List<Image> images) {
        Reforyou reforyouBoard = new Reforyou();
        reforyouBoard.setTitle(boardEditDto.getTitle());
        reforyouBoard.setBody(boardEditDto.getBody());
        reforyouBoard.setCreatedDateTime(LocalDateTime.now());
        reforyouBoard.setModifiedDateTime(LocalDateTime.now());
        reforyouBoard.setUser(user);
        reforyouBoard.setCategory(YouCategory.valueOf(boardEditDto.getCategory()));
        reforyouBoard.setImages(images);
        return reforyouBoard;
    }

    private Reforme createReforme(BoardEditDto boardEditDto, User user, List<Image> images) {
        Reforme reformeBoard = new Reforme();
        reformeBoard.setTitle(boardEditDto.getTitle());
        reformeBoard.setBody(boardEditDto.getBody());
        reformeBoard.setCreatedDateTime(LocalDateTime.now());
        reformeBoard.setModifiedDateTime(LocalDateTime.now());
        reformeBoard.setUser(user);
        reformeBoard.setCategory(MeCategory.valueOf(boardEditDto.getCategory()));
        reformeBoard.setImages(images);
        return reformeBoard;
    }

    //게시판 업데이트
    public ResponseBody<?> updateReformeBoard(Long boardId, BoardEditDto boardEditDto, MultipartFile[] images) {
        // 게시글 정보 업데이트
        Reforme existingBoard = null;
        try {
            existingBoard = reformeRepository.findById(boardId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        }catch (Exception e) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

        existingBoard.setTitle(boardEditDto.getTitle());
        existingBoard.setBody(boardEditDto.getBody());
        existingBoard.setModifiedDateTime(LocalDateTime.now());

        // 기존 이미지 삭제 로직
        if (existingBoard.getImages() != null) {
            Reforme board = reformeRepository.findById(boardId).get();
            List<Image> reformeImages = board.getImages();
            reformeImages.clear();
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
        Reforme reformeBoard = (Reforme) existingBoard;
        reformeBoard.setCategory(MeCategory.valueOf(boardEditDto.getCategory()));
        reformeRepository.save(reformeBoard);

        return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 업데이트되었습니다.");
    }
    public ResponseBody<?> updateReforyouBoard(Long boardId, BoardEditDto boardEditDto, MultipartFile[] images) {
        // 게시글 정보 업데이트
        Reforyou existingBoard = null;
        try {
            existingBoard = reforyouRepository.findById(boardId).orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        }catch (Exception e) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }

        existingBoard.setTitle(boardEditDto.getTitle());
        existingBoard.setBody(boardEditDto.getBody());
        existingBoard.setModifiedDateTime(LocalDateTime.now());

        // 기존 이미지 삭제 로직
        if (existingBoard.getImages() != null) {
            Reforyou board = reforyouRepository.findById(boardId).get();
            List<Image> reforyouImages = board.getImages();
            reforyouImages.clear();
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
        Reforyou reforyouBoard = (Reforyou) existingBoard;
        reforyouBoard.setCategory(YouCategory.valueOf(boardEditDto.getCategory())); //기능에 맞는 카테고리 추가
        reforyouRepository.save(reforyouBoard);

        return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 업데이트되었습니다.");
    }

    //게시글 삭제
    public ResponseBody<?> deleteReformeBoard(Long boardId) {
        Optional<Reforme> board = reformeRepository.findById(boardId);

        if (!board.isPresent()) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "게시글이 존재하지 않습니다");
        }

        try {
            // 파일 시스템에서 이미지 파일들을 삭제
            board.get().getImages().forEach(image -> {
                Reforme meBoard = reformeRepository.findById(boardId).get();
                List<Image> reformeImages = meBoard.getImages();
                reformeImages.clear();
                try {
                    Path fileToDelete = Paths.get(image.getImagePath());
                    Files.deleteIfExists(fileToDelete);
                    imageRepository.delete(image); //이게 이미지 레포에서 이미지를 삭제해주는거겠지?
                } catch (IOException e) {
                    e.printStackTrace(); // 로그 남기기 또는 에러 처리
                }
            });

            reformeRepository.delete(board.get());
            return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 삭제되었습니다");
        } catch (Exception e) {
            // 로깅 또는 다른 에러 처리
            return new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 삭제 중 오류 발생");
        }
    }
    public ResponseBody<?> deleteReforyouBoard(Long boardId) {
        Optional<Reforyou> board = reforyouRepository.findById(boardId);
        if (!board.isPresent()) {
            return new ResponseBody<>(HttpStatus.NOT_FOUND.value(), "게시글이 존재하지 않습니다");
        }

        try {
            // 파일 시스템에서 이미지 파일들을 삭제
            board.get().getImages().forEach(image -> {
                Reforyou youBoard = reforyouRepository.findById(boardId).get();
                List<Image> reforyouImages = youBoard.getImages();
                reforyouImages.clear();
                try {
                    Path fileToDelete = Paths.get(image.getImagePath());
                    Files.deleteIfExists(fileToDelete);
                    imageRepository.delete(image); //이게 이미지 레포에서 이미지를 삭제해주는거겠지?
                } catch (IOException e) {
                    e.printStackTrace(); // 로그 남기기 또는 에러 처리
                }
            });

            reforyouRepository.delete(board.get());
            return new ResponseBody<>(HttpStatus.OK.value(), "게시글이 성공적으로 삭제되었습니다");
        } catch (Exception e) {
            // 로깅 또는 다른 에러 처리
            return new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "파일 삭제 중 오류 발생");
        }
    }
    //이미지 업로드 기능(완)
    private List<Image> saveImages(MultipartFile[] files) {
        List<Image> images = new ArrayList<>();
        String directoryPath = "/Users/player7571/Desktop/images";  // 이미지를 저장할 디렉토리 경로

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
    //-----------------------나머지-----------------------//

    public List<BoardDto> getReformeData(Page<Reforme> boards){
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Reforme reforme : boards.getContent()) {
            BoardDto boardDto = new BoardDto(reforme);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public List<BoardDto> getReforyouData(Page<Reforyou> boards){
        List<BoardDto> boardDtos = new ArrayList<>();
        for (Reforyou reforyou : boards.getContent()) {
            BoardDto boardDto = new BoardDto(reforyou);
            boardDtos.add(boardDto);
        }
        return boardDtos;
    }

    public BoardDto findByIdReformeBoard(Long id){
        Optional<Reforme> result = reformeRepository.findById(id);
        if(result.isPresent()){
            Reforme reforme = result.get();
            return new BoardDto(reforme);
        }
        return null;
    }

    public BoardDto findByIdReforyouBoard(Long id){
        Optional<Reforyou> result = reforyouRepository.findById(id);
        if(result.isPresent()){
            Reforyou reforyou = result.get();
            return new BoardDto(reforyou);
        }
        return null;
    }



}
