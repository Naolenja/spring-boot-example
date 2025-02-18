ALTER TABLE customer ADD COLUMN gender TEXT;

UPDATE customer
SET gender = 'MALE'
WHERE gender IS NULL;

