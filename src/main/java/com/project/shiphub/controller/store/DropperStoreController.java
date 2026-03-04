package com.project.shiphub.controller.store;

import com.project.shiphub.dto.store.AiThemeResponse;
import com.project.shiphub.dto.store.DropperStoreDTO;
import com.project.shiphub.dto.store.DropperStoreRequest;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.service.store.DropperStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DropperStoreController {

    private final DropperStoreService storeService;

    @PostMapping("/api/dropper/store/ai-theme")
    public ResponseEntity<AiThemeResponse> generateAiTheme(
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();

        String prompt = body.get("prompt");
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        log.info("🤖 Gerando tema para prompt: {}", prompt);
        AiThemeResponse theme = storeService.generateThemeWithAI(prompt);
        return ResponseEntity.ok(theme);
    }

    @PostMapping("/api/dropper/store")
    public ResponseEntity<DropperStoreDTO> createStore(
            @RequestBody DropperStoreRequest request,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();

        User user = (User) authentication.getPrincipal();
        log.info("🏪 {} criando loja", user.getEmail());

        DropperStoreDTO store = storeService.createStore(user.getId(), request);
        return ResponseEntity.ok(store);
    }


    @GetMapping("/api/dropper/store/me")
    public ResponseEntity<DropperStoreDTO> getMyStore(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();

        User user = (User) authentication.getPrincipal();
        DropperStoreDTO store = storeService.getMyStore(user.getId());
        return ResponseEntity.ok(store);
    }

    @PutMapping("/api/dropper/store")
    public ResponseEntity<DropperStoreDTO> updateStore(
            @RequestBody DropperStoreRequest request,
            Authentication authentication
    ) {
        if (authentication == null) return ResponseEntity.status(401).build();

        User user = (User) authentication.getPrincipal();
        DropperStoreDTO store = storeService.updateStore(user.getId(), request);
        return ResponseEntity.ok(store);
    }

    @PatchMapping("/api/dropper/store/toggle")
    public ResponseEntity<DropperStoreDTO> toggleStore(Authentication authentication) {
        if (authentication == null) return ResponseEntity.status(401).build();

        User user = (User) authentication.getPrincipal();
        DropperStoreDTO store = storeService.toggleStore(user.getId());
        return ResponseEntity.ok(store);
    }

    @GetMapping("/loja/{slug}")
    public ResponseEntity<DropperStoreDTO> getPublicStore(@PathVariable String slug) {
        log.info("🌐 Acesso público à loja: {}", slug);
        DropperStoreDTO store = storeService.getPublicStore(slug);
        return ResponseEntity.ok(store);
    }
}