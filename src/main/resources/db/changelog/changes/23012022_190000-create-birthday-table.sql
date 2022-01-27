CREATE TABLE birthday (
    id SERIAL PRIMARY KEY,
    name VARCHAR(150),
    date DATE NOT NULL,
    photo BYTEA
)