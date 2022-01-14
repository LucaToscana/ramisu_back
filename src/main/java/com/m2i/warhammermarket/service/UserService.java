package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.model.UserInscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;

import java.util.Optional;

public interface UserService {

    Long save(UserSecurityDTO userSecurity);

    UserDTO findOneByUserMail(String mail);
    /**
     * Save a user in database
     * @param user the entity to save
     * @return the persisted entity
     */
    UserDTO save (UserDTO user);

    Page<UserDTO> findAll(Pageable pageable);

    Optional<UserDTO> findOne(Long id);

    void delete(Long id);

    ProfileWrapper getProfile(String mail);

	Long saveInscription(UserInscription userInscription);
}
