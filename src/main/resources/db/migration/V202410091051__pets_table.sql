create table pets
(
    id BIGSERIAL primary key ,
    name varchar(100) not null ,
    type varchar(100) not null ,
    company_id BIGINT not null,
    date_of_arrival date default CURRENT_DATE
);

-- --Insert data to your pet table for some pets.
--
-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Buddy', 'Dog', 1, '2024-09-06');
--
-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Waffle', 'Dog', 1, '2024-09-06');
--
--
-- --Retrieve the pets by type (dog, cat..)
--
-- Select Distinct type
-- From pets
--
--
-- --Delete pet by id
--          Delete from pets
-- Where (id = 2)
--
--
-- --Get the pets that arrived more than a year ago
-- Select *
-- from pets
-- where date_of_arrival < (CURRENT_DATE - INTERVAL '1 year')