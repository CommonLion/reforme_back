package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reforme.reforme.dto.CommentDto;
import reforme.reforme.entity.Comment;
import reforme.reforme.entity.User;
import reforme.reforme.entity.board.Board;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
import reforme.reforme.repository.CommentRepository;
import reforme.reforme.repository.ReformeRepository;
import reforme.reforme.repository.ReforyouRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void saveReformeComment(String content, Boolean secret, String userid, Long boardId){
        Comment comment = new Comment();
        comment.setSecret(secret);
        comment.setContent(content);
        Reforme board = new Reforme();
        board.setId(boardId);
        User user = new User();
        user.setId(userid);
        comment.setUser(user);
        var comments = board.getComments();
        comments.add(comment);
        board.setComments(comments);
        commentRepository.save(comment);
    }
    public void saveReforyouComment(String content, Boolean secret, String userid, Long boardId){
        Comment comment = new Comment();
        comment.setSecret(secret);
        comment.setContent(content);
        Reforyou board = new Reforyou();
        board.setId(boardId);
        User user = new User();
        user.setId(userid);
        comment.setUser(user);
        var comments = board.getComments();
        comments.add(comment);
        board.setComments(comments);
        commentRepository.save(comment);
    }

    public void deleteComment(Long id, String userid){

        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (comment.getUser().getId().equals(userid)) {
                commentRepository.deleteById(id);
            } else {
                // userid가 댓글의 작성자와 일치하지 않을 때 예외 처리
                throw new IllegalArgumentException("해당 댓글을 삭제할 수 있는 권한이 없습니다.");
            }
        } else {
            // 주어진 ID에 해당하는 댓글이 없을 때 예외 처리
            throw new IllegalArgumentException("주어진 ID에 해당하는 댓글을 찾을 수 없습니다.");
        }

    }

    public void editComment(Long id, String content, Boolean secret, String userid){
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            if (comment.getUser().getId().equals(userid)) {
                comment.setSecret(secret);
                comment.setContent(content);
            } else {
                // userid가 댓글의 작성자와 일치하지 않을 때 예외 처리
                throw new IllegalArgumentException("해당 댓글을 수정할 수 있는 권한이 없습니다.");
            }
        } else {
            // 주어진 ID에 해당하는 댓글이 없을 때 예외 처리
            throw new IllegalArgumentException("주어진 ID에 해당하는 댓글을 찾을 수 없습니다.");
        }
    }

}
