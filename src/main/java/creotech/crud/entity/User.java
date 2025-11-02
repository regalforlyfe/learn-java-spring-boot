package creotech.crud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User extends BaseEntity {
    @Id
    private String uuid;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    @Column(nullable = false)
    private String password;

    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    @OneToMany(mappedBy = "user")
    private List<Article> articles;
}
