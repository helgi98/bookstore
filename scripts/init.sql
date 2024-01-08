CREATE DATABASE bookstore_service;
CREATE USER bookstore_user WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE bookstore_service TO bookstore_user;
GRANT ALL ON SCHEMA public TO bookstore_user;