package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.util.Optional;

public interface UserService {

    /**
     * Save a user in database
     * @param user the entity to save
     * @return the persisted entity
     */
    UserDTO save (UserDTO user);

    Page<UserDTO> findAll(Pageable pageable);

    Optional<UserDTO> findOne(Long id);

    void delete(Long id);

}
