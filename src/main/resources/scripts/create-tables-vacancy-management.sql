CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS "diovanimottacombr";

CREATE TABLE IF NOT EXISTS "diovanimottacombr".candidate (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255),
  document_number VARCHAR(255),
  email VARCHAR(255),
  phone_number VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS "diovanimottacombr".resume (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  candidate_id UUID NOT NULL,
  content JSONB,
  FOREIGN KEY (candidate_id) REFERENCES "diovanimottacombr".candidate(id)
);

CREATE SCHEMA IF NOT EXISTS "seniorcombr";

CREATE TABLE IF NOT EXISTS "seniorcombr".candidate (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255),
  document_number VARCHAR(255),
  email VARCHAR(255),
  phone_number VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS "seniorcombr".resume (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  candidate_id UUID NOT NULL,
  content JSONB,
  FOREIGN KEY (candidate_id) REFERENCES "seniorcombr".candidate(id)
);
