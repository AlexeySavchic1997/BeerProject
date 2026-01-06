--liquibase formatted sql
--changeset Alexey Savchic:beer-table
create table beer
(
id bigserial primary key,
name varchar(20) not null,
description text,
volume decimal(4,3)  check (volume>0) not null,
price decimal(7,2)  check (price>0) not null,
brand_id bigint references beer_brand (id) on delete cascade not null
);