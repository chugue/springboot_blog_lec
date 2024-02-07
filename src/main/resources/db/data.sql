insert into user_tb(username, password, email, created_at) values('ssar', '1234', 'ssar@nate.com', now());
insert into user_tb(username, password, email, created_at) values('cos', '1234', 'cos@nate.com', now());

insert into board_tb(title, content, user_id, created_at) values('제목1', '내용1', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목2', '내용2', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목3', '내용3', 1, now());
insert into board_tb(title, content, user_id, created_at) values('제목4', '내용4', 2, now());

insert into commet_tb(user_id, board_id, user_username, content, created_at) inner join values (1,1,'ssar','댓글1',now());
insert into commet_tb(user_id, board_id, user_username, content, created_at) values (2,1,'cos','댓글2',now());
insert into commet_tb(user_id, board_id, user_username, content, created_at) values (3,1,'love','댓글3',now());
insert into commet_tb(user_id, board_id, user_username, content, created_at) values (1,1,'ssar','댓글1',now());

select comment_tb.*, user_tb.username, board_tb.board_name from comment inner join user_tb u on comment.user_id = u.id inner join board_tb b on comment.board_id = b.id;