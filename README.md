# FIBONACCI Task Manager

For more description see: [fibonacci-cluster-docker](https://github.com/gerardolenski/fibonacci-cluster-docker) and [fibonacci-cluster-k8s](https://github.com/gerardolenski/fibonacci-cluster-k8s)

## Configuration

App can be configured by environment variables:

- `BROKER_URL` - the URI to connect to the ActiveMQ cluster
- `BROKER_USER` - the AMQ user
- `BROKER_PASSWORD` - the AMQ password
- `WORKER_QUEUE_NAME` - the name of the worker queue
- `RESULT_LISTENER_CONCURRENCY` - the consumer concurrency, by default `1-10`
- `TOMCAT_PORT` - the port of exposed API, by default `8080` 
- `POSTGRES_DATASOURCE_URL` - the URL to the PostgreSQL datasource 
- `POSTGRES_USER` - the database user
- `POSTGRES_PASSWORD` - the database password
- `POSTGRESS_POOL_SIZE` - the database connection pool size, by default `5`
- `POSTGRESS_CONNECTION_TIMEOUT` the database connection timeout, by default `5000` ms
- `FIB_ALGORITHMS` - the list of active Fibonacci numbers algorithms, by default all are activated. The possible values are: `ITERATIVE`,`RECURSIVE`,`BINETS`,`EXPONENTIAL`


The example configuration:
```
BROKER_PASSWORD=admin
BROKER_URL=failover:(tcp://localhost:61616)?jms.useAsyncSend=true
BROKER_USER=admin
WORKER_QUEUE_NAME=worker

POSTGRES_DATASOURCE_URL=jdbc:postgresql://localhost:5432/task_manager
POSTGRES_PASSWORD=postgres
POSTGRES_USER=postgres
```