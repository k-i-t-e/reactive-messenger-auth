# messenger schema

# --- !Ups

CREATE TABLE messenger_user (
  id BIGINT PRIMARY KEY,
  name VARCHAR(127) NOT NULL
);

# --- !Downs

DROP TABLE messenger_user;