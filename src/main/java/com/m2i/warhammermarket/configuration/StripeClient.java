package com.m2i.warhammermarket.configuration;

import com.m2i.warhammermarket.model.CustomerData;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Token;

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
/*stripe-element/checkout => token FRONT*/
	public String createCustomer(String token, String email) throws StripeException {
		String idTest = "-1";
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 10);
		params.put("email", "lll@gmail.com");
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

	/*stripe-element/checkout => token FRONT*/

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

	public String newCustomerAndPay(CustomerData customerData) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException, StripeException, Exception {
		String idTest = "Vous avez dépassé la limite de cartes enregistrées, effectuez le paiement sans enregistrer la carte ou supprimez un ancien mode de paiement";
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("email", customerData.getEmail());
		CustomerCollection customers = Customer.list(params);
		int size = customers.getData().size();
		if (size < 3) {

		String date = customerData.getExpiryDate();
		System.out.println(date);
		String[] dateParts = date.split(" / ");
		String month = dateParts[0];
		String year = "20" + dateParts[1];
		System.out.println(customerData);
		System.out.println(month + "/" + year);
		Map<String, Object> card = new HashMap<>();
		card.put("number", customerData.getCardNumber());
		card.put("exp_month", month);
		card.put("exp_year", year);
		card.put("cvc", customerData.getCvc());
		Map<String, Object> params2 = new HashMap<>();
		params2.put("card", card);

		Token tokenCard = Token.create(params2);
		System.out.println(tokenCard);
		String c = createCustomer(tokenCard.getId(), customerData.getEmail());
		System.out.println(c);
		Charge charge = chargeCustomerCard(c, (int) (customerData.getAmount() * 100));
		System.out.println(charge.getStatus());	
	    idTest = ""+	charge.getStatus();}
		return idTest ;
	}

	public String payChargeOneTimes(CustomerData customerData) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {
		String date = customerData.getExpiryDate();
		System.out.println(date);
		String[] dateParts = date.split(" / ");
		String month = dateParts[0];
		String year = "20" + dateParts[1];
		System.out.println(customerData);
		System.out.println(month + "/" + year);
		Map<String, Object> card = new HashMap<>();
		card.put("number", customerData.getCardNumber());
		card.put("exp_month", month);
		card.put("exp_year", year);
		card.put("cvc", customerData.getCvc());
		Map<String, Object> params2 = new HashMap<>();
		params2.put("card", card);

		Token tokenCard = Token.create(params2);
		System.out.println(tokenCard);
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (int) (customerData.getAmount() * 100));
		chargeParams.put("currency", "EUR");
		chargeParams.put("source", tokenCard.getId());
		Charge charge = Charge.create(chargeParams);

		System.out.println(charge);
		return charge.getStatus();
	}
}