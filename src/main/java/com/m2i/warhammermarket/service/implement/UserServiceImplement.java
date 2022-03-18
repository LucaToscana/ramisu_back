package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.entity.DAO.*;
import com.m2i.warhammermarket.entity.DAO.AddressDAO;
import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.entity.wrapper.RegistrationProfile;
import com.m2i.warhammermarket.repository.*;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.ProductService;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.ProductMapper;
import com.m2i.warhammermarket.service.mapper.UserInformationMapper;
import com.m2i.warhammermarket.service.mapper.UserMapper;
import com.m2i.warhammermarket.utils.FileUpload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

import java.io.IOException;
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
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductMapper productMapper;
	@Autowired
	private ProductRepository productRepository;

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
		if (userDAO == null)
			return null; // Si jamais le user n'existe pas, on renvoie directement null

		UserDTO userDTO = this.userMapper.userToUserDTO(userDAO);
		userDTO.setAuthorities(userDAO.getAuthorities());

		return userDTO;
	}

	@Override
	public Page<UserDTO> findAll(Pageable pageable) {

		return this.userRepository.findAll(pageable).map(this.userMapper::userToUserDTO);
	}

	@Override
	public Optional<UserDTO> findOne(Long id) {

		return Optional.ofNullable(userMapper.userToUserDTO(userRepository.findById(id).get()));
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
		if (userDAO == null)
			return null;

		UserDTO userDTO = this.userMapper.userToUserDTO(userDAO);
		userDTO.setAuthorities(userDAO.getAuthorities());

		return userDTO;
	}

	/**
	 * Create a token to reset password and save it to the database
	 *
	 * @param userEmail mail of the user for whom the password token will be
	 *                  registered
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
	 * @param userDTO the information used to find the user and update their
	 *                password
	 * @author Cecile
	 */
	public void changeUserPasswordAndDeletePasswordToken(UserDTO userDTO, String newPassword) {
		UserDAO userDAO = this.userRepository.findByMail(userDTO.getMail());
		userDAO.setPassword(passwordEncoder.encode(newPassword));
		userDAO.setToken(null);
		this.userRepository.save(userDAO);
	}

	@Override
	public void delete(Long id) {

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
		ProfileWrapper p = new ProfileWrapper(user, address);
		return p;
	}

	@Override
	public boolean updateProfile(ProfileWrapper profile) throws IllegalArgumentException {

		UserDTO user = findOneByUserMail(profile.getMail());

		UsersInformationDAO infoProfile = userInformationRepository.getByMail(profile.getMail());
		infoProfile.setFirstName(profile.getFirstName());
		infoProfile.setLastName(profile.getLastName());
		infoProfile.setPhone(profile.getPhone());
		infoProfile.setAvatar(profile.getAvatar());
		infoProfile.setBirthdate(profile.getBirthdate());
		AddressDAO addressProfile = profile.getAddress();
		addressProfile.setId(addressRepository.getAddressMainByIdUser(user.getId()).getId());

		UsersInformationDAO savedInfo = userInformationRepository.save(infoProfile);
		AddressDAO savedAddress = addressRepository.save(addressProfile);

		return savedAddress != null && savedInfo != null;

	}

	@Override
	public Optional<UserInformationDTO> findUserInfoByUserMail(String mail) {
		UsersInformationDAO userInfoDao = this.userInformationRepository.getByMail(mail);
		return Optional.ofNullable(this.userInfoMapper.userInfoDAOToUserInfoDTO(userInfoDao));
	}

	/**
	 * Save in database and store on fileSystem The profile picture
	 * 
	 * @param userProfile   : The profile wrapper to update
	 * @param multipartFile : user profile picture
	 */
	@Override
	public boolean savePicture(ProfileWrapper userProfile, MultipartFile multipartFile) {

		boolean success = false;
		try {
			String fileName = FileUpload.getMD5Name(multipartFile.getInputStream())
					.concat(String.valueOf(System.currentTimeMillis()));
			String current = userProfile.getAvatar();
			if (current != null)
				FileUpload.removefile(current);

			if (!FileUpload.saveFile(fileName, multipartFile))
				return false;

			userProfile.setAvatar(fileName);
			success = updateProfile(userProfile);

		} catch (IOException e) {
			success = false;
			e.printStackTrace();
		}
		return success;
	}

	/**
	 * remove picture in database and delete on fileSystem the profile picture
	 * 
	 * @param userProfile : The profile wrapper to update
	 * 
	 */
	@Override
	public void removePictureProfile(ProfileWrapper userProfile) {

		String avatar = userProfile.getAvatar();
		if (avatar != null)
			FileUpload.removefile(avatar);

		userProfile.setAvatar(null);
		updateProfile(userProfile);
	}

	@Override
	public Long save(RegistrationProfile userProfile) {

		List<String> authorities = new ArrayList<String>();
		authorities.add("user");

		UserSecurityDTO userSecDTO = userProfile.getUserSecurity();
		UsersInformationDAO userInfo = userProfile.getUserInformations();

		Long userSecID = save(userSecDTO);
		userInfo.setUser(userRepository.findById(userSecID).get());

		try {
			userInfo.setId(userInformationRepository.save(userInfo).getId());
			AddressDAO address = addressRepository.save(userProfile.getAddress());
			inhabitRepository.save(InhabitDAO.getInhabit(address, userInfo, userSecID));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return userSecID;
	}

	/*
	 * @param value 1 user 2 commercial 3 admin
	 */
	@Override
	public void updateRoles(Long userID, Long roleID, boolean active) {

		Optional<UserDAO> user = userRepository.findById(userID);
		AuthorityDAO authUser = new AuthorityDAO(AuthorityConstant.ROLE_USER);

		Set<AuthorityDAO> authorities = new HashSet<>();
		authorities.add(authUser);
		if (roleID >= 2) {
			AuthorityDAO authComm = new AuthorityDAO(AuthorityConstant.ROLE_COMM);
			authorities.add(authComm);
		}

		if (roleID == 3) {
			AuthorityDAO authAdmin = new AuthorityDAO(AuthorityConstant.ROLE_ADMIN);
			authorities.add(authAdmin);
		}

		user.get().setAuthorities(authorities);
		user.get().setActive(active);
		userRepository.save(user.get());
	}

	/**
	 *
	 */
	public void addFavorite(String username, Long id) throws Exception {
		UserDAO user = userRepository.findByMail(username);
		Set<ProductDAO> favorites = user.getFavorites();
		ProductDAO product = favorites.stream().filter(p -> p.getId() == id).findFirst().orElse(null);

		if (product != null)
			throw new Exception("Product already present in wishlist ");

		product = productRepository.getById(id);
		favorites.add(product);
		userRepository.save(user);

	}

	public List<ProductDTO> getFavorites(String username) {
		UserDAO user = userRepository.findByMail(username);
		List<ProductDAO> fav = new ArrayList<ProductDAO>(user.getFavorites());
		List<ProductDTO> favDTO = productMapper.productsToProductsDTOList(fav);
		return favDTO;
	}

	public void removeFavorite(String username, Long id) {
		UserDAO user = userRepository.findByMail(username);
		List<ProductDAO> fav = new ArrayList<ProductDAO>(user.getFavorites());
		fav.removeIf(e -> e.getId().equals(id));
		user.setFavorites(new HashSet<ProductDAO>(fav));

		userRepository.save(user);

	}

}
