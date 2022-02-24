package com.m2i.warhammermarket.configuration;

import com.m2i.warhammermarket.model.CreditCardModel;
import com.m2i.warhammermarket.model.CustomerData;
import com.m2i.warhammermarket.model.ResponseCreditCardsDetails;
import com.stripe.Stripe;
import com.stripe.exception.APIConnectionException;
import com.stripe.exception.APIException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;
import com.stripe.model.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StripeClient {

	@Autowired
	StripeClient() {
		Stripe.apiKey = "sk_test_51KK9NkAvTn1DnSSq7R6P3AaDNvGrvjxE0wyhQVpwIQppedQjbYnTfgt5zbBOJETxpSTGwVn1njMs4uRRfK3rBlFn00zq3e8R1E";
	}

	/* stripe-element/checkout => token FRONT */
	public String createCustomer(String token, String email) throws StripeException {
		String idTest = "-1";
		Map<String, Object> customerParams = new HashMap<String, Object>();
		customerParams.put("email", email);
		customerParams.put("source", token);
		Customer c = Customer.create(customerParams);
		idTest = c.getId();
		return idTest;
	}

	private Customer getCustomer(String id) throws Exception {
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

	/* STRIPE-CUSTOM-FORM */
	public String newCustomerAndPay(CustomerData customerData) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException, StripeException, Exception {
		String idTest = "Vous avez dépassé la limite de cartes enregistrées, effectuez le paiement sans enregistrer la carte ou supprimez un ancien mode de paiement";
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("email", customerData.getEmail());
		CustomerCollection customers = Customer.list(params);
		int size = customers.getData().size();
		if (size < 3) {

			Token tokenCard = Token.create(createParameter(customerData));
			System.out.println(tokenCard);
			String c = createCustomer(tokenCard.getId(), customerData.getEmail());
			Charge charge = chargeCustomerCard(c, (int) (customerData.getAmount() * 100));
			idTest = "" + charge.getStatus();
		}
		return idTest;
	}

	public String payChargeOneTimes(CustomerData customerData) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException {

		Token tokenCard = Token.create(createParameter(customerData));
		Map<String, Object> chargeParams = new HashMap<String, Object>();
		chargeParams.put("amount", (int) (customerData.getAmount() * 100));
		chargeParams.put("currency", "EUR");
		chargeParams.put("source", tokenCard.getId());
		Charge charge = Charge.create(chargeParams);
		return charge.getStatus();
	}

	public String newCustomer(CustomerData customerData) throws AuthenticationException, InvalidRequestException,
			APIConnectionException, CardException, APIException, StripeException, Exception {
		String idTest = "Vous avez dépassé la limite de cartes enregistrées, supprimez un ancien mode de paiement";
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("email", customerData.getEmail());
		CustomerCollection customers = Customer.list(params);
		int size = customers.getData().size();
		if (size < 3) {

			Token tokenCard = Token.create(createParameter(customerData));
			System.out.println(tokenCard);
			String c = createCustomer(tokenCard.getId(), customerData.getEmail());

			idTest = "" + c;
		}
		return idTest;
	}

	public ResponseCreditCardsDetails allCustomerCards(String email) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException, StripeException, Exception {
		
		 List<CreditCardModel> listCards = new ArrayList<>();
		String idTest = "Vous avez dépassé la limite de cartes enregistrées, supprimez un ancien mode de paiement";
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("email", email);
		CustomerCollection customers = Customer.list(params);
		
		for(Customer client : customers.getData()) {
			String idCustomer = client.getId();	
			String idCard = client.getDefaultSource().intern();
			Map<String, Object> retrieveParams = new HashMap<>();
			List<String> expandList = new ArrayList<>();
			expandList.add("sources");
			retrieveParams.put("expand", expandList);
			Customer customer = Customer.retrieve(idCustomer, retrieveParams, null);
			Card card = (Card) customer.getSources().retrieve(idCard);
			System.out.println(card);
			
			CreditCardModel cb = new CreditCardModel(card.getId(),card.getLast4(),card.getExpMonth().toString(),
					card.getExpYear().toString(),card.getBrand());			
			listCards.add(cb);
		}
		

		if (listCards.size() < 100) {
			idTest = listCards.size() + "";
		}

		ResponseCreditCardsDetails response = new ResponseCreditCardsDetails(listCards);

		return response;
	}

	/* SHARED => create-parameter-for-customer */
	public Map<String, Object> createParameter(CustomerData customerData) {
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
		return params2;
	}

}