drop table if exists pot;
drop table if exists pot_state;
drop table if exists tank_state;

create table pot (
        id serial,
        code varchar(255) not null,
        name varchar(255) not null,
        min_humidity integer not null,
        check_interval integer  not null,
        watering_duration integer not null,
        primary key (id)
);

create table pot_state (
        id serial,
        pot_id bigint not null,
        date timestamptz not null,
        humidity decimal not null,
        watering boolean not null,
        primary key (id)
);

create table tank_state (
        id serial,
        date timestamptz not null,
        filled decimal not null,
        code varchar(255) not null,
        volume decimal not null,
        primary key (id)
);

create index POT_STATE_DATE on pot_state(date, pot_id);
create index TANK_STATE_DATE on tank_state(date);