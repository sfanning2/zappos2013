# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table product (
  id                        bigint not null,
  product_id                bigint,
  brand_id                  bigint,
  brand_name                varchar(255),
  product_name              varchar(255),
  default_product_url       varchar(255),
  default_image_url         varchar(255),
  constraint pk_product primary key (id))
;

create table e_watchtable (
  id                        bigint not null,
  product_id                bigint,
  email_address             varchar(255),
  the_product_id            bigint,
  constraint pk_e_watchtable primary key (id))
;

create table style (
  id                        bigint not null,
  product_id                bigint not null,
  style_id                  bigint,
  image_url                 varchar(255),
  price                     varchar(255),
  percent_off               varchar(255),
  color                     varchar(255),
  original_price            varchar(255),
  product_url               varchar(255),
  constraint pk_style primary key (id))
;

create sequence product_seq;

create sequence e_watchtable_seq;

create sequence style_seq;

alter table e_watchtable add constraint fk_e_watchtable_theProduct_1 foreign key (the_product_id) references product (id) on delete restrict on update restrict;
create index ix_e_watchtable_theProduct_1 on e_watchtable (the_product_id);
alter table style add constraint fk_style_product_2 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_style_product_2 on style (product_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists product;

drop table if exists e_watchtable;

drop table if exists style;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists product_seq;

drop sequence if exists e_watchtable_seq;

drop sequence if exists style_seq;

