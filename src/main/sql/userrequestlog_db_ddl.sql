CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE user_request_log (
	id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
	user_id uuid NOT NULL,
	max_payment_value_cents integer NOT NULL,
	created timestamp NOT NULL default current_timestamp,
	updated timestamp NOT NULL default current_timestamp
)
WITH (
	OIDS=FALSE
) ;