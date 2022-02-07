package com.m2i.warhammermarket.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.m2i.warhammermarket.model.ReCaptchResponseType;

@Service
public class ReCaptchaValidationService {

	private static final String GOOGLE_RECAPTCHA_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";

	@Value("${RECAPTCHA_SECRET}")
	private String RECAPTCHA_SECRET;

	public boolean validateCaptcha(String captchaResponse) {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
		requestMap.add("secret", RECAPTCHA_SECRET);
		requestMap.add("response", captchaResponse);

		ReCaptchResponseType apiResponse = restTemplate.postForObject(GOOGLE_RECAPTCHA_ENDPOINT, requestMap,
				ReCaptchResponseType.class);

		System.out.println("apiResponse:" + apiResponse);
		if (apiResponse == null) {
			return false;
		}
		return Boolean.TRUE.equals(apiResponse.isSuccess());
	}
}