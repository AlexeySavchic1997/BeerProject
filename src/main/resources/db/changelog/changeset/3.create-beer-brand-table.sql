--liquibase formatted sql
--changeset Alexey Savchic:beer-brand-table
create table beer_brand
(
id serial primary key,
brand_name varchar(30) not null
);
