package com.CommerceSpring.dto.request;

import com.CommerceSpring.constants.messages.ErrorMessages;
import jakarta.validation.constraints.NotBlank;


public record UserDeleteRequestDto(
        @NotBlank(message = ErrorMessages.USER_ID_CANT_BE_BLANK)
        Long userId
) {

}
