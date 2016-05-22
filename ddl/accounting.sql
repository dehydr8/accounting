drop table if exists client;
drop table if exists account;
drop table if exists transaction_history;
drop table if exists transaction_leg;
drop table if exists distribution_source;
drop table if exists distribution_destination;
drop table if exists distribution;

create table client (
	ref varchar(100) not null primary key,
	creation_date date not null
);

create table account (
	id int not null primary key,
	client_ref varchar(100) not null,
	account_ref varchar(80) not null,
	amount decimal(20,2) not null,
	currency varchar(3) not null
);

create table transaction_history (
	client_ref varchar(100) not null,
	transaction_ref varchar(80) not null,
	transaction_type varchar(80) not null,
	transaction_date date not null,
	primary key(client_ref, transaction_ref)
);

create table transaction_leg (
	client_ref varchar(100) not null,
	transaction_ref varchar(80) not null,
	amount decimal(20,2) not null,
	currency varchar(3) not null
);

create table distribution (
	id int auto_increment not null primary key,
	transaction_ref varchar(80) not null,
	transaction_type varchar(80) not null
);

create table distribution_source (
	account_ref varchar(80) not null,
	amount decimal(20,2) not null,
	currency varchar(3) not null,
	fk_distribution int not null,
	foreign key(fk_distribution) references distribution(id)
);


create table distribution_destination (
	account_ref varchar(80) not null,
	amount decimal(20,2) not null,
	currency varchar(3) not null,
	fk_distribution int not null,
	foreign key(fk_distribution) references distribution(id)
);

