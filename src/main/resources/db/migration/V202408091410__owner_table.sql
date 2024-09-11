create table owner(
    id BIGSERIAL primary key ,
    name varchar(100) not null ,
    company_id BIGINT not null,
    employee_id varchar(100) not null
);

CREATE INDEX idx_owner_company_id_employee_id ON owner(company_id, employee_id);