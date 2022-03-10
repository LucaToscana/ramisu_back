package com.m2i.warhammermarket.service.implement;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.m2i.warhammermarket.controller.exception.NotificationAlreadyExistsException;
import com.m2i.warhammermarket.controller.exception.NotificationNotFoundException;
import com.m2i.warhammermarket.entity.DAO.NotificationDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationId;
import com.m2i.warhammermarket.entity.DAO.OrderDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.enumeration.TypeMessage;
import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.repository.NotificationRepository;
import com.m2i.warhammermarket.repository.OrderRepository;
import com.m2i.warhammermarket.repository.UserInformationRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.NotificationService;
import com.m2i.warhammermarket.service.UserService;
import com.m2i.warhammermarket.service.mapper.UserInformationMapper;

@Service
@Transactional

public class NotificationServiceImplement implements NotificationService {

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

	public void sendPrivateNotificationForNewMessange(String email, String emailSender) {
		Message message = new Message();
		message.setReceiverName(email);
		message.setSenderName(emailSender);
		message.setStatus(TypeMessage.NOTIFICATION);
		message.setMessage(emailSender + "   vous a envoyé un nouveau message");
		messagingTemplate.convertAndSendToUser(email, "/notifications/private-messages", message);

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

	public void saveNotification(String email, String date, String message) {
		UserDAO user = userRepository.findByMail(email);
		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
		NotificationDAO notification = new NotificationDAO(new NotificationId(userInfo.getId(), date), userInfo, null,
				message);

		if (notificationRepository.existsById(notification.getId())) {
			throw new NotificationAlreadyExistsException();
		}
		notificationRepository.save(notification);
	}

	public void deleteNotification(String date, String mail) {
		System.out.println("1");
		UserDAO user = userRepository.findByMail(mail);
		System.out.println("2");

		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
		System.out.println("3");

		if (!notificationRepository.existsById(new NotificationId(userInfo.getId(), date))) {
			System.out.println("error1");

			throw new NotificationNotFoundException();
		}
		NotificationDAO not = notificationRepository.getById(new NotificationId(userInfo.getId(), date));
		System.out.println("not");

		OrderDAO order = not.getOrder();

		System.out.println("order");

		if (order != null) {
			System.out.println("equals");

			Set<NotificationDAO> notificatios = userInfo.getNotificationDAO();

			for (NotificationDAO n : notificatios) {
				if (n.getOrder().equals(order)) {
					notificationRepository.delete(n);
				}
			}
		} else {
			System.out.println("not equal");
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
				message.setSenderName("warhammermarket");
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

}