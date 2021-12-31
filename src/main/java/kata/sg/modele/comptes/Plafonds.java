package kata.sg.modele.comptes;

import java.math.BigDecimal;

public class Plafonds {
	
	//montant minimum autorisé pour les retraits
	public static BigDecimal MT_MIN_RETRAIT = new BigDecimal(10);
	public static BigDecimal MT_MAX_RETRAIT = new BigDecimal(200);
	
	//montant minimum autorisé pour les dépots
	public static BigDecimal MT_MIN_DEPOT = new BigDecimal(10);
	public static BigDecimal MT_MAX_DEPOT = new BigDecimal(3000);
	
	//montant autorisé pour les virement
	public static BigDecimal MT_MIN_VIREMENT = new BigDecimal(10);
	public static BigDecimal MT_MAX_VIREMENT = new BigDecimal(1000);
	
	//plafonds de retrait
	public static BigDecimal CPT_COURANT_RETRAIT_PLAFOND_J = new BigDecimal(200);
	public static BigDecimal CPT_COURANT_RETRAIT_PLAFOND_M = new BigDecimal(1000);
	
	//plafond d'achat
	public static BigDecimal CPT_COURANT_PAIEMENT_PLAFOND_J = new BigDecimal(500);
	public static BigDecimal CPT_COURANT_PAIEMENT_PLAFOND_M = new BigDecimal(3500);

}
