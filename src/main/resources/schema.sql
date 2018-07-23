use user_service;

DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS permission;
DROP TABLE IF EXISTS role_permission;
DROP TABLE IF EXISTS activity;
DROP TABLE IF EXISTS permission_activity;
DROP TABLE IF EXISTS user_orgs;
DROP TABLE IF EXISTS oauth_client_details;
DROP TABLE IF EXISTS oauth_client_token;
DROP TABLE IF EXISTS oauth_access_token;
DROP TABLE IF EXISTS oauth_refresh_token;
DROP TABLE IF EXISTS oauth_code;

CREATE TABLE IF NOT EXISTS user (
  user_id VARCHAR(36),
  username VARCHAR(100) NOT NULL ,
  password VARCHAR(100) NOT NULL ,
  enabled boolean NOT NULL ,
  PRIMARY KEY (user_id),
  UNIQUE(username));

CREATE TABLE IF NOT EXISTS role (
  role_id VARCHAR(36),
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (role_id));

CREATE TABLE IF NOT EXISTS user_role (
  user_id VARCHAR(36) NOT NULL,
  role_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (user_id, role_id));

CREATE TABLE IF NOT EXISTS permission (
  permission_id VARCHAR(36),
  description VARCHAR(100) NOT NULL,
  PRIMARY KEY (permission_id));

CREATE TABLE IF NOT EXISTS role_permission (
  role_id VARCHAR(36) NOT NULL,
  permission_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (role_id, permission_id));

CREATE TABLE IF NOT EXISTS activity (
  activity_id VARCHAR(36),
  url VARCHAR(100) NOT NULL,
  method VARCHAR(100) NOT NULL,
  url_regex VARCHAR(100) NOT NULL,
  PRIMARY KEY (activity_id));

CREATE TABLE IF NOT EXISTS permission_activity (
  permission_id VARCHAR(36) NOT NULL,
  activity_id VARCHAR(36) NOT NULL,
  PRIMARY KEY (permission_id, activity_id));
  
CREATE TABLE IF NOT EXISTS user_orgs (
  organization_id VARCHAR(100) NOT NULL,
  username        VARCHAR(100) NOT NULL,
  PRIMARY KEY (organization_id, username));
  
  CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_id               VARCHAR(255) PRIMARY KEY,
  resource_ids            VARCHAR(255),
  client_secret           VARCHAR(255),
  scope                   VARCHAR(255),
  authorized_grant_types  VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities             VARCHAR(255),
  access_token_validity   INTEGER,
  refresh_token_validity  INTEGER,
  additional_information  VARCHAR(4096),
  autoapprove             TINYINT
);

CREATE TABLE IF NOT EXISTS oauth_client_token (
  token_id          VARCHAR(255),
  token             BLOB,
  authentication_id VARCHAR(255),
  user_name         VARCHAR(255),
  client_id         VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id          VARCHAR(255),
  token             BLOB,
  authentication_id VARCHAR(255),
  user_name         VARCHAR(255),
  client_id         VARCHAR(255),
  authentication    BLOB,
  refresh_token     VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id       VARCHAR(255),
  token          BLOB,
  authentication BLOB
);

CREATE TABLE IF NOT EXISTS oauth_code (
  code           VARCHAR(255),
  authentication BLOB
);

INSERT INTO user (user_id, username, password, enabled) VALUES ('123e4567-e89b-12d3-a456-426655440000', 'passenger','password', true);
INSERT INTO user (user_id, username, password, enabled) VALUES ('223e4567-e89b-12d3-a456-426655440000', 'driver','password', true);
INSERT INTO user (user_id, username, password, enabled) VALUES ('323e4567-e89b-12d3-a456-426655440000', 'admin','password', true);

INSERT INTO role (role_id, description) VALUES ('123e4567-e89b-12d3-a456-426655440001', 'ROLE_ADMIN');
INSERT INTO role (role_id, description) VALUES ('123e4567-e89b-12d3-a456-426655440002', 'ROLE_USER');
INSERT INTO role (role_id, description) VALUES ('123e4567-e89b-12d3-a456-426655440003', 'ROLE_PASSENGER');
INSERT INTO role (role_id, description) VALUES ('123e4567-e89b-12d3-a456-426655440004', 'ROLE_DRIVER');

INSERT INTO user_role (user_id, role_id) VALUES ('123e4567-e89b-12d3-a456-426655440000', '123e4567-e89b-12d3-a456-426655440002');
INSERT INTO user_role (user_id, role_id) VALUES ('223e4567-e89b-12d3-a456-426655440000', '123e4567-e89b-12d3-a456-426655440002');
INSERT INTO user_role (user_id, role_id) VALUES ('123e4567-e89b-12d3-a456-426655440000', '123e4567-e89b-12d3-a456-426655440003');
INSERT INTO user_role (user_id, role_id) VALUES ('223e4567-e89b-12d3-a456-426655440000', '123e4567-e89b-12d3-a456-426655440004');
INSERT INTO user_role (user_id, role_id) VALUES ('323e4567-e89b-12d3-a456-426655440000', '123e4567-e89b-12d3-a456-426655440001');

--INSERT INTO permission (permission_id, description) VALUES (, '');

--INSERT INTO role_permission (role_id, permission_id) VALUES (, );

--INSERT INTO activity (activity_id, url, method, url_regex) VALUES (, '', '', '');

--INSERT INTO permission_activity (permission_id, activity_id) VALUES (, );

INSERT INTO user_orgs (organization_id, username) VALUES ('d1859f1f-4bd7-4593-8654-ea6d9a6a626e', 'passenger');
INSERT INTO user_orgs (organization_id, username) VALUES ('42d3d4f5-9f33-42f4-8aca-b7519d6af1bb', 'driver');


