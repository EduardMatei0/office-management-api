
create table if not exists users (
  id serial primary key,
  username varchar not null,
  email varchar unique not null,
  password varchar not null
);

create table if not exists roles (
    id serial primary key,
    name varchar not null
);

create table if not exists users_roles (
  user_id integer not null,
  role_id integer not null
);

alter table users_roles add constraint "fk_users_roles_user_id" foreign key (user_id)
    references users (id);

alter table users_roles add constraint "fk_users_roles_roles_id" foreign key (role_id)
    references roles (id);
