package com.giufus.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.giufus.demo.models.DeviceMessage;
import com.giufus.demo.services.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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
                                          @RequestParam(value = "consumerTime") Integer consumerTime) {

        int consumed = deviceService.consumeMessages(queue, consumerTime);
        return ResponseEntity.ok(new HashMap<>(){{put("consumed", consumed);}});
    }

}
