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

INSERT INTO user (user_id, username, password, enabled) VALUES ('4eaf29bc-3909-49d4-a104-3d17f68ba672', 'passenger','password', true);
INSERT INTO user (user_id, username, password, enabled) VALUES ('6ed6e72c-10ae-4908-ae74-f7e77d6dc18b', 'driver','password', true);
INSERT INTO user (user_id, username, password, enabled) VALUES ('35dd0d23-6a4f-474e-9246-461538456909', 'admin','password', true);

INSERT INTO role (role_id, description) VALUES ('429dabde-b407-42b8-b4a4-ad46874fb9ae', 'ROLE_ADMIN');
INSERT INTO role (role_id, description) VALUES ('3c77eaee-c60b-417b-92f3-d7503548b02b', 'ROLE_USER');
INSERT INTO role (role_id, description) VALUES ('2b992093-cb98-472b-b53b-eaca27f9133d', 'ROLE_PASSENGER');
INSERT INTO role (role_id, description) VALUES ('e723842b-9dfb-4f3e-9967-37d10642028f', 'ROLE_DRIVER');

INSERT INTO user_role (user_id, role_id) VALUES ('4eaf29bc-3909-49d4-a104-3d17f68ba672', '3c77eaee-c60b-417b-92f3-d7503548b02b');
INSERT INTO user_role (user_id, role_id) VALUES ('6ed6e72c-10ae-4908-ae74-f7e77d6dc18b', '3c77eaee-c60b-417b-92f3-d7503548b02b');
INSERT INTO user_role (user_id, role_id) VALUES ('4eaf29bc-3909-49d4-a104-3d17f68ba672', '2b992093-cb98-472b-b53b-eaca27f9133d');
INSERT INTO user_role (user_id, role_id) VALUES ('6ed6e72c-10ae-4908-ae74-f7e77d6dc18b', 'e723842b-9dfb-4f3e-9967-37d10642028f');
INSERT INTO user_role (user_id, role_id) VALUES ('35dd0d23-6a4f-474e-9246-461538456909', '429dabde-b407-42b8-b4a4-ad46874fb9ae');

--INSERT INTO permission (permission_id, description) VALUES (, '');

--INSERT INTO role_permission (role_id, permission_id) VALUES (, );

--INSERT INTO activity (activity_id, url, method, url_regex) VALUES (, '', '', '');

--INSERT INTO permission_activity (permission_id, activity_id) VALUES (, );

INSERT INTO user_orgs (organization_id, username) VALUES ('93f12c23-8efb-4365-b923-b3ce5b466b6d', 'passenger');
INSERT INTO user_orgs (organization_id, username) VALUES ('93f12c23-8efb-4365-b923-b3ce5b466b6d', 'driver');


