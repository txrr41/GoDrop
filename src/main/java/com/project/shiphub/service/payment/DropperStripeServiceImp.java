package com.project.shiphub.service.payment;

import com.project.shiphub.model.auth.DropperProfile;
import com.project.shiphub.model.auth.DropperStripeStatus;
import com.project.shiphub.model.auth.User;
import com.project.shiphub.repository.auth.DropperProfileRepository;
import com.stripe.Stripe;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DropperStripeServiceImp {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    // URL base do seu frontend — configure no application.properties
    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final DropperProfileRepository dropperProfileRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Transactional
    public Map<String, String> createOnboardingLink() throws Exception {
        DropperProfile profile = getAuthenticatedProfile();

        String accountId = profile.getStripeAccountId();

        if (accountId == null) {
            AccountCreateParams params = AccountCreateParams.builder()
                    .setType(AccountCreateParams.Type.EXPRESS)
                    .setEmail(profile.getUser().getEmail())
                    .setCapabilities(
                            AccountCreateParams.Capabilities.builder()
                                    .setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
                                            .setRequested(true).build())
                                    .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder()
                                            .setRequested(true).build())
                                    .build()
                    )
                    .setBusinessType(AccountCreateParams.BusinessType.COMPANY)
                    .build();

            Account account = Account.create(params);
            accountId = account.getId();

            profile.setStripeAccountId(accountId);
            profile.setStripeAccountStatus(DropperStripeStatus.PENDING);
            dropperProfileRepository.save(profile);
        }


        AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                .setAccount(accountId)
                .setRefreshUrl(frontendUrl + "/dropper/stripe/refresh")
                .setReturnUrl(frontendUrl + "/dropper/stripe/return")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink link = AccountLink.create(linkParams);

        return Map.of(
                "url", link.getUrl(),
                "stripeAccountId", accountId
        );
    }

    @Transactional
    public Map<String, Object> syncStripeAccount() throws Exception {
        DropperProfile profile = getAuthenticatedProfile();

        if (profile.getStripeAccountId() == null) {
            throw new IllegalStateException("Dropper não tem conta Stripe cadastrada");
        }

        Account account = Account.retrieve(profile.getStripeAccountId());

        boolean chargesEnabled = Boolean.TRUE.equals(account.getChargesEnabled());
        boolean detailsSubmitted = Boolean.TRUE.equals(account.getDetailsSubmitted());

        DropperStripeStatus newStatus;
        if (chargesEnabled) {
            newStatus = DropperStripeStatus.ACTIVE;
        } else if (detailsSubmitted) {
            newStatus = DropperStripeStatus.RESTRICTED;
        } else {
            newStatus = DropperStripeStatus.PENDING;
        }

        profile.setStripeAccountStatus(newStatus);
        profile.setStripeChargesEnabled(chargesEnabled);
        dropperProfileRepository.save(profile);

        return Map.of(
                "status", newStatus.name(),
                "chargesEnabled", chargesEnabled,
                "detailsSubmitted", detailsSubmitted
        );
    }

    private DropperProfile getAuthenticatedProfile() {
        User userDetails = (User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        return dropperProfileRepository.findByUserId(userDetails.getId())
                .orElseThrow(() -> new IllegalStateException("Perfil de dropper não encontrado"));
    }
}