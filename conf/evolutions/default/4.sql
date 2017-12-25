# --- Ups!

ALTER TABLE messenger_user ALTER COLUMN id SET DEFAULT next_val('s_messenger_user');

# --- Downs!

ALTER TABLE messenger_user ALTER COLUMN id SET DEFAULT NULL;