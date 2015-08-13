drop table if exists ec_task;
drop table if exists ec_user;

create table ec_task (
	id bigint auto_increment,
	title varchar(128) not null,
	description varchar(255),
	user_id bigint not null,
    primary key (id)
) engine=InnoDB;

create table ec_user (
	id bigint auto_increment,
	login_name varchar(64) not null unique,
	name varchar(64) not null,
	password varchar(255) not null,
	salt varchar(64) not null,
	roles varchar(255) not null,
	register_date timestamp not null default 0,
	primary key (id)
) engine=InnoDB;


drop table if exists mbe_areamg;

create table mbe_areamg (
	pk_areamg bigint auto_increment,
	code varchar(50) not null,
	name varchar(50) not null,
	description varchar(500),
	creater varchar(20),
	cteatetime date,
	vstatus int,
	ts timestamp not null default '0000-00-00 00:00:00',
	dr int not null default 0,
	primary key (pk_areamg)
) engine=InnoDB;

drop table if exists mbe_divisionmg;

create table mbe_divisionmg (
	pk_divisionmg bigint auto_increment,
	code varchar(50) not null,
	name varchar(50) not null,
	description varchar(500),
	pk_group varchar(20),
	curcore int,
	creater varchar(20),
	cteatetime date,
	vstatus int,
	ts date not null default '0000-00-00 00:00:00',
	dr int not null default 0,
	primary key (pk_divisionmg)
) engine=InnoDB;