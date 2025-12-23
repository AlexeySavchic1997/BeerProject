--liquibase formatted sql
--changeset Alexey Savchic:users-table
create table users
(
id serial primary key,
username varchar(30) unique not null,
password varchar(255) not null,
email varchar(50) check(email like '[A-Za-z0-9._/-]+@[A-Za-z]{2,4}.[A-Za-z]{2,3}]') not null
);
