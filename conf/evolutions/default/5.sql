# --- !Ups
CREATE TABLE IF NOT EXISTS contact_list (
  user_id BIGINT NOT NULL REFERENCES messenger_user(id),
  contact_id BIGINT NOT NULL REFERENCES messenger_user(id),
  created_date TIMESTAMP NOT NULL,
  PRIMARY KEY(user_id, contact_id)
);

# --- !Downs
DROP TABLE IF EXISTS contact_list;