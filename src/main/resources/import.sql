INSERT INTO USER_ENTITY (ID, LAST_PASSWORD_CHANGE_AT, PASSWORD, USERNAME) VALUES (9999, '2021-05-23 23:17:02.86153', '$2a$10$2PPPchRsUoqqKXn5u02SDO3FSTeTo./5AbhKTEy3VumXXVagTQBVK', 'maiki');
INSERT INTO USER_ENTITY (ID, LAST_PASSWORD_CHANGE_AT, PASSWORD, USERNAME) VALUES (9998, '2021-05-23 23:18:48.841238', '$2a$10$dToKcrL7tsedq3bHnSsqEu6ufPv02biK3aH2s8xhjBIveB7aYYGQC', 'jovi');
INSERT INTO USER_ENTITY_ROLES (USER_ENTITY_ID, ROLES) VALUES (9999, 'USER');
INSERT INTO USER_ENTITY_ROLES (USER_ENTITY_ID, ROLES) VALUES (9999, 'ADMIN');
INSERT INTO USER_ENTITY_ROLES (USER_ENTITY_ID, ROLES) VALUES (9998, 'USER');
INSERT INTO USER_ENTITY_ROLES (USER_ENTITY_ID, ROLES) VALUES (9998, 'ADMIN');

INSERT INTO PRODUCT_ENTITY (ID, NAME, DESCRIPTION, PRICE) VALUES (9999, 'Zapatillas Nike', 'Air boost max', 130);
INSERT INTO PRODUCT_ENTITY (ID, NAME, DESCRIPTION, PRICE) VALUES (9998, 'Helado Haggen Dazs', 'Flavour of mint and chocolate', 6.9);
INSERT INTO PRODUCT_ENTITY (ID, NAME, DESCRIPTION, PRICE) VALUES (9997, 'TV Samsung OLED65', 'TV with technology pure black 65', 1350);

drop table if exists oauth_client_details;
create table oauth_client_details (client_id VARCHAR(255) PRIMARY KEY, resource_ids VARCHAR(255), client_secret VARCHAR(255), scope VARCHAR(255), authorized_grant_types VARCHAR(255), web_server_redirect_uri VARCHAR(255), authorities VARCHAR(255), access_token_validity INTEGER, refresh_token_validity INTEGER, additional_information VARCHAR(4096), autoapprove VARCHAR(255));

drop table if exists oauth_client_token;
create table oauth_client_token (token_id VARCHAR(255), token LONGVARBINARY, authentication_id VARCHAR(255) PRIMARY KEY, user_name VARCHAR(255), client_id VARCHAR(255));

drop table if exists oauth_access_token;
create table oauth_access_token (token_id VARCHAR(255), token LONGVARBINARY, authentication_id VARCHAR(255) PRIMARY KEY, user_name VARCHAR(255), client_id VARCHAR(255), authentication LONGVARBINARY, refresh_token VARCHAR(255));

drop table if exists oauth_refresh_token;
create table oauth_refresh_token (token_id VARCHAR(255), token LONGVARBINARY, authentication LONGVARBINARY);

drop table if exists oauth_code;
create table oauth_code (code VARCHAR(255), authentication LONGVARBINARY);

drop table if exists oauth_approvals;
create table oauth_approvals (userId VARCHAR(255), clientId VARCHAR(255), scope VARCHAR(255), status VARCHAR(10), expiresAt TIMESTAMP, lastModifiedAt TIMESTAMP);

drop table if exists ClientDetails;
create table ClientDetails (appId VARCHAR(255) PRIMARY KEY, resourceIds VARCHAR(255), appSecret VARCHAR(255), scope VARCHAR(255), grantTypes VARCHAR(255), redirectUrl VARCHAR(255), authorities VARCHAR(255), access_token_validity INTEGER, refresh_token_validity INTEGER, additionalInformation VARCHAR(4096), autoApproveScopes VARCHAR(255));