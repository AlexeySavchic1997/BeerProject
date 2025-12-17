--liquibase formatted sql
--changeset Alexey Savchic:subscription-table
create table subscription
(
id serial primary key,
beer_id integer references beers (id) not null,
time_of_expiration timestamp without time zone not null,
type_of_subscribe varchar(20) not null
);