package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.entity.DAO.AuthorityDAO;
import com.m2i.warhammermarket.entity.DTO.OrderDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;
import com.m2i.warhammermarket.model.RequestAddOrderWithAddress;
import com.m2i.warhammermarket.model.ResponseOrderDetails;
import com.m2i.warhammermarket.model.UpdateStatusOrder;
import com.m2i.warhammermarket.service.OrderService;
import com.m2i.warhammermarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasAuthority('user')")
@RequestMapping("/api")

public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;

	@PostMapping("/user/addorder")
	public ResponseEntity<Boolean> createOrder(@RequestBody RequestAddOrderWithAddress order) {
		List<ProductOrderWrapper> productsFilter = order.getProductsOrder().stream().filter(c -> c.getQuantite() > 0)
				.collect(Collectors.toList());
		System.out.println( orderService.checkStock(productsFilter));
		System.out.println( orderService.checkStock(productsFilter));
		System.out.println( orderService.checkStock(productsFilter));
		System.out.println( orderService.checkStock(productsFilter));
		System.out.println( orderService.checkStock(productsFilter));
		System.out.println( orderService.checkStock(productsFilter));

		if (productsFilter.size() > 0 && orderService.checkStock(productsFilter)) {
			try {
				orderService.createOrder(productsFilter,
						SecurityContextHolder.getContext().getAuthentication().getName(), order);
				return ResponseEntity.ok(true);
			} catch (Exception e) {
				System.out.println("Exception dans createOrder");
				return ResponseEntity.ok(false);
			}	}
		return ResponseEntity.ok(false);
	}

	@GetMapping("/orders")
	public ResponseEntity<List<OrderDTO>> getOrdersById(Principal currentUserId) {

	
/**/
			String mail = currentUserId.getName();

			Long id = userService.findOneByUserMail(mail).getId();

			List<OrderDTO> orderDTO = orderService.findAllByUserId(id);

			return ResponseEntity.ok(orderDTO);/**/
		
	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<List<ProductOrderWrapper>> getProductsByOrderId(@PathVariable Long id) {
		List<ProductOrderWrapper> productOrderWrappers = orderService.findAllByOrderId(id);
		return ResponseEntity.ok(productOrderWrappers);
	}

	@GetMapping("/orders/details/{id}")
	public ResponseEntity<ResponseOrderDetails> getOrderAndProductsByOrderId(@PathVariable Long id) {

		ResponseOrderDetails response = orderService.getOrderAndProductsByOrderId(id);

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all-orders")
	public ResponseEntity<List<OrderDTO>> getAllOrders() {


			List<OrderDTO> ordersDTO = orderService.findAll();

			return ResponseEntity.ok(ordersDTO);
		
	}
	
	@PutMapping("/orders/update-status")
	public ResponseEntity<OrderDTO> updateStatusOrder(@RequestBody UpdateStatusOrder updateStatus) {


		OrderDTO ordersDTO = orderService.updateOrderStatus(updateStatus);

			return ResponseEntity.ok(ordersDTO);
		
	}

	
	
	
	

}
