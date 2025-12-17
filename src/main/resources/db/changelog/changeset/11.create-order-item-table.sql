--liquibase formatted sql
--changeset Alexey Savchic:order-item-table
create table order_item
(
order_id integer references orders (id) on delete cascade,
beer_id integer references beers (id) on delete cascade,
primary key (order_id, beer_id)
);