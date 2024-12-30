package com.CommerceSpring.service;

import com.CommerceSpring.dto.request.*;
import com.CommerceSpring.entity.Auth;
import com.CommerceSpring.exception.AuthServiceException;
import com.CommerceSpring.repository.AuthRepository;
import com.CommerceSpring.utilty.JwtTokenManager;
import com.CommerceSpring.config.security.SecurityConfig;
import com.CommerceSpring.utilty.enums.EStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.CommerceSpring.exception.ErrorType.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Boolean register(RegisterRequestDto dto) {
        checkEmailExist(dto.email());
        checkPasswordMatch(dto.password(), dto.rePassword());
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(dto.password());

        Auth auth = Auth.builder()
                .email(dto.email())
                .password(encodedPassword)
                .build();
        authRepository.save(auth);

        return true;
    }

    public String login(LoginRequestDto dto) {
        Auth auth = authRepository.findOptionalByEmail(dto.email())
                .orElseThrow(() -> new AuthServiceException(EMAIL_OR_PASSWORD_WRONG));

        if (!auth.getStatus().equals(EStatus.ACTIVE)) {
            throw new AuthServiceException(USER_IS_NOT_ACTIVE);
        }

        if (!passwordEncoder.bCryptPasswordEncoder().matches(dto.password(), auth.getPassword())) {
            throw new AuthServiceException(EMAIL_OR_PASSWORD_WRONG);
        }

        return jwtTokenManager.createToken(auth.getId())
                .orElseThrow(() -> new AuthServiceException(TOKEN_CREATION_FAILED));
    }

    public Auth findById(Long authId) {
        return authRepository.findById(authId)
                .orElseThrow(() -> new AuthServiceException(USER_NOT_FOUND));
    }

    private void checkEmailExist(String email) {
        if (authRepository.existsByEmail(email)) {
            throw new AuthServiceException(EMAIL_ALREADY_TAKEN);
        }
    }

    private void checkPasswordMatch(String password, String rePassword) {
        if (!password.equals(rePassword)) {
            throw new AuthServiceException(PASSWORD_MISMATCH);
        }
    }

}