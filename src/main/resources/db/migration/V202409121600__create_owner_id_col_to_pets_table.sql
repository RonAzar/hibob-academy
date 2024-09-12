-- Step 1: Add the new column owner_id
ALTER TABLE pets
    ADD COLUMN owner_id BIGINT;