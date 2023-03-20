package com.example.user_rest_service.dto.mapper;


import com.example.user_rest_service.dto.UserDto;
import com.example.user_rest_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserDtoMapper {

        @Mapping(target="userAge", expression = "java(Integer.valueOf(dto.getUserAge()))")
        User fromDto(UserDto dto);


        @Mapping(target = "userAge", expression = "java(String.valueOf(user.getUserAge()))")
        UserDto toDto(User user);

        @Mapping(target="userAge", expression = "java(Integer.valueOf(dto.getUserAge()))")
        List<User> fromDto(List<UserDto> dto);


        @Mapping(target = "userAge", expression = "java(String.valueOf(user.getUserAge()))")
        List<UserDto> toDto(List<User> user);

}
