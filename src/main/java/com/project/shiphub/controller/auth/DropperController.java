package com.project.shiphub.controller.auth;

import com.project.shiphub.dto.auth.DropperProfileDTO;
import com.project.shiphub.dto.auth.DropperRegisterRequest;
import com.project.shiphub.model.auth.DropperStatus;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.service.auth.DropperService;
import com.project.shiphub.service.auth.DropperServiceImp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DropperController {

    private final DropperService dropperService;
    private final DropperServiceImp dropperServiceImp;

    @PostMapping("/api/dropper/register")
    public ResponseEntity<DropperProfileDTO> register(
            @RequestBody DropperRegisterRequest request,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();
        User user = (User) authentication.getPrincipal();
        log.info("📋 {} solicitando cadastro dropper", user.getEmail());
        DropperProfileDTO dto = dropperService.register(user.getId(), request);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/api/dropper/me")
    public ResponseEntity<DropperProfileDTO> getMyProfile(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();
        User user = (User) authentication.getPrincipal();
        DropperProfileDTO dto = dropperService.getByUserId(user.getId());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/api/admin/droppers")
    public ResponseEntity<List<DropperProfileDTO>> listDroppers(
            @RequestParam(required = false) String status,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();

        List<DropperProfileDTO> list;
        if (status != null && !status.isBlank()) {
            list = dropperService.listByStatus(DropperStatus.valueOf(status));
        } else {
            list = dropperServiceImp.listAll();
        }
        return ResponseEntity.ok(list);
    }

    @PatchMapping("/api/admin/droppers/{id}/approve")
    public ResponseEntity<Map<String, Object>> approve(
            @PathVariable Long id,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();
        DropperProfileDTO dto = dropperService.approve(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Dropper aprovado com sucesso!",
                "dropper", dto
        ));
    }

    @PatchMapping("/api/admin/droppers/{id}/reject")
    public ResponseEntity<Map<String, Object>> reject(
            @PathVariable Long id,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();
        DropperProfileDTO dto = dropperServiceImp.reject(id);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Dropper recusado.",
                "dropper", dto
        ));
    }
}