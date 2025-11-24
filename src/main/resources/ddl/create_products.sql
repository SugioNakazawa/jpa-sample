create table products (
    id bigint not null,
    name varchar(50) not null,
    description varchar(100),
    price DECIMAL(10,2),
    stock int,
    category varchar(50),
    created_at timestamp default localtimestamp, 
    updated_at timestamp default localtimestamp, 
    primary key (id)
);
