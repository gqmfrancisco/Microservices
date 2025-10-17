package br.edu.atitus.product_service.clients;

import org.springframework.stereotype.Component;

@Component
public class CurrencyFallback implements CurrencyClient{

	@Override
	public CurrencyResponse getCurrency(double value, String source, String target) {
		
		CurrencyResponse currency = new CurrencyResponse();
		
		currency.setSource(source);
		currency.setTarget(target);
		currency.setConversionRate(0);
		currency.setConvertedValue(-1);
		currency.setEnviroment("ERROR: Currency Service not found");
		
		return currency;
	}

}