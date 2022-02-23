package com.m2i.warhammermarket.controller;

import com.m2i.warhammermarket.configuration.StripeClient;
import com.m2i.warhammermarket.model.CustomerData;
import com.m2i.warhammermarket.model.ProductSearchCriteria;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Token;

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
	
	
	
	/*custom-stripe*/
	
	@CrossOrigin("*")
	@PostMapping("/new-customer-and-pay")
	public String  saveCunstomer(
			@RequestBody CustomerData customerData) throws Exception {
		
		return stripeClient.newCustomerAndPay(customerData);

	}
	
	
	
	
	@CrossOrigin("*")
	@PostMapping("/one-times-pay")
	public String  payCharge(
			@RequestBody CustomerData customerData) throws Exception {
		
		return stripeClient.payChargeOneTimes(customerData);

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*stripe-element-checkout*/
/*
	@CrossOrigin("*")
	@PostMapping("/charge")
	public Charge chargeCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "amount") Double amount) throws Exception {
		// Customer c = stripeClient.createCustomer(token, "luca@email.com");
		/*System.out.println(token);
		Map<String, Object> params = new HashMap<>();
		params.put(
		  "description",
		  "My First Test Customer (created for API docs)"
		);*/

		//Customer customer = Customer.create(params);
		// System.out.println(c);
	/*	Map<String, Object> card = new HashMap<>();
		card.put("number", "4242424242424242");
		card.put("exp_month", 2);
		card.put("exp_year", 2023);
		card.put("cvc", "314");
		Map<String, Object> params2 = new HashMap<>();
		params2.put("card", card);

		Token tokenS = Token.create(params2);
		System.out.println(tokenS);
		// int i = amount.intValue();
	    String c=     stripeClient.createCustomer(tokenS.getId(), "lucatscddddn@gmail.com");
	    System.out.println(c);*/
	/*	return this.stripeClient.chargeNewCard(token, amount);

	}
/*
	@CrossOrigin("*")
	@PostMapping("/charge-customer-card")
	public Charge chargeCustomerCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "amount") Double amount,@RequestHeader(value = "customer") String customer) throws Exception {

		return  this.stripeClient.chargeCustomerCard(customer, amount);
		
	}*/
/*stripe-element-checkout*/
	/*@CrossOrigin("*")
	@PostMapping("/new-customer")
	public String newCustomerCard(@RequestHeader(value = "token") String token,
			@RequestHeader(value = "mail") String mail) throws Exception {

			
		return  stripeClient.createCustomer(token, mail);
	}*/
}
