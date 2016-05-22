#!/bin/bash


docker run \
	--name accounting-data \
	-v $PWD:/var/lib/mysql \
	-e MYSQL_ROOT_PASSWORD=wopniep \
	-e MYSQL_DATABASE=accounting \
	-e MYSQL_USER=ninjav \
	-e MYSQL_PASSWORD=sunofabitch \
	-d mysql:5.7.12

