create table owner(
    owner_id UUID primary key default gen_random_uuid(),
    name varchar(100) not null ,
    company_id int,
    employee_id int
)

CREATE INDEX idx_owner_id_employee_id ON owner(owner_id,employee_id);