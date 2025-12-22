--liquibase formatted sql
--changeset Alexey Savchic:subscription-table
create type type_of_subscription AS ENUM ('Beer of the month', 'Your favorite beer');
create table subscription
(
subscription_type type_of_subscription primary key,
description text
);