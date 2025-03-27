-- 한국 (KR)
INSERT INTO users (name, phone, country, role, created_at, created_by, updated_at, updated_by)
VALUES
    ('홍길동', '+821012345678', 'KR', 'PARTNER', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('이매니저', '+821012345679', 'KR', 'ADMIN', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('김고객', '+821012345680', 'KR', 'CUSTOMER', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- 미국 (US)
INSERT INTO users (name, phone, country, role, created_at, created_by, updated_at, updated_by)
VALUES
    ('John Creator', '+14151234567', 'US', 'PARTNER', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('Jane Admin', '+14151234568', 'US', 'ADMIN', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('Mike Customer', '+14151234569', 'US', 'CUSTOMER', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- 일본 (JP)
INSERT INTO users (name, phone, country, role, created_at, created_by, updated_at, updated_by)
VALUES
    ('田中作家', '+819012345678', 'JP', 'PARTNER', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('鈴木管理者', '+819012345679', 'JP', 'ADMIN', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    ('佐藤顧客', '+819012345680', 'JP', 'CUSTOMER', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- DRAFT
INSERT INTO products (partner_id, price, status, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 12000.00, 'DRAFT', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (4, 22000.00, 'DRAFT', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- REQUESTED
INSERT INTO products (partner_id, price, status, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 18000.00, 'REQUESTED', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (7, 25000.00, 'REQUESTED', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- REVIEWING
INSERT INTO products (partner_id, price, status, created_at, created_by, updated_at, updated_by)
VALUES
    (4, 15000.00, 'REVIEWING', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (7, 19900.00, 'REVIEWING', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- REJECTED
INSERT INTO products (partner_id, price, status, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 9000.00, 'REJECTED', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (4, 16000.00, 'REJECTED', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- APPROVED
INSERT INTO products (partner_id, price, status, created_at, created_by, updated_at, updated_by)
VALUES
    (7, 13500.50, 'APPROVED', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (1, 24000.00, 'APPROVED', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- 번역 샘플: product_id = 1, 2, 3 (한국어, 영어, 일본어)
-- Product 1
INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 'KO', '한국어 상품 제목 1', '한국어 상품 설명 1', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (1, 'EN', 'Product Title 1 (EN)', 'Product Description 1 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (1, 'JA', '商品タイトル1 (JA)', '商品説明1 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- Product 2
INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (2, 'KO', '한국어 상품 제목 2', '한국어 상품 설명 2', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (2, 'EN', 'Product Title 2 (EN)', 'Product Description 2 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (2, 'JA', '商品タイトル2 (JA)', '商品説明2 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

-- Product 3
INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (3, 'KO', '한국어 상품 제목 3', '한국어 상품 설명 3', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (3, 'EN', 'Product Title 3 (EN)', 'Product Description 3 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (3, 'JA', '商品タイトル3 (JA)', '商品説明3 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (4, 'KO', '한국어 상품 제목 4', '한국어 상품 설명 4', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (4, 'EN', 'Product Title 4 (EN)', 'Product Description 4 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (4, 'JA', '商品タイトル4 (JA)', '商品説明4 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (5, 'KO', '한국어 상품 제목 5', '한국어 상품 설명 5', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (5, 'EN', 'Product Title 5 (EN)', 'Product Description 5 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (5, 'JA', '商品タイトル5 (JA)', '商品説明5 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (6, 'KO', '한국어 상품 제목 6', '한국어 상품 설명 6', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (6, 'EN', 'Product Title 6 (EN)', 'Product Description 6 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (6, 'JA', '商品タイトル6 (JA)', '商品説明6 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (7, 'KO', '한국어 상품 제목 7', '한국어 상품 설명 7', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (7, 'EN', 'Product Title 7 (EN)', 'Product Description 7 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (7, 'JA', '商品タイトル7 (JA)', '商品説明7 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (8, 'KO', '한국어 상품 제목 8', '한국어 상품 설명 8', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (8, 'EN', 'Product Title 8 (EN)', 'Product Description 8 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (8, 'JA', '商品タイトル8 (JA)', '商品説明8 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (9, 'KO', '한국어 상품 제목 9', '한국어 상품 설명 9', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (9, 'EN', 'Product Title 9 (EN)', 'Product Description 9 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (9, 'JA', '商品タイトル9 (JA)', '商品説明9 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_translations (product_id, language, title, description, created_at, created_by, updated_at, updated_by)
VALUES
    (10, 'KO', '한국어 상품 제목 10', '한국어 상품 설명 10', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (10, 'EN', 'Product Title 10 (EN)', 'Product Description 10 (EN)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (10, 'JA', '商品タイトル10 (JA)', '商品説明10 (JA)', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');

INSERT INTO product_review_histories (product_id, previous_status, new_status, user_id, message, created_at, created_by, updated_at, updated_by)
VALUES
    (1, 'REQUESTED', 'REVIEWING', 2, NULL, CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system'),
    (1, 'REVIEWING', 'REJECTED', 2, '상품 설명 누락됨', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP, 'system');