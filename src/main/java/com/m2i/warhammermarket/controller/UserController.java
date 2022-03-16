package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DTO.ProductDTO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.DTO.UserInformationDTO;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.ChatMessageService;
import com.m2i.warhammermarket.service.EmailSenderService;
import com.m2i.warhammermarket.service.NotificationService;
import com.m2i.warhammermarket.service.UserService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
//@PreAuthorize("hasAuthority('user')")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/user")
public class UserController {

	
	@Autowired
	private ChatMessageService chatService;
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailSenderService emailSenderService;

	/**
	 * REST: {POST: /pictureProfile}
	 *
	 * @param multipartFile profile picture to record in database and save in upload
	 *                      directory
	 * @throws IOException
	 * @return HttpStatus OK, UNAUTHORIZED or BAD_REQUEST
	 */
	@RequestMapping(value = "/pictureProfile", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> savePicture(@RequestParam("image") MultipartFile multipartFile)
			throws IOException {
		MediaType mediaType = MediaType.parseMediaType(multipartFile.getContentType());
		if (!mediaType.getType().equals("image"))
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ProfileWrapper userProfile = userService.getProfile(userDetails.getUsername());

		if (userService.savePicture(userProfile, multipartFile))
			return ResponseEntity.ok(HttpStatus.OK);
		else
			return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/removePictureProfile", method = RequestMethod.PUT)
	public ResponseEntity<HttpStatus> removePicture() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ProfileWrapper userProfile = userService.getProfile(userDetails.getUsername());
		userService.removePictureProfile(userProfile);

		return ResponseEntity.ok(HttpStatus.OK);

	}

	/*
	 * get User informations & address
	 * 
	 * @return ProfileWrapper
	 */
	@GetMapping("/profile")
	public ResponseEntity<ProfileWrapper> getProfile() {
		return ResponseEntity
				.ok(userService.getProfile(SecurityContextHolder.getContext().getAuthentication().getName()));
	}

	/*
	 * updates User informations & address
	 *
	 * @param ProfileWrapper
	 * 
	 * @return HttpStatus
	 */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PutMapping("/profile")
	public ResponseEntity<HttpStatus> editProfile(@RequestBody ProfileWrapper profile) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		// current user validity
		boolean success = profile.getMail().equals(userDetails.getUsername());
		if (!success)
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		try {
			userService.updateProfile(profile);
		} catch (Exception e) {
			return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(HttpStatus.OK);
	}

	/**
	 * user change password
	 *
	 */
	@Autowired
	private PasswordEncoder passwordEncoder;

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PutMapping("/changePSW")
	public ResponseEntity<HttpStatus> changePSW(@RequestBody PSWValues values) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserDAO user = userRepository.findByMail(userDetails.getUsername());
		if (passwordEncoder.matches(values.getPassword(), user.getPassword())) {
			if (values.getNewPassword().equals(values.getNewPasswordTest())) {
				UserDTO userDTO = userService.findOneByUserMail(user.getMail());
				userService.changeUserPasswordAndDeletePasswordToken(userDTO, values.getNewPassword());
				return ResponseEntity.ok(HttpStatus.OK);
			}
			return ResponseEntity.ok(HttpStatus.UNPROCESSABLE_ENTITY);
		} else {
			return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);
		}
	}

	/**
	 * user request for the password change procedure
	 * 
	 * @return HttpStatus code
	 */
	@CrossOrigin("*")
	@GetMapping("/requestChangePSW")
	public ResponseEntity<HttpStatus> requestPSW() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails != null) {
			try {
				String passwordToken = this.userService.createPasswordResetToken(userDetails.getUsername());
				Optional<UserInformationDTO> userInformationDTO = this.userService
						.findUserInfoByUserMail(userDetails.getUsername());
				this.emailSenderService
						.sendEmail(EmailSenderService.getMailPasswordHandling(userInformationDTO.get().getFirstName(),
								userInformationDTO.get().getLastName(), passwordToken, userDetails.getUsername()));
				return ResponseEntity.ok(HttpStatus.OK);
			} catch (MessagingException e) {
				e.printStackTrace();
				return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			} catch (IOException e) {
				e.printStackTrace();
				return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return ResponseEntity.ok(HttpStatus.FORBIDDEN);
	}

	/*
	 * Check token validity
	 * 
	 * @key token
	 *
	 * @return HttpStatus HttpStatus.OK HttpStatus.NOT_FOUND HttpStatus.LOCKED
	 * HttpStatus.FORBIDDEN
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/userPasswordCheck/{key}")
	public ResponseEntity<HttpStatus> checkTokenValidityFromUserAccount(@PathVariable String key) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails != null) {
			UserDTO user = userService.findOneByUserMail(userDetails.getUsername());

			if (user.getToken() != null && user.getToken().equals(key)) {
				if (this.userService.isPasswordTokenValid(user.getTokenExpiryDate())) {
					return ResponseEntity.ok(HttpStatus.OK);
				}
				return ResponseEntity.ok(HttpStatus.LOCKED);
			}
			return ResponseEntity.ok(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(HttpStatus.FORBIDDEN);
	}

	/**
	 * Add product in user favorites list
	 * 
	 * @param product wrap id product
	 * @return httpStatus OK || FORBIDDEN
	 *
	 */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/addFavorite/")
	public ResponseEntity<HttpStatus> addFavorite(@RequestBody ProductDTO product) {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			userService.addFavorite(userDetails.getUsername(), product.getId());
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.ok(HttpStatus.FORBIDDEN);
		}

		return ResponseEntity.ok(HttpStatus.OK);
	}

	/**
	 * remove product of user favorites list
	 * 
	 * @param product wrap id product
	 * @return httpStatus OK || FORBIDDEN
	 *
	 */
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@PostMapping("/removeFavorite/")
	public ResponseEntity<HttpStatus> rmFavorite(@RequestBody ProductDTO product) {
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			userService.removeFavorite(userDetails.getUsername(), product.getId());
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.ok(HttpStatus.FORBIDDEN);
		}

		return ResponseEntity.ok(HttpStatus.OK);
	}

	/**
	 * get list of favorites products
	 * 
	 * @return List<ProductDTO>
	 *
	 */

	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/getFavorites/")
	public ResponseEntity<List<ProductDTO>> getFavorites()// getFavorites(Pageable pageable)
	{
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<ProductDTO> list = userService.getFavorites(userDetails.getUsername());
			return ResponseEntity.ok().body(list);
		} catch (Exception ex) {
			return ResponseEntity.ok(new ArrayList<>());
		}
	}
	
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@GetMapping("/sendAllUserNotification")
	public ResponseEntity<HttpStatus> sendAllUserNotification()
	{
		try {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			notificationService.sendAllUserNotifications(userDetails.getUsername());
			
			/*
			if (userDetails != null && userDetails.getAuthorities().stream()
				      .anyMatch(a -> a.getAuthority().equals("commercial"))) {
			
			chatService.sendAllChats(userDetails.getUsername());}else {

			chatService.sendAllUserChats(userDetails.getUsername());}*/
			return ResponseEntity.ok(HttpStatus.OK);
		} catch (Exception ex) {
			return ResponseEntity.ok(HttpStatus.FORBIDDEN);
		}
	}
	
	
	
	
	@CrossOrigin(origins = "*", allowedHeaders = "*")
	@DeleteMapping("/delete-notification-user/{date}")
	public ResponseEntity<HttpStatus> deleteNotificationUser(@PathVariable String date) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userDetails != null) {
			try {
				notificationService.deleteNotification(date, userDetails.getUsername());
			} catch (Exception exception) {
				exception.printStackTrace();
				return ResponseEntity.ok(HttpStatus.FORBIDDEN);
			}			
		}
		return ResponseEntity.ok(HttpStatus.OK);

	}
}
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
class PSWValues{
    String password;
    String newPassword;
    String newPasswordTest;
}
