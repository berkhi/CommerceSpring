package com.CommerceSpring.util;

import com.CommerceSpring.dto.request.UserSaveRequestDto;
import com.CommerceSpring.entity.Role;
import com.CommerceSpring.entity.User;
import com.CommerceSpring.entity.enums.EStatus;
import com.CommerceSpring.repository.RoleRepository;
import com.CommerceSpring.repository.UserRepository;
import com.CommerceSpring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DemoData implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final Random random = new Random();

    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveBaseRoles();
        saveSuperAdmin();
//        saveUsers();
    }
    private void saveSuperAdmin(){
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(1L).get());
        User superAdmin = User.builder()
                .role(roles)
                .authId(UUID.randomUUID())
                .firstName("Admin")
                .lastName("Admin")
                .status(EStatus.ACTIVE)
                .build();
        userRepository.save(superAdmin);
    }

    private void saveBaseRoles(){
        Role adminRole = Role.builder()
                .roleName("ADMIN")
                .roleDescription("VP")
                .build();

        Role member = Role.builder().roleName("MEMBER").build();

        roleRepository.save(adminRole); //1
        roleRepository.save(member);    //2
    }


    private void saveUsers(){
        List<Long> roles = new ArrayList<>(List.of(2L));
        UserSaveRequestDto user = new UserSaveRequestDto("Member","USER","member@example.com","123",roles);
        userService.saveUser(user);
    }

}
