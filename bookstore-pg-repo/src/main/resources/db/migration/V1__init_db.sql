CREATE TABLE IF NOT EXISTS "user"
(
    id            TEXT NOT NULL PRIMARY KEY,
    user_name     TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS "book_id_seq";

CREATE TABLE IF NOT EXISTS "book"
(
    id               INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('book_id_seq'),
    title            TEXT    NOT NULL,
    author           TEXT    NOT NULL,
    isbn             TEXT    NOT NULL UNIQUE,
    description      TEXT    NOT NULL,
    pages            INTEGER NOT NULL,
    price            DECIMAL NOT NULL,
    publication_year INTEGER NOT NULL
);