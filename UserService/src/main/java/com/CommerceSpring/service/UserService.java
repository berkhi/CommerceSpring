package com.CommerceSpring.service;

import com.CommerceSpring.RabbitMQ.Model.SaveUserFromAuthModel;
import com.CommerceSpring.RabbitMQ.Model.UserRoleListModel;
import com.CommerceSpring.dto.request.AddRoleToUserRequestDto;
import com.CommerceSpring.dto.request.UserDeleteRequestDto;
import com.CommerceSpring.dto.request.UserSaveRequestDto;
import com.CommerceSpring.dto.request.UserUpdateRequestDto;
import com.CommerceSpring.dto.response.GetAllUsersResponseDto;
import com.CommerceSpring.entity.Role;
import com.CommerceSpring.entity.User;
import com.CommerceSpring.entity.enums.EStatus;
import com.CommerceSpring.exception.ErrorType;
import com.CommerceSpring.exception.UserException;
import com.CommerceSpring.mapper.UserMapper;
import com.CommerceSpring.repository.UserRepository;
import com.CommerceSpring.util.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtTokenManager jwtTokenManager;
    private final RabbitTemplate rabbitTemplate;

    public User findByAuthId(UUID authId) {
        return userRepository.findByAuthId(authId).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
    }

    @Transactional
    public void saveUser(UserSaveRequestDto userSaveRequestDTO) {
        User user = UserMapper.INSTANCE.userSaveRequestDTOToUser(userSaveRequestDTO);
        if(!userSaveRequestDTO.roleIds().isEmpty()){
            List<Role> usersRoles = roleService.getRolesByRoleId(userSaveRequestDTO.roleIds());
            user.setRole(usersRoles);
        }else {
            user.setRole(new ArrayList<>());
        }

        user.setStatus(EStatus.ACTIVE);

        userRepository.save(user);
    }

    @Transactional
    public void updateUser(UserUpdateRequestDto userUpdateRequestDTO) {
        User user = userRepository.findByAuthId(userUpdateRequestDTO.authId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));

        if(!user.getFirstName().equals(userUpdateRequestDTO.firstName())){
            user.setFirstName(userUpdateRequestDTO.firstName());
        }
        if(!user.getLastName().equals(userUpdateRequestDTO.lastName())){
            user.setLastName(userUpdateRequestDTO.lastName());
        }

        userRepository.save(user);
    }

    public void deleteUser(UserDeleteRequestDto userDeleteRequestDTO) {
        User user = userRepository.findById(userDeleteRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        user.setStatus(EStatus.DELETED);

        userRepository.save(user);
    }

    @Transactional
    public void saveUserFromAuthService(SaveUserFromAuthModel saveUserFromAuthModel){
        List<Role> usersRoles = new ArrayList<>();
        usersRoles.add(roleService.getRoleById(2L)); //MEMBER rol olarak kaydedilir.
        User user = User.builder()
                .authId(saveUserFromAuthModel.getAuthId())
                .firstName(saveUserFromAuthModel.getFirstName())
                .lastName(saveUserFromAuthModel.getLastName())
                .status(EStatus.PENDING)
                .role(usersRoles)
                .build();
        userRepository.save(user);
    }
    @RabbitListener(queues = "queueSaveUserFromAuth")
    private void listenAndSaveUserFromAuthService(SaveUserFromAuthModel saveUserFromAuthModel){
        saveUserFromAuthService(saveUserFromAuthModel);
    }

    public List<GetAllUsersResponseDto> getAllUser() {
        List<User> allUsersList = userRepository.findAll();
        allUsersList = allUsersList.stream()
                .filter(user -> user.getRole().stream().noneMatch(role -> role.getRoleName().equals("SUPER_ADMIN")))
                .toList();

        List<GetAllUsersResponseDto> allUsersResponseDTOList = new ArrayList<>();

        allUsersList.forEach(user -> {
            List<String> userRolesString = user.getRole().stream().map(Role::getRoleName).toList();
            allUsersResponseDTOList.add(GetAllUsersResponseDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .status(user.getStatus())
                    .userRoles(userRolesString)
                    .build());
        });


        return allUsersResponseDTOList;
    }

    public void addRoleToUser(AddRoleToUserRequestDto addRoleToUserRequestDTO) {
        User user = userRepository.findById(addRoleToUserRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        Role roleById = roleService.getRoleById(addRoleToUserRequestDTO.roleId());
        user.getRole().add(roleById);
        userRepository.save(user);
    }

    @RabbitListener(queues = "queueRolesByAuthId")
    private UserRoleListModel sendAuthRoles(UUID authId) {
        return getRolesForSecurity(authId);
    }

    public UserRoleListModel getRolesForSecurity(UUID authId) {
        List<Role> userRoles = userRepository.getUserRoles(authId);
        List<String> userRolesString = new ArrayList<>();
        userRoles.forEach(role -> {
            userRolesString.add(role.getRoleName());
        });
        return UserRoleListModel.builder().userRoles(userRolesString).build();
    }


    public List<String> getUserRoles(String jwtToken) {
        UUID authId = jwtTokenManager.getAuthIdFromToken(jwtToken).orElseThrow(() -> new UserException(ErrorType.INVALID_TOKEN));

        User user = userRepository.findByAuthId(authId).orElseThrow(() -> new UserException(ErrorType.USER_NOT_FOUND));
        System.out.println("JWT Token: " + jwtToken);
        System.out.println("Extracted Auth ID: " + authId);
        return user.getRole().stream().map(Role::getRoleName).toList();
    }


}
