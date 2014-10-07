import java.util.ArrayList;


public class ClasseCompte {
	private ArrayList<LigneCompte> tableauLigneCompte;
	private String libelle;
	private int totalAnnee1;
	private int totalAnnee2;
	private int totalAnnee3;
	private int totalBudget1;
	private int totalBudget2;
	
	public ClasseCompte (ArrayList<LigneCompte> tableauLigneCompte,String libelle, int totalannee1, int totalannee2, int totalannee3, int totalbudget1, int totalbudget2){
		this.setTableauLigneCompte(tableauLigneCompte);
		this.setLibelle(libelle);
		this.setTotalAnnee1(totalannee1);
		this.setTotalAnnee2(totalannee2);
		this.setTotalAnnee3(totalannee3);
		this.setTotalBudget1(totalbudget1);
		this.setTotalBudget2(totalbudget2);
	}
	
	public ClasseCompte (String libelle){
		this.setTableauLigneCompte(new ArrayList<LigneCompte>());
		this.setLibelle(libelle);
		this.setTotalAnnee1(0);
		this.setTotalAnnee2(0);
		this.setTotalAnnee3(0);
		this.setTotalBudget1(0);
		this.setTotalBudget2(0);
	}

	public ArrayList<LigneCompte> getTableauLigneCompte() {
		return tableauLigneCompte;
	}

	public void setTableauLigneCompte(ArrayList<LigneCompte> tableauLigneCompte) {
		this.tableauLigneCompte = tableauLigneCompte;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getTotalAnnee1() {
		return totalAnnee1;
	}

	public void setTotalAnnee1(int totalAnnee1) {
		this.totalAnnee1 = totalAnnee1;
	}

	public int getTotalAnnee2() {
		return totalAnnee2;
	}

	public void setTotalAnnee2(int totalAnnee2) {
		this.totalAnnee2 = totalAnnee2;
	}

	public int getTotalAnnee3() {
		return totalAnnee3;
	}

	public void setTotalAnnee3(int totalAnnee3) {
		this.totalAnnee3 = totalAnnee3;
	}

	public int getTotalBudget1() {
		return totalBudget1;
	}

	public void setTotalBudget1(int totalBudget1) {
		this.totalBudget1 = totalBudget1;
	}

	public int getTotalBudget2() {
		return totalBudget2;
	}

	public void setTotalBudget2(int totalBudget2) {
		this.totalBudget2 = totalBudget2;
	}
	
	public void ajoutLigneCompte(LigneCompte lc) {
		
		
		this.totalAnnee1+=lc.getAnnee1();
		this.totalAnnee2+=lc.getAnnee2();
		this.totalAnnee3+=lc.getAnnee3();
		this.totalBudget1+=lc.getBudget1();
		this.totalBudget2+=lc.getBudget2();
		if (this.libelle!="FONCTIONNEMENT"){
			this.tableauLigneCompte.add(lc);
			return;
		}
			
		LigneCompte lcEquals = getLigneCompteEquals(lc.getCodecompte());
		if(lcEquals!=null){
			lcEquals.setAnnee1(lc.getAnnee1()+lcEquals.getAnnee1());
			lcEquals.setAnnee2(lc.getAnnee2()+lcEquals.getAnnee2());
			lcEquals.setAnnee3(lc.getAnnee3()+lcEquals.getAnnee3());
			lcEquals.setBudget1(lc.getBudget1()+lcEquals.getBudget1());
			lcEquals.setBudget2(lc.getBudget2()+lcEquals.getBudget2());
		}
		else
			
			this.tableauLigneCompte.add(lc);

	}
	
	private LigneCompte getLigneCompteEquals (int codeCompte){
		
		for(LigneCompte lc : tableauLigneCompte){
			if(lc.getCodecompte()==codeCompte)
				return lc;
			
		}
		return null;
	}

}
