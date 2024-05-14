package reforme.reforme.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDto {

    private Long id;

    private String title;

    private String imagePath;

    private String category;

    private String createdDateTime;

    private Integer comment;


}
