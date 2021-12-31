package kata.sg.modele.operations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import kata.sg.modele.comptes.Compte;

public class Operation {
	
	private LocalDateTime date;
	private BigDecimal montant;
	private String description;
	private TypeOperations type;
	private String lieu;
	
	private Compte compteEmet;
	private Compte compteRecept;
		
	public Operation() {
		super();
	}

	public Operation(LocalDateTime date, BigDecimal montant, String description, TypeOperations type, String lieu) {
		super();
		this.date = date;
		this.montant = montant;
		this.description = description;
		this.type = type;
		this.lieu = lieu;
	}
	
	public Operation(LocalDateTime date, BigDecimal montant, String description, TypeOperations type, Compte compteEmet, Compte compteRecept) {
		super();
		this.date = date;
		this.montant = montant;
		this.description = description;
		this.type = type;
		this.compteEmet = compteEmet;
		this.compteRecept = compteRecept;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getMontant() {
		return montant;
	}

	public void setMontant(BigDecimal montant) {
		this.montant = montant;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TypeOperations getType() {
		return type;
	}

	public void setType(TypeOperations type) {
		this.type = type;
	}

	public Compte getCompteEmet() {
		return compteEmet;
	}

	public void setCompteEmet(Compte compteEmet) {
		this.compteEmet = compteEmet;
	}

	public Compte getCompteRecept() {
		return compteRecept;
	}

	public void setCompteRecept(Compte compteRecept) {
		this.compteRecept = compteRecept;
	}

	@Override
	public int hashCode() {
		return Objects.hash(compteEmet, compteRecept, date, description, lieu, montant, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operation other = (Operation) obj;
		return Objects.equals(compteEmet, other.compteEmet) && Objects.equals(compteRecept, other.compteRecept)
				&& Objects.equals(date, other.date) && Objects.equals(description, other.description)
				&& Objects.equals(lieu, other.lieu) && Objects.equals(montant, other.montant) && type == other.type;
	}
	
	@Override
	public String toString() {
		String out = null;
		switch (this.type){
		case DEPOT_ESPECE : out = "Dépot espéce d'un montant = "+this.montant+" réalisé le "+this.date.toString()+" à "+this.lieu+" description :"+this.description; 
		     break;
		     
		case RETRAIT_ESPCE : out = "retrait espece d'un montant = "+this.montant+" réalisé le "+this.date.toString()+" à "+this.lieu+" description :"+this.description; 
	     break;
	     
		case ACHAT : out = "Achat d'un montant = "+this.montant+" réalisé le "+this.date.toString()+" à "+this.lieu+" description :"+this.description; 
		 break;
		 
		case VIREMENT_EMIS : out = "Virement pour "+compteRecept.getClient().getNom()+" "+compteRecept.getClient().getPrenom()+
				                    " d'un montant = "+this.montant+" réalisé le "+this.date.toString()+" description :"+this.description;  
		 break;
		
		case VIREMENT_RECU : out = "Virement reçu de "+compteEmet.getClient().getNom()+" "+compteEmet.getClient().getPrenom()+
                " d'un montant = "+this.montant+" réalisé le "+this.date.toString()+" description :"+this.description;  
         break;
		
		}
		return out;
	}
	
	

}
