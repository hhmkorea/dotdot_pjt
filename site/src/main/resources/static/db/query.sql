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

-- AUTO_INCREMENT 증가값 초기화
-- alter table board auto_increment = 1;

-- AUTO_INCREMENT 조회.
select auto_increment from information_schema.tables where table_schema = 'dotdot' and table_name = 'board';

SHOW TABLE STATUS LIKE 'board';

select
    nvl((id), 0) + 1 as newId
from
    board;
select
#    b.content
#    , locate('img src', b.content) as result
#     , substr(b.content, locate('image/', b.content), 47) as result1
     REGEXP_REPLACE(b.content, '<(/)?(img|label|table|thead|tbody|tfoot|tr|td|p|br|div|span|font|strong|b|iframe|a)(.|\s|\t|\n|\r\n)*?>', '') as post_text_content
from
    Board b
        join
    Member m
    on b.memberId = m.id
where 1=1
#     (m.username like '%|'+'1'+'|%'
#    or b.title like '%|'+'1'+'|%'
#    or b.content  like '%|'+'1'+'|%')
 and b.content like concat('%', 'a','%')
-- and REGEXP_REPLACE(b.content, '<(/)?(img|label|table|thead|tbody|tfoot|tr|td|p|br|div|span|font|strong|b|iframe|a)(.|\s|\t|\n|\r\n)*?>', '') like '%a%'
;

select length('image/afe72cb2-a0d9-4cbe-9795-4433c452277e..png') from dual; -- 47