package br.edu.atitus.product_service.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.atitus.product_service.clients.CurrencyClient;
import br.edu.atitus.product_service.clients.CurrencyResponse;
import br.edu.atitus.product_service.entities.ProductEntity;
import br.edu.atitus.product_service.repositories.ProductRepository;

@RestController
@RequestMapping("products")
public class OpenProductController {

	private final ProductRepository repository;
	
	private final CurrencyClient currencyClient;
	
	private final CacheManager cacheManager;

	public OpenProductController(ProductRepository repository, CurrencyClient currencyClient, CacheManager cacheManager) {
		super();
		this.repository = repository;
		this.currencyClient = currencyClient;
		this.cacheManager = cacheManager;
	}
	
	@Value("${server.port}")
	private int serverPort;
	
	@GetMapping("/{idProduct}/{targetCurrency}")
	public ResponseEntity<ProductEntity> getProduct(
			@PathVariable Long idProduct,
			@PathVariable String targetCurrency
			) throws Exception {
		
		targetCurrency.toUpperCase();
		
		String dataSource = "None";
		String keyCache = idProduct + targetCurrency;
		String nameCache = "ProductCache";
		
		ProductEntity product = cacheManager.getCache(nameCache).get(keyCache, ProductEntity.class);
		
		if(product != null) {
			dataSource = "Cache";
		} else {
			product = new ProductEntity();
			product = repository.findById(idProduct)
						.orElseThrow(() -> new Exception("Product not found"));
		
			if (targetCurrency.equalsIgnoreCase(product.getCurrency())) {
					product.setConvertedPrice(product.getPrice());
					dataSource = "None (target value equals registered value)";
			} else {
				CurrencyResponse currency = currencyClient.getCurrency(
						product.getPrice(),
						product.getCurrency(),
						targetCurrency);
				
				product.setConvertedPrice(currency.getConvertedValue());
				dataSource = "Currency Service (" + currency.getEnviroment() + ")";
				
			}
		}
		
		cacheManager.getCache(nameCache).put(keyCache, product);
		
		product.setEnviroment("Product Service running on port: " + serverPort + " | Source: " + dataSource);
		
		return ResponseEntity.ok(product);
	}
	
	
}