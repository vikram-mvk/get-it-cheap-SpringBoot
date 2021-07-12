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
    category VARCHAR(255) NOT NULL,
    itemType ENUM('for_rent', 'for_sale') NOT NULL,
    image VARCHAR(255),
    price DOUBLE(5,2) NOT NULL,
    rentalBasis ENUM('per_hour', 'per_day', 'per_week', 'per_month'),
    userId BIGINT NOT NULL,
    username VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    datePosted DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    active int NOT NULL,
    FOREIGN KEY(userId) REFERENCES users(id) ON UPDATE CASCADE,
    PRIMARY KEY(id)
);