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
SELECT  * FROM board;
SELECT  * FROM member;

-- UPDATE member SET role = 'USER' where username = 'test';

desc board;
desc member;



select b.*
from Board b join Member m on b.memberid = m.id
where (
    m.username like '%1%'
or b.title like '%1%'
or b.content like '%1%')
;

select
    b.*
from
    Board b
        join
    Member m
    on b.memberId = m.id
where
    (m.username like '%|'+'1'+'|%'
   or b.title like '%|'+'1'+'|%'
   or b.content  like '%|'+'1'+'|%')


