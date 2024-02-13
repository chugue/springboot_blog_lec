package shop.mtcoding.blog._core.config.security;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;



// 조건 Post요청, /login, x-www-form-urlencoded, 키값 username과 password
@RequiredArgsConstructor
@Service
public class MyLoginService implements UserDetailsService {
    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername : "+ username);
        User user = (User)userRepository.findByUsername(username);

        if (user == null){
            System.out.println("user는 null");
            return null;
        } else {
            System.out.println("user를 찾았어요!");
            session.setAttribute("sessionUser", user); //머스태치에 쓰는 용도로만 사용
            return new MyLoginUser(user); //SecurityContextHolder에 저장
        }
    }
}
