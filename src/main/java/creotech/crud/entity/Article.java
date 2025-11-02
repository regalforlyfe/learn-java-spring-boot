package creotech.crud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article extends BaseEntity {

    @Id
    private String uuid;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    private User user;
}
