-- liquibase formatted sql

--changeset kenan:1
CREATE TABLE "USER"
(
    USERNAME   VARCHAR2(60) NOT NULL PRIMARY KEY,
    EMAIL      VARCHAR2(60) NOT NULL,
    FIRST_NAME VARCHAR2(60),
    LAST_NAME  VARCHAR2(60)
);

create table SECTION
(
    TITLE VARCHAR2(60) not null primary key
);

create table BOARD
(
    ID              NUMBER not null primary key,
    TITLE           VARCHAR2(60),
    OWNER_USERNAME  varchar2(60)
        constraint FK_BOARD_OWNER references "USER" (USERNAME),
    PARTICIPANT_IDS VARCHAR2(1000),
    CREATED_DATE    TIMESTAMP,
    UPDATED_DATE    TIMESTAMP
);

create table TICKET
(
    ID           NUMBER not null primary key,
    TITLE        VARCHAR2(60),
    DESCRIPTION  VARCHAR2(60),
    OWNER
        constraint FK_TICKET_OWNER references "USER" (USERNAME),
    ASSIGNEE
        constraint FK_TICKET_ASSIGNEE references "USER" (USERNAME),
    SECTION_TITLE
        constraint FK_TICKET_SECTION references SECTION (TITLE),
    BOARD_ID
        constraint FK_TICKET_BOARD references BOARD (ID),
    CREATED_DATE TIMESTAMP,
    UPDATED_DATE TIMESTAMP
);

create table "COMMENT"
(
    ID           NUMBER not null primary key,
    CONTENT      VARCHAR2(60),
    TICKET_ID    NUMBER
        constraint FK_COMMENT_TICKET references TICKET (ID),
    AUTHOR
        constraint FK_COMMENT_AUTHOR references "USER" (USERNAME),
    CREATED_DATE TIMESTAMP,
    UPDATED_DATE TIMESTAMP
);


alter table "USER" add NICKNAME VARCHAR2(50);