package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.configuration.StripeClient;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

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

		return this.stripeClient.chargeCustomerCard("cus_LC9auXniWTMP7q", amount); // this.stripeClient.chargeNewCard(token,
																					// amount);

	}
	@CrossOrigin("*")
	@PostMapping("/charge-customer-card")
	public Charge chargeCustomerCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "amount") Double amount) throws Exception {
		// Customer c = stripeClient.createCustomer(token, "luca@email.com");
		System.out.println(token);
		// System.out.println(c);
		System.out.println(amount);
		System.out.println(amount);

		System.out.println(amount);
		System.out.println(amount);
		System.out.println(amount);
		System.out.println(amount);

		// int i = amount.intValue();

		return //this.stripeClient.chargeCustomerCard("cus_LC9auXniWTMP7q", amount);
	 this.stripeClient.chargeNewCard(token,amount);

	}
	
	
	@CrossOrigin("*")
	@PostMapping("/new-customer-card")
	public Charge newCustomerCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "amount") Double amount) throws Exception {
		// Customer c = stripeClient.createCustomer(token, "luca@email.com");
		System.out.println(token);
		// System.out.println(c);
		System.out.println(amount);
		System.out.println(amount);

		System.out.println(amount);
		System.out.println(amount);
		System.out.println(amount);
		System.out.println(amount);

		// int i = amount.intValue();

		return this.stripeClient.chargeCustomerCard("cus_LC9auXniWTMP7q", amount); // this.stripeClient.chargeNewCard(token,
																					// amount);

	}
}
