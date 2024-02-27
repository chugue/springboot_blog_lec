package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Table(name = "love_tb")
@Data
@Entity
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private Integer boardId;
    @Column(unique = true)
    private Integer userId;
    private Timestamp createdAt;
}