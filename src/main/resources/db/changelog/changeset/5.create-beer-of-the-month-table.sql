--liquibase formatted sql
--changeset Alexey Savchic:beer-of-the-mounth-table
create table beer_of_the_month
(
beer_id integer references beers (id) on delete cascade not null,
year integer check(year between 2024 and 2050) not null,
month smallint check(month between 1 and 12),
primary key (year,month)
);