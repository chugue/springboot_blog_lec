package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog.reply.Reply;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final HttpSession session;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @PostMapping("/board/{id}/update")  // @RequestBody는 바디데이터를 JSON타입으로 변환해준다.
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO) {
        //1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        //2. 권한 체크
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }
        //3. 핵심 로직
        //update board_tb set title = ?, content = ? where id = ?;
        boardRepository.update(requestDTO, id);
        return "redirect:/board/" + id;
    }

    // 게시글 수정 페이지 정보를 조회해서 뿌리는 책임을 가진다.
    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request) {
        // 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 모델 위임 (id로 board를 조회)
        Board board = boardRepository.findById(id);
        // 권한 체크
        if (board == null) {
            return "error/400";
        }
        if (board.getUserId() != sessionUser.getId()) {
            return "error/403";
        }
        // 가방에 담기
        request.setAttribute("board", board);
        return "board/updateForm";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id, HttpServletRequest request) {
        // 1. 인증 안되면 나가
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) { // 401
            return "redirect:/loginForm";
        }
        // 2. 권한 없으면 나가
        Board board = boardRepository.findById(id);
        if (board.getUserId() != sessionUser.getId()) {
            request.setAttribute("status", 403);
            request.setAttribute("msg", "게시글을 삭제할 권한이 없습니다");
            return "error/40x";
        }
        boardRepository.deleteById(id);
        return "redirect:/";
    }


    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO requestDTO, HttpServletRequest request) {
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        // 2. 바디 데이터 확인 및 유효성 검사
        System.out.println(requestDTO);

        if (requestDTO.getTitle().length() > 30) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }

        // 3. 모델 위임
        // insert into board_tb(title, content, user_id, created_at) values(?,?,?, now());
        boardRepository.save(requestDTO, sessionUser.getId());
        return "redirect:/";
    }


    // localhost:8080?page=1
    // localhost:8080
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "") String keyword) {

        // isEmpty -> null, 공백
        // isBlank -> null, 공백, 화이트스페이스
        List<Board> boardList = null;
        if (keyword.isBlank()) {
            boardList = boardRepository.findAll(page);
            //전체 페이지 개수
            int count = boardRepository.count().intValue();

            int remainder = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + remainder;

            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", "");
        } else {
            boardList = boardRepository.findAll(page, keyword);
            //전체 페이지 개수
            int count = boardRepository.count(keyword).intValue();
            // 5 -> 2 page
            // 6 -> 2 page
            // 7 -> 3 page
            // 8 -> 3 page
            int remainder = count % 3 == 0 ? 0 : 1;
            int allPageCount = count / 3 + remainder;

            request.setAttribute("boardList", boardList);
            request.setAttribute("first", page == 0);
            request.setAttribute("last", allPageCount == page + 1);
            request.setAttribute("prev", page - 1);
            request.setAttribute("next", page + 1);
            request.setAttribute("keyword", keyword);
        }
        return "index";
    }

    //   /board/saveForm 요청(Get)이 온다
    @GetMapping("/board/saveForm")
    public String saveForm() {
        //   session 영역에 sessionUser 키값에 user 객체 있는지 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        //   값이 null 이면 로그인 페이지로 리다이렉션
        //   값이 null 이 아니면, /board/saveForm 으로 이동
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOnwer(sessionUser);

        List<BoardResponse.ReplyDTO> replyList = replyRepository.findByBoardId(id, sessionUser);

        request.setAttribute("board", boardDTO);
        request.setAttribute("replyList", replyList);
        return "board/detail";
    }

}