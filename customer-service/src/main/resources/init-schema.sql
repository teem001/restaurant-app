drop schema if exists customer cascade;

create schema customer;

create extension if not exists "uuid-ossp";

create table customer.customers
(
    id         uuid                                           not null,
    username   character varying collate pg_catalog."default" not null,
    first_name character varying collate pg_catalog."default" not null,
    last_name  character varying collate pg_catalog."default" not null,
    constraint customers_fk primary key (id)

);
drop materialized view if exists customer.order_customer_m_view;

create materialized view customer.order_customer_m_view tablespace pg_default
AS
select id,
       username,
       first_name,
       last_name
from customer.customers
with data;

refresh materialized view customer.order_customer_m_view;

drop function if exists customer.refresh_order_customer_m_view;

create or replace function customer.refresh_order_customer_m_view()
    returns trigger
as
'
    begin
        refresh materialized view customer.order_customer_m_view;
        return null;
    end
' language plpgsql;

drop trigger if exists refresh_order_customer_m_view on customer.customers;

create trigger refresh_order_customer_m_view
    after insert or update or delete or truncate
    on customer.customers
    for each statement
execute procedure customer.refresh_order_customer_m_view();
