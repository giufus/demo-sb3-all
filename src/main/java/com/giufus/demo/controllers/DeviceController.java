package com.giufus.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.giufus.demo.models.DeviceMessage;
import com.giufus.demo.services.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
