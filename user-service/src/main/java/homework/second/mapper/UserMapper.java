package homework.second.mapper;

import homework.second.dto.UserDto;
import homework.second.model.UserEntity;

public final class UserMapper {
    private UserMapper() {}

    public static UserDto dtoFromEntity(UserEntity user) {
        if (user == null) return null;
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAge(user.getAge());

        return dto;
    }

    public static UserEntity entityFromDto(UserDto dto) {
        if (dto == null) return null;
        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());

        return user;
    }

    public static void updateEntityFromDto(UserEntity user, UserDto dto) {
        if (dto == null || user == null) return;
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
    }
}
