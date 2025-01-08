package com.CommerceSpring.dto.request;

import com.CommerceSpring.constants.messages.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleCreateDto (
        @Size(max = 60)
        @NotBlank(message = ErrorMessages.ROLE_CANT_BE_BLANK)
        String roleName,
        @Size(max = 500)
        String roleDescription){

}

