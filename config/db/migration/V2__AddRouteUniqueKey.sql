SET SCHEMA 'deliveryservice';

ALTER TABLE routes ADD CONSTRAINT routes_unique UNIQUE (origin, destination);