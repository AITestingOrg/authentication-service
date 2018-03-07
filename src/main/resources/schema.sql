use user_service;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS permission;
DROP TABLE IF EXISTS role_permission;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS permission_activity;

CREATE TABLE IF NOT EXISTS user (
  user_id SERIAL,
  username VARCHAR(100) NOT NULL ,
  password VARCHAR(100) NOT NULL ,
  enabled boolean NOT NULL ,
  PRIMARY KEY (username));

CREATE TABLE role (
  role_id SERIAL,
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (role_id));

CREATE TABLE user_role (
  user_id BIGINT UNSIGNED NOT NULL,
  role_id BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (user_id, role_id));

CREATE TABLE permission (
  permission_id SERIAL,
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (permission_id));

CREATE TABLE role_permission (
  role_id BIGINT UNSIGNED NOT NULL,
  permission_id BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (role_id, permission_id));

CREATE TABLE activity (
  activity_id SERIAL,
  url VARCHAR(100) NOT NULL,
  method VARCHAR(100) NOT NULL,
  url_regex VARCHAR(100) NOT NULL,
  PRIMARY KEY (activity_id));

CREATE TABLE permission_activity (
  permission_id BIGINT UNSIGNED NOT NULL,
  activity_id BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (permission_id, activity_id));

INSERT INTO user (user_id, username,password,enabled) VALUES (1, 'user1','password', true);
INSERT INTO user (user_id, username,password,enabled) VALUES (2, 'user2','password', true);
INSERT INTO user (user_id, username,password,enabled) VALUES (3, 'user3','password', true);

INSERT INTO role (role_id, description) VALUES (1, 'ROLE_ADMIN');
INSERT INTO role (role_id, description) VALUES (2, 'ROLE_USER');
INSERT INTO role (role_id, description) VALUES (3, 'ROLE_PASSENGER');
INSERT INTO role (role_id, description) VALUES (4, 'ROLE_DRIVER');

INSERT INTO user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (1, 3);
INSERT INTO user_role (user_id, role_id) VALUES (2, 4);
INSERT INTO user_role (user_id, role_id) VALUES (3, 1);

--INSERT INTO permision (permission_id, description) VALUES (, '');

--INSERT INTO role_permision (role_id, permission_id) VALUES (, );

--INSERT INTO activity (activity_id, url, method, url_regex) VALUES (, '', '', '');

--INSERT INTO permission_activity (permission_id, activity_id) VALUES (, );

