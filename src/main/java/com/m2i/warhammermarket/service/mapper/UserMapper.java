package com.m2i.warhammermarket.service.mapper;

import com.m2i.warhammermarket.entity.DAO.User;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserMapper {

    public List<UserDTO> usersToUsersDTO(List<User> users) {
        return users.stream()
                .filter(Objects::nonNull)
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user);
    }

    public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::userDTOToUser)
                .collect(Collectors.toList());
    }

    public User userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            user.setId(userDTO.getId());
            user.setMail(userDTO.getMail());
            user.setDateOfCreation(userDTO.getDateOfCreation());
            user.setActive(userDTO.isActive());
            return user;
        }
    }

    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }
}
