package reforme.reforme.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reforme.reforme.dto.CommentDto;
import reforme.reforme.repository.CommentRepository;
import reforme.reforme.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @PostMapping("/reforme/board/{boardId}/comment")
    String addComment(String content, Boolean secret, Authentication auth, @PathVariable Long boardId){
        String userid;
        if(auth==null) return "redirect:/reforme/board/{boardId}";
        else userid = auth.getName();
        commentService.saveReformeComment(content, secret, userid, boardId);
        return "redirect:/reforme/board/{boardId}";
    }

    @PatchMapping("/reforme/board/{boardId}/comment")
    String editComment(@RequestParam Long id, String content, Boolean secret, Authentication auth){
        if(auth==null) return "redirect:/reforme/board/{boardId}";
        String userid = auth.getName();
        commentService.editComment(id, content, secret, userid);
        return "redirect:/reforme/board/{boardId}";
    }

    @DeleteMapping("/reforme/board/{boardId}/comment")
    String deleteComment(@RequestParam Long id, Authentication auth){
        String userid = auth.getName();
        commentService.deleteComment(id, userid);
        return "redirect:/reforme/board/{boardId}";

    }

    @PostMapping("/reforyou/board/{boardId}/comment")
    String addReforyouComment(String content, Boolean secret, Authentication auth, @PathVariable Long boardId){
        String userid;
        if(auth==null) return "redirect:/reforyou/board/{boardId}";
        else userid = auth.getName();
        commentService.saveReforyouComment(content, secret, userid, boardId);
        return "redirect:/reforyou/board/{boardId}";
    }

    @PatchMapping("/reforyou/board/{boardId}/comment")
    String editReforyouComment(@RequestParam Long id, String content, Boolean secret, Authentication auth){
        if(auth==null) return "redirect:/reforyou/board/{boardId}";
        String userid = auth.getName();
        commentService.editComment(id, content, secret, userid);
        return "redirect:/reforyou/board/{boardId}";
    }

    @DeleteMapping("/reforyou/board/{boardId}/comment")
    String deleteReforyouComment(@RequestParam Long id, Authentication auth){
        String userid = auth.getName();
        commentService.deleteComment(id, userid);
        return "redirect:/reforyou/board/{boardId}";

    }


}
