package com.project.shiphub.service.auth;

import com.project.shiphub.dto.auth.DropperProfileDTO;
import com.project.shiphub.dto.auth.DropperRegisterRequest;
import com.project.shiphub.model.auth.DropperLevel;
import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.auth.DropperStatus;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.model.auth.UserRole;
import com.project.shiphub.repository.auth.DropperProfileRepository;
import com.project.shiphub.repository.auth.LoginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DropperServiceImp implements DropperService {

    private final DropperProfileRepository dropperProfileRepository;
    private final LoginRepository loginRepository;

    @Override
    public DropperProfileDTO register(Long userId, DropperRegisterRequest request) {
        log.info("📋 Registrando dropper para userId: {}", userId);

        User user = loginRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dropperProfileRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("Usuário já possui cadastro de dropper");
        }

        if (dropperProfileRepository.existsByCnpj(request.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado");
        }

        DropperProfile profile = DropperProfile.builder()
                .user(user)
                .cnpj(request.getCnpj())
                .storeName(request.getStoreName())
                .whatsapp(request.getWhatsapp())
                .status(DropperStatus.PENDING)
                .level(DropperLevel.BRONZE)
                .build();

        DropperProfile saved = dropperProfileRepository.save(profile);
        log.info("✅ Dropper registrado: ID {}", saved.getId());

        return new DropperProfileDTO(saved);
    }

    @Override
    public DropperProfileDTO approve(Long dropperProfileId) {
        log.info("✅ Aprovando dropper ID: {}", dropperProfileId);

        DropperProfile profile = dropperProfileRepository.findById(dropperProfileId)
                .orElseThrow(() -> new RuntimeException("Perfil dropper não encontrado"));

        profile.setStatus(DropperStatus.ACTIVE);

        User user = profile.getUser();
        user.setRole(UserRole.DROPPER);
        loginRepository.save(user);

        DropperProfile updated = dropperProfileRepository.save(profile);
        log.info("✅ Dropper aprovado: {}", user.getEmail());

        return new DropperProfileDTO(updated);
    }

    @Override
    public DropperProfileDTO getByUserId(Long userId) {
        DropperProfile profile = dropperProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Perfil dropper não encontrado"));
        return new DropperProfileDTO(profile);
    }

    @Override
    public List<DropperProfileDTO> listByStatus(DropperStatus status) {
        return dropperProfileRepository.findByStatus(status)
                .stream()
                .map(DropperProfileDTO::new)
                .toList();
    }

    @Override
    public int getDiscount(Long userId) {
        return dropperProfileRepository.findByUserId(userId)
                .filter(p -> p.getStatus() == DropperStatus.ACTIVE)
                .map(p -> switch (p.getLevel()) {
                    case BRONZE   -> 5;
                    case SILVER   -> 10;
                    case GOLD     -> 15;
                    case PLATINUM -> 22;
                    case DIAMOND  -> 30;
                })
                .orElse(0);
    }
}