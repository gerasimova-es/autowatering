drop table if exists hibernate_sequence;
drop table if exists pot;
drop table if exists pot_state;
drop table if exists tank_state;

create table hibernate_sequence (next_val bigint);
insert into hibernate_sequence values (1);
insert into hibernate_sequence values (1);
insert into hibernate_sequence values (1);

create table pot (
       id bigint not null,
        code varchar(255) not null,
        humidity double,
        name varchar(255) not null,
        primary key (id)
);
create table pot_state (
       id bigint not null,
        date date not null,
        humidity double not null,
        pot_id bigint not null,
        primary key (id)
);
create table tank_state (
       id bigint not null,
        date date not null,
        filled double not null,
        code varchar(255) not null,
        volume double not null,
        primary key (id)
);
create index POT_STATE_DATE on pot_state(date, pot_id);
create index TANK_STATE_DATE on tank_state(date);