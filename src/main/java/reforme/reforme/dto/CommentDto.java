package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import reforme.reforme.entity.Comment;

@Getter @Setter
public class CommentDto {

    private Long id;

    private String nickname;

    private String content;

    private Boolean secret;


    public CommentDto(String content, Boolean secret){
        this.content = content;
        this.secret = secret;
    }

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.nickname = comment.getUser().getNickname();
        this.content = comment.getContent();
        this.secret = comment.getSecret();
    }

}
