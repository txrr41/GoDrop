package com.project.shiphub.service.auth;

import com.project.shiphub.dto.auth.DropperProfileDTO;
import com.project.shiphub.dto.auth.DropperRegisterRequest;
import com.project.shiphub.model.auth.DropperStatus;

import java.math.BigDecimal;
import java.util.List;

public interface DropperService {
    DropperProfileDTO register(Long userId, DropperRegisterRequest request);
    DropperProfileDTO approve(Long dropperProfileId);
    DropperProfileDTO getByUserId(Long userId);
    List<DropperProfileDTO> listByStatus(DropperStatus status);
    int getDiscount(Long userId);
    DropperProfileDTO updateSalesAndLevel(Long userId, BigDecimal amount);
}