package com.m2i.warhammermarket.service.mapper;

import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserMapper {
    @Autowired
    private ModelMapper modelMapper;
    public List<UserDTO> usersToUsersDTO(List<UserDAO> users) {
        return users.stream()
                .filter(Objects::nonNull)
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO userToUserDTO(UserDAO user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public List<UserDAO> userDTOsToUsers(List<UserDTO> userDTOs) {
        return userDTOs.stream()
                .filter(Objects::nonNull)
                .map(this::userDTOToUser)
                .collect(Collectors.toList());
    }

    public UserDAO userDTOToUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            UserDAO user = new UserDAO();
            user.setId(userDTO.getId());
            user.setMail(userDTO.getMail());
            user.setDateOfCreation(userDTO.getDateOfCreation());
            user.setActive(userDTO.isActive());
            return user;
        }
    }

    public UserDAO userFromId(Long id) {
        if (id == null) {
            return null;
        }
        UserDAO user = new UserDAO();
        user.setId(id);
        return user;
    }
}
