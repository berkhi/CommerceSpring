package com.CommerceSpring.dto.request;

import java.util.UUID;

public record AddRoleToUserRequestDto(
        UUID userId,
        Long roleId
) {
}
