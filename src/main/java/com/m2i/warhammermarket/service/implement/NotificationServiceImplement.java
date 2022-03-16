package com.m2i.warhammermarket.service.implement;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.m2i.warhammermarket.controller.exception.NotificationAlreadyExistsException;
import com.m2i.warhammermarket.controller.exception.NotificationNotFoundException;
import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.ChatMessageDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationId;
import com.m2i.warhammermarket.entity.DAO.OrderDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.enumeration.TypeMessage;
import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.repository.AuthorityRepository;
import com.m2i.warhammermarket.repository.NotificationRepository;
import com.m2i.warhammermarket.repository.OrderRepository;
import com.m2i.warhammermarket.repository.UserInformationRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.security.AuthorityConstant;
import com.m2i.warhammermarket.service.ChatMessageService;
import com.m2i.warhammermarket.service.NotificationService;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.UserInformationMapper;

@Service
@Transactional

public class NotificationServiceImplement implements NotificationService {
	@Autowired
	private AuthorityRepository authorityRepository;
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private UserInformationRepository userInfoRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private ChatMessageService chatMessageService;

	public void sendGlobalNotification(String messageNotification) {
		Message message = new Message();
		message.setMessage(messageNotification);
		messagingTemplate.convertAndSend("/topic/global-notifications", message);
	}

	public void sendPrivateNotification(final String email) {
		Message message = new Message("Private Notification", "warhammermarket", email, TypeMessage.NOTIFICATION);
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);

	}

	public Message sendCustomPrivateNotification(final String email, String messageNotification) {

		Message message = new Message("warhammermarket", email, messageNotification, TypeMessage.NOTIFICATION);
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);
		return message;
	}

	public Message sendPrivateNotificationForNewMessange(String email, String emailSender) {
		Message message = new Message();
		message.setReceiverName(email);
		message.setSenderName(emailSender);
		message.setStatus(TypeMessage.NOTIFICATION);
		message.setMessage(emailSender + "   vous a envoyé un nouveau message");
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);
		return message;
	}

	public void sendOrderStatusNotification(long idOrder, String email, String messageOrder) {
		Message message = new Message();
		message.setReceiverName(email);
		message.setSenderName("warhammermarket");
		message.setStatus(TypeMessage.NOTIFICATION);
		message.setMessage(messageOrder);
		message.setIdorder(idOrder);
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);
		saveNotificationOrder(idOrder, email, message.getDate(), message.getMessage());

	}

	public void saveNotificationOrder(long idOrder, String email, String date, String message) {
		UserDAO user = userRepository.findByMail(email);
		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
		OrderDAO order = orderRepository.getById(idOrder);
		NotificationDAO notification = new NotificationDAO(new NotificationId(userInfo.getId(), date), userInfo, order,
				message);

		if (notificationRepository.existsById(notification.getId())) {
			throw new NotificationAlreadyExistsException();
		}
		notificationRepository.save(notification);
	}

	public NotificationDAO saveNotification(String email, String date, String message) {
		UserDAO user = userRepository.findByMail(email);
		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
		NotificationDAO notification = new NotificationDAO(new NotificationId(userInfo.getId(), date), userInfo, null,
				message);

		if (notificationRepository.existsById(notification.getId())) {
			throw new NotificationAlreadyExistsException();
		}
		return notificationRepository.save(notification);
	}

	public void deleteNotification(String date, String mail) {
		UserDAO user = userRepository.findByMail(mail);
		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
		if (!notificationRepository.existsById(new NotificationId(userInfo.getId(), date))) {
			throw new NotificationNotFoundException();
		}
		NotificationDAO not = notificationRepository.getById(new NotificationId(userInfo.getId(), date));
		OrderDAO order = not.getOrder();
		if (order != null) {
			Set<NotificationDAO> notificatios = userInfo.getNotificationDAO();

			for (NotificationDAO n : notificatios) {
				if (n.getOrder().equals(order)) {
					notificationRepository.delete(n);
				}
			}
		} else {
			notificationRepository.delete(not);
		}
	}

	public void sendAllUserNotifications(String mail) {
		if (mail != null) {
			UserDAO user = userRepository.findByMail(mail);
			UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
			Set<NotificationDAO> userNotifications = userInfo.getNotificationDAO();
			for (NotificationDAO n : userNotifications) {
				Message message = new Message();
				message.setReceiverName(mail);
				message.setSenderName("WARHAMMERMARKET");
				message.setStatus(TypeMessage.NOTIFICATION);
				message.setMessage(n.getMessage());
				message.setDate(n.getId().getDate());
				if (n.getOrder() != null) {

					message.setIdorder(n.getOrder().getId());

				}

				messagingTemplate.convertAndSendToUser(mail, "/notifications/private-messages", message);
			}
		}
	}

	@Override
	public void sendMessage(Message message) {
		simpMessagingTemplate.convertAndSendToUser(message.getSenderName(), "/private", message);

		if (message.getReceiverName().equals("WARHAMMERMARKET")) {
			Message m = sendMessageToRole(message, AuthorityConstant.ROLE_COMM);
		} else {
			sendMessageToUser(message, message.getReceiverName());
		}

	}

	public Message sendMessageToRole(Message message, String role) {
		String mRec = message.getReceiverName();
		System.out.println(mRec);
		List<UserDAO> admins = userRepository.findByAuthorities_Name(role);
		System.out.println(mRec);

		System.out.println(admins);
		for (UserDAO a : admins) {
			System.out.println(mRec);

			if (a.getMail().equals(message.getSenderName()) == false) {
				simpMessagingTemplate.convertAndSendToUser(a.getMail(), "/private", message);
				Message m = sendPrivateNotificationForNewMessange(a.getMail(), message.getSenderName());
				message.setReceiverName(a.getMail());
				NotificationDAO not = saveNotification(a.getMail(), message.getDate(), m.getMessage());
			}
			System.out.println(mRec);

		}
		System.out.println(mRec);

		message.setReceiverName(message.getSenderName());
		chatMessageService.saveChatMessage(message);

		return message;

	}

	public Message sendMessageToUser(Message message, String receiver) {
		String mRec = message.getReceiverName();

		simpMessagingTemplate.convertAndSendToUser(mRec, "/private", message);
		Message m = sendPrivateNotificationForNewMessange(mRec, message.getSenderName());
		message.setReceiverName(mRec);
		NotificationDAO not = saveNotification(mRec, message.getDate(), m.getMessage());

		//message.setReceiverName(message.getSenderName());
		ChatMessageDAO chatt = chatMessageService.saveChatMessage(message);
		
	

		return message;

	}
}