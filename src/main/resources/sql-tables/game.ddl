create schema game;
create schema telegram;

create table telegram.users
(
    id       integer not null
        primary key,
    username varchar(150)
);

create table if not exists game.cards
(
    title       varchar(100),
    description text,
    power       integer
);
