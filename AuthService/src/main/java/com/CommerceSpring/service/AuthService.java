package com.CommerceSpring.service;

import com.CommerceSpring.RabbitMQ.Model.EmailAndPasswordModel;
import com.CommerceSpring.RabbitMQ.Model.UserSaveFromAuthModel;
import com.CommerceSpring.dto.request.*;
import com.CommerceSpring.entity.Auth;
import com.CommerceSpring.exception.AuthServiceException;
import com.CommerceSpring.repository.AuthRepository;
import com.CommerceSpring.utilty.JwtTokenManager;
import com.CommerceSpring.utilty.enums.EStatus;
import lombok.RequiredArgsConstructor;
import com.CommerceSpring.utilty.PasswordEncoder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.CommerceSpring.exception.ErrorType.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;
    private final PasswordEncoder passwordEncoder;
    private final RabbitTemplate rabbitTemplate;

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
        rabbitTemplate.convertAndSend("commerceSpringDirectExchange","keySaveUserFromAuth", UserSaveFromAuthModel.builder()
                .authId(auth.getId())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .build());

//        rabbitTemplate.convertAndSend("commerceSpringDirectExchange","keySendVerificationEmail", EmailVerificationModel.builder()
//                .email(dto.email()).firstName(dto.firstName()).lastName(dto.lastName()).authId(auth.getId()).build());
        return true;
    }

    @RabbitListener(queues = "queueEmailAndPasswordFromAuth")
    public EmailAndPasswordModel emailAndPasswordFromAuth(Long authId) {
        Auth auth = authRepository.findById(authId).orElseThrow();
        return EmailAndPasswordModel.builder()
                .email(auth.getEmail())
                .encryptedPassword(auth.getPassword())
                .build();


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

        String token = jwtTokenManager.createToken(auth.getId()).orElseThrow(() -> new AuthServiceException(TOKEN_CREATION_FAILED));
        return token;
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