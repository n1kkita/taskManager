

create table if not exists files (
    id bigint not null auto_increment,
    name varchar(255) not null,
    content_type varchar(255) not null,
    type enum ('OWNER_FILE','USER_FILE'),
    task_id bigint not null,
    data longblob not null,
    primary key (id)
);

alter table files add constraint task_file_fk foreign key (task_id) references tasks (id);