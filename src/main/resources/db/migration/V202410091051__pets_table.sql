create table pets
(
    id serial primary key ,
    name varchar(100) not null ,
    type varchar(100) not null ,
    company_id int not null,
    date_of_arrival date default CURRENT_DATE
);

CREATE INDEX idx_pets_company_id ON pets(company_id);

-- --Insert data to your pet table for some pets.
--
-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Buddy', 'Dog', "Shi-Tzu", '2024-09-06');
--
-- INSERT INTO pets (name, type, company_id, date_of_arrival)
-- VALUES ('Waffle', 'Dog', "Terrier", '2024-09-06');
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