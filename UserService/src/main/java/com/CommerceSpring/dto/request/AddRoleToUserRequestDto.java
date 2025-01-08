package com.CommerceSpring.dto.request;

public record AddRoleToUserRequestDto(
        Long userId,
        Long roleId
) {
}
