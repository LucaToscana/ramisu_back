package com.m2i.warhammermarket.service.implement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.m2i.warhammermarket.entity.DAO.ChatMessageDAO;
import com.m2i.warhammermarket.entity.DAO.ChatMessageId;
import com.m2i.warhammermarket.entity.DAO.ChatsDAO;

import com.m2i.warhammermarket.entity.DAO.UserDAO;
import com.m2i.warhammermarket.entity.DAO.UsersInformationDAO;
import com.m2i.warhammermarket.entity.enumeration.TypeMessage;
import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.repository.ChatMessageRepository;
import com.m2i.warhammermarket.repository.ChatsRepository;
import com.m2i.warhammermarket.repository.UserInformationRepository;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.ChatMessageService;

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

		UsersInformationDAO userInfo = userInfoRepository.findByUser(user);

		UserDAO userReceiver = userRepository.findByMail(client);

		UsersInformationDAO userInfoReceiver = userInfoRepository.findByUser(userReceiver);

		ChatsDAO newChat = new ChatsDAO();

		try {
			newChat = chatTopicRepository.findByUserCustomers(userInfoReceiver).get(0);

		} catch (Exception e) {

			ChatsDAO newChatSave = new ChatsDAO(userInfo);
			newChat = chatTopicRepository.save(newChatSave);

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
			if (c.getChat().getUserCustomers().getUser().getMail().equals(mail)) {
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
		//	messagingTemplate.convertAndSendToUser(mail, "/private", message);

		}

	}

	public List<Message> sendAllChatsList() {
		List<ChatMessageDAO> list = chatRepository.findAll();
		List<Message> listMessage = new ArrayList<Message>();

		for (ChatMessageDAO c : list) {
			Message message = new Message();
			message.setReceiverName(c.getUsers_receiver().getUser().getMail());
			message.setSenderName(c.getUser().getUser().getMail());
			message.setStatus(TypeMessage.MESSAGE);
			message.setMessage(c.getMessage());
			message.setDate(c.getId().getDate());
			message.setChat(c.getChat().getUserCustomers().getUser().getMail());
			listMessage.add(message);
		}
		return listMessage;
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