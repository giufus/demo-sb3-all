package com.giufus.demo.clients;

import org.springframework.stereotype.Component;

@Component
public class EchoClient {

    /*
    private WebClient webClient;

    public EchoClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseEntity echoGetRequest(Map<String, Object> params) {

        Mono<Object> objectMono = webClient.get()
                .uri("/get", params)
                .retrieve().bodyToMono(ParameterizedTypeReference.forType(Map.class));

        return ResponseEntity.ok(objectMono.block());

    }
    */

}
