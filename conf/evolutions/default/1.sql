# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table e_watchtable (
  id                        bigint not null,
  product_id                bigint,
  email_address             varchar(255),
  constraint pk_e_watchtable primary key (id))
;

create sequence e_watchtable_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists e_watchtable;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists e_watchtable_seq;

