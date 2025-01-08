package com.CommerceSpring.dto.response;

import com.CommerceSpring.entity.enums.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class GetAllUsersResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private EStatus status;
    private String email;
    private List<String> userRoles;
}
