package kata.sg.modele.client;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Objects;

import kata.sg.modele.comptes.Compte;
import kata.sg.modele.comptes.CompteCourant;
import kata.sg.modele.comptes.CompteEpargne;
import kata.sg.modele.comptes.TypeEpargne;
import kata.sg.services.ITransactions;


public class Client implements ITransactions{
	//Information à générer
	private String matricule;
	private String codeAcces;
	
	//Informations personnel
	private String nom;
	private String prenom;
	private Genre genre;
	private String paysNaissance;
	private String nationnalite;
	private LocalDate dateNaissance;
	
	private String mail;
	private String adresse;
	
	//@OneToMany
	private Collection<Compte> comptes;

	public Client() {
		super();
	}

	public Client(String nom, String prenom, Genre genre, String paysNaissance, String nationnalite,
			LocalDate dateNaissance, String mail, String adresse) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.genre = genre;
		this.paysNaissance = paysNaissance;
		this.nationnalite = nationnalite;
		this.dateNaissance = dateNaissance;
		this.mail = mail;
		this.adresse = adresse;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getCodeAcces() {
		return codeAcces;
	}

	public void setCodeAcces(String codeAcces) {
		this.codeAcces = codeAcces;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public String getPaysNaissance() {
		return paysNaissance;
	}

	public void setPaysNaissance(String paysNaissance) {
		this.paysNaissance = paysNaissance;
	}

	public String getNationnalite() {
		return nationnalite;
	}

	public void setNationnalite(String nationnalite) {
		this.nationnalite = nationnalite;
	}

	public LocalDate getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(LocalDate dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public Collection<Compte> getComptes() {
		return comptes;
	}

	public void setComptes(Collection<Compte> comptes) {
		this.comptes = comptes;
	}
	
	public CompteCourant getCompteCourant() {
		for (Compte compte : comptes) {
			if(compte instanceof CompteCourant)
				return (CompteCourant) compte;			
		}
		return null;
	}
	
	public CompteEpargne getCompteEpargne(TypeEpargne type) {
		for (Compte compte : comptes) {
			if((compte instanceof CompteEpargne) && (((CompteEpargne) compte).getType()== type))
				return (CompteEpargne) compte;			
		}
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(adresse, codeAcces, comptes, dateNaissance, genre, mail, matricule, nationnalite, nom,
				paysNaissance, prenom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return Objects.equals(adresse, other.adresse) && Objects.equals(codeAcces, other.codeAcces)
				&& Objects.equals(comptes, other.comptes) && Objects.equals(dateNaissance, other.dateNaissance)
				&& genre == other.genre && Objects.equals(mail, other.mail)
				&& Objects.equals(matricule, other.matricule) && Objects.equals(nationnalite, other.nationnalite)
				&& Objects.equals(nom, other.nom) && Objects.equals(paysNaissance, other.paysNaissance)
				&& Objects.equals(prenom, other.prenom);
	}
	
	public String toString(){		
		return (this.genre == Genre.HOMME ? "Monsieur":"Madame")+" "+this.nom+" "+this.prenom+
				" de matricule "+this.matricule;
	}
	
	
	
}
