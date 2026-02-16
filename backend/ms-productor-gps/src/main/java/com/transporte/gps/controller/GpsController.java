package com.transporte.gps.controller;

import com.transporte.gps.dto.GpsMessage;
import com.transporte.gps.service.GpsProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gps")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GpsController {

    private final GpsProducerService gpsProducerService;

    @PostMapping
    public ResponseEntity<Void> enviarGps(@RequestBody GpsMessage gpsMessage) {
        gpsProducerService.enviarGps(gpsMessage);
        return ResponseEntity.accepted().build();
    }
}
