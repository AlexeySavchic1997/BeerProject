--liquibase formatted sql
--changeset Alexey Savchic:orders-table
create type order_status AS ENUM ('PROCESSING', 'SUCCESSFUL', 'CANCELED');
create table orders
(
id bigserial primary key,
user_id bigint references users (id) on delete cascade not null,
set_id bigint references order_set (id),
wave_id bigint references wave (id),
order_date timestamp without time zone not null,
summary_price decimal(7,2) check (summary_price>=0) not null,
type order_type not null,
status order_status not null
);
