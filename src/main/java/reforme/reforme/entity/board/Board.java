package reforme.reforme.entity.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reforme.reforme.entity.Comment;
import reforme.reforme.entity.Image;
import reforme.reforme.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String body;

    @OneToMany
    private List<Image> images = new ArrayList<>();

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdDateTime;

    private LocalDateTime modifiedDateTime;
}
