package com.project.shiphub.model.offer;

public enum OfferStatus {
    SCHEDULED,  // Oferta agendada para o futuro
    ACTIVE,     // Oferta ativa no momento
    EXPIRED,    // Oferta expirada (passou da data/hora de fim)
    PAUSED,     // Oferta pausada manualmente
    CANCELLED   // Oferta cancelada
}