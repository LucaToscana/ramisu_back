package com.m2i.warhammermarket.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.entity.wrapper.RegistrationProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;

import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;

public interface UserService {

  

    /**
     * Save a user in database
     * @param userSecurity the entity to save
     * @return the persisted entity
     */
    Long save (UserSecurityDTO userSecurity);

    Page<UserDTO> findAll(Pageable pageable);

    Optional<UserDTO> findOne(Long id);

    UserDTO findUserByPasswordResetToken(String passwordResetToken);
    
    Optional<UserInformationDTO> findUserInfoByUserMail(String mail);
    
    
    UserDTO findOneByUserMail(String mail);


    String createPasswordResetToken(String userEmail);

    boolean isPasswordTokenValid(Date tokenExpiryDate);

    void changeUserPasswordAndDeletePasswordToken(UserDTO userDTO, String newPassword);
   
    void delete(Long id);
    
    Long save(RegistrationProfile userProfile);
    ProfileWrapper getProfile(String mail);

	
    /*
     * 	Update user profile information
     * @param user data for update database
     * @return true success
     * */
	boolean updateProfile(ProfileWrapper profile) throws IllegalArgumentException ;

	boolean savePicture(ProfileWrapper userProfile, MultipartFile multipartFile);

	void removePictureProfile(ProfileWrapper userProfile);

	void updateRoles(Long userID, Long roleID, boolean b);


    List<ProductDTO> getFavorites(String username);
    void addFavorite(String username, Long id) throws Exception;
    void removeFavorite(String username, Long id);



}
