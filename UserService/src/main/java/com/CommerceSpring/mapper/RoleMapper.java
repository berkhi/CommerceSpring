package com.CommerceSpring.mapper;

import com.CommerceSpring.dto.request.RoleCreateDto;
import com.CommerceSpring.dto.response.RoleResponseDto;
import com.CommerceSpring.entity.Role;
import com.CommerceSpring.views.GetAllRoleView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);


    Role roleCreateDTOToRole(RoleCreateDto roleCreateDTO);

    RoleResponseDto getAllRoleViewToRoleResponseDto(GetAllRoleView getAllRoleView);


    List<RoleResponseDto> rolesToRoleResponseDtoList(List<Role> roles);

    @Mapping(target = "roleId", source = "id")
    RoleResponseDto roleToRoleResponseDto(Role role);




}