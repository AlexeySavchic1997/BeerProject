--liquibase formatted sql
--changeset Alexey Savchic:role-table
create table role
(
user_id integer references users (id) on delete cascade not null,
role varchar(20) not null
);
