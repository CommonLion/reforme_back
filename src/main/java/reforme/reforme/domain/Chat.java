package reforme.reforme.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Chat {

    @Id @GeneratedValue
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String massage;

    @OneToOne(fetch = FetchType.LAZY)
    private Image image;

    private LocalDateTime sendedDateTime;
}
