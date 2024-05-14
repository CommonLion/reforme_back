package reforme.reforme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reforme.reforme.dto.BoardDto;
import reforme.reforme.entity.MeCategory;
import reforme.reforme.entity.YouCategory;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
import reforme.reforme.repository.ReformeRepository;
import reforme.reforme.repository.ReforyouRepository;
import reforme.reforme.service.BoardService;
import reforme.reforme.service.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
//@ResponseBody
public class BoardController {

    private final BoardService boardService;
    private final ReformeRepository reformeRepository;
    private final ReforyouRepository reforyouRepository;
    private final CommentService commentService;

    //리포미 메인화면
    @GetMapping("/reforme")
    public List<BoardDto> getBoards(@RequestParam(defaultValue = "0") int page) {
        // 요청된 페이지와 사이즈에 해당하는 데이터를 가져와서 반환
        Page<Reforme> boards = reformeRepository.findAllByOrderByCreatedDateTimeDesc(PageRequest.of(page, 10));
        return boardService.getReformeData(boards);
    }

    //게시글 필터링(리포미)
    @GetMapping("/reforme/{category}")
    public List<BoardDto> getBoardsByCategory(
            @PathVariable MeCategory category,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforme> boards = reformeRepository.findByCategory(category, PageRequest.of(page, 10));
        return boardService.getReformeData(boards);
    }

    //게시글 검색(리포미)
    @GetMapping("/reforme/search/{searchWord}")
    public List<BoardDto> getBoardsByMeSearch(
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforme> boards = reformeRepository.findByTitleContaining(searchWord, PageRequest.of(page, 10));
        return boardService.getReformeData(boards);
    }

    //게시글 필터링+검색(리포미)
    @GetMapping("/reforme/{category}/search/{searchWord}")
    public List<BoardDto> searchBoardsInCategory(
            @PathVariable MeCategory category,
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 선택된 카테고리에 속하는 게시글 중에서 제목에 검색어가 포함된 게시글을 가져오는 쿼리
        Page<Reforme> boards = reformeRepository.findByCategoryAndTitleContaining(category, searchWord, PageRequest.of(page, 10));
        return boardService.getReformeData(boards);
    }


    //리포유 메인화면
    @GetMapping("/reforyou")
    public List<BoardDto> getReforyouBoards(@RequestParam(defaultValue = "0") int page) {
        // 요청된 페이지와 사이즈에 해당하는 데이터를 가져와서 반환
        Page<Reforyou> boards = reforyouRepository.findAllByOrderByCreatedDateTimeDesc(PageRequest.of(page, 10));
        return boardService.getReforyouData(boards);
    }

    //게시글 필터링(리포유)
    @GetMapping("/reforyou/{category}")
    public List<BoardDto> getBoardsByYouCategory(
            @PathVariable YouCategory category,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforyou> boards = reforyouRepository.findByCategory(category, PageRequest.of(page, 10));
        return boardService.getReforyouData(boards);
    }

    //게시글 검색(리포유)
    @GetMapping("/reforyou/search/{searchWord}")
    public List<BoardDto> getBoardsBYouSearch(
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforyou> boards = reforyouRepository.findByTitleContaining(searchWord, PageRequest.of(page, 10));
        return boardService.getReforyouData(boards);
    }

    //게시글 필터링+검색(리포유)
    @GetMapping("/reforyou/{category}/search/{searchWord}")
    public List<BoardDto> searchReforyouBoardsInCategory(
            @PathVariable YouCategory category,
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page
    ) {
        // 선택된 카테고리에 속하는 게시글 중에서 제목에 검색어가 포함된 게시글을 가져오는 쿼리
        Page<Reforyou> boards = reforyouRepository.findByCategoryAndTitleContaining(category, searchWord, PageRequest.of(page, 10));
        return boardService.getReforyouData(boards);
    }

    //상세글 조회(리포미)
    @GetMapping("/reforme/board/{boardId}")
    public String reformeDetail(@PathVariable Long boardId, Model model){
        model.addAttribute("board", boardService.findByIdReformeBoard(boardId));
        model.addAttribute("comment", commentService.findByIdMeComment(boardId));
        return "detail.html";

    }

    //상세글 조회(리포유)
    @GetMapping("/reforyou/board/{boardId}")
    String reforyouDetail(@PathVariable Long boardId, Model model){
        model.addAttribute("board", boardService.findByIdReformeBoard(boardId));
        model.addAttribute("comment", commentService.findByIdYouComment(boardId));
        return "detail.html";

    }

}
