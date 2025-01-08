package com.CommerceSpring.controller;

import com.CommerceSpring.constants.EndPoints;
import com.CommerceSpring.constants.messages.SuccesMessages;
import com.CommerceSpring.dto.request.PageRequestDto;
import com.CommerceSpring.dto.request.RoleCreateDto;
import com.CommerceSpring.dto.request.RoleUpdateRequestDto;
import com.CommerceSpring.dto.response.PageableRoleListResponseDto;
import com.CommerceSpring.dto.response.ResponseDto;
import com.CommerceSpring.service.RoleService;
import com.CommerceSpring.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import static com.CommerceSpring.constants.EndPoints.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ROLE)
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;

    @PostMapping(CREATE_USER_ROLE)
    //@PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Admin tarafından kullanıcı rolü eklenmesi",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<Boolean>> createUserRole(@RequestBody @Valid RoleCreateDto roleCreateDTO){
        roleService.createUserRole(roleCreateDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().code(200).message(SuccesMessages.ROLE_CREATED).build());
    }

    @PutMapping(UPDATE_USER_ROLE)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "admin tarafından var olan bir rolun düzenlenmesi",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<Boolean>> updateUserRole(@RequestBody @Valid RoleUpdateRequestDto roleUpdateRequestDTO){
        roleService.updateUserRole(roleUpdateRequestDTO);
        return ResponseEntity.ok(ResponseDto.<Boolean>builder().code(200).message(SuccesMessages.ROLE_UPDATED).build());
    }

    @PostMapping(GET_ALL_USER_ROLES)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Rollerin hepsini getiren get isteği",security = @SecurityRequirement(name = "adminBearerAuth"))
    public ResponseEntity<ResponseDto<PageableRoleListResponseDto>> getAllUserRoles(@RequestBody PageRequestDto pageRequestDTO){
        return ResponseEntity.ok(ResponseDto.<PageableRoleListResponseDto>builder().code(200).data(roleService.getAllUserRoles(pageRequestDTO)).message(SuccesMessages.All_ROLES_SENT).build());
    }

}
