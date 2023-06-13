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
    username varchar(64) not null,
    password varchar(255),
    role     varchar(32) not null
);

create table educator
(
    id                  bigserial primary key,
    first_name          varchar(64),
    last_name           varchar(64),
    birthday            date,
    email               varchar(64),
    telegram_contact    varchar(64),
    github_access_token varchar(128),
    user_id             bigint unique,
    foreign key (user_id) references users (id)
        on delete cascade
        on update cascade
);

create table course
(
    id          bigserial primary key,
    title       varchar(64),
    description varchar(512),
    educator_id bigint,
    start_date  timestamp,
    end_date    timestamp,
    foreign key (educator_id) references educator (id)
        on delete cascade
        on update cascade
);

create table task
(
    id               bigserial primary key,
    reference        varchar(255),
    title            varchar(64),
    description      varchar(512),
    github_reference varchar(128),
    max_point        float,
    course_id        bigint,
    start_date       timestamp,
    end_date         timestamp,
    foreign key (course_id) references course (id)
        on delete cascade
        on update cascade
);

create table student
(
    id                  bigserial primary key,
    first_name          varchar(64),
    last_name           varchar(64),
    birthday            date,
    email               varchar(64),
    telegram_contact    varchar(64),
    student_group       varchar(64),
    github_access_token varchar(128),
    user_id             bigint unique,
    registration_date   timestamp,
    foreign key (user_id) references users (id)
        on delete cascade
        on update cascade
);

create table passed_task
(
    id               bigserial primary key,
    reference        varchar(255),
    github_reference varchar(128),
    point            float,
    is_assessed      boolean,
    educator_comment varchar(255),
    student_comment  varchar(255),
    submission_date  timestamp,
    student_id       bigint,
    task_id          bigint,
    foreign key (task_id) references task (id)
        on delete cascade
        on update cascade,
    foreign key (student_id) references student (id)
        on delete cascade
        on update cascade
);

create table typical_mistake
(
    id             bigserial primary key,
    title          varchar(64),
    description    varchar(512),
    deducted_point float,
    course_id      bigint,
    foreign key (course_id) references course (id)
        on delete cascade
        on update cascade
);

create table file_to_check
(
    id       bigserial primary key,
    filename varchar(64),
    description varchar(512),
    task_id  bigint,
    foreign key (task_id) references task (id)
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
        on delete no action
        on update cascade
)
