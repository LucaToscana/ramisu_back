package com.m2i.warhammermarket.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.m2i.warhammermarket.controller.exception.NotificationAlreadyExistsException;
import com.m2i.warhammermarket.controller.exception.NotificationNotFoundException;
import com.m2i.warhammermarket.entity.DAO.NotificationDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationId;
import com.m2i.warhammermarket.entity.DAO.OrderDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.model.ResponseMessage;
import com.m2i.warhammermarket.model.StatusMessage;
import com.m2i.warhammermarket.repository.NotificationRepository;
import com.m2i.warhammermarket.repository.OrderRepository;
import com.m2i.warhammermarket.repository.UserInformationRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.mapper.UserInformationMapper;

@Service
public class NotificationService {

	private final String MESSAGE_STATUS = "Le statut d'une commande vient de changer !";

	private final SimpMessagingTemplate messagingTemplate;

	private final UserInformationRepository userInfoRepository;

	private final UserRepository userRepository;

	private final OrderRepository orderRepository;

	private final NotificationRepository notificationRepository;

	@Autowired
	public NotificationService(SimpMessagingTemplate messagingTemplate, UserInformationRepository userInfoRepository,
			UserRepository userRepository, OrderRepository orderRepository,
			NotificationRepository notificationRepository) {
		this.messagingTemplate = messagingTemplate;
		this.userInfoRepository = userInfoRepository;
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
		this.notificationRepository = notificationRepository;
	}

	public void sendGlobalNotification() {
		ResponseMessage message = new ResponseMessage("Global Notification");

		messagingTemplate.convertAndSend("/topic/global-notifications", message);
	}

	public void sendPrivateNotification(final String email) {
		Message message = new Message("Private Notification", "warhammermarket", email, StatusMessage.NOTIFICATION);
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);

	}

	public void sendCustomPrivateNotification(final String email, String messageNotification) {
		/*
		 * (String senderName, String receiverName, String message, StatusMessage
		 * status)
		 */
		Message message = new Message("warhammermarket", email, messageNotification, StatusMessage.NOTIFICATION);
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);

	}

	public void sendPrivateNotificationForNewMessange(final String email, final String emailSender) {
		Message message = new Message();
		message.setReceiverName(email);
		message.setSenderName(emailSender);
		message.setStatus(StatusMessage.NOTIFICATION);
		message.setMessage(emailSender + "   vous a envoyé un nouveau message");
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);

	}

	public void sendOrderStatusNotification(long idOrder, final String email,String messageOrder) {
		Message message = new Message();
		message.setReceiverName(email);
		message.setSenderName("warhammermarket");
		message.setStatus(StatusMessage.NOTIFICATION);
		message.setMessage(messageOrder);
		message.setIdorder(idOrder);
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);
		saveNotificationStatusOrder(idOrder, email, message.getDate(),message.getMessage());

	}

	public void saveNotificationStatusOrder(long idOrder, final String email, final String date,String message) {
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

	public void deleteNotificationStatusOrder(String date, String mail) {

		UserDAO user = userRepository.findByMail(mail);
		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
		if (!notificationRepository.existsById(new NotificationId(userInfo.getId(), date))) {
			throw new NotificationNotFoundException();
		}
		NotificationDAO not = notificationRepository.getById(new NotificationId(userInfo.getId(), date));

		OrderDAO order = not.getOrder();

		if (!order.equals(null)) {
			Set<NotificationDAO> notificatios = userInfo.getNotificationDAO();

			for (NotificationDAO n : notificatios) {
				if (n.getOrder().equals(order)) {
					notificationRepository.delete(n);
				}

			}

		}
		/*
		 * UserDAO user = userRepository.findByMail(email); UsersInformationDAO userInfo
		 * = userInfoRepository.findByUser(user); OrderDAO order =
		 * orderRepository.getById(string); NotificationDAO notification = new
		 * NotificationDAO(new NotificationId(userInfo.getId()), userInfo, order,
		 * MESSAGE_STATUS);
		 * 
		 * if (notificationRepository.existsById(notification.getId())) { throw new
		 * NotificationAlreadyExistsException(); }
		 * notificationRepository.save(notification);
		 */
	}

	public void sendAllUserNotification(String mail) {
		if (mail != null) {
			UserDAO user = userRepository.findByMail(mail);
			UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
			Set<NotificationDAO> userNotifications = userInfo.getNotificationDAO();
			for (NotificationDAO n : userNotifications) {

				if (n.getOrder() != null) {
					Message message = new Message();
					message.setReceiverName(mail);
					message.setSenderName("warhammermarket");
					message.setStatus(StatusMessage.NOTIFICATION);
					message.setMessage( n.getMessage());
					message.setIdorder(n.getOrder().getId());
					message.setDate(n.getId().getDate());
					messagingTemplate.convertAndSendToUser(mail, "/notifications/private-messages", message);
				}
			}
		}
	}

}