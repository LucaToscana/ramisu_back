package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserSecurityDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.model.UserInscription;
import com.m2i.warhammermarket.repository.AddressRepository;
import com.m2i.warhammermarket.repository.UserInformationRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.UserMapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImplement implements UserService {

    @Autowired
    private UserMapper userMapper;
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
        if(userSecurity.getAuthorities().size() == 0) {
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
        
         Date  date_of_creation = new Date();
         userDAO.setDateOfCreation(date_of_creation);
        UserDAO userNewDAO=   userRepository.save(userDAO);
        return userNewDAO.getId();
    }

    @Override
    public UserDTO findOneByUserMail(String mail) {
        UserDAO userDAO = userRepository.findByMail(mail);
        if(userDAO == null) return  null; //Si jamais le user n'existe pas, on renvoi directement null
        UserDTO userDTO = new UserDTO(
                userDAO.getId(),
                userDAO.getMail(),
                userDAO.getAuthorities().stream().map(AuthorityDAO::getAuthority).collect(Collectors.toList()));
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
    public void delete(Long id) {

    }

    @Override
    public ProfileWrapper getProfile(String mail) {
        UsersInformationDAO user =
            userInformationRepository.getByMail(mail);
        return new ProfileWrapper(user,addressRepository.getAddressMainByIdUser(user.getUser().getId()));
    }
    
    /*
    @Column (name = "date_of_creation", nullable = false)
    private Date dateOfCreation;

    @Column (name = "active", nullable = false)
    private boolean active;

    @Column (name = "reset_password_token", nullable = true)
    private String token;*/

	@Override
	public Long saveInscription(UserInscription userInscription) {
		List<String> authorities =new ArrayList<String> ();
		authorities.add("user");
		UserSecurityDTO userSecDTO = new UserSecurityDTO();
		userSecDTO.setAuthorities(authorities);
		userSecDTO.setMail(userInscription.getEmail());
		userSecDTO.setPassword(userInscription.getPassword());
		Long newId = save(userSecDTO);
		return newId;
	}
}
