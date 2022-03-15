package com.m2i.warhammermarket.controller;
import com.m2i.warhammermarket.model.CreditCardModel;
import com.m2i.warhammermarket.model.CustomerData;
import com.m2i.warhammermarket.model.ResponseCreditCardsDetails;
import com.m2i.warhammermarket.service.StripeClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasAuthority('user')")
@RequestMapping("/api/user")
public class PaymentGatewayController {

	private StripeClient stripeClient;

	@Autowired
	PaymentGatewayController(StripeClient stripeClient) {
		this.stripeClient = stripeClient;
	}
	
	
	
	/*custom-stripe*/
	
	@PostMapping("/payment/new-customer-and-pay")
	public String  saveCustomer(
			@RequestBody CustomerData customerData) throws Exception {
		
		return stripeClient.newCustomerAndPay(customerData);

	}
	
	
	
	
	@PostMapping("/payment/one-times-pay")
	public String  payCharge(
			@RequestBody CustomerData customerData) throws Exception {
		
		return stripeClient.payChargeOneTimes(customerData);

	}
	

	@PostMapping("/payment/new-customer")
	public String  newCustomer(
			@RequestBody CustomerData customerData) throws Exception {
		
		return stripeClient.newCustomer(customerData);

	}
	

	@GetMapping("/payment/customer-cards")
	public ResponseEntity<ResponseCreditCardsDetails>  allCustomerCards() throws Exception {
   	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails!=null)
        {		return  ResponseEntity.ok().body(stripeClient.allCustomerCards(userDetails.getUsername())); }
	
		return null;


	}
	
	
	

	@PostMapping("/payment/delete-customer-card")
	public void  deleteCustomerCard(@RequestBody CreditCardModel card) throws Exception {
   	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails!=null)
        {
       stripeClient.deleteCustomer(card.getCardStripe(), userDetails.getUsername());
        }

	}
	
	
	
	@PostMapping("/payment/registred-card-pay")
	public String  payWithRegistredCard(@RequestBody CreditCardModel card) throws Exception {
   	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userDetails!=null)
        {
			
			return stripeClient.payWithRegistredCard(card,userDetails.getUsername());
        }
		return null;
	}
	
	
	
	
	
	
	
	
	

}
