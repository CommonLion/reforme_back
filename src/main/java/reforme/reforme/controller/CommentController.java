package reforme.reforme.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reforme.reforme.dto.CommentDto;
import reforme.reforme.service.CommentService;
import reforme.reforme.dto.ResponseBody;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/reforme/board/{boardId}/comment")
    ResponseBody<?> addComment(@RequestBody CommentDto commentDto,
                                    Authentication auth,
                                    @PathVariable Long boardId){

        return commentService.saveReformeComment(commentDto.getContent(), commentDto.getSecret(), auth, boardId);
    }

    @PatchMapping("/reforme/board/{boardId}/comment")
    ResponseBody<?> editComment(@RequestParam Long id,
                                     @RequestBody CommentDto commentDto){

        return commentService.editComment(id, commentDto.getContent(), commentDto.getSecret());
    }

    @DeleteMapping("/reforme/board/{boardId}/comment")
    ResponseBody<?> deleteComment(@RequestParam Long id,
                                       @PathVariable Long boardId){

        return commentService.deleteReformeComment(id, boardId);

    }

    @PostMapping("/reforyou/board/{boardId}/comment")
    ResponseBody<?> addReforyouComment(@RequestBody CommentDto commentDto,
                                            Authentication auth,
                                            @PathVariable Long boardId){

        return commentService.saveReforyouComment(commentDto.getContent(), commentDto.getSecret(), auth, boardId);
    }

    @PatchMapping("/reforyou/board/{boardId}/comment")
    ResponseBody<?> editReforyouComment(@RequestParam Long id,
                                             @RequestBody CommentDto commentDto){

        return commentService.editComment(id, commentDto.getContent(), commentDto.getSecret());

    }

    @DeleteMapping("/reforyou/board/{boardId}/comment")
    ResponseBody<?> deleteReforyouComment(@RequestParam Long id,
                                               @PathVariable Long boardId){

        return commentService.deleteReforyouComment(id, boardId);

    }


}
