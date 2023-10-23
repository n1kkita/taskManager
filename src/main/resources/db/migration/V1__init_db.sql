Hibernate: alter table group_entity drop foreign key FKh6vuwsedlgmy50aeam9eg6may
Hibernate: alter table tasks drop foreign key FKtibqlgm8mdn22hukxmjr4uur1
Hibernate: alter table tasks drop foreign key FKqjk25pcieac7t8833ddos0cdu
Hibernate: alter table user_group drop foreign key FK1c1dsw3q36679vaiqwvtv36a6
Hibernate: alter table user_group drop foreign key FKigraic5s1w8kvvvppwltekao2
Hibernate: drop table if exists group_entity
    Hibernate: drop table if exists tasks
    Hibernate: drop table if exists user
    Hibernate: drop table if exists user_group
    Hibernate: create table group_entity (id bigint not null, owner_id bigint, name varchar(255) not null, primary key (id)) engine=InnoDB
Hibernate: create table tasks (date_of_create datetime(6), date_of_end datetime(6) not null, date_of_start datetime(6) not null, group_id bigint, id bigint not null auto_increment, user_id bigint, description varchar(255) not null, status enum ('CREATED','DONE','IN_PROCESS','NOT_DONE') not null, title varchar(255) not null, primary key (id)) engine=InnoDB
Hibernate: create table user (id bigint not null auto_increment, login varchar(255) not null, password varchar(255) not null, primary key (id)) engine=InnoDB
Hibernate: create table user_group (group_id bigint not null, user_id bigint not null) engine=InnoDB
Hibernate: create index password_index on user (password)
    Hibernate: alter table user add constraint UK_ew1hvam8uwaknuaellwhqchhb unique (login)
    Hibernate: alter table group_entity add constraint FKh6vuwsedlgmy50aeam9eg6may foreign key (owner_id) references user (id)
    Hibernate: alter table tasks add constraint FKtibqlgm8mdn22hukxmjr4uur1 foreign key (group_id) references group_entity (id)
    Hibernate: alter table tasks add constraint FKqjk25pcieac7t8833ddos0cdu foreign key (user_id) references user (id)
    Hibernate: alter table user_group add constraint FK1c1dsw3q36679vaiqwvtv36a6 foreign key (user_id) references user (id)
    Hibernate: alter table user_group add constraint FKigraic5s1w8kvvvppwltekao2 foreign key (group_id) references group_entity (id)