package shop.mtcoding.blog.board;

import lombok.Data;
import shop.mtcoding.blog.user.User;


public class BoardResponse {

    @Data
    public static class DetailDTO {
        private Integer id;
        private String title;
        private String content;
        private int userId;
        private String username;
        private Boolean boardOwner;

        public void isBoardOnwer(User sessionUser){
            if (sessionUser == null) boardOwner = false;
            else boardOwner = true;



        }
    }

    @Data
    public static class ReplyDTO {
        private Integer id;
        private Integer userId;
        private String username;
        private String comment;
        private Boolean replyOwner;

        public ReplyDTO(Object[] ob, User sessionUser) {
            this.id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.username = (String) ob[2];
            this.comment = (String) ob[3];
            if (sessionUser == null){
                replyOwner = false;
            } else {
                replyOwner = sessionUser.getId() == userId;
            }
        }
    }
}
