package com.m2i.warhammermarket.service.implement;

import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.m2i.warhammermarket.configuration.ApplicationConstants;
import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.repository.AddressRepository;
import com.m2i.warhammermarket.repository.UserInformationRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.UserInformationMapper;
import com.m2i.warhammermarket.service.mapper.UserMapper;

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


    // password encode permet d'encoder le mot de passe avant de l'enregistrer en BDD
    // Pour fonctionner, PasswordEncoder est défini dans SecurityConfig (dans le package Security)
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Long save(UserSecurityDTO userSecurity) {
        UserDAO userDAO = new UserDAO();
        userDAO.setMail(userSecurity.getMail());
        userDAO.setPassword(passwordEncoder.encode(userSecurity.getPassword())); //On encode le password avec PasswordEncoder
        if (userSecurity.getAuthorities().size() == 0) {
            AuthorityDAO authorityDAO = new AuthorityDAO(AuthorityConstant.ROLE_USER);
            Set<AuthorityDAO> authorities = new HashSet<>();
            authorities.add(authorityDAO);
            userDAO.setAuthorities(authorities);
        } else {
            userDAO.setAuthorities(
                    userSecurity.getAuthorities()
                            .stream()
                            .map(AuthorityDAO::new)
                            .collect(Collectors.toSet())
            );
        }
        return this.userRepository.save(userDAO).getId();
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
        /*
        UserDTO userDTO = new UserDTO(
                userDAO.getId(),
                userDAO.getMail(),
                userDAO.getAuthorities().stream().map(AuthorityDAO::getAuthority).collect(Collectors.toList()));
                */
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
     * @param token
     *
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
     *
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
     *
     * @author Cecile
     */
    public boolean isPasswordTokenValid(Date tokenExpiryDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        System.out.println("CALENDAR ===> : " + calendar.getTime().getTime() + " -- TOKEN ===> " + tokenExpiryDate);
        return calendar.getTime().before(tokenExpiryDate);
    }

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
    public ProfileWrapper getProfile(String mail) {
        UsersInformationDAO user =
                userInformationRepository.getByMail(mail);
        return new ProfileWrapper(user, addressRepository.getAddressMainByIdUser(user.getUser().getId()));
    }

    @Override
    public Optional<UserInformationDTO> findUserInfoByUserMail(String mail) {
        UsersInformationDAO userInfoDao = this.userInformationRepository.getByMail(mail);
        return Optional.ofNullable(this.userInfoMapper.userInfoDAOToUserInfoDTO(userInfoDao));
    }
}
