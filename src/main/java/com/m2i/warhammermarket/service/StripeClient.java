package com.m2i.warhammermarket.service;

import com.m2i.warhammermarket.model.CreditCardModel;
import com.m2i.warhammermarket.model.CustomerData;
import com.m2i.warhammermarket.model.Message;
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
import com.stripe.model.DeletedCustomer;
import com.stripe.model.Token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component

public class StripeClient {

	static String newCard = "Un nouveau moyen de paiement a été enregistré";
	static String limitCardPay = "Vous avez atteint la limite de cartes enregistrées, effectuez le paiement sans enregistrer la carte ou supprimez un ancien mode de paiement";
	static String limitCard = "Vous avez dépassé la limite de cartes enregistrées, supprimez un ancien mode de paiement";
	static String deleteCard = "Un  moyen de paiement a été supprimé";
	static String errorCard = "Un problème s'est produit lors du paiement";

	@Autowired
	private NotificationService notificationService;

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
		if (idTest.equals("-1") == false && !idTest.equals(null)) {

			Message m = notificationService.sendCustomPrivateNotification(email, newCard);
			notificationService.saveNotification(email, m.getDate(), m.getMessage());
		}
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
		String idTest = limitCardPay;
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
		String idTest = limitCard;
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

	public ResponseCreditCardsDetails allCustomerCards(String email) {
		ResponseCreditCardsDetails response = new ResponseCreditCardsDetails(null);

		List<CreditCardModel> listCards = new ArrayList<>();
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("email", email);
		CustomerCollection customers;
		try {
			customers = Customer.list(params);
			for (Customer client : customers.getData()) {
				String idCustomer = client.getId();
				String idCard = client.getDefaultSource().intern();
				Map<String, Object> retrieveParams = new HashMap<>();
				List<String> expandList = new ArrayList<>();
				expandList.add("sources");
				retrieveParams.put("expand", expandList);
				Customer customer = Customer.retrieve(idCustomer, retrieveParams, null);
				Card card = (Card) customer.getSources().retrieve(idCard);

				CreditCardModel cb = new CreditCardModel(card.getId(), card.getLast4(), card.getExpMonth().toString(),
						card.getExpYear().toString(), card.getBrand());

				System.out.println(client);
				listCards.add(cb);
			}

			ResponseCreditCardsDetails responseList = new ResponseCreditCardsDetails(listCards);
			response = responseList;
		} catch (AuthenticationException | InvalidRequestException | APIConnectionException | CardException
				| APIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public void deleteCustomer(String card, String customerMail) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {

		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("email", customerMail);
		CustomerCollection customers;

		customers = Customer.list(params);
		for (Customer c : customers.getData()) {
			if (c.getDefaultSource().intern().equals(card)) {

				Customer customer = Customer.retrieve(c.getId());
				DeletedCustomer cust = customer.delete();

			}
		}
	Message m =	notificationService.sendCustomPrivateNotification(customerMail, deleteCard);
		notificationService.saveNotification(customerMail, m.getDate(), m.getMessage());

	}

	public String payWithRegistredCard(CreditCardModel card, String customer) throws AuthenticationException,
			InvalidRequestException, APIConnectionException, CardException, APIException {
		String idTest = errorCard;
		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);
		params.put("email", customer);
		CustomerCollection customers;

		customers = Customer.list(params);
		for (Customer c : customers.getData()) {
			if (c.getDefaultSource().intern().equals(card.getCardStripe())) {

				try {
					Charge charge = chargeCustomerCard(c.getId(), card.getAmountOrder());
					System.out.println(charge);
					idTest = charge.getStatus();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		return idTest;
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