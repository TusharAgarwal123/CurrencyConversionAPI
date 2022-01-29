package com.microservices.demo.currencyconversionservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "currency-exchange", url = "localhost:8000")
//here name is the application name of exchange service.
@FeignClient(name = "currency-exchange") // as we have connected to naming server so we need not
//to provide the url.now it will talk to eureka and pick up the instances of exchange service
//and do load balanceing between them.
public interface CurrencyExchangeProxy {

	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversion retrieveExchnagedValue(@PathVariable String from, @PathVariable String to);

	// here return type is currency conversion bcoz it contains all the attributes
	// of currnecy exchnage, we
	// can also return exchnage but for this we have to copy exchange pojo from the
	// echange service
	// and paste it in our conversion service.

}
