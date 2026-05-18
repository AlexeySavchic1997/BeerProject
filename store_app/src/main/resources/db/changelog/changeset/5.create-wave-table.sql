--liquibase formatted sql
--changeset Alexey Savchic:order-item-table
create type wave_status AS ENUM ('PROCESSING', 'SUCCESSFUL', 'CANCELED');
create type order_type AS ENUM ('REGULAR_ORDER', 'BEER_OF_THE_MONTH', 'YOUR_FAVORITE_BEER');
create table wave
(
id bigserial primary key,
name varchar(70) unique not null,
status wave_status,
type order_type,
processed_date timestamp without time zone not null,
month smallint check(month between 1 and 12) not null,
year smallint check(year between 2024 and 2050) not null
);