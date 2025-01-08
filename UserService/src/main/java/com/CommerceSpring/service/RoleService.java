package com.CommerceSpring.service;

import com.CommerceSpring.dto.request.PageRequestDto;
import com.CommerceSpring.dto.request.RoleCreateDto;
import com.CommerceSpring.dto.request.RoleUpdateRequestDto;
import com.CommerceSpring.dto.response.PageableRoleListResponseDto;
import com.CommerceSpring.dto.response.RoleResponseDto;
import com.CommerceSpring.entity.Role;
import com.CommerceSpring.entity.enums.EStatus;
import com.CommerceSpring.exception.ErrorType;
import com.CommerceSpring.exception.UserException;
import com.CommerceSpring.mapper.RoleMapper;
import com.CommerceSpring.repository.RoleRepository;
import com.CommerceSpring.views.GetAllRoleView;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public void createUserRole(RoleCreateDto roleCreateDto) {
        Role role = RoleMapper.INSTANCE.roleCreateDTOToRole(roleCreateDto);
        role.setRoleName(role.getRoleName().toUpperCase());
        roleRepository.save(role);
    }

    public List<Role> getRolesByRoleId(List<Long> roleIds) {
        List<Role> findedRoles = roleRepository.findAllById(roleIds);
        if (findedRoles.isEmpty()) {
            throw new UserException(ErrorType.ROLE_DATA_IS_EMPTY);
        }
        return findedRoles;
    }

    public void updateUserRole(RoleUpdateRequestDto roleUpdateRequestDto) {
        Role role = roleRepository.findById(roleUpdateRequestDto.roleId()).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
        role.setRoleName(roleUpdateRequestDto.roleName());
        role.setRoleDescription(roleUpdateRequestDto.roleDescription());
        roleRepository.save(role);
    }

    public void deleteUserRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
        role.setStatus(EStatus.DELETED);
        roleRepository.save(role);
    }

    public PageableRoleListResponseDto getAllUserRoles(PageRequestDto pageRequestDto) {
        Pageable pageable = PageRequest.of(pageRequestDto.page(), pageRequestDto.size());
        Page<GetAllRoleView> allRoles = roleRepository.getAllRolesWithSearch(pageRequestDto.searchText(), pageable);

        List<RoleResponseDto> roleResponseDtos = new ArrayList<>();
        allRoles.getContent().forEach(role -> {
            roleResponseDtos.add(RoleMapper.INSTANCE.getAllRoleViewToRoleResponseDto(role));
        });
        return PageableRoleListResponseDto.builder().roleList(roleResponseDtos).currentPage(allRoles.getNumber()).totalPages(allRoles.getTotalPages()).totalElements(allRoles.getTotalElements()).build();

    }

    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(() -> new UserException(ErrorType.ROLE_NOT_FOUND));
    }


    public Boolean checkIfRoleExistsByRoleName(String roleName) {
        return roleRepository.existsByRoleNameIgnoreCase(roleName);
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleNameIgnoreCase(roleName).orElseThrow(()-> new UserException(ErrorType.ROLE_NOT_FOUND));
    }
}
