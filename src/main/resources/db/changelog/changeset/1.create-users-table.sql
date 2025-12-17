--liquibase formatted sql
--changeset Alexey Savchic:users-table
create table users
(
id serial primary key,
username varchar(30) not null,
password varchar(255) not null,
email varchar(30) not null
);
