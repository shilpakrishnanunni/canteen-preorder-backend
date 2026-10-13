-- DROP TABLE IF EXISTS order_items;
-- DROP TABLE IF EXISTS orders;
-- DROP TABLE IF EXISTS menu_items;
-- DROP TABLE IF EXISTS menu_categories;
-- DROP TABLE IF EXISTS users;
--
-- DROP TYPE IF EXISTS user_role_enum;
-- DROP TYPE IF EXISTS order_status_enum;

-- CREATE TYPE user_role_enum AS ENUM ('USER', 'STAFF', 'ADMIN');
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
--     role user_role_enum NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS menu_categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) UNIQUE NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS menu_items (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES menu_categories(id),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    image_url VARCHAR(500),
    available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- CREATE TYPE order_status_enum AS ENUM ('PLACED', 'ACCEPTED', 'PREPARING', 'READY', 'COMPLETED', 'CANCELLED', 'REJECTED');
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    order_token VARCHAR(32) NOT NULL UNIQUE,
--     status order_status_enum DEFAULT 'PLACED',
    status VARCHAR(20) NOT NULL DEFAULT 'PLACED',
    total_amount DECIMAL(10,2) NOT NULL CHECK (total_amount >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id),
    menu_item_id BIGINT NOT NULL REFERENCES menu_items(id),
    quantity INTEGER NOT NULL CHECK (quantity > 0),
    unit_price DECIMAL(10,2) NOT NULL CHECK (unit_price >= 0),

    UNIQUE (order_id, menu_item_id)
);

INSERT INTO users (name, email, password, role)
VALUES
    ('John Doe', 'john@example.com', '$2a$10$HgnDmLCq7l9hzV/TgG2XZ.ZRPbylwXHip4txMPkx9oi1SWlscgVqW', 'USER'),
    ('Jane Smith', 'jane@example.com', '$2a$10$HgnDmLCq7l9hzV/TgG2XZ.ZRPbylwXHip4txMPkx9oi1SWlscgVqW', 'USER'),
    ('Bob Johnson', 'bob@example.com', '$2a$10$HgnDmLCq7l9hzV/TgG2XZ.ZRPbylwXHip4txMPkx9oi1SWlscgVqW', 'USER'),
    ('Alice Williams', 'alice@example.com', '$2a$10$HgnDmLCq7l9hzV/TgG2XZ.ZRPbylwXHip4txMPkx9oi1SWlscgVqW', 'STAFF'),
    ('Charlie Brown', 'charlie@example.com', '$2a$10$HgnDmLCq7l9hzV/TgG2XZ.ZRPbylwXHip4txMPkx9oi1SWlscgVqW', 'ADMIN');

INSERT INTO menu_categories (name, slug)
VALUES
    ('Beverages', 'beverages'),
    ('Snacks', 'snacks'),
    ('Biscuits', 'biscuits'),
    ('Chocolates', 'chocolates');

INSERT INTO menu_items (
    category_id,
    name,
    description,
    price,
    available
)
VALUES
    (
        (SELECT id FROM menu_categories WHERE name = 'Beverages'),
        'Filter Coffee',
        'Hot South Indian filter coffee',
        20.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Beverages'),
        'Tea',
        'Hot black tea',
        15.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Beverages'),
        'Lime Juice',
        'Fresh lime juice',
        25.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Beverages'),
        'Cold Coffee',
        'Chilled coffee with milk',
        50.00,
        FALSE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Snacks'),
        'Veg Puff',
        'Vegetable puff',
        18.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Snacks'),
        'Samosa',
        'Crispy potato samosa',
        15.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Snacks'),
        'Banana Fry',
        'Crispy fried banana slices',
        25.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Biscuits'),
        'Hide and Seek',
        'Classic chocolate chip biscuit',
        30.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Biscuits'),
        'Good Day',
        'Classic butter biscuit',
        30.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Biscuits'),
        'Marie Gold',
        'Light tea biscuit',
        25.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Chocolates'),
        'Cadbury Dairy Milk 13g',
        'Small Dairy Milk chocolate bar',
        10.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Chocolates'),
        '5 Star',
        'Chocolate bar with caramel',
        20.00,
        TRUE
    ),
    (
        (SELECT id FROM menu_categories WHERE name = 'Chocolates'),
        'KitKat',
        'Chocolate-covered wafer',
        40.00,
        TRUE
    );

INSERT INTO orders (user_id, order_token, status, total_amount)
VALUES
    (
        (SELECT id FROM users WHERE email = 'john@example.com'),
        'ABC123',
        'COMPLETED',
        70.00
    ),
    (
        (SELECT id FROM users WHERE email = 'jane@example.com'),
        'DEF123',
        'PREPARING',
        55.00
    ),
    (
        (SELECT id FROM users WHERE email = 'bob@example.com'),
        'GHI123',
        'PLACED',
        100.00
    ),
    (
        (SELECT id FROM users WHERE email = 'john@example.com'),
        'JKL123',
        'CANCELLED',
        40.00
    );

INSERT INTO order_items (order_id, menu_item_id, quantity, unit_price)
VALUES
    (
        (SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'john@example.com')
                                 AND status = 'COMPLETED' LIMIT 1),
    (SELECT id FROM menu_items WHERE name = 'Filter Coffee'),
    2,
    20.00
    ),
    (
        (SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE email = 'john@example.com')
         AND status = 'COMPLETED' LIMIT 1),
        (SELECT id FROM menu_items WHERE name = 'Veg Puff'),
        1,
        30.00
    );

CREATE INDEX IF NOT EXISTS idx_menu_items_category_id
    ON menu_items(category_id);

CREATE INDEX IF NOT EXISTS idx_orders_user_id
    ON orders(user_id);

CREATE INDEX IF NOT EXISTS idx_orders_status
    ON orders(status);

CREATE INDEX IF NOT EXISTS idx_order_items_order_id
    ON order_items(order_id);

CREATE INDEX IF NOT EXISTS idx_order_items_menu_item_id
    ON order_items(menu_item_id);