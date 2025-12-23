--liquibase formatted sql
--changeset Alexey Savchic:beer-table
create table beer
(
id serial primary key,
name varchar(20) not null,
description text,
volume real check (volume>0) not null,
price real check (price>0) not null,
brand_id integer references beer_brand (id) on delete cascade not null
);