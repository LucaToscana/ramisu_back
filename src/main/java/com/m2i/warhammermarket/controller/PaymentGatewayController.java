package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.configuration.StripeClient;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/payment")
public class PaymentGatewayController {

	private StripeClient stripeClient;

	@Autowired
	PaymentGatewayController(StripeClient stripeClient) {
		this.stripeClient = stripeClient;
	}

	@CrossOrigin("*")
	@PostMapping("/charge")
	public Charge chargeCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "amount") Double amount) throws Exception {
		// Customer c = stripeClient.createCustomer(token, "luca@email.com");
		System.out.println(token);
		// System.out.println(c);

		// int i = amount.intValue();
		// stripeClient.createCustomer(token, "lucatscn@gmail.com");
		return this.stripeClient.chargeNewCard(token, amount);

	}

	@CrossOrigin("*")
	@PostMapping("/charge-customer-card")
	public Charge chargeCustomerCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "amount") Double amount,@RequestHeader(value = "customer") String customer) throws Exception {

		return  this.stripeClient.chargeCustomerCard(customer, amount);
		
	}

	@CrossOrigin("*")
	@PostMapping("/new-customer")
	public String newCustomerCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "mail") String mail) throws Exception {

			
		return  stripeClient.createCustomer(token, mail);
	}
}
