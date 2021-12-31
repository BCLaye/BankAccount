package kata.sg.modele.comptes;

import java.math.BigDecimal;

import kata.sg.modele.client.Client;

public class CompteEpargne extends Compte {
	
	private TypeEpargne type;

	public CompteEpargne(String numero, BigDecimal solde, Client client, TypeEpargne type) {
		super(numero, solde, client);
		this.type = type;
	}

	public CompteEpargne(String numero, BigDecimal solde,TypeEpargne type) {
		super(numero, solde);
		this.type = type;
	}

	public TypeEpargne getType() {
		return type;
	}

	public void setType(TypeEpargne type) {
		this.type = type;
	}
}
