package shop.mtcoding.blog.love;


import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(LoveRepository.class)
@DataJpaTest
public class LoveRepositoryTest {

}
