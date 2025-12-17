--liquibase formatted sql
--changeset Alexey Savchic:roles-table
create table roles
(
user_id integer references users (id) on delete cascade not null,
role varchar(20) not null
);
