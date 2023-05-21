-- liquibase formatted sql

--changeset kenan:1
CREATE TABLE "USER"
(
    USERNAME   VARCHAR2(60)                      NOT NULL PRIMARY KEY,
    FIRST_NAME VARCHAR2(60),
    LAST_NAME  VARCHAR2(60),
    EMAIL      VARCHAR2(60),
    ROLES      VARCHAR2(1000) DEFAULT '["DEVELOPER"]' NOT NULL,
    PASSWORD   VARCHAR(60)                       NOT NULL,
    DELETED    CHAR           DEFAULT 'N'        NOT NULL
);

create table SECTION
(
    SECTION_NAME VARCHAR2(60) not null primary key
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
    TITLE        VARCHAR2(500),
    DESCRIPTION  VARCHAR2(2000),
    OWNER_USERNAME
        constraint FK_TICKET_OWNER references "USER" (USERNAME),
    ASSIGNEE_USERNAME
        constraint FK_TICKET_ASSIGNEE references "USER" (USERNAME),
    SECTION_TITLE
        constraint FK_TICKET_SECTION references SECTION (SECTION_NAME),
    BOARD_ID
        constraint FK_TICKET_BOARD references BOARD (ID),
    CREATED_DATE TIMESTAMP,
    UPDATED_DATE TIMESTAMP
);

create table "COMMENT"
(
    ID           NUMBER not null primary key,
    CONTENT      VARCHAR2(2000),
    TICKET_ID    NUMBER
        constraint FK_COMMENT_TICKET references TICKET (ID),
    AUTHOR_USERNAME
        constraint FK_COMMENT_AUTHOR references "USER" (USERNAME),
    CREATED_DATE TIMESTAMP,
    UPDATED_DATE TIMESTAMP
);


create sequence BOARDS_SEQUENCE increment by 1 nocache;
create sequence TICKETS_SEQUENCE increment by 1 nocache;
create sequence COMMENTS_SEQUENCE increment by 1 nocache;

insert into "USER" (USERNAME, FIRST_NAME, LAST_NAME, EMAIL, ROLES, PASSWORD, DELETED)
values ('admin', 'ADMIN', 'ADMIN', null, '["MAINTAINER", "DEVELOPER"]',
        '$2a$10$hMUG.RFBiHCiVXdO.Vvr2eGt7qW6s7u0mjMKmXsrpIvxcz8CUPkDW', 'N');
-- the password for the initial access user 'admin' is 'admin'
insert into SECTION (SECTION_NAME)
VALUES ('Open');
insert into SECTION (SECTION_NAME)
VALUES ('Ready For Dev');
insert into SECTION (SECTION_NAME)
VALUES ('In Development');
insert into SECTION (SECTION_NAME)
VALUES ('UAT');
insert into SECTION (SECTION_NAME)
VALUES ('Done');
