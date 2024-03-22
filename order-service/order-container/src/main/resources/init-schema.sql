drop schema if exists "order" cascade;

create schema "order";

create extension if not exists "uuid-ossp";

drop type if exists order_status;
create type order_status as enum ('PENDING', 'PAID', 'APPROVED', 'CANCELLED', 'CANCELLING');

drop table if exists "order".orders;
create table "order".orders
(
     id               uuid           not null,
    customer_id      uuid           not null,
    restaurant_id    uuid           not null,
    tracking_id      uuid           not null,
    price            numeric(10, 2) not null,
    order_status     order_status   not null,
    failure_messages character varying collate pg_catalog."default",
    constraint order_fk primary key (id)

);

drop table if exists "order".order_items cascade;

create table "order".order_items
(
    id         bigint         not null,
    order_id   uuid           not null,
    product_id uuid           not null,
    price      numeric(10, 2) not null,
    quantity   integer        not null,
    sub_total  numeric(10, 2) not null,

    constraint order_items_fk primary key (id, order_id)
);


alter table "order".order_items
    add constraint "FK_ORDER_ID" foreign key (order_id)
        references "order".orders match simple
        on update no action
        on delete cascade
        not valid;


drop table if exists "order".order_address cascade;

create table "order".order_address
(
    id          uuid                                           not null,
    order_id    uuid UNIQUE                                    NOT NULL,
    street      character varying collate pg_catalog."default" not null,
    postal_code character varying collate pg_catalog."default",
    city        character varying collate pg_catalog."default",
    constraint order_address_pk primary key (id, order_id)
);

alter table "order".order_address
    add constraint "order_address_fk" foreign key (order_id)
        references "order".orders (id) match simple
        on update no action
        on delete cascade
        not valid