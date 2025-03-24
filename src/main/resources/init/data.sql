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
