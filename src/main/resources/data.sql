DROP TABLE device IF EXISTS;

CREATE TABLE device (
  user_id bigint,
  device_id bigint,
  status enum('I', 'A'),
  token VARCHAR(200) PRIMARY KEY
);
CREATE INDEX i_device_id ON device(device_id);

DROP TABLE user IF EXISTS;

CREATE TABLE user (
  id bigint AUTO_INCREMENT,
  pub_cred VARCHAR(200) PRIMARY KEY,
  priv_cred VARCHAR(200)
);

INSERT INTO user (pub_cred, priv_cred) VALUES
  ('testuser', 'password123'),
  ('tester', 'helloThere'),
  ('default', 'password');
