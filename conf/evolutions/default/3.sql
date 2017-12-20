# --- !Ups

ALTER TABLE messenger_user ADD COLUMN IF NOT EXISTS password VARCHAR(1000) NOT NULL;

# --- !Downs

ALTER TABLE messenger_user DROP COLUMN password;