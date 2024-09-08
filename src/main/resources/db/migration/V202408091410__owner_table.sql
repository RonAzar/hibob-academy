create table owner(
    owner_id serial primary key ,
    name varchar(100) not null ,
    company_id int,
    employee_id int
);

CREATE INDEX idx_owner_id ON owner(owner_id);
CREATE INDEX idx_employee_id ON owner(employee_id);