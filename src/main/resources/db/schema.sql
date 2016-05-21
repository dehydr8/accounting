DROP table client IF EXISTS;
DROP table account IF EXISTS;
DROP table transaction_history IF EXISTS;
DROP table transaction_leg IF EXISTS;
DROP table distribution_source IF EXISTS;
DROP table distribution_destination IF EXISTS;
DROP table distribution IF EXISTS;

CREATE TABLE client (
   ref VARCHAR(100) NOT NULL,
   creation_date DATE NOT NULL,
   
   PRIMARY KEY(ref)
);

CREATE TABLE account (
  id INTEGER IDENTITY,
  client_ref VARCHAR(100) NOT NULL,
  account_ref VARCHAR(80) NOT NULL,
  amount DECIMAL(20,2) NOT NULL,
  currency VARCHAR(3) NOT NULL
);

CREATE TABLE transaction_history (
  client_ref VARCHAR(100) NOT NULL,
  transaction_ref VARCHAR(80) NOT NULL,
  transaction_type VARCHAR(80) NOT NULL,
  transaction_date DATE NOT NULL,
  
  PRIMARY KEY(client_ref, transaction_ref)
);

CREATE TABLE transaction_leg (
	client_ref VARCHAR(100) NOT NULL,
	transaction_ref VARCHAR(80) NOT NULL, 
	account_ref VARCHAR(80) NOT NULL, 
	amount DECIMAL(20,2) NOT NULL, 
	currency VARCHAR(3) NOT NULL
);

CREATE TABLE distribution (
  id INTEGER IDENTITY,
	transaction_ref VARCHAR(80) NOT NULL,
  transaction_type VARCHAR(80) NOT NULL,

  PRIMARY KEY(id)
);

CREATE TABLE distribution_source (
  account_ref VARCHAR(80) NOT NULL,
  amount DECIMAL(20,2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  fk_distribution INTEGER,
  FOREIGN KEY(fk_distribution) REFERENCES distribution(id)
);

CREATE TABLE distribution_destination (
  account_ref VARCHAR(80) NOT NULL,
  amount DECIMAL(20,2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  fk_distribution INTEGER,
  FOREIGN KEY(fk_distribution) REFERENCES distribution(id)
);
