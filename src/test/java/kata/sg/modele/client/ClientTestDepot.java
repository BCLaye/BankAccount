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

public class ClientTestDepot {
	
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
	
	//-------------------------  Tests fonctionalité DEPOT_ESPECE
		@Test(expected = ExceptionTransaction.class)
		public void test_depotEspece_exceptionTransaction() throws ExceptionMontantAutorise, ExceptionTransaction {		
			BigDecimal depot = new BigDecimal(500);
			ITransactions.depotEspece(null,depot, "Cergy Pref", "dépot initiale");
			
		}
		
		@Test(expected = ExceptionMontantAutorise.class)
		public void test_depotEspece_exceptionMontantAutorise() throws ExceptionMontantAutorise, ExceptionTransaction {		
			BigDecimal depot = Plafonds.MT_MIN_DEPOT.subtract(new BigDecimal(1));
			ITransactions.depotEspece(client.getCompteCourant(),depot, "Cergy Pref", "dépot initiale");
			
		}
		
		@Test
		public void test_depotEspece() {		
			BigDecimal depot = new BigDecimal(500);
			try {
				ITransactions.depotEspece(client.getCompteCourant(),depot, "Cergy Pref", "dépot initiale");
				assertTrue(depot.compareTo(client.getCompteCourant().getSolde())==0);
			} catch (Exception e) {
				fail("Aucune exception levée");
			}	
		}

}
