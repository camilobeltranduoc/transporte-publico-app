package com.transporte.horarios.controller;

import com.transporte.horarios.dto.HorarioMessage;
import com.transporte.horarios.service.HorarioProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HorarioController {

    private final HorarioProducerService horarioProducerService;

    @PostMapping
    public ResponseEntity<Void> enviarHorario(@RequestBody HorarioMessage horarioMessage) {
        horarioProducerService.enviarHorario(horarioMessage);
        return ResponseEntity.accepted().build();
    }
}
