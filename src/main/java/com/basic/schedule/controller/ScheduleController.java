package com.basic.schedule.controller;

import com.basic.schedule.dto.*;
import com.basic.schedule.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<ScheduleSaveResponse> save(
            @RequestBody ScheduleSaveRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(scheduleService.save(request));
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<ScheduleGetAllResponse>> getAll(
            @RequestParam(required = false) String author
    ) {
        return ResponseEntity.ok(scheduleService.findAll(author));
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleGetOneResponse> getOne(
            @PathVariable Long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.findOne(scheduleId));
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<ScheduleUpdateResponse> updateTitleAndAuthor(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleUpdateRequest request
    ) {
        return ResponseEntity.ok(scheduleService.updateTitleAndAuthor(scheduleId, request));
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long scheduleId,
            @RequestParam String password
    ) {
        scheduleService.delete(scheduleId, password);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
