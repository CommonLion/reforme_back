package reforme.reforme.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id @GeneratedValue
    @Column(name = "image_id")
    private Long id;

    private String origImageName;

    private String imagePath;

}
