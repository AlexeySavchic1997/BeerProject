--liquibase formatted sql
--changeset Alexey Savchic:order-item-table
create type order_set_status AS ENUM ('NEW', 'READY_TO_SPLIT', 'WAITING_FOR_SPLIT');
create type tag AS ENUM ('GENDER', 'LOCATION');
create table order_set
(
id bigserial primary key,
status order_set_status not null,
tag tag,
wave_id bigint references wave(id),
type order_type not null
);