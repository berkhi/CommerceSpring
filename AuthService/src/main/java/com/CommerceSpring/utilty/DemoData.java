package com.CommerceSpring.utilty;

import com.CommerceSpring.entity.Auth;
import com.CommerceSpring.repository.AuthRepository;
import com.CommerceSpring.utilty.enums.EStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DemoData {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    @PostConstruct
    public void saveAdmin(){
        Auth auth = Auth.builder()
                .email("admin@example.com")
                .password(passwordEncoder.bCryptPasswordEncoder().encode("123"))
                .status(EStatus.ACTIVE)
                .build();
        authRepository.save(auth);
    }

}
