package kata.sg.modele.comptes;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import kata.sg.modele.client.Client;
import kata.sg.modele.operations.Operation;

public class Compte {

	private String numero;
	private BigDecimal solde;
	private Client client;
	
	//@OneToMany
	private Collection<Operation> operations;

	public Compte(String numero, BigDecimal solde) {
		super();
		this.numero = numero;
		this.solde = solde;
		this.operations = new HashSet<Operation>();
	}
	
	public Compte(String numero, BigDecimal solde, Client client) {
		super();
		this.numero = numero;
		this.solde = solde;
		this.client = client;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public BigDecimal getSolde() {
		return solde;
	}

	public void setSolde(BigDecimal solde) {
		this.solde = solde;
	}

	public Collection<Operation> getOperations() {
		return operations;
	}

	public void setOperations(Collection<Operation> operations) {		
		this.operations = operations;
	}
	
	public void addOperations(Operation operation) {
		if(this.operations == null) {
			this.operations = new HashSet<Operation>();
		}
		this.operations.add(operation);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numero, solde);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compte other = (Compte) obj;
		return Objects.equals(client, other.client) && Objects.equals(numero, other.numero)
				&& Objects.equals(operations, other.operations) && Objects.equals(solde, other.solde);
	}

	@Override
	public String toString() {
		return "Compte "+this.numero+" , solde actuel ="+solde;
	}
}
