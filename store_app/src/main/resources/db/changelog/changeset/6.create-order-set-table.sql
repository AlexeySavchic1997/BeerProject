--liquibase formatted sql
--changeset Alexey Savchic:order-item-table
create table order_set
(
id bigserial primary key,
wave_id bigint references wave(id),
type order_type,
processed_date timestamp without time zone not null
);