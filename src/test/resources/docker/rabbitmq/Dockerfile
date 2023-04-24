FROM rabbitmq:3.12.0-beta.6-management

ENV RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS="-rabbitmq_stream advertised_host localhost"

RUN rabbitmq-plugins enable rabbitmq_stream
RUN rabbitmq-plugins enable rabbitmq_stream_management

