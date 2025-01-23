-- 2025년 01월 23일~ myboard 게시판

USE scit;

-- 1) 게시판 테이블
--    첨부파일 관련 컬럼 포함 (나중 추가)

DROP TABLE IF EXISTS scit.myboard_reply;
DROP TABLE IF EXISTS scit.myboard;

CREATE TABLE scit.myboard
(
	board_seq       int AUTO_INCREMENT 
	, board_writer  varchar(50)  NOT NULL
	, board_title   varchar(200) DEFAULT 'Untitled'
	, board_content varchar(4000)
	, hit_count     int DEFAULT 0           -- 조회수
	, create_date   datetime DEFAULT current_timestamp
	, update_date   datetime DEFAULT current_timestamp
		, CONSTRAINT myboard_boardseq PRIMARY KEY (board_seq)
)

-- 3) 댓글 테이블  (게시글과 1:다 관계 형성)
create table scit.myboard_reply
(
   reply_seq int auto_increment,
   board_seq int -- FK
   , reply_writer varchar(50) not null
   , reply_content varchar(1000) not null
   , create_date datetime default current_timestamp
      , constraint myboard_reply_replyseq primary key (reply_seq)
      , constraint myboard_reply_boardseq foreign key (board_seq) references scit.myboard(board_seq)
      on delete cascade
)

commit;
select * from myboard;
select * from myboard_reply;