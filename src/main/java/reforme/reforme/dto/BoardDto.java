package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import reforme.reforme.entity.Comment;
import reforme.reforme.entity.Image;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;

import java.util.List;

@Getter
@Setter
public class BoardDto {

    private Long boardId;

    private String userId;

    private String title;

    private String body;

    private Image[] images;

    private Comment[] comments;

    private String createdDateTime;

    private String modifiedDateTime;

    private String category;

    public BoardDto(Reforme reforme){
        this.boardId = reforme.getId();
        this.userId = reforme.getUser().getId();
        this.title = reforme.getTitle();
        this.body = reforme.getBody();
        this.images = reforme.getImages().toArray(new Image[0]);
        this.comments = reforme.getComments().toArray(new Comment[0]);
        this.createdDateTime = String.valueOf(reforme.getCreatedDateTime());
        this.modifiedDateTime = String.valueOf(reforme.getModifiedDateTime());
    }

    public BoardDto(Reforyou reforyou){
        this.boardId = reforyou.getId();
        this.userId = reforyou.getUser().getId();
        this.title = reforyou.getTitle();
        this.body = reforyou.getBody();
        this.images = reforyou.getImages().toArray(new Image[0]);
        this.comments = reforyou.getComments().toArray(new Comment[0]);
        this.createdDateTime = String.valueOf(reforyou.getCreatedDateTime());
        this.modifiedDateTime = String.valueOf(reforyou.getModifiedDateTime());
    }

}
