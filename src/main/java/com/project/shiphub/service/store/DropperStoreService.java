package com.project.shiphub.service.store;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shiphub.dto.store.AiThemeResponse;
import com.project.shiphub.dto.store.DropperStoreDTO;
import com.project.shiphub.dto.store.DropperStoreRequest;
import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.auth.DropperStatus;
import com.project.shiphub.model.product.Product;
import com.project.shiphub.model.store.DropperStore;
import com.project.shiphub.repository.auth.DropperProfileRepository;
import com.project.shiphub.repository.product.ProductRepository;
import com.project.shiphub.repository.store.DropperStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DropperStoreService {

    private final DropperStoreRepository storeRepository;
    private final DropperProfileRepository dropperProfileRepository;
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    public AiThemeResponse generateThemeWithAI(String prompt) {
        log.info("🤖 Gerando tema com IA para prompt: {}", prompt);

        String systemPrompt = """
        Você é um especialista em design de lojas virtuais.
        Baseado na descrição do usuário, gere um tema para a loja.
        
        Responda APENAS com um JSON válido, sem texto antes ou depois, no formato:
        {
          "storeName": "nome sugerido para a loja",
          "slogan": "slogan criativo e curto",
          "primaryColor": "#HEXCODE",
          "secondaryColor": "#HEXCODE",
          "backgroundColor": "#HEXCODE",
          "theme": "MODERN ou MINIMAL ou BOLD ou ELEGANT",
          "description": "descrição da loja em 2 frases",
          "reasoning": "por que escolhei esse estilo"
        }
        """;

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + groqApiKey);

            Map<String, Object> body = Map.of(
                    "model", "llama-3.3-70b-versatile",
                    "messages", List.of(
                            Map.of("role", "system", "content", systemPrompt),
                            Map.of("role", "user", "content", prompt)
                    ),
                    "max_tokens", 500,
                    "temperature", 0.7
            );

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.groq.com/openai/v1/chat/completions",
                    entity,
                    String.class
            );

            JsonNode root = objectMapper.readTree(response.getBody());
            String aiText = root.path("choices").get(0).path("message").path("content").asText();

            log.info("🤖 Resposta da IA: {}", aiText);

            return objectMapper.readValue(aiText, AiThemeResponse.class);

        } catch (Exception e) {
            log.error("❌ Erro ao chamar Groq: {}", e.getMessage());
            AiThemeResponse fallback = new AiThemeResponse();
            fallback.setStoreName("Minha Loja");
            fallback.setSlogan("Os melhores produtos para você");
            fallback.setPrimaryColor("#6366F1");
            fallback.setSecondaryColor("#818CF8");
            fallback.setBackgroundColor("#F8FAFC");
            fallback.setTheme("MODERN");
            fallback.setDescription("Loja criada com GoDrop");
            return fallback;
        }
    }

    public DropperStoreDTO createStore(Long userId, DropperStoreRequest request) {
        log.info("🏪 Criando loja para userId: {}", userId);

        DropperProfile profile = dropperProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil dropper não encontrado"));

        if (profile.getStatus() != DropperStatus.ACTIVE) {
            throw new RuntimeException("Dropper precisa estar ativo para criar uma loja");
        }

        if (storeRepository.findByDropperProfileId(profile.getId()).isPresent()) {
            throw new RuntimeException("Dropper já possui uma loja");
        }

        String slug = generateSlug(request.getStoreName());

        List<Product> products = productRepository.findAllById(
                request.getProductIds() != null ? request.getProductIds() : List.of()
        );

        DropperStore store = DropperStore.builder()
                .dropperProfile(profile)
                .slug(slug)
                .storeName(request.getStoreName())
                .description(request.getDescription())
                .logoUrl(request.getLogoUrl())
                .bannerUrl(request.getBannerUrl())
                .primaryColor(request.getPrimaryColor() != null ? request.getPrimaryColor() : "#6366F1")
                .secondaryColor(request.getSecondaryColor() != null ? request.getSecondaryColor() : "#818CF8")
                .backgroundColor(request.getBackgroundColor() != null ? request.getBackgroundColor() : "#F8FAFC")
                .slogan(request.getSlogan())
                .theme(request.getTheme() != null ? request.getTheme() : "MODERN")
                .aiPrompt(request.getAiPrompt())
                .active(false)
                .products(products)
                .build();

        DropperStore saved = storeRepository.save(store);
        log.info("✅ Loja criada: {} (slug: {})", saved.getStoreName(), saved.getSlug());

        return new DropperStoreDTO(saved);
    }

    public DropperStoreDTO updateStore(Long userId, DropperStoreRequest request) {
        DropperStore store = storeRepository.findByDropperProfileUserId(userId)
                .orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        if (request.getStoreName() != null) store.setStoreName(request.getStoreName());
        if (request.getDescription() != null) store.setDescription(request.getDescription());
        if (request.getLogoUrl() != null) store.setLogoUrl(request.getLogoUrl());
        if (request.getBannerUrl() != null) store.setBannerUrl(request.getBannerUrl());
        if (request.getPrimaryColor() != null) store.setPrimaryColor(request.getPrimaryColor());
        if (request.getSecondaryColor() != null) store.setSecondaryColor(request.getSecondaryColor());
        if (request.getBackgroundColor() != null) store.setBackgroundColor(request.getBackgroundColor());
        if (request.getSlogan() != null) store.setSlogan(request.getSlogan());
        if (request.getTheme() != null) store.setTheme(request.getTheme());

        if (request.getProductIds() != null) {
            List<Product> products = productRepository.findAllById(request.getProductIds());
            store.setProducts(products);
        }

        return new DropperStoreDTO(storeRepository.save(store));
    }

    public DropperStoreDTO getMyStore(Long userId) {
        DropperStore store = storeRepository.findByDropperProfileUserId(userId)
                .orElseThrow(() -> new RuntimeException("Loja não encontrada"));
        return new DropperStoreDTO(store);
    }

    public DropperStoreDTO getPublicStore(String slug) {
        DropperStore store = storeRepository.findActiveBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Loja não encontrada ou inativa"));
        return new DropperStoreDTO(store);
    }

    public DropperStoreDTO toggleStore(Long userId) {
        DropperStore store = storeRepository.findByDropperProfileUserId(userId)
                .orElseThrow(() -> new RuntimeException("Loja não encontrada"));

        store.setActive(!store.getActive());
        return new DropperStoreDTO(storeRepository.save(store));
    }

    private String generateSlug(String name) {
        String slug = Normalizer.normalize(name, Normalizer.Form.NFD);
        slug = Pattern.compile("[^\\p{ASCII}]").matcher(slug).replaceAll("");
        slug = slug.toLowerCase().trim().replaceAll("[^a-z0-9\\s-]", "").replaceAll("\\s+", "-");

        String baseSlug = slug;
        int counter = 1;
        while (storeRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter++;
        }
        return slug;
    }
}