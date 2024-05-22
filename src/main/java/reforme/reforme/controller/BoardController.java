package reforme.reforme.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.security.access.prepost.PreAuthorize;
import reforme.reforme.dto.BoardDto;
import reforme.reforme.dto.BoardUpdateDto;
import reforme.reforme.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;
    // 이거는 작성 페이지 열기
    @GetMapping("/board")
    public String showCreatePostPage() {
        return "postForm";
    }

    //이미지도 같이넘기려면 requestPart를 써야함 Body가 아니라
    @PostMapping("/reforyou/board")
    public ResponseEntity save(@RequestPart("board") BoardDto boardDto,
                               @RequestPart("images") MultipartFile[] images) {
        ResponseEntity responseEntity = boardService.createBoard(boardDto, images, "reforyou");
        return responseEntity;
    }

    //게시판 수정
    @PatchMapping("/reforyou/board/{id}")
    public ResponseEntity update(@PathVariable Long id,
                                 @RequestPart("board") BoardUpdateDto updateDto,
                                 @RequestPart("images") MultipartFile[] images){
        ResponseEntity responseEntity = boardService.updateBoard(id, updateDto, images, "reforyou");
        return responseEntity;
    }
    
    //게시판 삭제
    @DeleteMapping("/reforyou/board/{boardId}")
    public ResponseEntity remove(@PathVariable Long boardId) {
        ResponseEntity responseEntity = boardService.deleteBoard(boardId, "reforyou");
        return responseEntity;
    }

    //----------------------------reforme -----------------------//

    @PostMapping("/reforme/board")
    public ResponseEntity reformesave(@RequestPart("board") BoardDto boardDto,
                                      @RequestPart("images") MultipartFile[] images) {
        ResponseEntity responseEntity = boardService.createBoard(boardDto, images, "reforme");
        return responseEntity;
    }

    @PatchMapping("/reforme/board/{id}")
    public ResponseEntity reformeupdate(@PathVariable Long id,
                                        @RequestPart("board") BoardUpdateDto updateDto,
                                        @RequestPart("images") MultipartFile[] images){
        ResponseEntity responseEntity = boardService.updateBoard(id, updateDto, images, "reforme");
        return responseEntity;
    }

    @DeleteMapping("/reforme/board/{boardId}")
    public ResponseEntity reformeremove(@PathVariable Long boardId) {
        ResponseEntity responseEntity = boardService.deleteBoard(boardId, "reforme");
        return responseEntity;
    }

}