package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import reforme.reforme.entity.Comment;

@Getter
@Setter
public class CommentDto {

    private String commentUsername;

    private String content;

    private Boolean secret;

    private String createdTime;

    public CommentDto(Comment comment){
        this.content = comment.getContent();
        this.commentUsername = comment.getUser().getNickname();
        this.secret = comment.getSecret();
        this.createdTime = String.valueOf(comment.getCreatedTime());
    }

}
