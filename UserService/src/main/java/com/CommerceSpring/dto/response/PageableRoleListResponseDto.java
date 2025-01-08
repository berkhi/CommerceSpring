package com.CommerceSpring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PageableRoleListResponseDto {
    private List<RoleResponseDto> roleList;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
