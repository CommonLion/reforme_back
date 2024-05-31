package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;
import reforme.reforme.entity.Image;

@Getter @Setter
public class ImageDto {

    private Long imageId;

    private String imagePath;

    private String origImageName;

    public ImageDto(Image image){
        this.imageId = image.getId();
        this.imagePath = image.getImagePath();
        this.origImageName = image.getOrigImageName();
    }

}
