--liquibase formatted sql
--changeset Alexey Savchic:beer-charasteristic-table
create table beer_characteristics
(
beer_id integer references beers (id) on delete cascade,
characteristic varchar(3),
value smallint not null,
primary key (beer_id,characteristic)
);