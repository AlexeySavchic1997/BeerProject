--liquibase formatted sql
--changeset Alexey Savchic:beer-brand-table
create table beer_brand
(
id bigserial primary key,
description text,
brand_name varchar(30) unique not null
);
