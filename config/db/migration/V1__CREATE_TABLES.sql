CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

SET SCHEMA 'deliveryservice';

CREATE TABLE IF NOT EXISTS points(
    id uuid DEFAULT uuid_generate_v4(),
    name varchar(100) NOT NULL,
    createdAt timestamp,
    createdBy numeric,
    updatedAt timestamp,
    updatedBy numeric,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS routes(
    id uuid DEFAULT uuid_generate_v4(),
    time numeric not null,
    cost numeric not null,
    origin uuid,
    destination uuid,
    createdAt timestamp,
    createdBy numeric,
    updatedAt timestamp,
    updatedBy numeric,
    PRIMARY KEY(id),
    CONSTRAINT "fk_origin_point" FOREIGN KEY(origin) REFERENCES points(id),
    CONSTRAINT "fk_destination_point" FOREIGN KEY(destination) REFERENCES points(id)
);
