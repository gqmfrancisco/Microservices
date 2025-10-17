package br.edu.atitus.currency_service.clients;

import java.util.Collections;

import org.springframework.stereotype.Component;

@Component
public class CurrencyBCFallback implements CurrencyBCClient{

	@Override
	public CurrencyBCResponse getCurrencyBC(String moeda, String date) {
		CurrencyBCResponse currency = new CurrencyBCResponse();
		currency.setValue(Collections.emptyList());
		return currency;
	}

	
	
}