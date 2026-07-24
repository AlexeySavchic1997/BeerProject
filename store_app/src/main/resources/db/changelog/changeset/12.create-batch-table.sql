--liquibase formatted sql
--changeset Alexey Savchic:batch-table
create type batch_status AS ENUM ('NEW', 'PROCESSING', 'COMPLETE', 'ERROR');
create table batch
(
id bigserial primary key,
created_or_last_modified_date timestamp without time zone,
status batch_status not null,
count integer unique not null,
batch_number varchar unique not null,
set_id bigint references order_set(id)
);