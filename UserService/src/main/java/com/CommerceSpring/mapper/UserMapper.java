package com.CommerceSpring.mapper;

import com.CommerceSpring.RabbitMQ.Model.SaveUserFromOtherServicesModel;
import com.CommerceSpring.dto.request.UserSaveRequestDto;
import com.CommerceSpring.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    @Mapping(target = "role",ignore = true)
    User userSaveRequestDTOToUser(UserSaveRequestDto userSaveRequestDTO);

    @Mapping(target = "role",ignore = true)
    User saveUserFromOtherServicesToUser(SaveUserFromOtherServicesModel saveUserFromOtherServicesModel);


}