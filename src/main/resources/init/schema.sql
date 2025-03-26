CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    phone      VARCHAR(20),
    role       VARCHAR(20)  NOT NULL,
    country    VARCHAR(3)   NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS products
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    partner_id BIGINT         NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    status     VARCHAR(20)    NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by VARCHAR(100),
    CONSTRAINT fk_product_partner FOREIGN KEY (partner_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS product_translations
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id  BIGINT       NOT NULL,
    language    VARCHAR(10)  NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by  VARCHAR(100),
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by  VARCHAR(100),
    CONSTRAINT fk_translation_product FOREIGN KEY (product_id) REFERENCES products (id)
);

CREATE TABLE IF NOT EXISTS product_review_histories
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id      BIGINT      NOT NULL,
    previous_status VARCHAR(20) NOT NULL,
    new_status      VARCHAR(20) NOT NULL,
    user_id      BIGINT      NOT NULL,
    reason          TEXT,
    snapshot_title_ko       VARCHAR(255) NULL,
    snapshot_description_ko TEXT         NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by      VARCHAR(100),
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    updated_by      VARCHAR(100),
    CONSTRAINT fk_review_history_product FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_review_history_user FOREIGN KEY (user_id) REFERENCES users (id)
);

