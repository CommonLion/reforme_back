package reforme.reforme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import reforme.reforme.entity.MeCategory;
import reforme.reforme.entity.YouCategory;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
import reforme.reforme.repository.ReformeRepository;
import reforme.reforme.repository.ReforyouRepository;
import reforme.reforme.service.BoardService;

@Controller
@RequiredArgsConstructor
//@ResponseBody
public class BoardController {

    private final BoardService boardService;
    private final ReformeRepository reformeRepository;
    private final ReforyouRepository reforyouRepository;

    //리포미 메인화면
    @GetMapping("/reforme")
    public String getBoards(@RequestParam(defaultValue = "0") int page, Model model) {
        // 요청된 페이지와 사이즈에 해당하는 데이터를 가져와서 반환
        Page<Reforme> boards = reformeRepository.findAllByOrderByCreatedDateTimeDesc(PageRequest.of(page, 10));
        model.addAttribute("boardListItem", boardService.getReformeData(boards));
        return "reforme";
    }

    //게시글 필터링(리포미)
    @GetMapping("/reforme/{category}")
    public String getBoardsByCategory(
            @PathVariable MeCategory category,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforme> boards = reformeRepository.findByCategory(category, PageRequest.of(page, 10));
        model.addAttribute("searchListItem", boardService.getReformeData(boards));
        return "reforme";
    }

    //게시글 검색(리포미)
    @GetMapping("/reforme/search/{searchWord}")
    public String getBoardsByMeSearch(
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforme> boards = reformeRepository.findByTitleContaining(searchWord, PageRequest.of(page, 10));
        model.addAttribute("searchListItem", boardService.getReformeData(boards));
        return "reforme";
    }

    //게시글 필터링+검색(리포미)
    @GetMapping("/reforme/{category}/search/{searchWord}")
    public String searchBoardsInCategory(
            @PathVariable MeCategory category,
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        // 선택된 카테고리에 속하는 게시글 중에서 제목에 검색어가 포함된 게시글을 가져오는 쿼리
        Page<Reforme> boards = reformeRepository.findByCategoryAndTitleContaining(category, searchWord, PageRequest.of(page, 10));
        model.addAttribute("searchListItem", boardService.getReformeData(boards));
        return "reforme";
    }


    //리포유 메인화면
    @GetMapping("/reforyou")
    public String getReforyouBoards(@RequestParam(defaultValue = "0") int page, Model model) {
        // 요청된 페이지와 사이즈에 해당하는 데이터를 가져와서 반환
        Page<Reforyou> boards = reforyouRepository.findAllByOrderByCreatedDateTimeDesc(PageRequest.of(page, 10));
        model.addAttribute("boardListItem", boardService.getReforyouData(boards));
        return "reforyou";
    }

    //게시글 필터링(리포유)
    @GetMapping("/reforyou/{category}")
    public String getBoardsByYouCategory(
            @PathVariable YouCategory category,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforyou> boards = reforyouRepository.findByCategory(category, PageRequest.of(page, 10));
        model.addAttribute("searchListItem", boardService.getReforyouData(boards));
        return "reforyou";
    }

    //게시글 검색(리포유)
    @GetMapping("/reforyou/search/{searchWord}")
    public String getBoardsBYouSearch(
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        // 선택된 카테고리에 속하는 페이지 번호에 해당하는 데이터를 가져와 반환
        Page<Reforyou> boards = reforyouRepository.findByTitleContaining(searchWord, PageRequest.of(page, 10));
        model.addAttribute("searchListItem", boardService.getReforyouData(boards));
        return "reforyou";
    }

    //게시글 필터링+검색(리포유)
    @GetMapping("/reforyou/{category}/search/{searchWord}")
    public String searchReforyouBoardsInCategory(
            @PathVariable YouCategory category,
            @PathVariable String searchWord,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        // 선택된 카테고리에 속하는 게시글 중에서 제목에 검색어가 포함된 게시글을 가져오는 쿼리
        Page<Reforyou> boards = reforyouRepository.findByCategoryAndTitleContaining(category, searchWord, PageRequest.of(page, 10));
        model.addAttribute("searchListItem", boardService.getReforyouData(boards));
        return "reforyou";
    }

    //상세글 조회(리포미)
    @GetMapping("/reforme/board/{boardId}")
    public String reformeDetail(@PathVariable Long boardId, Model model){
        model.addAttribute("boardItem", boardService.findByIdReformeBoard(boardId));
        return "detail.html";

    }

    //상세글 조회(리포유)
    @GetMapping("/reforyou/board/{boardId}")
    String reforyouDetail(@PathVariable Long boardId, Model model){
        model.addAttribute("boardItem", boardService.findByIdReforyouBoard(boardId));
        return "detail.html";

    }

}
