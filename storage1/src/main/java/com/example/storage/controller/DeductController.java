package com.example.storage.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.Port;

@RestController
public class DeductController {
    @Value("${server.port}")
    Integer port;

    @GetMapping("/deduct")
    public String deduct(){
        return "storage deduct from number"+port;
    }
}
