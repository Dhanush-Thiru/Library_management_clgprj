CREATE DATABASE IF NOT EXISTS library_db;

USE library_db;

DROP TABLE IF EXISTS books;

CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    is_issued BOOLEAN NOT NULL DEFAULT 0
);

INSERT INTO books (title, author, isbn, is_issued) VALUES
('The Secret Garden', 'Frances Hodgson Burnett', '978-0140367372', 0),
('Dune', 'Frank Herbert', '978-0441172719', 0),
('1984', 'George Orwell', '978-0451524935', 1);

SELECT * FROM books;
