create database cursobranas;

create table stock_item
(
    id          bigserial        not null
        constraint pk_stock_item primary key,
    category    text             not null,
    description text             not null,
    price       numeric(5, 2)    not null,
    height_cm   double precision not null,
    width_cm    double precision not null,
    length_cm   double precision not null,
    weight_kg   double precision not null
);

create table coupon
(
    id                  bigserial        not null
        constraint pk_coupon primary key,
    code                text             not null,
    expires_at          timestamp with time zone,
    percentage_discount double precision not null
);

create table orders
(
    id             bigserial                not null
        constraint pk_order primary key,
    user_cpf       text                     not null,
    used_coupon_id bigint
        constraint fk_coupon references coupon (id),
    created_at     timestamp with time zone not null,
    order_total    numeric(7, 2)            not null,
    shipping_cost  numeric(7, 2)            not null
);

create table order_item
(
    id          bigserial        not null
        constraint pk_order_item primary key,
    order_id    bigint           not null
        constraint fk_order references orders (id),
    price       numeric(5, 2)    not null,
    quantity    integer          not null
);

insert into stock_item (category, description, price, height_cm, width_cm, length_cm, weight_kg)
values ('Electronics', 'Mouse', 50, 2, 3, 5, 0.3),
       ('Electronics', 'Keyboard', 200, 2, 30, 10, 0.5),
       ('Electronics', 'Smartphone', 800, 2, 3, 5, 0.3);

-- Drops.
-- drop table stock_item;
-- drop table order_item;
-- drop table orders;
-- drop table coupon;