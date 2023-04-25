package com.giufus.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.giufus.demo.models.DeviceMessage;
import com.giufus.demo.services.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("device")
public class DeviceController {

    private DeviceService deviceService;



    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity sendMessages(@RequestBody List<DeviceMessage> deviceMessages) throws JsonProcessingException, InterruptedException {
        deviceService.sendMessages(deviceMessages);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity consumeMessages(@RequestParam("queue") String queue,
                                          @RequestParam("offset") Long offset,
                                          @RequestParam("countMessages") Integer countMessages) {
        AtomicInteger atomicInteger = deviceService.consumeMessages(queue, offset, countMessages);
        return ResponseEntity.ok(atomicInteger.get());
    }

}
