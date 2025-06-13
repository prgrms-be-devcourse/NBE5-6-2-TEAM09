package com.grepp.codemap.coverletter.controller;

import com.grepp.codemap.coverletter.dto.CoverLetterRequestDto;
import com.grepp.codemap.coverletter.dto.CoverLetterResponseDto;
import com.grepp.codemap.coverletter.service.CoverLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coverletters")
@RequiredArgsConstructor
public class CoverLetterController {

    private final CoverLetterService coverLetterService;

    @GetMapping
    public ResponseEntity<List<CoverLetterResponseDto>> getAll(@RequestParam Long userId) {
        return ResponseEntity.ok(coverLetterService.findAllByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoverLetterResponseDto> getOne(@PathVariable Long id,
        @RequestParam Long userId) {
        return ResponseEntity.ok(coverLetterService.findById(id, userId));
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CoverLetterRequestDto dto,
        @RequestParam Long userId) {
        return ResponseEntity.ok(coverLetterService.create(dto, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
        @RequestBody CoverLetterRequestDto dto,
        @RequestParam Long userId) {
        coverLetterService.update(id, dto, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
        @RequestParam Long userId) {
        coverLetterService.delete(id, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Void> toggleComplete(@PathVariable Long id,
        @RequestParam Long userId) {
        coverLetterService.toggleComplete(id, userId);
        return ResponseEntity.ok().build();
    }
}
