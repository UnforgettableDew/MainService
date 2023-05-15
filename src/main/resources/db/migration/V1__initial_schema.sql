-- drop table if exists users;
--
-- create table users
-- (
--     id       serial primary key,
--     username varchar(64)  not null,
--     password varchar(255),
--     role     varchar(32) not null,
-- );
create table users
(
    id       bigserial primary key,
    username varchar(64)  not null,
    password varchar(255),
    role     varchar(32) not null
);

create table educator
(
    id               bigserial primary key,
    first_name       varchar(64),
    last_name        varchar(64),
    age              int,
    email            varchar(64),
    telegram_contact varchar(64),
    user_id bigint unique,
    foreign key (user_id) references users(id)
        on delete cascade
        on update cascade
);

create table course
(
    id          bigserial primary key,
    title       varchar(64),
    description varchar(255),
    educator_id bigint,
    foreign key (educator_id) references educator (id)
        on delete cascade
        on update cascade
);


create table lw_task
(
    id          bigserial primary key,
    title       varchar(64),
    description varchar(255),
    max_score float,
    course_id   bigint,
    foreign key (course_id) references course (id)
        on delete cascade
        on update cascade
);

create table student
(
    id               bigserial primary key,
    first_name       varchar(64),
    last_name        varchar(64),
    age              int,
    email            varchar(64),
    telegram_contact varchar(64),
    student_group    varchar(64),
    user_id bigint unique,
    registration_date timestamp,
    foreign key (user_id) references users(id)
        on delete cascade
        on update cascade
);

create table laboratory_work
(
    id         bigserial primary key,
    title      varchar(64),
    github_reference varchar(128),
    score float,
    is_passed boolean,
    comment varchar(255),
    student_id bigint,
    task_id   bigint,
    foreign key (task_id) references lw_task (id)
        on delete cascade
        on update cascade,
    foreign key (student_id) references student (id)
        on delete cascade
        on update cascade
);

create table course_student
(
    student_id bigint,
    course_id  bigint,
    foreign key (student_id) references student (id)
        on delete cascade
        on update cascade,
    foreign key (course_id) references course (id)
        on delete cascade
        on update cascade
)
