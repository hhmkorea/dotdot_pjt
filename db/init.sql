DROP TABLE IF EXISTS board;
create table board
(
    id         int auto_increment   primary key,
    memberId   int         null,
    viewCnt    int         not null,
    createDate datetime(6) null,
    updateDate datetime(6) null,
    title      varchar(50) not null,
    content    longblob    not null
);
DROP TABLE IF EXISTS member;
create table member
(
    id          int auto_increment     primary key,
    createdDate datetime(6)            null,
    username    varchar(20)            not null,
    password    varchar(100)           not null,
    role        enum ('ADMIN', 'USER') null
);

