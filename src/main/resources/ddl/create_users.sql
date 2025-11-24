create table users (
    id bigint not null,
    username varchar(50) not null,
    email varchar(100),
    age int,
    created_at timestamp default localtimestamp, 
    updated_at timestamp default localtimestamp, 
    primary key (id)
);
