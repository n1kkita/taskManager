create table if not exists group_entity (
    id bigint not null auto_increment,
    name varchar(255) not null, primary key (id)
);

create table if not exists group_entity_owners (
    own_groups_id bigint not null,
    owners_id bigint not null
);

create table if not exists tasks (
    id bigint not null auto_increment,
    date_of_create datetime(6),
    date_of_end datetime(6) not null,
    date_of_start datetime(6) not null,
    description varchar(255) not null,
    status enum ('CREATED','DONE','IN_PROCESS','NOT_DONE') not null,
    title varchar(255) not null, group_id bigint,
    user_id bigint, primary key (id)
);
create table if not exists user (
    id bigint not null auto_increment,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    primary key (id)
);
create table if not exists user_group (
    group_id bigint not null,
    user_id bigint not null
);
create table if not exists users_role (
    user_id bigint not null,
    roles enum ('ROLE_ADMIN','ROLE_USER')
);
create table if not exists group_histories (
    id bigint not null auto_increment,
    group_id bigint not null,
    text varchar(255) not null,
    save_at datetime(6) not null,
    primary key (id)
);

alter table user add constraint email_index unique (email);

alter table group_entity_owners add constraint group_entity_owners_owner_id_fk foreign key (owners_id) references user (id);

alter table group_entity_owners add constraint group_entity_owners_group_id_fk foreign key (own_groups_id) references group_entity (id);

alter table tasks add constraint tasks_group_id_fk foreign key (group_id) references group_entity (id);

alter table tasks add constraint tasks_user_id_fk foreign key (user_id) references user (id);

alter table user_group add constraint user_group_user_id_fk foreign key (user_id) references user (id);

alter table user_group add constraint user_group_id_fk foreign key (group_id) references group_entity (id);

alter table users_role add constraint users_role_user_id_fk foreign key (user_id) references user (id);

alter table group_histories add constraint grouphistory_group_fk foreign key (group_id) references group_entity(id);