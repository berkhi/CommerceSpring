package com.CommerceSpring.controller;

import com.CommerceSpring.constants.EndPoints;
import com.CommerceSpring.constants.messages.SuccesMessages;
import com.CommerceSpring.dto.request.AddRoleToUserRequestDto;
import com.CommerceSpring.dto.request.UserDeleteRequestDto;
import com.CommerceSpring.dto.request.UserSaveRequestDto;
import com.CommerceSpring.dto.request.UserUpdateRequestDto;
import com.CommerceSpring.dto.response.GetAllUsersResponseDto;
import com.CommerceSpring.dto.response.ResponseDto;
import com.CommerceSpring.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.CommerceSpring.constants.EndPoints.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(USER)
public class UserController {
    private final UserService userService;

    @PostMapping(SAVE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Admin tarafından kullanıcı oluşturma",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<Boolean>> saveUser(@RequestBody @Valid UserSaveRequestDto userSaveRequestDTO){
        userService.saveUser(userSaveRequestDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().code(200).message(SuccesMessages.USER_SAVED).build());
    }
    
    @PutMapping(UPDATE)
    @PreAuthorize("hasAnyAuthority('ADMIN','MEMBER')")
    @Operation(summary = "AuthId'si verilen kullanıcıların bilgilerinin güncellenmesi",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<Boolean>> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDTO){
        userService.updateUser(userUpdateRequestDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().code(200).message(SuccesMessages.USER_UPDATED).build());
    }

    @PutMapping(DELETE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "AuthId'si verilen kullanıcının soft delete'i",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<Boolean>> deleteUser(@RequestBody UserDeleteRequestDto userDeleteRequestDTO){
        userService.deleteUser(userDeleteRequestDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().code(200).message(SuccesMessages.USER_DELETED).build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/get-all-users")
    @Operation(summary = "Tüm kuullanıcıları getirir, adminin rol atamasi için ilk yöntem",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<List<GetAllUsersResponseDto>>> getAllUsers(){
        return ResponseEntity.ok(ResponseDto.<List<GetAllUsersResponseDto>>builder().code(200).data(userService.getAllUser()).message("All users sent").build());
    }

    @Operation(security = @SecurityRequirement(name = "memberBearerAuth"))
    @GetMapping("/get-user-roles")
    public ResponseEntity<ResponseDto<List<String>>> getAllUsersRoles(@RequestHeader("Authorization") String token){
        String jwtToken = token.replace("Bearer ", "");
        System.out.println("Received Token: " + token);
        return ResponseEntity.ok(ResponseDto.<List<String>>builder().code(200).message("User roles sent").data(userService.getUserRoles(jwtToken)).build());
    }

    @PutMapping("/add-role-to-user")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Admin tarafından tüm rollerin görüntülenmesi için gerekli istek",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<Boolean>> addRoleToUser(@RequestBody AddRoleToUserRequestDto addRoleToUserRequestDTO){
        userService.addRoleToUser(addRoleToUserRequestDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().code(200).message("Role added to user").build());
    }
    
}
