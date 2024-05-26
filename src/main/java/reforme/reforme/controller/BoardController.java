package reforme.reforme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.security.access.prepost.PreAuthorize;
import reforme.reforme.dto.BoardDto;
import reforme.reforme.dto.BoardUpdateDto;
import reforme.reforme.dto.ResponseBody;
import reforme.reforme.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    //이거는 작성 페이지 열기
    //그게 없음, 게시글 작성 페이지로 넘어가는 거에 대한 Mapping은 API명세서에도 언급이 없다는 듯.
    //그 챗봇 위에 +버튼 누르면 Mappinge되게 해야 함. 챗봇도 아직 주소가 안정해 져있긴함.
    @GetMapping("/board")
    public String showCreatePostPage() {
        return "postForm";
    }

    //이미지도 같이넘기려면 requestPart를 써야함 Body가 아니라
    @PostMapping("/reforyou/board")
    public ResponseEntity<ResponseBody<?>> save(@RequestPart("board") BoardDto boardDto,
                               @RequestPart("images") MultipartFile[] images) {
        ResponseBody<?> responseBody = boardService.createBoard(boardDto, images, "reforyou");
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    //게시판 수정
    @PatchMapping("/reforyou/board/{id}")
    public ResponseEntity<ResponseBody<?>> update(@PathVariable Long id,
                                 @RequestPart("board") BoardUpdateDto updateDto,
                                 @RequestPart("images") MultipartFile[] images){
        ResponseBody<?> responseBody  = boardService.updateBoard(id, updateDto, images, "reforyou");
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }
    
    //게시판 삭제
    @DeleteMapping("/reforyou/board/{boardId}")
    public ResponseEntity<ResponseBody<?>> remove(@PathVariable Long boardId) {
        ResponseBody<?> responseBody = boardService.deleteBoard(boardId, "reforyou");
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    //----------------------------reforme -----------------------//

    @PostMapping("/reforme/board")
    public ResponseEntity<ResponseBody<?>> reformesave(@RequestPart("board") BoardDto boardDto,
                                      @RequestPart("images") MultipartFile[] images) {
        ResponseBody<?> responseBody = boardService.createBoard(boardDto, images, "reforme");
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    @PatchMapping("/reforme/board/{id}")
    public ResponseEntity<ResponseBody<?>> reformeupdate(@PathVariable Long id,
                                        @RequestPart("board") BoardUpdateDto updateDto,
                                        @RequestPart("images") MultipartFile[] images){
        ResponseBody<?> responseBody = boardService.updateBoard(id, updateDto, images, "reforme");
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

    @DeleteMapping("/reforme/board/{boardId}")
    public ResponseEntity<ResponseBody<?>> reformeremove(@PathVariable Long boardId) {
        ResponseBody<?> responseBody = boardService.deleteBoard(boardId, "reforme");
        return ResponseEntity.status(responseBody.getStatusCode())
                .body(responseBody);
    }

}