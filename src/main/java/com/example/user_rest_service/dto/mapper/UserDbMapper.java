package com.example.user_rest_service.dto.mapper;

import com.example.user_rest_service.entity.User;
import com.example.user_rest_service.entity.UserDB;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDbMapper {

    @Mapping(target="userId", expression = "java(Integer.valueOf(db.getUserId()))")
    User fromDB(UserDB db);

    @Mapping(target ="userId", expression = "java(String.valueOf(user.getUserId()))")
    UserDB toDB(User user);

    @Mapping(target="userId", expression = "java(Integer.valueOf(db.getUserId()))")
    List<User> fromDB(List<UserDB> dbList);

    @Mapping(target ="userId", expression = "java(String.valueOf(user.getUserId()))")
    List<UserDB> toDB(List<User> userList);


}
