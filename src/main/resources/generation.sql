CREATE DATABASE "dbCleverBank"
    WITH OWNER "postgres";

CREATE TABLE public.users
(
    id BIGSERIAL
        NOT NULL
        CONSTRAINT users_pk PRIMARY KEY,
    first_name VARCHAR(128),
    last_name VARCHAR(128),
    patronymic VARCHAR(128),
    login VARCHAR(64)
);

ALTER TABLE public.users
    OWNER TO postgres;

CREATE TABLE public.accounts
(
    id BIGSERIAL
        NOT NULL
        CONSTRAINT accounts_pk PRIMARY KEY,
    balance NUMERIC(10, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    number VARCHAR(10) NOT NULL,
    user_id BIGINT
        CONSTRAINT accounts_users_id_fk
        REFERENCES users(id)
);

ALTER TABLE public.accounts
    OWNER TO postgres;

CREATE TABLE public.banks
(
    id BIGSERIAL
        NOT NULL
        CONSTRAINT banks_pk PRIMARY KEY,
    name VARCHAR(30)
);

ALTER TABLE public.banks
    OWNER TO postgres;

CREATE TABLE public.transactions
(
    id BIGSERIAL
        NOT NULL
        CONSTRAINT transactions_pk PRIMARY KEY,
    time time,
    type VARCHAR(30),
    sender_account BIGINT
        CONSTRAINT sender_account_banks_id_fk
        REFERENCES accounts(id),
    sender_bank_id BIGINT
        CONSTRAINT sender_bank_banks_id_fk
        REFERENCES banks(id),
    recipient_account BIGINT
        CONSTRAINT recipient_bank_banks_id_fk
        REFERENCES accounts(id),
    recipient_bank_id BIGINT
        CONSTRAINT recipient_bank_banks_id_fk
        REFERENCES banks(id),
    amount NUMERIC(10, 2)
);

ALTER TABLE public.transactions
    OWNER TO postgres;

