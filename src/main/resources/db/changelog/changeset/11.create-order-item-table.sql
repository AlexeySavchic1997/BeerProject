--liquibase formatted sql
--changeset Alexey Savchic:order-item-table
create table order_item
(
order_id integer references orders (id) on delete cascade,
beer_id integer references beer (id) on delete cascade,
quantity integer check(quantity>0) not null,
price double precision check (price>=0) not null,
primary key (order_id, beer_id)
);