package com.project.donate.mapper;

import com.project.donate.dto.Response.AddressResponse;
import com.project.donate.dto.Response.UserResponse;
import com.project.donate.dto.UserDTO;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final UserRepository userRepository;

    public UserResponse userToUserDto(User user) {
        return  UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname())
                .phone(user.getPhone())
                .age(user.getAge())
                .role(user.getRole())
                .build();
        //TODO buraya address alanı eklenilebilir
    }

    public List<UserResponse> usersToUserDtoList(List<User> users){
        if(users == null){
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>(users.size());

        for(User user :users){
            list.add(userToUserDto(user));
        }

        return list;
    }

}