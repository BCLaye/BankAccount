package kata.sg.modele.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import kata.sg.modele.comptes.Compte;
import kata.sg.modele.comptes.CompteCourant;
import kata.sg.modele.comptes.CompteEpargne;
import kata.sg.modele.comptes.Plafonds;
import kata.sg.modele.comptes.TypeEpargne;
import kata.sg.services.ExceptionMontantAutorise;
import kata.sg.services.ExceptionTransaction;
import kata.sg.services.ITransactions;

public class TestClientVirement {
	
	private Client client = new Client(); //compte couranyt et livret A
	private Client beneficiaire = new Client();	//compte couranyt et livret A
	private Client beneficiaire2 = new Client();	//compte epargne COMPTE_A_TERME
	
	@Before
	public void initialisation() {
		
		//Initialisation client
		client = new Client("JULIOT", "Benoit", Genre.HOMME, "France", "française",LocalDate.of(1985, 01, 25), "benoit.juliot@gmail.com", "6 rue des marjobert, 95000 Cergy");
		client.setMatricule("A17593214H02");
		client.setCodeAcces("254HBC21$");
		
		Set<Compte> comptesClient = new HashSet<Compte>();
		comptesClient.add(new CompteCourant("CC12547A01", new BigDecimal(0), client));
		comptesClient.add(new CompteEpargne("LIVA12547A01", new BigDecimal(0),client,TypeEpargne.LIVERT_A ));
		client.setComptes(comptesClient);
		
		//Initialisation beneficiaire
		beneficiaire = new Client("JULIOT", "Jean Noel", Genre.HOMME, "France", "française",LocalDate.of(2010, 01, 02), "jean-noel.juliot@gmail.com", "6 rue des marjobert, 95000 Cergy");
		beneficiaire.setMatricule("J00086054H02");
		beneficiaire.setCodeAcces("*125478Z02");
		
		Set<Compte> comptesBenef = new HashSet<Compte>();
		comptesBenef.add(new CompteCourant("CC25176R99", new BigDecimal(0), beneficiaire));
		comptesBenef.add(new CompteEpargne("LIVA25176R99", new BigDecimal(0),beneficiaire,TypeEpargne.LIVERT_JEUNE ));
		beneficiaire.setComptes(comptesBenef);
		
		//Initialisation beneficiaire2
		beneficiaire2 = new Client("VERLEY", "Lodovic", Genre.HOMME, "France", "française",LocalDate.of(1960, 04, 22), "ludo.verley@gmail.com", "6 rue des marjobert, 95000 Cergy");
		beneficiaire2.setMatricule("V25701364K03");
		beneficiaire2.setCodeAcces("MLjd217!");
		
		Set<Compte> comptesBenef2 = new HashSet<Compte>();
		comptesBenef2.add(new CompteEpargne("CAT2354741W11", new BigDecimal(0),beneficiaire2,TypeEpargne.COMPTE_A_TERME ));
		beneficiaire2.setComptes(comptesBenef2);		
	}
	
	@Test(expected = ExceptionTransaction.class)
	public void emettreVirement_exceptionTransaction1() throws ExceptionMontantAutorise, ExceptionTransaction {		
		BigDecimal montantVir = new BigDecimal(300);
		ITransactions.emettreVirement(montantVir, "frais médicaux", client.getCompteCourant(), beneficiaire2.getCompteCourant());
	}
	
	@Test(expected = ExceptionTransaction.class)
	public void emettreVirement_exceptionTransaction2() throws ExceptionMontantAutorise, ExceptionTransaction {		
		BigDecimal montantVir = new BigDecimal(300);
		ITransactions.emettreVirement(montantVir, "frais médicaux", client.getCompteCourant(), beneficiaire2.getCompteEpargne(TypeEpargne.COMPTE_A_TERME));
	}
	
	//monatnt superieur au montant max autorisé pour les virement
	@Test(expected = ExceptionMontantAutorise.class)
	public void emettreVirement_exceptionMontantAutorise1() throws ExceptionMontantAutorise, ExceptionTransaction {		
		BigDecimal montantVir = Plafonds.MT_MAX_VIREMENT.add(new BigDecimal(1));
		ITransactions.emettreVirement(montantVir, "frais médicaux", client.getCompteCourant(), beneficiaire.getCompteCourant());
	}
	
	//solde du compte emetteur insuffisant
	@Test(expected = ExceptionMontantAutorise.class)
	public void emettreVirement_exceptionMontantAutorise2() throws ExceptionMontantAutorise, ExceptionTransaction {	
		BigDecimal montantVir = new BigDecimal(150);
		ITransactions.emettreVirement(montantVir, "frais médicaux", client.getCompteCourant(), beneficiaire.getCompteCourant());
	}
	
	//Dépassement plafond jour
	@Test(expected = ExceptionMontantAutorise.class)
	public void emettreVirement_exceptionMontantAutorise3() throws ExceptionMontantAutorise, ExceptionTransaction {		
		BigDecimal montantVir = new BigDecimal(150);
		client.getCompteCourant().setPlafondJour(Plafonds.CPT_COURANT_PAIEMENT_PLAFOND_J);
		ITransactions.emettreVirement(montantVir, "frais médicaux", client.getCompteCourant(), beneficiaire.getCompteCourant());
	}
	
	@Test
	public void emettreVirement() {
		
		client.getCompteCourant().setSolde(new BigDecimal(1500));
		BigDecimal montantVir = new BigDecimal(150),
				   soldeCourantClient = new BigDecimal(1500),
				   plafondJourClient  = client.getCompteCourant().getPlafondJour(),
				   plafondMoisClient  = client.getCompteCourant().getPlafondMois(),
				   soldeCourantBnef   = beneficiaire.getCompteCourant().getSolde();
		try {
			ITransactions.emettreVirement(montantVir, "frais médicaux", client.getCompteCourant(), beneficiaire.getCompteCourant());
			assertTrue(client.getCompteCourant().getSolde().compareTo(soldeCourantClient.subtract(montantVir)) == 0);
			assertTrue(client.getCompteCourant().getPlafondJour().compareTo(plafondJourClient.add(montantVir)) == 0);
			assertTrue(client.getCompteCourant().getPlafondMois().compareTo(plafondMoisClient.add(montantVir)) == 0);
			assertTrue(beneficiaire.getCompteCourant().getSolde().compareTo(soldeCourantBnef.add(montantVir)) == 0);
		} catch (Exception e) {
			fail("Aucune exception levée");
		}
	}
	

}
