package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import reforme.reforme.entity.Image;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;
;

@Getter
@Setter
public class BoardDto {

    private Long boardId;

    private String userId;

    private String title;

    private String body;

    private ImageDto[] images;

    private CommentDto[] comments;

    private String createdDateTime;

    private String modifiedDateTime;

    private String category;

    public BoardDto(Reforme reforme){
        this.boardId = reforme.getId();
        this.userId = reforme.getUser().getId();
        this.title = reforme.getTitle();
        this.body = reforme.getBody();
        this.images = reforme.getImages().stream()
                .map(ImageDto::new)
                .toArray(ImageDto[]::new);
        this.comments = reforme.getComments().stream()
                .map(CommentDto::new)
                .toArray(CommentDto[]::new);
        this.createdDateTime = String.valueOf(reforme.getCreatedDateTime());
        this.modifiedDateTime = String.valueOf(reforme.getModifiedDateTime());
        this.category = String.valueOf(reforme.getCategory());
    }

    public BoardDto(Reforyou reforyou){
        this.boardId = reforyou.getId();
        this.userId = reforyou.getUser().getId();
        this.title = reforyou.getTitle();
        this.body = reforyou.getBody();
        this.images = reforyou.getImages().stream()
                .map(ImageDto::new)
                .toArray(ImageDto[]::new);
        this.comments = reforyou.getComments().stream()
                .map(CommentDto::new)
                .toArray(CommentDto[]::new);
        this.createdDateTime = String.valueOf(reforyou.getCreatedDateTime());
        this.modifiedDateTime = String.valueOf(reforyou.getModifiedDateTime());
        this.category = String.valueOf(reforyou.getCategory());
    }

}
