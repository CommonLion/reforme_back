package reforme.reforme.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Users")
@Getter @Setter
public class User {

    @Id
    @Column(name = "user_id")
    private String id;

    private String password;

    private String nickname;
}
