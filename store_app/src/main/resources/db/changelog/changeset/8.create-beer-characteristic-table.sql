--liquibase formatted sql
--changeset Alexey Savchic:beer-charasteristic-table
create type beer_characteristic_type AS ENUM ('ABV', 'OG', 'IBU', 'EBC');
create table beer_characteristic
(
beer_id bigint references beer (id) on delete cascade,
characteristic beer_characteristic_type not null,
value decimal(3,1) not null,
primary key (beer_id,characteristic)
);