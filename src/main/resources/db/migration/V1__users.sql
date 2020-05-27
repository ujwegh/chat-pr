create table user_roles
(
    user_id int8 not null,
    role    varchar(255)
);

create table users
(
    id              bigserial    not null,
    created_date    timestamp    not null,
    updated_date    timestamp    not null,
    email           varchar(255) not null,
    enabled         boolean,
    first_name      varchar(255),
    gender          varchar(255),
    image_url       varchar(255),
    last_login_date timestamp,
    last_name       varchar(255),
    password        varchar(255) not null,
    phone           varchar(255),
    primary key (id)
);

alter table user_roles add constraint FK_user_roles foreign key (user_id) references users;