package kata.sg.modele.comptes;

import java.math.BigDecimal;

import kata.sg.modele.client.Client;

public class CompteCourant extends Compte {
	
	BigDecimal plafondJour;
	BigDecimal plafondMois;
		
	public CompteCourant(String numero, BigDecimal solde) {
		super(numero, solde);
	}
	
	public CompteCourant(String numero, BigDecimal solde, Client client) {
		super(numero, solde, client);
		this.plafondJour = new BigDecimal(0);
		this.plafondMois = new BigDecimal(0);
	}

	public BigDecimal getPlafondJour() {
		return plafondJour;
	}

	public void setPlafondJour(BigDecimal plafondJour) {
		this.plafondJour = plafondJour;
	}

	public BigDecimal getPlafondMois() {
		return plafondMois;
	}

	public void setPlafondMois(BigDecimal plafondMois) {
		this.plafondMois = plafondMois;
	}

}
