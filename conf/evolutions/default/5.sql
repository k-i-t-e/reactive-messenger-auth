# --- !Ups
CREATE TABLE contact_list (
  user_id BIGINT NOT NULL REFERENCES messenger_user(id),
  contact_id BIGINT NOT NULL REFERENCES messenger_user(id),
  created_date NOT NULL TIMESTAMP,
  PRIMARY KEY(user_id, contact_id)
)