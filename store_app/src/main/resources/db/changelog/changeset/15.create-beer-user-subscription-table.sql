--liquibase formatted sql
--changeset Alexey Savchic:beer-user-subscription
create table beer_user_subscription
(
user_subscription_id bigint references user_subscription (id),
beer_id bigint references beer (id),
primary key (user_subscription_id, beer_id)
);