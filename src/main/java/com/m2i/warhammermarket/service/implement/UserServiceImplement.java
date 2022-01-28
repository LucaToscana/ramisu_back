package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.entity.DAO.*;
import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.model.UserInscription;
import com.m2i.warhammermarket.repository.AddressRepository;
import com.m2i.warhammermarket.repository.InhabitRepository;
import com.m2i.warhammermarket.repository.UserInformationRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.UserInformationMapper;
import com.m2i.warhammermarket.service.mapper.UserMapper;
import com.m2i.warhammermarket.utils.FileUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImplement implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInformationMapper userInfoMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserInformationRepository userInformationRepository;

    @Autowired
    private InhabitRepository inhabitRepository;
    // password encode permet d'encoder le mot de passe avant de l'enregistrer en
    // BDD
    // Pour fonctionner, PasswordEncoder est défini dans SecurityConfig (dans le
    // package Security)
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Long save(UserSecurityDTO userSecurity) {
        UserDAO userDAO = new UserDAO();
        userDAO.setMail(userSecurity.getMail());
        userDAO.setPassword(passwordEncoder.encode(userSecurity.getPassword())); // On encode le password avec
        // PasswordEncoder
        if (userSecurity.getAuthorities().size() == 0) {
            AuthorityDAO authorityDAO = new AuthorityDAO(AuthorityConstant.ROLE_USER);
            Set<AuthorityDAO> authorities = new HashSet<>();
            authorities.add(authorityDAO);
            userDAO.setAuthorities(authorities);
        } else {
            userDAO.setAuthorities(
                    userSecurity.getAuthorities().stream().map(AuthorityDAO::new).collect(Collectors.toSet()));
        }

        Date date_of_creation = new Date();
        userDAO.setDateOfCreation(date_of_creation);
        UserDAO userNewDAO = userRepository.save(userDAO);
        return userNewDAO.getId();
    }

    @Override
    public UserDTO findOneByUserMail(String mail) {
        UserDAO userDAO = userRepository.findByMail(mail);
        if (userDAO == null) return null; //Si jamais le user n'existe pas, on renvoi directement null

        UserDTO userDTO = this.userMapper.userToUserDTO(userDAO);
        userDTO.setAuthorities(userDAO.getAuthorities()
                .stream()
                .map(AuthorityDAO::getAuthority)
                .collect(Collectors.toList()));

        return userDTO;
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

    /**
     * Find a user by using a password token
     *
     * @param passwordResetToken the token used to search for a user
     * @return UserDTO : the user found
     * @author Cecile
     */
    @Override
    public UserDTO findUserByPasswordResetToken(String passwordResetToken) {
        UserDAO userDAO = this.userRepository.findByToken(passwordResetToken);
        if (userDAO == null) return null;

        UserDTO userDTO = this.userMapper.userToUserDTO(userDAO);
        userDTO.setAuthorities(userDAO.getAuthorities()
                .stream()
                .map(AuthorityDAO::getAuthority)
                .collect(Collectors.toList()));

        return userDTO;
    }

    /**
     * Create a token to reset password and save it to the database
     *
     * @param userEmail mail of the user for whom the password token will be registered
     * @return String : return the created password token
     * @author Cecile
     */
    @Override
    public String createPasswordResetToken(String userEmail) {
        UserDAO userDAO = this.userRepository.findByMail(userEmail);
        String passwordToken = UUID.randomUUID().toString();
        userDAO.setToken(passwordToken);
        userDAO.setTokenExpiryDate(this.calculatePasswordTokenExpiryDate(ApplicationConstants.TOKEN_EXPIRATION));
        this.userRepository.save(userDAO);

        return passwordToken;
    }

    /**
     * Calculate and return the expiration date of a password token
     *
     * @param expiryTimeInMinutes the expiration time in minutes
     * @return Date : the date at which the token becomes invalid
     * @author Cecile
     */
    private Date calculatePasswordTokenExpiryDate(final int expiryTimeInMinutes) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(calendar.getTime().getTime());
    }

    /**
     * Check if the password token is still valid before changing the password
     *
     * @param tokenExpiryDate the date at which the password token will expire
     * @return boolean : true if the token is valid, false if not
     * @author Cecile
     */
    public boolean isPasswordTokenValid(Date tokenExpiryDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        return calendar.getTime().before(tokenExpiryDate);
    }

    /**
     * Change the user's password and delete their password token
     *
     * @param userSecurityDTO the information used to find the user and update their password
     * @author Cecile
     */
    public void changeUserPasswordAndDeletePasswordToken(UserSecurityDTO userSecurityDTO) {
        UserDAO userDAO = this.userRepository.findByMail(userSecurityDTO.getMail());
        userDAO.setPassword(passwordEncoder.encode(userSecurityDTO.getPassword()));
        userDAO.setToken(null);
        this.userRepository.save(userDAO);
    }

    @Override
    public void delete(Long id) {

    }

  

    @Override
    public Long saveInscription(UserInscription userInscription) {
        List<String> authorities = new ArrayList<String>();
        authorities.add("user");
        UserSecurityDTO userSecDTO = new UserSecurityDTO();
        userSecDTO.setAuthorities(authorities);
        userSecDTO.setMail(userInscription.getEmail());
        userSecDTO.setPassword(userInscription.getPassword());
        Long newId = save(userSecDTO);
        UserDAO user = userRepository.findById(newId).get();
        UsersInformationDAO userInfo = new UsersInformationDAO();
        userInfo.setUser(user);
        String sDate1 = userInscription.getAnniversaire();

        userInfo.setBirthdate(getDate(sDate1));
        userInfo.setFirstName(userInscription.getNom());
        userInfo.setLastName(userInscription.getPrenom());
        userInfo.setPhone(userInscription.getTelephone());
        UsersInformationDAO usInfo = userInformationRepository.save(userInfo);

        AddressDAO newadd = new AddressDAO();

        newadd.setAdditionalAddress(userInscription.getComplementadresse());
        newadd.setCity(userInscription.getVille());
        newadd.setCountry(userInscription.getPays());
        newadd.setPostalCode(userInscription.getCodepostal());
        newadd.setNumber(userInscription.getNumeroA());
        newadd.setStreet(userInscription.getRue());
        AddressDAO newaddID = addressRepository.save(newadd);

        InhabitDAO newInhabit = new InhabitDAO();
        InhabitId newIdInhabit = new InhabitId();
        newIdInhabit.setIdAddress(newaddID.getId());
        newIdInhabit.setIdUser(usInfo.getId());
        newInhabit.setId(newIdInhabit);
        newInhabit.setAddress(newaddID);
        newInhabit.setUser(userInfo);
        newInhabit.setIsMain(1);
        inhabitRepository.save(newInhabit);
        return usInfo.getId();
    }

    public static java.sql.Date getDate(String date) {
        java.util.Date utilDate;
        java.sql.Date sqlDate = null;
        try {
            utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            sqlDate = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return sqlDate;
    }

    @Override
    public ProfileWrapper getProfile(String mail) {
        UsersInformationDAO user = userInformationRepository.getByMail(mail);
        long id = user.getUser().getId();
        AddressDAO address = addressRepository.getAddressMainByIdUser(id);
        
        return new ProfileWrapper(user,address);
    }


	@Override
	public boolean updateProfile(ProfileWrapper profile) throws IllegalArgumentException {
		
		UserDTO user = findOneByUserMail(profile.getMail());
	
		UsersInformationDAO infoProfile =  userInformationRepository.getByMail(profile.getMail());
							infoProfile.setLastName( profile.getLastName());
							infoProfile.setFirstName(profile.getFirstName());
							infoProfile.setPhone(profile.getPhone());  
							infoProfile.setAvatar(profile.getAvatar());
							infoProfile.setBirthdate(profile.getBirthdate());    	
    	AddressDAO 	addressProfile = profile.getAddress();
    				addressProfile.setId(addressRepository.getAddressMainByIdUser(user.getId()).getId());
         
    	UsersInformationDAO savedInfo = userInformationRepository.save(infoProfile);
    	AddressDAO savedAddress = addressRepository.save(addressProfile);
    	
    	return savedAddress!=null && savedInfo != null;
    	
	}

	 @Override
	    public Optional<UserInformationDTO> findUserInfoByUserMail(String mail) {
	        UsersInformationDAO userInfoDao = this.userInformationRepository.getByMail(mail);
	        return Optional.ofNullable(this.userInfoMapper.userInfoDAOToUserInfoDTO(userInfoDao));
	    }

	 /**
	  * 		Save in database and store on fileSystem
	  * 		The profile picture
	  *		 @param userProfile : The profile wrapper to update
	  * 	@param	multipartFile : user profile picture
	  * */
	@Override
	public boolean savePicture(ProfileWrapper userProfile, MultipartFile multipartFile) {
		
		boolean success = false;
		try {
			String fileName = FileUpload.getMD5Name(multipartFile.getInputStream());
			String current = userProfile.getAvatar();
			
			if(!FileUpload.saveFile(fileName, multipartFile))return false;
			
			if(current!=null)FileUpload.removefile(current);
			
			userProfile.setAvatar(fileName);
			success = updateProfile(userProfile);	
		
		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}
	 /**
	  * 		remove picture in database and delete on fileSystem
	  * 		the profile picture
	  *		 @param userProfile : The profile wrapper to update
	  * 	
	  * */
	@Override
	public void removePictureProfile(ProfileWrapper userProfile) {
		
		String avatar = userProfile.getAvatar();
		if(avatar!=null)FileUpload.removefile(avatar);
		
		userProfile.setAvatar(null);
		updateProfile(userProfile);
	}



}
