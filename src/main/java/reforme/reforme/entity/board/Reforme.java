package reforme.reforme.entity.board;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import reforme.reforme.entity.MeCategory;

@Entity
@Getter @Setter
public class Reforme extends Board {

    @Enumerated(EnumType.STRING)
    private MeCategory category; // 카테고리 [TOP, BOTTOM, OUTWEAR, BAG, ETC]
}
