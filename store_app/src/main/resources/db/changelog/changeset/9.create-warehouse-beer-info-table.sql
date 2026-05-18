--liquibase formatted sql
--changeset Alexey Savchic:warehouse-beer-info-table
create type zone_type AS ENUM ('ZONE_SORTING','ZONE_BOOKED', 'ZONE_UNLOADING', 'ZONE_UNSPECIFIED');
create table warehouse_beer_info
(
id bigserial primary key,
sku varchar(30) not null,
beer_id bigint references beer (id),
zone zone_type not null,
amount integer check(amount>=0) not null,
unique (zone, sku)
);