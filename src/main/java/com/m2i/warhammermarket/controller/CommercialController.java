package com.m2i.warhammermarket.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.m2i.warhammermarket.entity.DTO.OrderDTO;
import com.m2i.warhammermarket.entity.DTO.UserDTO;
import com.m2i.warhammermarket.entity.wrapper.ProductOrderWrapper;
import com.m2i.warhammermarket.entity.wrapper.ProfileWrapper;
import com.m2i.warhammermarket.model.RequestAddOrderWithAddress;
import com.m2i.warhammermarket.repository.UserRepository;
import com.m2i.warhammermarket.service.OrderService;
import com.m2i.warhammermarket.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasAuthority('commercial')")
@RequestMapping("/api/commercial/")
public class CommercialController {

	
	@Autowired
	private OrderService orderService;
	

	@GetMapping("/all-orders")
	public ResponseEntity<List<OrderDTO>> getAllOrders() {


			List<OrderDTO> orderDTO = orderService.findAll();

			return ResponseEntity.ok(orderDTO);/**/
		
	}
}