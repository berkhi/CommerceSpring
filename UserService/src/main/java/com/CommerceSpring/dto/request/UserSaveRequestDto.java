package com.CommerceSpring.dto.request;

import com.CommerceSpring.constants.messages.ErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;


public record UserSaveRequestDto(

        @Size(max = 40, message = ErrorMessages.FIRST_NAME_CANT_EXCEED_LENGTH)
        @NotBlank(message = ErrorMessages.FIRST_NAME_CANT_BE_BLANK)
        String firstName,
        @Size(message = ErrorMessages.LAST_NAME_EXCEED_LENGTH)
        @NotBlank(message = ErrorMessages.LAST_NAME_CANT_BE_BLANK)
        String lastName,


        @Email(message = ErrorMessages.EMAIL_TYPE_IS_WRONG)
        @NotBlank (message = ErrorMessages.EMAIL_NOT_BLANK)
        String email,

        @NotBlank(message = ErrorMessages.PASSWORD_NOT_BLANK)
        String password,


        List<Long> roleIds
) {

}
