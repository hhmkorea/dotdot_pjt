-- 데이터베이스 생성
CREATE DATABASE dotdot CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
-- 데이터베이스 사용
use dotdot;
-- 한글설정 확인
show variables like 'c%';

-- root에서 전체 사용자 조회
select * from mysql.user;
-- 사용자 추가 : 유저이름@아이피주소
create user 'cos'@'%' identified by 'cos1234';
-- 권한 설정 : ON DB이름.테이블명 TO 유저이름@아이피주소
-- GRANT ALL PRIVILEGES ON *.* TO

-- 테이블 확인
SELECT  * FROM tb_board;
SELECT  * FROM tb_member;

desc tb_board;
desc tb_member;

/* <criteria> */ select
                     b1_0.id,
                     b1_0.content,
                     b1_0.creatDate,
                     b1_0.title,
                     b1_0.updatedDate,
                     b1_0.userName,
                     b1_0.viewcnt
                 from
                     tb_board b1_0
                 order by
                     b1_0.id desc
                 limit
                     0, 10;