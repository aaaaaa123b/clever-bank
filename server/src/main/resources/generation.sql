create sequence banks_id_seq;

alter sequence banks_id_seq owner to postgres;

create table users
(
    id         bigserial
        constraint users_pk
            primary key,
    first_name varchar(128),
    last_name  varchar(128),
    patronymic varchar(128),
    login      varchar(64)
);

alter table users
    owner to postgres;

create table banks
(
    id   integer default nextval('banks_id_seq'::regclass) not null
        constraint banks_pk
            primary key,
    name varchar(30)
);

alter table banks
    owner to postgres;

alter sequence banks_id_seq owned by banks.id;

create table accounts
(
    id           bigserial
        constraint accounts_pk
            primary key,
    balance      numeric(10, 2) not null,
    currency     varchar(3)     not null,
    number       varchar(128)   not null,
    user_id      bigint
        constraint accounts_users_id_fk
            references users,
    bank_id      integer
        constraint accounts__id_fk
            references banks,
    created_date date
);

alter table accounts
    owner to postgres;

create table transactions
(
    id                bigserial
        constraint transactions_pk
            primary key,
    time              time,
    type              varchar(255),
    sender_account    bigint
        constraint sender_account_banks_id_fk
            references accounts,
    recipient_account bigint
        constraint recipient_account_banks_id_fk
            references accounts,
    amount            numeric(10, 2),
    date              date
);

alter table transactions
    owner to postgres;

