# simplewebserver

This repository demonstrates the usage of
[`fabric8io`'s `docker-maven-plugin`](https://github.com/fabric8io/docker-maven-plugin) :

- build an image
- run integration tests
- push an image

## App description

The application is just a simple webserver, developed with
[`sparkjava`](http://sparkjava.com). 2 endpoints are defined :

- `/items/:id`
- `/health`

Item retrieval can be done from a PostgreSQL or a Mongo database,
depending on environment variables passed (see `App.java`).

## Integration tests

**For demonstration purpose**, plugin configuration for integration tests
have been defined in multiple profiles :

- `postgresql` : run only integration tests related to PostgreSQL
- `mongo` : run only integration tests related to Mongo
- `webserver-mongo` : run only integration tests related to the
webserver itself, with a Mongo backend
- `all` : active by default, run all integration tests

For Mongo related tests, data is imported from `mongodata.tar.bz2`,
unlike PostgreSQL tests (data insertion in `PostgresqlIntegrationTestBase.java`).

Only last profile (`all`) should be present normally, directly in
plugin configuration (there is no need to have a profile).

## Running app

To run the application, use `docker-compose` and one of the following :

    export SIMPLEWEBSERVER_VERSION=<tag of simplewebserver image>

    # postgresql database
    docker-compose -f docker-compose-postgresql.yml up

    # mongo database
    docker-compose -f docker-compose-mongo.yml up

For PostgreSQL database, you should see errors the first time because
you must create the `POSTGRESQL_DATABASE`.

In both cases, you must insert your own data.
