package br.edu.atitus.currency_service.clients;

import java.util.List;

public class CurrencyBCResponse {

	private List<responseBC> value;
	
	public List<responseBC> getValue() {
		return value;
	}

	public void setValue(List<responseBC> value) {
		this.value = value;
	}

	public static class responseBC {
		
		private double cotacaoVenda;

		public double getCotacaoVenda() {
			return cotacaoVenda;
		}

		public void setCotacaoVenda(double cotacaoVenda) {
			this.cotacaoVenda = cotacaoVenda;
		}
		
		
	}
	
}