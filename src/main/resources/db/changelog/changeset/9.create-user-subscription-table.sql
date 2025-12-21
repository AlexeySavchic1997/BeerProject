--liquibase formatted sql
--changeset Alexey Savchic:user-subscription-table
create table user_subscription
(
id serial primary key,
user_id integer references users (id) on delete cascade,
beer_id integer references beers (id) not null,
subscription_type type_of_subscription references subscription (subscription_type),
time_of_expiration timestamp without time zone not null
);
