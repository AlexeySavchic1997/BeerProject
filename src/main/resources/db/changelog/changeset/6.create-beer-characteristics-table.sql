--liquibase formatted sql
--changeset Alexey Savchic:beer-charasteristic-table
create type beer_charasteristic_type AS ENUM ('ABV', 'OG', 'IBU', 'EBC');
create table beer_characteristic
(
beer_id integer references beers (id) on delete cascade,
characteristic beer_charasteristic_type not null,
value smallint not null,
primary key (beer_id,characteristic)
);