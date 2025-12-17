--liquibase formatted sql
--changeset Alexey Savchic:orders-table
create table orders
(
id serial primary key,
user_id integer references users (id) on delete cascade not null,
date date not null,
summary_price integer not null
);
