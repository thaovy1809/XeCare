package com.group3.xecare2.user.mappers;

import org.mapstruct.Mapper;

import com.group3.xecare2.user.dtos.UserCreateDto;
import com.group3.xecare2.user.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
	User toEntity(UserCreateDto dto);
    //CustomerDto toDto(Customer customer); // nếu cần map ngược lại

	//User toEntity(UserUpdateDto dto);
	
}
