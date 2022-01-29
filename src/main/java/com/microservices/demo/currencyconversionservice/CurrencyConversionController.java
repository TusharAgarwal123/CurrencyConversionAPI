package com.microservices.demo.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeProxy proxy;

	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyconversion(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		// now we are calling the CurrnecyExchange service.

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("from", from);
		map.put("to", to);

		// this map will contain the value of from and to.

		ResponseEntity<CurrencyConversion> re = new RestTemplate().getForEntity(
				"http://localhost:8000//currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, map);

		// we are passing CurrencyConversion.class as parameter to getForEntity() method
		// bcoz
		// this class has all attributes of CurrencyExchange Service. we can also pass
		// CurrencyExchange.

		CurrencyConversion currencyConversion = re.getBody(); // it will fetch object of currency conversion from the
																// re.

		// calculate value for totalCalculatedAmount.

		BigDecimal val = quantity.multiply(currencyConversion.getConversionMultiple());

		return new CurrencyConversion(currencyConversion.getId(), from, to, quantity,
				currencyConversion.getConversionMultiple(), val,
				currencyConversion.getEnvironment() + " " + "Rest Template");

		// return new CurrencyConversion(1000L, from, to, quantity, BigDecimal.ONE,
		// BigDecimal.ONE, "");
	}

	// using feign framwork to call exchange api

	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversion calculateCurrencyconversionFeign(@PathVariable String from, @PathVariable String to,
			@PathVariable BigDecimal quantity) {

		CurrencyConversion currencyConversion = proxy.retrieveExchnagedValue(from, to);

		// calculate value for totalCalculatedAmount.

		BigDecimal val = quantity.multiply(currencyConversion.getConversionMultiple());

		return new CurrencyConversion(currencyConversion.getId(), from, to, quantity,
				currencyConversion.getConversionMultiple(), val, currencyConversion.getEnvironment() + " " + "feign");

	}

}
