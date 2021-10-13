--ALTER TABLE authorities DROP INDEX ix_auth_username;
SET foreign_key_checks = 0;
DROP TABLE  users;
DROP TABLE  authorities;

CREATE TABLE users (
                       username VARCHAR(50) NOT NULL,
                       password VARCHAR(100) NOT NULL,
                       enabled TINYINT NOT NULL DEFAULT 1,
                       PRIMARY KEY (username)
);

CREATE TABLE authorities (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             FOREIGN KEY (username) REFERENCES users(username)
);

CREATE UNIQUE INDEX ix_auth_username
    on authorities (username,authority);

-- User user/pass
INSERT INTO users (username, password, enabled)
values ('user',
        '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
        1);

INSERT INTO users (username,password,enabled)
VALUES ('namhm',
        '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu',
        1);

INSERT INTO users (username,password,enabled)
VALUES ('admin',
        '$2a$10$zxvEq8XzYEYtNjbkRsJEbukHeRx3XS6MDXHMu8cNuNsRfZJWwswDy',
         1);


INSERT INTO authorities (username, authority)
values ('user', 'ROLE_USER');

INSERT INTO authorities (username, authority)
values ('namhm', 'ROLE_USER');

INSERT INTO authorities (username, authority)
values ('admin', 'ROLE_USER');


-- Now enter correct username namhm and password codejava,
-- you will see the homepage as follows:
-- And the second user admin has role ADMIN with password is nimda.
-- Both users are enabled.