package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.mtcoding.blog._core.config.security.MyLoginUser;


@RequiredArgsConstructor // final이 붙은 애들에 대한 생성자를 만들어줌
@Controller
public class UserController {
    // 자바는 final 변수는 반드시 초기화가 되어야함.
    private final UserRepository userRepository;
    private final HttpSession session;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/user/update")
    public String update (UserRequest.UserUpdateDTO requestDTO, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/";
        }
        if (requestDTO.getPassword().length() > 20){
            request.setAttribute("msg", "비밀번호는 20자 이하여야 합니다.");
            request.setAttribute("status", "400");
            return "error/40x";
        }
        if (requestDTO.getPassword().equals(sessionUser.getPassword())){
            request.setAttribute("msg", "이전 비밀번호와 같습니다.");
            request.setAttribute("status", "400");
            return "error/40x";
        }
        User user = userRepository.updatePassword(sessionUser.getId(), requestDTO);
        session.setAttribute("sessionUser", user);

        return "redirect:/";
    }


    @GetMapping("/user/updateForm")
    public String updateForm(HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        // 인증 체크
        User user = (User) userRepository.findByUsername(myLoginUser.getUsername());
        request.setAttribute("user", user);
        return "user/updateForm";
    }

    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {

        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        User user = userRepository.findByUsernameAndPassword(requestDTO);
        if (user == null) {
            return "error/401";
        } else {
            session.setAttribute("sessionUser", user);
        }

        return "redirect:/";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        System.out.println(requestDTO);

        String rawPassword = requestDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);
        requestDTO.setPassword(encPassword);

        userRepository.save(requestDTO);
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
