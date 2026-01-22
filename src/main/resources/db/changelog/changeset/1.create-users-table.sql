--liquibase formatted sql
--changeset Alexey Savchic:users-table
create table users
(
id bigserial primary key,
username varchar(30) unique not null,
password varchar(255) not null,
email varchar(50) check(email ~ '[A-Za-z0-9._/-]+@[A-Za-z]{2,5}\.[A-Za-z]{2,3}') not null
);
