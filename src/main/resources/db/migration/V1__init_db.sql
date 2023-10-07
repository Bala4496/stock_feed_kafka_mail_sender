create table email_event_logs
(
    id         serial primary key,
    email_type varchar(32) not null,
    email      varchar(64) not null,
    first_name varchar(32) not null,
    last_name  varchar(32) not null,
    token      varchar(128) not null,
    created_at timestamp   not null default now()
);
