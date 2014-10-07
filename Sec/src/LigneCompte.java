
public class LigneCompte {
	private String libelleCompte, libelleCentreGension;
	public String getLibelleCentreGension() {
		return libelleCentreGension;
	}

	public void setLibelleCentreGension(String libelleCentreGension) {
		this.libelleCentreGension = libelleCentreGension;
	}

	private int codecompte, annee1 ,annee2, annee3, budget1, budget2;
	
	public LigneCompte(int codecompte ,String Libellecompte,String libelleCentreGension, int annee1, int annee2, int annee3, int budget1,int budget2){
		
		this.setCodecompte(codecompte);
		this.setLibelleCompte(Libellecompte);
		this.setBudget1(budget1);
		this.setBudget2(budget2);
		this.setLibelleCentreGension(libelleCentreGension);
	
	}
	
	public LigneCompte(){						
		
	}

	public int getCodecompte() {
		return codecompte;
	}

	public void setCodecompte(int codecompte) {
		this.codecompte = codecompte;
	}

	public int getAnnee1() {
		return annee1;
	}

	public void setAnnee1(int annee1) {
		this.annee1 = annee1;
	}

	public int getAnnee2() {
		return annee2;
	}

	public void setAnnee2(int annee2) {
		this.annee2 = annee2;
	}

	public int getAnnee3() {
		return annee3;
	}

	public void setAnnee3(int annee3) {
		this.annee3 = annee3;
	}

	public int getBudget1() {
		return budget1;
	}

	public void setBudget1(int budget1) {
		this.budget1 = budget1;
	}

	public int getBudget2() {
		return budget2;
	}

	public void setBudget2(int budget2) {
		this.budget2 = budget2;
	}

	public String getLibelleCompte() {
		return libelleCompte;
	}

	public void setLibelleCompte(String libelleCompte) {
		this.libelleCompte = libelleCompte;
	}
	 
	

}

