--liquibase formatted sql
--changeset Alexey Savchic:warehouse-beer-info-table
create type zone_type AS ENUM ('shipment', 'booked', 'sorting', 'unloading');
create table warehouse_beer_info
(
beer_id integer references beers (id),
zone zone_type not null,
amount integer check(amount>=0) not null,
primary key (beer_id, zone)
);