package kata.sg.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import kata.sg.modele.comptes.Compte;
import kata.sg.modele.comptes.CompteCourant;
import kata.sg.modele.comptes.Plafonds;
import kata.sg.modele.operations.Operation;
import kata.sg.modele.operations.TypeOperations;


public interface ITransactions {
	
	/*
	 * Afficher l'historique des opérations du compte
	 * @param{compte}: compte conserné
	 */
	public static List<String> historiqueCompte(Compte compte) throws ExceptionTransaction{		
		
		if(compte == null)
			throw new ExceptionTransaction("Le compte n'existe pas");
		
		if(compte.getOperations()==null || compte.getOperations().size()==0)
			throw new ExceptionTransaction("Pas d'historique pour ce compte");
		
		 return compte.getOperations().stream().sorted(Comparator.comparing(Operation::getDate).reversed()).map(Operation::toString).collect(Collectors.toList());
	}
	
	/*
	 * Fonction par défaut pour emission de virement
	 * @param{montant} : montant du virement
	 * @param{description} : description faite par le client
	 * @param{compteEmet}: compte du client
	 * @param{compteRecept} : compte destinataire
	 */
	public static void emettreVirement(BigDecimal montant, String description,Compte compteEmet, Compte compteRecept) throws ExceptionMontantAutorise, ExceptionTransaction {
		if(compteEmet == null || compteRecept == null)
			throw new ExceptionTransaction("Le compte emetteur et ou le compte du bénéficiare n'existe pas");
		
		//Vérifier si le virement se fait sur un compte courant pour bénéficiaire
		if((!compteEmet.getClient().equals(compteRecept.getClient())) && (!(compteRecept instanceof CompteCourant)))
				throw new ExceptionTransaction("Les virements vers les bénéficiaires ne sont autorisés que sur des compte courants");
		
		//Verification des montants autorisés
		if(montant.compareTo(Plafonds.MT_MIN_VIREMENT) < 0) 
			throw new ExceptionMontantAutorise(" Montant infériur au minimum autorisé");
		if(montant.compareTo(Plafonds.MT_MAX_VIREMENT) > 0)
			throw new ExceptionMontantAutorise(" Montant supérieur au maximum autorisé");
				
		//Vérifiaction des dépassements de plafond pour les comptes courants
		if(compteEmet instanceof CompteCourant) {
			CompteCourant compteEmetC = (CompteCourant) compteEmet;
			if(compteEmetC.getPlafondJour().add(montant).compareTo(Plafonds.CPT_COURANT_PAIEMENT_PLAFOND_J)>0)
				throw new ExceptionMontantAutorise("Dépassement plafond jour");
			

			if(compteEmetC.getPlafondMois().add(montant).compareTo(Plafonds.CPT_COURANT_PAIEMENT_PLAFOND_M)>0)
				throw new ExceptionMontantAutorise("Dépassement plafond mois");			
		}		
			
		if(montant.compareTo(compteEmet.getSolde())<= 0) {
			//mie à jour des solde des deux comptes
			compteRecept.setSolde(compteRecept.getSolde().add(montant));
			compteEmet.setSolde(compteEmet.getSolde().subtract(montant));
			
			//Historisation des opérations
			compteRecept.addOperations(new Operation(LocalDateTime.now(), montant, description, TypeOperations.VIREMENT_RECU,compteEmet, compteRecept));
			compteEmet.addOperations(new Operation(LocalDateTime.now(), montant, description, TypeOperations.VIREMENT_EMIS,compteEmet, compteRecept));
			
			//mise à jour des plafonds
			if(compteEmet instanceof CompteCourant) {
				CompteCourant compteEmetC = (CompteCourant) compteEmet;
				
				compteEmetC.setPlafondJour(compteEmetC.getPlafondJour().add(montant));
				compteEmetC.setPlafondMois(compteEmetC.getPlafondMois().add(montant));				
				compteEmet = compteEmetC;
				
			}
		
		}
		else
			throw new ExceptionMontantAutorise("Solde insuffisant");
	}
	
	
	
	/*
	 * depotEspece : dépôt des espéces faite depuis les distributeurs en agence
	 * @param{compte} : compte à créditer
	 * @param{montant}: la somme déposée
	 * @param{agance}: l'agence du dépôt
	 * @param{description} : description faite par les client
	 */
	public static void depotEspece(CompteCourant compte,BigDecimal montant, String agence, String description) throws ExceptionMontantAutorise, ExceptionTransaction {
		
		if(compte == null)
			throw new ExceptionTransaction("Le compte n'existe pas");
		
		//@param{érifiaction des montant autorisés
		if(montant.compareTo(Plafonds.MT_MIN_DEPOT) < 0) 
			throw new ExceptionMontantAutorise(" Montant infériur au minimum autorisé");
		if(montant.compareTo(Plafonds.MT_MAX_DEPOT) > 0)
			throw new ExceptionMontantAutorise(" Montant supérieur au maximum autorisé");
		
		compte.setSolde(compte.getSolde().add(montant));		
		compte.addOperations(new Operation(LocalDateTime.now(), montant, description, TypeOperations.DEPOT_ESPECE,agence));
	}
	
	/*
	 * retraitEspece : retrait  espéces faite depuis les distributeurs en agence
	 * @param{compte} : compte à débiter
	 * @param{montant}: la somme retirée
	 * @param{agance}: l'agence du dépôt
	 * @param{description} : description faite par les client
	 */
	public static void retraitEspece(CompteCourant compte,BigDecimal montant, String agence, String description) throws ExceptionMontantAutorise, ExceptionTransaction{
		
		if(compte == null)
			throw new ExceptionTransaction("Le compte n'existe pas");
		
		//@param{érifiaction des montant autorisés
		if(montant.compareTo(Plafonds.MT_MIN_RETRAIT) < 0) 
			throw new ExceptionMontantAutorise(" Montant infériur au minimum autorisé");
		if(montant.compareTo(Plafonds.MT_MAX_RETRAIT) > 0)
			throw new ExceptionMontantAutorise(" Montant supérieur au maximum autorisé");
		
		//Vérifiaction des dépassements de plafond pour les comptes courants		
		if(compte.getPlafondJour().add(montant).compareTo(Plafonds.CPT_COURANT_RETRAIT_PLAFOND_J)<0)
			throw new ExceptionMontantAutorise("Dépassement plafond jour");
		
		if(compte.getPlafondJour().add(montant).compareTo(Plafonds.CPT_COURANT_RETRAIT_PLAFOND_M)<0)
			throw new ExceptionMontantAutorise("Dépassement plafond mois");
		
		//mie à jour du solde
		compte.setSolde(compte.getSolde().subtract(montant));
		
		//Historisation des opérations
		compte.addOperations(new Operation(LocalDateTime.now(), montant, description, TypeOperations.RETRAIT_ESPCE,agence));
		
		//mise à jour des plafonds
		compte.setPlafondJour(compte.getPlafondJour().add(montant));
		compte.setPlafondMois(compte.getPlafondMois().add(montant));
			
	}

}
