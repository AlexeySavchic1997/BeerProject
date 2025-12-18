--liquibase formatted sql
--changeset Alexey Savchic:beers-table
create table beers
(
id serial primary key,
name varchar(20) not null,
volume smallint check (volume>0) not null,
price integer check (price>0) not null,
brand_id integer references beer_brand (id) on delete cascade not null
);