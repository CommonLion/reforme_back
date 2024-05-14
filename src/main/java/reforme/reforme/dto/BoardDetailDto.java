package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import reforme.reforme.entity.Image;
import reforme.reforme.entity.board.Reforme;
import reforme.reforme.entity.board.Reforyou;

import java.util.List;

@Getter
@Setter
public class BoardDetailDto {

    private Long id;

    private String title;

    private String boardUsername;

    private List<String> imagePath;

    private String boardBody;

    public BoardDetailDto(Reforme reforme){
        this.id = reforme.getId();
        this.title = reforme.getTitle();
        this.boardUsername = reforme.getUser().getNickname();
        this.boardBody = reforme.getBody();
        List<Image> images = reforme.getImages();
        if(!images.isEmpty()) {
            for (Image image : images) {
                this.imagePath.add(image.getImagePath());
            }
        }
    }

    public BoardDetailDto(Reforyou reforyou){
        this.id = reforyou.getId();
        this.title = reforyou.getTitle();
        this.boardUsername = reforyou.getUser().getNickname();
        this.boardBody = reforyou.getBody();
        List<Image> images = reforyou.getImages();
        if(!images.isEmpty()) {
            for (Image image : images) {
                this.imagePath.add(image.getImagePath());
            }
        }
    }


}
