
create table if not exists group_histories (
    id bigint not null auto_increment,
    group_id bigint not null,
    text varchar(255) not null,
    save_at datetime(6) not null,
    primary key (id)
);

alter table group_histories add constraint grouphistory_group_fk foreign key (group_id) references group_entity(id);

alter table tasks add column creator_email varchar(255) not null;