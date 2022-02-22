package com.m2i.warhammermarket.configuration;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StripeClient {

	@Autowired
	StripeClient() {
		Stripe.apiKey = "sk_test_51KK9NkAvTn1DnSSq7R6P3AaDNvGrvjxE0wyhQVpwIQppedQjbYnTfgt5zbBOJETxpSTGwVn1njMs4uRRfK3rBlFn00zq3e8R1E";
	}

	public String createCustomer(String token, String email) throws StripeException {
		String idTest = "-1";
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 10);
		params.put("email", email);
		CustomerCollection customers = Customer.list(params);
		int size = customers.getData().size();
		if (size < 10) {

			Map<String, Object> customerParams = new HashMap<String, Object>();
			customerParams.put("email", email);
			customerParams.put("source", token);
			Customer c = Customer.create(customerParams);
			idTest = c.getId();
		} else {
			idTest = "0";
		}

		return idTest;

	}

	private Customer getCustomer(String id) throws Exception {
		return Customer.retrieve(id);
	}

	private Customer getAllCustomerAndFind(String id) throws Exception {
		return Customer.retrieve(id);
	}

	public Charge chargeNewCard(String token, double amount) throws Exception {
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (int) (amount * 100));
		chargeParams.put("currency", "EUR");
		chargeParams.put("source", token);
		Charge charge = Charge.create(chargeParams);

		return charge;
	}

	public Charge chargeCustomerCard(String customerId, double amount) throws Exception {
		String sourceCard = getCustomer(customerId).getDefaultSource();
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (int) (amount * 100));
		chargeParams.put("currency", "EUR");
		chargeParams.put("customer", customerId);
		chargeParams.put("source", sourceCard);
		Charge charge = Charge.create(chargeParams);
		return charge;
	}
}