package reforme.reforme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reforme.reforme.dto.ResponseBody;
import reforme.reforme.entity.Comment;
import reforme.reforme.entity.User;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
import reforme.reforme.repository.CommentRepository;
import reforme.reforme.repository.ReformeRepository;
import reforme.reforme.repository.ReforyouRepository;
import reforme.reforme.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ReformeRepository reformeRepository;
    private final ReforyouRepository reforyouRepository;

    public ResponseBody<String> saveReformeComment(String content, Boolean secret, Authentication auth, Long boardId){
        try {
            if(secret==null) secret = false;
            String userid = auth.getName();
            Comment comment = new Comment();
            comment.setSecret(secret);
            comment.setContent(content);
            Optional<Reforme> board = reformeRepository.findById(boardId);
            User user = userRepository.findOne(userid);
            comment.setUser(user);
            comment.setCreatedTime(LocalDateTime.now());
            var comments = board.get().getComments();
            comments.add(comment);
            board.get().setComments(comments);
            commentRepository.save(comment);
            return new ResponseBody<>(HttpStatus.OK.value());
        } catch (Exception e){
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value());
        }
    }
    public ResponseBody<String> saveReforyouComment(String content, Boolean secret, Authentication auth, Long boardId){
        try {
            if(secret==null) secret = false;
            String userid = auth.getName();
            Comment comment = new Comment();
            comment.setSecret(secret);
            comment.setContent(content);
            Optional<Reforyou> board = reforyouRepository.findById(boardId);
            User user = userRepository.findOne(userid);
            comment.setUser(user);
            comment.setCreatedTime(LocalDateTime.now());
            var comments = board.get().getComments();
            comments.add(comment);
            board.get().setComments(comments);
            commentRepository.save(comment);
            return new ResponseBody<>(HttpStatus.OK.value());
        } catch (Exception e){
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value());
        }
    }

    public ResponseBody<String> deleteReformeComment(Long id, Long boardId){

        try {
            Reforme board = reformeRepository.findById(boardId).get();
            List<Comment> comment = board.getComments();
            for(int i=0; i<comment.size(); i++){
                if(comment.get(i).getId().equals(id)) {
                    comment.remove(i);
                    break;
                }
            }
            commentRepository.deleteById(id);

            return new ResponseBody<>(HttpStatus.OK.value());
        } catch (Exception e){
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value());
        }

    }

    public ResponseBody<String> deleteReforyouComment(Long id, Long boardId){

        try {
            Reforyou board = reforyouRepository.findById(boardId).get();
            List<Comment> comment = board.getComments();
            for(int i=0; i<comment.size(); i++){
                if(comment.get(i).getId().equals(id)) {
                    comment.remove(i);
                    break;
                }
            }

            commentRepository.deleteById(id);

            return new ResponseBody<>(HttpStatus.OK.value());
        } catch (Exception e){
            return new ResponseBody<>(HttpStatus.BAD_REQUEST.value());
        }

    }

    public ResponseBody<String> editComment(Long id, String content, Boolean secret){
        try {
            Optional<Comment> commentOptional = commentRepository.findById(id);
            Comment comment = commentOptional.get();
            comment.setSecret(secret);
            comment.setContent(content);
            comment.setModifiedDateTime(LocalDateTime.now());
            commentRepository.save(comment);
            return new ResponseBody<>(HttpStatus.OK.value());
        } catch (Exception e){
            return new ResponseBody<>(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
