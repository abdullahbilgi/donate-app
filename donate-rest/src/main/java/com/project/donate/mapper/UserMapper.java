package com.project.donate.mapper;

import com.project.donate.dto.UserDTO;
import com.project.donate.model.User;
import com.project.donate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final UserRepository userRepository;

    public UserDTO userToUserDto(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setAddress(user.getAddress());
        userDto.setPhone(user.getPhone());
        userDto.setAge(user.getAge());
        userDto.setRoles(
                user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
        return userDto;
    }

    public List<UserDTO> usersToUserDtoList(List<User> users){
        if(users == null){
            return null;
        }

        List<UserDTO> list = new ArrayList<UserDTO>(users.size());

        for(User user :users){
            list.add(userToUserDto(user));
        }

        return list;
    }

}