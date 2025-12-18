--liquibase formatted sql
--changeset Alexey Savchic:orders-table
create table orders
(
id serial primary key,
user_id integer references users (id) on delete cascade not null,
order_date timestamp without time zone not null,
summary_price integer check (summary_price>=0) not null
);
