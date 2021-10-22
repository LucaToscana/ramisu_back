package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImplement implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;

    public UserServiceImplement() {
    }

    @Override
    public UserDTO save(UserDTO user) {
        return null;
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<UserDTO> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }
}
