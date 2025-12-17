--liquibase formatted sql
--changeset Alexey Savchic:user-subscription-table
create table user_subscription
(
user_id integer references users (id) on delete cascade,
subscription_id integer references subscription (id) on delete cascade,
primary key (user_id,subscription_id)
);
