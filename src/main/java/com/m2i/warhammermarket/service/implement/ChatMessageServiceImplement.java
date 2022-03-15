package com.m2i.warhammermarket.service.implement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.m2i.warhammermarket.controller.exception.NotificationAlreadyExistsException;
import com.m2i.warhammermarket.controller.exception.NotificationNotFoundException;
import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DAO.ChatMessageDAO;
import com.m2i.warhammermarket.entity.DAO.ChatMessageId;
import com.m2i.warhammermarket.entity.DAO.ChatsDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationDAO;
import com.m2i.warhammermarket.entity.DAO.NotificationId;
import com.m2i.warhammermarket.entity.DAO.OrderDAO;
import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.enumeration.TypeMessage;
import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.repository.AuthorityRepository;
import com.m2i.warhammermarket.repository.ChatMessageRepository;
import com.m2i.warhammermarket.repository.ChatsRepository;
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

public class ChatMessageServiceImplement implements ChatMessageService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private UserInformationRepository userInfoRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChatMessageRepository chatRepository;

	@Autowired
	private ChatsRepository chatTopicRepository;

	@Override
	public ChatMessageDAO saveChatMessage(Message message) {

		String client = message.getReceiverName();
		
		UserDAO user = userRepository.findByMail(message.getSenderName());
		System.out.println(client);

		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);
		System.out.println(client);

		UserDAO userReceiver = userRepository.findByMail(client);
		System.out.println(client);

		UsersInformationDAO userInfoReceiver = userInfoRepository.findByUser(userReceiver);
		System.out.println(client);

		ChatsDAO newChat = new ChatsDAO();

		try {
			newChat = chatTopicRepository.findByUserCustomers(userInfoReceiver).get(0);
			System.out.println("FIND");

		} catch (Exception e) {
			System.out.println(message.getSenderName() + "     CATCH");

			ChatsDAO newChatSave = new ChatsDAO(userInfo);

			newChat = chatTopicRepository.save(newChatSave);
			System.out.println("SAVE");

			// }
		}
		ChatMessageDAO messageDao = new ChatMessageDAO(
				new ChatMessageId(userInfo.getId(), userInfoReceiver.getId(), message.getDate()), userInfo,
				userInfoReceiver, message.getMessage(), newChat);
		System.out.println(message.getMessage());
		if (chatRepository.existsById(messageDao.getId())) {
		}
		return chatRepository.save(messageDao);

	}

	public void sendAllUserChats(String mail) {
		List<ChatMessageDAO> list = chatRepository.findAll();

		for (ChatMessageDAO c : list) {
			if (c.getChat().getUserCustomers().getUser().getMail().equals(mail) ) {
				Message message = new Message();
				message.setReceiverName(c.getUsers_receiver().getUser().getMail());
				message.setSenderName(c.getUser().getUser().getMail());
				message.setStatus(TypeMessage.MESSAGE);
				message.setMessage(c.getMessage());
				message.setDate(c.getId().getDate());
				message.setChat(c.getChat().getUserCustomers().getUser().getMail());

				messagingTemplate.convertAndSendToUser(mail, "/private", message);

			}
		}
		
	}

	public void sendAllChats(String mail) {
		List<ChatMessageDAO> list = chatRepository.findAll();

		for (ChatMessageDAO c : list) {

			Message message = new Message();
			message.setReceiverName(c.getUsers_receiver().getUser().getMail());
			message.setSenderName(c.getUser().getUser().getMail());
			message.setStatus(TypeMessage.MESSAGE);
			message.setMessage(c.getMessage());
			message.setDate(c.getId().getDate());
			message.setChat(c.getChat().getUserCustomers().getUser().getMail());
			messagingTemplate.convertAndSendToUser(mail, "/private", message);

		}

	}

	public Date parseDate(String date) {
		java.util.Date temp = null;
		try {
			temp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(date);
		} catch (ParseException e) {
			System.out.println("opssss");
			e.printStackTrace();
		}
		System.out.println(temp);
		return temp;
	}


}