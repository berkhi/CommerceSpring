package com.CommerceSpring.dto.response;

import com.CommerceSpring.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoleResponseDto {
    private Long roleId;
    private String roleName;
    private EStatus status;
    private String roleDescription;
}
