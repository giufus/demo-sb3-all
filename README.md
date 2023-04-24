
# Demo SB3 
[![Java CI with Maven](https://github.com/giufus/demo-sb3-all/actions/workflows/maven.yml/badge.svg)](https://github.com/giufus/demo-sb3-all/actions/workflows/maven.yml)  

#### A sample Spring Boot 3 Project to try some features and libs:   
- [Testcontainers ](https://www.testcontainers.org/)
- [RabbitMQ (Stream queues, MQTT 'native' support)](https://blog.rabbitmq.com/)
- others (webflux, websocket)

## RUN LOCALLY
`mvn spring-boot:run`  

You need the following services running:
- **redis** on localhost, port 6379
- **rabbitmq** on localhost, port 5552 (stream queues)

### RABBITMQ  

- Create a docker instance of rabbitmq 3.12 with STREAMS queue support  
`docker run -d --name rbt-mq-3.12.0-beta.6-management -p 5552:5552 -p 5672:5672 -p 15672:15672 -e RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS='-rabbitmq_stream advertised_host localhost' rabbitmq:3.12.0-beta.6-management`

- Login to container to enable stream plugins  
`docker exec -it rbt-mq-3.12.0-beta.6-management bash`

- Execute in container  
`rabbitmq-plugins enable rabbitmq_stream rabbitmq_stream_management`

Login to interface with guest/guest at http://localhost:15672/

### REDIS
- Run a simple redis container  
`docker run -d --name some-redis -p 6379:6379 redis`

### RELATIONAL DB
- h2 as spring default datasource

## UNIT TESTS
Run them with `mvn test` commmand

## INTEGRATION TESTS
Run them with `mvn verify` command

