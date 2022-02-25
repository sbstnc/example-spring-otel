# An example Spring Boot service with OpenTelemetry

## Raison d'être:

This repository contains example code on how to integrate a basic Spring Boot todo application with OpenTelemetry. It
also ships with a preconfigured stack of monitoring and tracing applications including:

* [Grafana](https://grafana.com)
* [Loki](https://grafana.com/oss/loki)
* [Prometheus](https://prometheus.io)
* [Tempo](https://grafana.com/oss/tempo)

The code presented here is merely for demonstration purposes, do not use it in production environments.

## Running

To get everything running you first need to build the application
[Docker](https://www.docker.com) image and install
the [Docker Driver Client](https://grafana.com/docs/loki/latest/clients/docker-driver/) for Loki.

```bash
./gradlew jibDockerBuild
```

Afterwards you can start the system with

```bash
docker-compose up
```

### Available endpoints

The service provides a reasonable REST API to manage todos. The following endpoints are available:

* `GET /api/todos`: Get a list of todos.
* `POST /api/todos`: Create a new todo.
* `GET /api/todos/{id}`: Get todo with the given {id}.
* `PUT /api/todos/{id}`: Update todo with the given {id}.
* `PUT /api/todos/{id}/pending`: Set status of todo with {id} to 'pending'.
* `PUT /api/todos/{id}/in-progress`: Set status of todo with {id} to 'in progress'.
* `PUT /api/todos/{id}/done`: Set status of todo with {id} to 'done'.
* `DELETE /api/todos/{id}`: Delete todo with the given {id}.

You may use [HTTPie](https://httpie.io/docs/cli) or a similar tool to make requests. The service API is available at
port 8080 of your machine.

## Tracing

### Grafana

After you have made some requests take a look at the logs in [Grafana](http://localhost:3000/explore). Select the
preconfigured Loki datasource and filter for the log label `{job=example-spring-otel}`. You can then expand a log
message and click on the `Tempo` button next to the detected `TraceID` field. A new view with all spans belonging to the
same trace will be opened.

### Jaeger

You can also lookup a trace ID using [Jaeger](http://localhost:16686/search)

### Prometheus Metrics

Metrics can be viewed in
[Prometheus](http://localhost:9090/graph?g0.expr=http_server_requests_seconds_count%7Bjob%3D%22example-spring-otel%22%7D&g0.tab=1&g0.stacked=0&g0.show_exemplars=0&g0.range_input=1h)
and [Grafana](http://localhost:3000/explore?orgId=1&left=%5B%22now-1h%22,%22now%22,%22Prometheus%22,%7B%22expr%22:%22http_server_requests_seconds_count%22,%22requestId%22:%22Q-0a6b4a46-2eeb-428a-b98d-0170a5fe4900-0A%22%7D%5D)
as well
