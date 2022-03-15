package com.m2i.warhammermarket.service.implement;

import com.m2i.warhammermarket.entity.DAO.*;
import com.m2i.warhammermarket.entity.DTO.OrderDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;
import com.m2i.warhammermarket.model.Message;
import com.m2i.warhammermarket.model.RequestAddOrderWithAddress;
import com.m2i.warhammermarket.model.ResponseOrderDetails;
import com.m2i.warhammermarket.model.UpdateStatusOrder;
import com.m2i.warhammermarket.repository.*;
import com.m2i.warhammermarket.service.NotificationService;
import com.m2i.warhammermarket.service.OrderService;
import com.m2i.warhammermarket.service.mapper.OrderMapper;
import com.m2i.warhammermarket.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImplement implements OrderService {
	private final String NEW_COMMANDE = "Vous venez d'envoyer une nouvelle commande !";
	private final String MESSAGE_STATUS = "Le statut d'une commande vient de changer !";
	private final String MESSAGE_ADDRESS = "Une nouvelle adresse postale a été enregistrée";

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private StatusRepository statusRepository;

	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserInformationRepository userInformationRepository;
	@Autowired
	private LineOfOrderRepository lineOfOrderRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private InhabitRepository inhabitRepository;

	@Override
	public void createOrder(List<ProductOrderWrapper> productsOrder, String login,
			RequestAddOrderWithAddress orderNew) {
		OrderDAO orderDao = order(productsOrder, login, orderNew);
		OrderDAO order = orderRepository.save(orderDao);
		for (ProductOrderWrapper p : productsOrder) {
			ProductDAO productGet = productRepository.getById(p.getId());
			lineOfOrderRepository.save(new LineOfOrderDAO(new LineOfOrderId(p.getId(), order.getId()), p.getQuantite(),
					productGet, order));
			ProductDAO product = productRepository.getById(p.getId());
			product.setStock(product.getStock() - p.getQuantite());
			productRepository.save(product);
		}
		notificationService.sendOrderStatusNotification(order.getId(), login, NEW_COMMANDE);

	}

	/**
	 * test if the quantity ordered is less than or equal to the stock
	 * 
	 * @param productsOrder list product ordered
	 * @return boolean
	 * @author Brahim TALLA
	 */
	@Override
	public boolean checkStock(List<ProductOrderWrapper> productsOrder) {
		boolean verif = true;
		int i = 0;
		do {
			ProductDAO product = productRepository.getById(productsOrder.get(i).getId());
			if (product.getStock() < productsOrder.get(i).getQuantite())
				verif = false;

		} while (verif && productsOrder.size() > ++i);

		return verif;
	}

	@Override
	public List<OrderDTO> findAllByUserId(Long id) {

		List<OrderDAO> orderDAOS = orderRepository.findAllByUserUserId(id, Sort.by(Sort.Direction.DESC, "id"));
		return orderDAOS.stream().map(orderDAO -> orderMapper.OrderDAOtoOrderDTO(orderDAO))
				.collect(Collectors.toList());
	}

	@Override
	public List<ProductOrderWrapper> findAllByOrderId(Long id) {
		List<LineOfOrderDAO> lineOfOrderDAOS = lineOfOrderRepository.findAllByIdIdOrder(id);
		List<ProductOrderWrapper> productOrderWrappers = lineOfOrderDAOS.stream().map(lineOfOrderDAO -> {
			Integer quantity = lineOfOrderDAO.getQuantity();
			Long idProduct = lineOfOrderDAO.getId().getId();
			Optional<ProductDAO> productDAO = productRepository.findById(idProduct);
			if (productDAO.isPresent()) {
				return new ProductOrderWrapper(productDAO.get().getId(), productDAO.get().getLabel(),
						productDAO.get().getPrice(), 0, quantity, productDAO.get().getPicture());
			} else {
				return null;
			}
		}).collect(Collectors.toList());
		return productOrderWrappers;
	}

	private OrderDAO order(List<ProductOrderWrapper> productsOrder, String login, RequestAddOrderWithAddress orderNew) {
		OrderDAO order = new OrderDAO();
		UsersInformationDAO user = userInformationRepository.getByMail(login);
		Set<InhabitDAO> setInhabit = inhabitRepository.findAllByUserUserId(user.getId());
		order.setUser(user);
		order.setDate(new Date(System.currentTimeMillis()));
		order.setTotal(sumTotal(productsOrder));
		StatusDAO status = new StatusDAO();
		status.setId(1L);
		order.setStatus(status);
		AddressDAO add = orderNew.getAddress();
		AddressDAO addTest = null;
		if (add.getId() != null) {
			addTest = addressRepository.findById(add.getId()).orElse(null);
		}

		if (orderNew.getType().equals("domicile")) {
			setOrderDomicile(order, orderNew, addTest, add, setInhabit, user, login);
		} else {// En magasin
			order.setAddress(add);
		}
		return order;
	}

	private BigDecimal sumTotal(List<ProductOrderWrapper> productsOrder) {
		BigDecimal total = new BigDecimal(0);
		for (ProductOrderWrapper p : productsOrder) {
			total = total.add(p.getPrice().multiply(new BigDecimal(p.getQuantite())));
		}
		return total.multiply(BigDecimal.valueOf(1.20));
	}

	@Override
	public ResponseOrderDetails getOrderAndProductsByOrderId(Long id) {

		List<ProductOrderWrapper> listProductsOrderById = findAllByOrderId(id);

		OrderDTO order = orderMapper.OrderDAOtoOrderDTO(orderRepository.getById(id));
		ResponseOrderDetails newResponse = new ResponseOrderDetails(order, listProductsOrderById);
		return newResponse;
	}

	@Override
	public List<OrderDTO> findAll() {

		List<OrderDAO> orderDAOS = orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		return orderDAOS.stream().map(orderDAO -> orderMapper.OrderDAOtoOrderDTO(orderDAO))
				.collect(Collectors.toList());
	}

	@Override
	public OrderDTO updateOrderStatus(UpdateStatusOrder updateStatus) {

		OrderDAO order = orderRepository.findById(updateStatus.getIdOrder()).orElse(null);

		StatusDAO status = statusRepository.findByLabel(updateStatus.getStatus());

		order.setStatus(status);
		OrderDAO orderUpdate = orderRepository.save(order);
		notificationService.sendOrderStatusNotification(updateStatus.getIdOrder(), order.getUser().getUser().getMail(),
				MESSAGE_STATUS);
		return orderMapper.OrderDAOtoOrderDTO(orderUpdate);
	}

	public void setOrderDomicile(OrderDAO order, RequestAddOrderWithAddress orderNew, AddressDAO addTest,
			AddressDAO add, Set<InhabitDAO> setInhabit, UsersInformationDAO user, String login) {

		BigDecimal bg25 = new BigDecimal("25");
		BigDecimal bg10 = new BigDecimal("10");
		// create int object
		int res;
		res = order.getTotal().compareTo(bg25); // compare bg1 with bg2
		if (res != 1) {
			BigDecimal sum = order.getTotal().add(bg10);
			order.setTotal(sum);
		}
		if (!addTest.equals(add)) {
			add.setId(null);
			AddressDAO newAddress = addressRepository.save(add);

			order.setAddress(newAddress);
			if (orderNew.getIsMain().equals("true")) {// set boolean!!
				Message m = notificationService.sendCustomPrivateNotification(login, MESSAGE_ADDRESS);
				notificationService.saveNotification(login, m.getDate(), m.getMessage());
				inhabitRepository.save(InhabitDAO.getInhabit(newAddress, user, user.getUser().getId()));
				for (InhabitDAO i : setInhabit) {
					i.setIsMain(0);
				}
				inhabitRepository.saveAll(setInhabit);
			}
		} else {
			order.setAddress(add);
		}

	}

}
