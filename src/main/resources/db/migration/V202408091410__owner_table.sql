create table owner(
    id serial primary key ,
    name varchar(100) not null ,
    company_id int not null,
    employee_id varchar(100) not null
);

CREATE INDEX idx_owner_owner_id ON owner(owner_id);
CREATE INDEX idx_employee_id ON owner(employee_id);