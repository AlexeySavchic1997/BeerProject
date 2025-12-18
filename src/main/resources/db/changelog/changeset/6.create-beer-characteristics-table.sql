--liquibase formatted sql
--changeset Alexey Savchic:beer-charasteristic-table
create type beer_charasteristic AS ENUM ('ABV', 'OG', 'IBU', 'EBC');
create table beer_characteristics
(
beer_id integer references beers (id) on delete cascade,
characteristic beer_charasteristic not null,
value smallint not null,
primary key (beer_id,characteristic)
);