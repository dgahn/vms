FROM postgres:latest

COPY ./postgresql.conf /etc/postgresql/postgresql.conf
COPY ./init-user-db.sh /docker-entrypoint-initdb.d/init-user-db.sh

CMD ["postgres", "-c", "config_file=/etc/postgresql/postgresql.conf"]