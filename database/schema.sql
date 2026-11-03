CREATE DATABASE IF NOT EXISTS smart_canteen;
USE smart_canteen;

CREATE TABLE IF NOT EXISTS users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash CHAR(64) NOT NULL,
  role ENUM('STUDENT','ADMIN') NOT NULL DEFAULT 'STUDENT',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS food_items (
  food_id INT PRIMARY KEY AUTO_INCREMENT,
  food_name VARCHAR(100) NOT NULL,
  description VARCHAR(255) DEFAULT '',
  price DECIMAL(10,2) NOT NULL CHECK (price > 0),
  available BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS orders (
  order_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  status ENUM('PENDING','PREPARING','READY','COMPLETED','CANCELLED') NOT NULL DEFAULT 'PENDING',
  order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS order_items (
  order_item_id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  food_id INT NOT NULL,
  food_name VARCHAR(100) NOT NULL,
  quantity INT NOT NULL CHECK (quantity > 0),
  unit_price DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders(order_id),
  CONSTRAINT fk_items_food FOREIGN KEY (food_id) REFERENCES food_items(food_id)
);

INSERT IGNORE INTO users(name, username, password_hash, role)
VALUES ('Canteen Admin', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN');

INSERT INTO food_items(food_name, description, price, available)
SELECT 'Veg Biryani', 'Aromatic vegetable biryani', 80.00, TRUE
WHERE NOT EXISTS (SELECT 1 FROM food_items WHERE food_name='Veg Biryani');
INSERT INTO food_items(food_name, description, price, available)
SELECT 'Fried Rice', 'Vegetable fried rice', 60.00, TRUE
WHERE NOT EXISTS (SELECT 1 FROM food_items WHERE food_name='Fried Rice');
INSERT INTO food_items(food_name, description, price, available)
SELECT 'Tea', 'Hot tea', 15.00, TRUE
WHERE NOT EXISTS (SELECT 1 FROM food_items WHERE food_name='Tea');
INSERT INTO food_items(food_name, description, price, available)
SELECT 'Samosa', 'Crispy potato samosa', 20.00, TRUE
WHERE NOT EXISTS (SELECT 1 FROM food_items WHERE food_name='Samosa');
