package com.CommerceSpring.dto.request;

import com.CommerceSpring.constants.messages.ErrorMessages;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;


public record UserDeleteRequestDto(
        @NotBlank(message = ErrorMessages.USER_ID_CANT_BE_BLANK)
        UUID userId
) {

}
