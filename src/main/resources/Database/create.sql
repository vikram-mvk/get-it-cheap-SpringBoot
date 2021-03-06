CREATE TABLE IF NOT EXISTS users(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    active int NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS items(
    id BIGINT NOT NULL AUTO_INCREMENT,
    itemName VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    category ENUM('Electronics', 'Clothing', 'Outdoor', 'Others') NOT NULL,
    itemType ENUM('for_rent', 'for_sale') NOT NULL,
    image VARCHAR(255),
    price int NOT NULL,
    rentalBasis ENUM('per_hour', 'per_day', 'per_week', 'per_month'),
    userId BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    itemLocation VARCHAR(255) NOT NULL,
    datePosted DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    active int,
    FOREIGN KEY(userId) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS address(
    id BIGINT NOT NULL AUTO_INCREMENT,
    itemId BIGINT NOT NULL,
    itemLocation VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zipcode VARCHAR(255),
    country VARCHAR(255),
    FOREIGN KEY(itemId) REFERENCES items(id) ON UPDATE CASCADE ON DELETE CASCADE,
    PRIMARY KEY (id)
);