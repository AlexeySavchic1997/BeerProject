--liquibase formatted sql
--changeset Alexey Savchic:subscription-table
create type type_of_subscription AS ENUM ('Beer of the month', 'Your favorite beer');
create table subscription
(
id bigserial primary key,
subscription_type type_of_subscription,
description text
);