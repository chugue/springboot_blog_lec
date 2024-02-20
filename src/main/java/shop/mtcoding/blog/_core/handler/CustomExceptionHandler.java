package shop.mtcoding.blog._core.handler;


import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.util.Script;

@ControllerAdvice // 응답 에러 컨트롤러 (view=파일 리턴)
public class CustomExceptionHandler {

    @ExceptionHandler(Exception.class) // 에러 타입을 정하는 곳
    public @ResponseBody String error1(Exception e) {
        return Script.back(e.getMessage());
    }



}
