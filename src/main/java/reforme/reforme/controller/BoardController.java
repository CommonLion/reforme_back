package reforme.reforme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reforme.reforme.dto.BoardEditDto;
import reforme.reforme.dto.ResponseBody;
import reforme.reforme.entity.MeCategory;
import reforme.reforme.entity.YouCategory;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
import reforme.reforme.repository.ReformeRepository;
import reforme.reforme.repository.ReforyouRepository;
import reforme.reforme.service.BoardService;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final ReformeRepository reformeRepository;
    private final ReforyouRepository reforyouRepository;


    //------------------------------------------게시글 작성, 수정, 삭제----------------------------------------//

    //이거는 작성 페이지 열기
    //그게 없음, 게시글 작성 페이지로 넘어가는 거에 대한 Mapping은 API명세서에도 언급이 없다는 듯.
    //그 챗봇 위에 +버튼 누르면 Mappinge되게 해야 함. 챗봇도 아직 주소가 안정해 져있긴함.
    @GetMapping("/board")
    public String showCreatePostPage() {
        return "postForm";
    }

    //이미지도 같이넘기려면 requestPart를 써야함 Body가 아니라
    @PostMapping("/reforyou/board")
    public ResponseEntity<ResponseBody<?>> save(@RequestPart("board") BoardEditDto boardEditDto,
                                                @RequestPart("images") MultipartFile[] images,
                                                Authentication auth) {
        ResponseBody<?> responseBody = boardService.createReforyouBoard(boardEditDto, images, auth);
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    //게시판 수정
    @PatchMapping("/reforyou/board/{id}")
    public ResponseEntity<ResponseBody<?>> update(@PathVariable Long id,
                                                  @RequestPart("board") BoardEditDto boardEditDto,
                                                  @RequestPart("images") MultipartFile[] images,
                                                  Authentication auth){
        ResponseBody<?> responseBody  = boardService.updateReforyouBoard(id, boardEditDto, images, auth);
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    //게시판 삭제
    @DeleteMapping("/reforyou/board/{boardId}")
    public ResponseEntity<ResponseBody<?>> remove(@PathVariable Long boardId, Authentication auth) {
        ResponseBody<?> responseBody = boardService.deleteReforyouBoard(boardId, auth);
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    //----------------------------reforme -----------------------//

    @PostMapping("/reforme/board")
    public ResponseEntity<ResponseBody<?>> reformesave(@RequestPart("board") BoardEditDto boardEditDto,
                                                       @RequestPart("images") MultipartFile[] images,
                                                       Authentication auth) {
        ResponseBody<?> responseBody = boardService.createReformeBoard(boardEditDto, images, auth);
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    @PatchMapping("/reforme/board/{id}")
    public ResponseEntity<ResponseBody<?>> reformeupdate(@PathVariable Long id,
                                                         @RequestPart("board") BoardEditDto boardEditDto,
                                                         @RequestPart("images") MultipartFile[] images,
                                                         Authentication auth){
        ResponseBody<?> responseBody = boardService.updateReformeBoard(id, boardEditDto, images, auth);
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    @DeleteMapping("/reforme/board/{boardId}")
    public ResponseEntity<ResponseBody<?>> reformeremove(@PathVariable Long boardId, Authentication auth) {
        ResponseBody<?> responseBody = boardService.deleteReformeBoard(boardId, auth);
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    //------------------------------나머지 board 기능----------------------------------//

    //리포미 메인화면
    @GetMapping("/reforme/ALL")
    public ResponseBody<?> getBoards(@RequestParam(defaultValue = "0") int page) {
        try {
            // 요청된 페이지와 사이즈에 해당하는 데이터를 가져와서 반환
            Page<Reforme> boards = reformeRepository.findAllByOrderByCreatedDateTimeDesc(PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReformeData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    //게시글 필터링(리포미)
    @GetMapping("/reforme/{category}")
    public ResponseBody<?> getBoardsByCategory(
            @PathVariable MeCategory category,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
            Page<Reforme> boards = reformeRepository.findByCategory(category, PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReformeData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    //게시글 검색(리포미)
    @GetMapping("/reforme/search/{searchWord}")
    public ResponseBody<?> getBoardsByMeSearch(
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
            Page<Reforme> boards = reformeRepository.findByTitleContaining(searchWord, PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReformeData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    //게시글 필터링+검색(리포미)
    @GetMapping("/reforme/{category}/search/{searchWord}")
    public ResponseBody<?> searchBoardsInCategory(
            @PathVariable MeCategory category,
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {

        try {
            // 선택된 카테고리에 속하는 게시글 중에서 제목에 검색어가 포함된 게시글을 가져오는 쿼리
            Page<Reforme> boards = reformeRepository.findByCategoryAndTitleContaining(category, searchWord, PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReformeData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }


    //리포유 메인화면
    @GetMapping("/reforyou/ALL")
    public ResponseBody<?> getReforyouBoards(@RequestParam(defaultValue = "0") int page) {

        try {
            // 요청된 페이지와 사이즈에 해당하는 데이터를 가져와서 반환
            Page<Reforyou> boards = reforyouRepository.findAllByOrderByCreatedDateTimeDesc(PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReforyouData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    //게시글 필터링(리포유)
    @GetMapping("/reforyou/{category}")
    public ResponseBody<?> getBoardsByYouCategory(
            @PathVariable YouCategory category,
            @RequestParam(defaultValue = "0") int page
    ) {

        try {
            // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
            Page<Reforyou> boards = reforyouRepository.findByCategory(category, PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReforyouData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    //게시글 검색(리포유)
    @GetMapping("/reforyou/search/{searchWord}")
    public ResponseBody<?> getBoardsBYouSearch(
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {

        try {
            // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
            Page<Reforyou> boards = reforyouRepository.findByTitleContaining(searchWord, PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReforyouData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    //게시글 필터링+검색(리포유)
    @GetMapping("/reforyou/{category}/search/{searchWord}")
    public ResponseBody<?> searchReforyouBoardsInCategory(
            @PathVariable YouCategory category,
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {

        try {
            // 선택된 카테고리에 속하는 게시글 중에서 제목에 검색어가 포함된 게시글을 가져오는 쿼리
            Page<Reforyou> boards = reforyouRepository.findByCategoryAndTitleContaining(category, searchWord, PageRequest.of(page, 10));
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.getReforyouData(boards));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    //상세글 조회(리포미)
    @GetMapping("/reforme/board/{boardId}")
    public ResponseBody<?> reformeDetail(@PathVariable Long boardId){

        try {
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.findByIdReformeBoard(boardId));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

    }

    //상세글 조회(리포유)
    @GetMapping("/reforyou/board/{boardId}")
    ResponseBody<?> reforyouDetail(@PathVariable Long boardId){
        try {
            return new ResponseBody<>(HttpStatus.OK.value(), boardService.findByIdReforyouBoard(boardId));
        } catch (Exception e) {
            return new ResponseBody<String>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
