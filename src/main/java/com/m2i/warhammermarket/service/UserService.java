package com.m2i.warhammermarket.service;

import java.util.Date;
import java.util.Optional;

import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.model.UserInscription;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;

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

    UserDTO findUserByPasswordResetToken(String passwordResetToken);
    
    Optional<UserInformationDTO> findUserInfoByUserMail(String mail);
    
    String createPasswordResetToken(String userEmail);

    boolean isPasswordTokenValid(Date tokenExpiryDate);

    void changeUserPasswordAndDeletePasswordToken(UserSecurityDTO userSecurityDTO);

    void delete(Long id);

    ProfileWrapper getProfile(String mail);

	Long saveInscription(UserInscription userInscription);
}
