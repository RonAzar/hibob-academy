create table pets
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(100) not null ,
    type varchar(100) not null ,
    company_id int,
    date_of_arrival date not null
);

CREATE INDEX idx_company_id ON pets(company_id);