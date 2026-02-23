--liquibase formatted sql
--changeset Alexey Savchic:warehouse-beer-info-table
create type zone_type AS ENUM ('SORTING','BOOKED', 'UNLOADING');
create table warehouse_beer_info
(
id bigserial primary key,
beer_id bigint references beer (id),
zone zone_type not null,
amount integer check(amount>=0) not null,
unique (beer_id, zone)
);