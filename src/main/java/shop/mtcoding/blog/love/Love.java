package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Table(name = "love_tb",uniqueConstraints = {
        @UniqueConstraint(
                name="loveUK",
                columnNames={"board_id","user_id"}
        )
})
@Data
@Entity
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer boardId;
    private Timestamp createdAt;
}
