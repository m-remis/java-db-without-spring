package com.example.demo.util;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.UserEntity;

import java.util.Optional;

public class ConvertUtil {

    private ConvertUtil() {
        // not to be instantiated
    }

    public static UserEntity convert(UserDto userDto) {
        return new UserEntity(userDto.id(), userDto.guid(), userDto.userName());
    }

    public static Optional<UserDto> convertToOptionalDto(UserEntity userEntity) {
        return Optional.ofNullable(userEntity).map(present -> new UserDto(userEntity.getUserId(), userEntity.getUserGuid(), userEntity.getUserName()));
    }

    public static UserDto convert(UserEntity userEntity) {
        return new UserDto(userEntity.getUserId(), userEntity.getUserGuid(), userEntity.getUserName());
    }
}