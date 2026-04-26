--liquibase formatted sql
--changeset Alexey Savchic:transaction-outbox
create type saga_status AS ENUM ('STARTED', 'COMPLETED');
create type saga_stage AS ENUM ('ORDER_CRATING');
CREATE TABLE transaction_outbox (
  id UUID NOT NULL PRIMARY KEY,
  status saga_status NOT NULL DEFAULT 'WAITING',
  stage saga_status NOT NULL DEFAULT 'WAITING',
  payload JSONB NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT LOCALTIMESTAMP
);