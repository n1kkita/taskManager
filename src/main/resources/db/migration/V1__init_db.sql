
create table if not exists group_entity (
                              owner_id bigint not null,
                              name varchar(255) not null,
                              primary key (owner_id)
);

create table if not exists tasks (
                       date_of_create datetime(6),
                       date_of_end datetime(6) not null,
                       date_of_start datetime(6) not null,
                       group_owner_id bigint,
                       id bigint not null auto_increment,
                       user_id bigint,
                       description varchar(255) not null,
                       status enum ('CREATED','DONE','IN_PROCESS','NOT_DONE') not null,
                       title varchar(255) not null, primary key (id)
);

create table if not exists user (
                      id bigint not null auto_increment,
                      login varchar(255) not null,
                      password varchar(255) not null,
                      primary key (id)
);
create table if not exists user_group (
                            group_id bigint not null,
                            user_id bigint not null
);

alter table user
    add constraint uk_login unique (login);

alter table group_entity
    add constraint group_entity_owner_id_fk foreign key (owner_id) references user (id);

alter table tasks
    add constraint task_group_owner_id_fk foreign key (group_owner_id) references group_entity (owner_id);

alter table tasks
    add constraint task_user_id_fk foreign key (user_id) references user (id);

alter table user_group
    add constraint user_group_user_id_fk foreign key (user_id) references user (id);

alter table user_group
    add constraint user_group_id_fk foreign key (group_id) references group_entity (owner_id);