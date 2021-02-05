SET SCHEMA 'deliveryservice';

ALTER TABLE points ADD CONSTRAINT points_unique UNIQUE (name);