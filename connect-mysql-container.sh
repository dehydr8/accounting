#!/bin/bash


docker run \
	-ti --rm \
	--link accounting-data:mysql \
	mysql:5.7.12 sh \
		-c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"'
