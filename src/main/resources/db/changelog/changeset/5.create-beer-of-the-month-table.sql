--liquibase formatted sql
--changeset Alexey Savchic:beer-of-the-mounth-table
create table beer_of_the_month
(
beer_id integer references beers (id) on delete cascade not null,
year integer,
month smallint,
primary key (year,month)
);