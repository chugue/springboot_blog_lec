package shop.mtcoding.blog.comment;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Table(name = "comment_tb")
@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;
    private int userId;
    private int boardId;
    private String userUsername;

    @Column(length = 200)
    private String comment;
    private LocalDateTime createdAT;
}
