--liquibase formatted sql
--changeset Alexey Savchic:warehouse-beer-info-table
create table warehouse_beer_info
(
beer_id integer references beers (id),
zone varchar(20),
amount integer not null,
primary key (beer_id, zone)
);