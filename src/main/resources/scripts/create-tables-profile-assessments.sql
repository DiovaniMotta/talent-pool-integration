CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS "diovanimottacombr";

CREATE TABLE IF NOT EXISTS "diovanimottacombr".test_result (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  type VARCHAR(50),
  percent numeric(6,2),
  candidate_id uuid not null
);


CREATE SCHEMA IF NOT EXISTS "seniorcombr";

CREATE TABLE IF NOT EXISTS "seniorcombr".test_result (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  type VARCHAR(50),
  percent numeric(6,2),
  candidate_id uuid not null
);

