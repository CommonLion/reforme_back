package reforme.reforme.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import reforme.reforme.domain.board.Board;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Comment {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;

    private Boolean secret;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedDateTime;
}
