CREATE TABLE `user`
(
    id         BIGINT PRIMARY KEY,
    social_id  VARCHAR(255)    NOT NULL,
    email      VARCHAR(255)    NOT NULL,
    provider   ENUM ('GOOGLE') NOT NULL,
    roles      VARCHAR(100)    NOT NULL,
    is_active  BIT(1)          NOT NULL,
    created_at TIMESTAMP(6)    NOT NULL,
    updated_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    CONSTRAINT uk_user_provider_social_id UNIQUE (provider, social_id)
);

CREATE TABLE profile
(
    id           BIGINT PRIMARY KEY,
    user_id      BIGINT         NOT NULL,
    first_name   VARCHAR(255)   NOT NULL,
    last_name    VARCHAR(255)   NOT NULL,
    image_url    VARCHAR(255)   NOT NULL,
    time_zone    ENUM ('SEOUL') NOT NULL,
    recap_period TIME(6),
    created_at   TIMESTAMP(6)   NOT NULL,
    updated_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6)
);

CREATE TABLE website_category
(
    id         BIGINT PRIMARY KEY,
    code       ENUM (
                   'STUDY',
                   'SHOPPING',
                   'GAMING',
                   'CONTENT',
                   'COMMUNITY',
                   'NEWS',
                   'FINANCE',
                   'LIFESTYLE',
                   'BROWSING',
                   'DESIGN',
                   'DEVELOPMENT',
                   'AI',
                   'OTHER'
               ) NOT NULL UNIQUE,
    name       VARCHAR(50)  NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6)
);

CREATE TABLE website
(
    id          BIGINT PRIMARY KEY,
    domain      VARCHAR(255) NOT NULL UNIQUE,
    category_id BIGINT,
    favicon_url VARCHAR(500),
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6),
    deleted_at  TIMESTAMP(6)
);

CREATE TABLE page
(
    id          BIGINT PRIMARY KEY,
    website_id  BIGINT       NOT NULL,
    url         VARCHAR(768) NOT NULL UNIQUE,
    title       VARCHAR(500),
    description TEXT,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6),
    deleted_at  TIMESTAMP(6)
);

CREATE TABLE history
(
    id           BIGINT PRIMARY KEY,
    user_id      BIGINT       NOT NULL,
    website_id   BIGINT       NOT NULL,
    page_id      BIGINT       NOT NULL,
    visited_at   TIMESTAMP(6) NOT NULL,
    closed_at    TIMESTAMP(6) NOT NULL,
    is_closed    BIT(1)       NOT NULL,
    scroll_depth INT,
    created_at   TIMESTAMP(6) NOT NULL,
    updated_at   TIMESTAMP(6),
    deleted_at   TIMESTAMP(6)
);

CREATE INDEX idx_user_id_visited_at ON history (user_id, visited_at);
CREATE INDEX idx_user_id_closed_at ON history (user_id, closed_at);

CREATE TABLE user_excluded_website_domain
(
    id         BIGINT PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    domain     VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    CONSTRAINT uk_user_excluded_website_domain UNIQUE (user_id, domain)
);

CREATE TABLE recap
(
    id         BIGINT PRIMARY KEY,
    user_id    BIGINT                       NOT NULL,
    recap_date DATE                         NOT NULL,
    title      VARCHAR(255)                 NOT NULL,
    summary    TEXT                         NOT NULL,
    image_url  VARCHAR(255),
    started_at TIMESTAMP(6)                 NOT NULL,
    closed_at  TIMESTAMP(6)                 NOT NULL,
    model      VARCHAR(255)                 NOT NULL,
    status     ENUM ('COMPLETED', 'FAILED') NOT NULL,
    created_at TIMESTAMP(6)                 NOT NULL,
    updated_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6),
    CONSTRAINT uk_recap_user_date UNIQUE (user_id, recap_date)
);

CREATE TABLE recap_section
(
    id         BIGINT PRIMARY KEY,
    recap_id   BIGINT       NOT NULL,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6),
    deleted_at TIMESTAMP(6)
);

CREATE TABLE recap_timeline
(
    id               BIGINT PRIMARY KEY,
    recap_id         BIGINT       NOT NULL,
    started_at       TIME(6)      NOT NULL,
    ended_at         TIME(6)      NOT NULL,
    title            VARCHAR(255) NOT NULL,
    duration_minutes INT          NOT NULL,
    created_at       TIMESTAMP(6) NOT NULL,
    updated_at       TIMESTAMP(6),
    deleted_at       TIMESTAMP(6)
);

CREATE TABLE recap_topic
(
    id         BIGINT PRIMARY KEY,
    recap_id   BIGINT       NOT NULL,
    keyword    VARCHAR(255) NOT NULL,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    updated_at DATETIME(6),
    deleted_at DATETIME(6)
);
