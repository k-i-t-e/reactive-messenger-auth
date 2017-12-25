# --- !Ups

ALTER TABLE messenger_user ALTER COLUMN id SET DEFAULT nextval('s_messenger_user');

# --- !Downs

ALTER TABLE messenger_user ALTER COLUMN id SET DEFAULT NULL;