package kata.sg.modele.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import kata.sg.modele.comptes.Compte;
import kata.sg.modele.comptes.CompteCourant;
import kata.sg.modele.comptes.CompteEpargne;
import kata.sg.modele.comptes.TypeEpargne;
import kata.sg.services.ExceptionMontantAutorise;
import kata.sg.services.ExceptionTransaction;
import kata.sg.services.ITransactions;

public class ClientHistorique {
	
	private Client client = new Client(); //compte couranyt et livret A
	private Client beneficiaire = new Client();	//compte couranyt et livret A
	
	
	@Rule
	public TestName nameTest = new TestName();
	
	@Before
	public void initialisation() throws ExceptionMontantAutorise, ExceptionTransaction {
		
		//Initialisation client
		client = new Client("JULIOT", "Benoit", Genre.HOMME, "France", "française",LocalDate.of(1985, 01, 25), "benoit.juliot@gmail.com", "6 rue des marjobert, 95000 Cergy");
		client.setMatricule("A17593214H02");
		client.setCodeAcces("254HBC21$");
		
		Set<Compte> comptesClient = new HashSet<Compte>();
		comptesClient.add(new CompteCourant("CC12547A01", new BigDecimal(0), client));
		comptesClient.add(new CompteEpargne("LIVA12547A01", new BigDecimal(0),client,TypeEpargne.LIVERT_A ));
		client.setComptes(comptesClient);
		ITransactions.depotEspece(client.getCompteCourant(),new BigDecimal(1500), "Cergy Pref", "dépot initiale");
		ITransactions.emettreVirement(new BigDecimal(150), "frais médicaux", client.getCompteCourant(), client.getCompteEpargne(TypeEpargne.LIVERT_A));
		
		
		
		//Initialisation beneficiaire
		beneficiaire = new Client("JULIOT", "Jean Noel", Genre.HOMME, "France", "française",LocalDate.of(2010, 01, 02), "jean-noel.juliot@gmail.com", "6 rue des marjobert, 95000 Cergy");
		beneficiaire.setMatricule("J00086054H02");
		beneficiaire.setCodeAcces("*125478Z02");
		
		Set<Compte> comptesBenef = new HashSet<Compte>();
		comptesBenef.add(new CompteCourant("CC25176R99", new BigDecimal(0), beneficiaire));
		beneficiaire.setComptes(comptesBenef);
		
	}
	
	
	
	//-------------------------  Tests fonctionalité AFFICHER_HISTORIQUE des opérations
	@Test(expected = ExceptionTransaction.class)
	public void test_afficherDetail_exceptionTransaction() throws ExceptionTransaction {
		ITransactions.historiqueCompte(beneficiaire.getCompteCourant()).forEach(System.out::println);
	}
	
	@Test
	public void test_afficherDetail() throws ExceptionTransaction {
		try {
			System.out.println("--- Historique du "+client.getCompteCourant().toString());
			List<String> histos = ITransactions.historiqueCompte(client.getCompteCourant());
			histos.forEach(System.out::println);
			assertTrue(histos.size() > 0 );
		} catch (ExceptionTransaction e) {
			fail("Aucune exception levée");
		}
		
	}
}
