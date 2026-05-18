--liquibase formatted sql
--changeset Alexey Savchic:user-subscription-table
create table user_subscription
(
id bigserial primary key,
user_id bigint references users (id) on delete cascade,
subscription_id bigint references subscription (id),
subscribe_date timestamp without time zone not null,
time_of_expiration timestamp without time zone not null
);
