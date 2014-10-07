import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.hssf.record.formula.functions.Cell;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


public class traitementFichierXSL {

	private String Libellecompte, libelleannee1,libelleannee2,libelleannee3,libellebudget1,libellebudget2;

	private static final int CODE_FONCTIONNEMENT = 1;
	private static final int CODE_LOCAUX = 2;
	private static final int CODE_SECOURS = 3;
	private static final int CODE_ACTIVITES = 4;
	private static final int CODE_SUBVENTION = 5;
	private String libellefichier;

	private POIFSFileSystem fichierlecture;
	private FileOutputStream fichierecriture;
	private	HSSFWorkbook book;
	private	HSSFSheet sheetlecture;
	private	HSSFSheet sheetecriture;
	private String nomsheetcriture;
	private String cheminfichierlecture;
	private String nomFichier;
	private ClasseCompte classeCompteFonctionnement = new ClasseCompte("FONCTIONNEMENT");
	private ClasseCompte classeCompteLocaux = new ClasseCompte("LOCAUX");
	private ClasseCompte classeCompteSecours = new ClasseCompte("SECOURS");
	private ClasseCompte classeCompteActivites = new ClasseCompte("ACTIVITES");
	private ClasseCompte classeCompteSubvention = new ClasseCompte("SUBVENTIONS");
	private HashMap<Integer,Integer> correspondanceCodeCompta_ClasseCompta;

	public traitementFichierXSL(String cheminfic) throws IOException{

		fichierlecture = new POIFSFileSystem (new FileInputStream(cheminfic));
		cheminfichierlecture=cheminfic;
		book = new HSSFWorkbook(fichierlecture);
		this.nomFichier=cheminfic.substring(cheminfic.lastIndexOf("\\")+1,cheminfic.lastIndexOf("."));
		nomsheetcriture="Budget - "+nomFichier;
		//Init du hashmap et des libelles
		initHashMap_Libelles();

	}


	// Lit le fichier xl
	public void lecture(){
		sheetlecture = book.getSheetAt(0);
		Iterator<HSSFRow> rowIt= sheetlecture.rowIterator();
		
		//Parcourt des libelles
		
		for ( int i =0; i<6; i++){
			HSSFRow row = rowIt.next();
			
			if(i==1)				
				libellefichier= row.getCell((short)6).getStringCellValue();
			else if (i==4){
				this.libelleannee1=row.getCell((short)0).getStringCellValue();
				this.libelleannee2=row.getCell((short)2).getStringCellValue();
				this.libelleannee3=row.getCell((short)13).getStringCellValue();
				this.libellebudget1=row.getCell((short)5).getStringCellValue();
				
			}
			else if (i==5){			
				this.libelleannee1+= "-"+(int)row.getCell((short)0).getNumericCellValue();
				this.libelleannee2+= "-"+(int)row.getCell((short)2).getNumericCellValue();
				this.libelleannee3+= "-"+row.getCell((short)13).getStringCellValue();
				this.libellebudget1+= "-"+(int)row.getCell((short)5).getNumericCellValue();
				this.libellebudget2="Budget-"+((int)row.getCell((short)5).getNumericCellValue()+1);
			}
			
		}
		//Parcourt des lignes codes
		while (rowIt.hasNext()){
			HSSFRow row = rowIt.next();
			LigneCompte ligneCompte = new LigneCompte();

			Iterator<HSSFCell> cellIt= row.cellIterator();

			/*for (int i = 0; i<row.getPhysicalNumberOfCells();i++){
				System.out.println(i+" : "+row.getCell((short) i));
			}*/

			//Parcourt des colonnes
			while (cellIt.hasNext()){
				HSSFCell cell = cellIt.next();
				switch (cell.getCellNum()){


				case 0:
					ligneCompte.setAnnee1((int)cell.getNumericCellValue());
					break;
				case 2:
					ligneCompte.setAnnee2((int)cell.getNumericCellValue());
					break;
				case 5:
					ligneCompte.setBudget1((int)cell.getNumericCellValue());
				case 13:
					ligneCompte.setAnnee3((int)cell.getNumericCellValue());
					break;
				/*case 12:
					ligneCompte.setBudget1((int)cell.getNumericCellValue());
					break;*/
				case 8:
					String k=cell.getRichStringCellValue().getString();

					String code=k.substring(k.indexOf(" | ")+3, k.lastIndexOf("-"));
					String libelleCentregestion=k.substring(0,k.indexOf(" | "));
					String libelle=k.substring(k.lastIndexOf("-")+1,k.length());
                    ligneCompte.setLibelleCentreGension(libelleCentregestion);
					ligneCompte.setCodecompte(Integer.parseInt(code));
					ligneCompte.setLibelleCompte(libelle);	
					break;
				}



			}
			
			// Verif que le code compta existe dans le hashmap
			if (correspondanceCodeCompta_ClasseCompta.get(ligneCompte.getCodecompte())!=null){

				if (correspondanceCodeCompta_ClasseCompta.get(ligneCompte.getCodecompte())==1){
					classeCompteFonctionnement.ajoutLigneCompte(ligneCompte);	
				}
				else if (correspondanceCodeCompta_ClasseCompta.get(ligneCompte.getCodecompte())==2){
					classeCompteLocaux.ajoutLigneCompte(ligneCompte);	
				}
				else if (correspondanceCodeCompta_ClasseCompta.get(ligneCompte.getCodecompte())==3){
					classeCompteSecours.ajoutLigneCompte(ligneCompte);	
				}
				else if (correspondanceCodeCompta_ClasseCompta.get(ligneCompte.getCodecompte())==4){
					classeCompteActivites.ajoutLigneCompte(ligneCompte);		
				}
				else if (correspondanceCodeCompta_ClasseCompta.get(ligneCompte.getCodecompte())==5){
					classeCompteSubvention.ajoutLigneCompte(ligneCompte);		
				}
				
			}

		}
	}








	// Ecrire de la nouvelle feuille dans le workbook
	public void ecriture() throws IOException{

		/*if(book.getSheet(nomfichierecriture)!=null){
			System.out.println("la feuille existe deja inutile de refaire le traitement");
			return;
		}*/

		if (book.getSheet(nomsheetcriture)!=null)
			book.removeSheetAt(book.getSheetIndex(nomsheetcriture));
		sheetecriture = book.createSheet(nomsheetcriture);

		//Première ligne de libellé
		HSSFRow rowx= sheetecriture.createRow(0);
		//Création des cellules
		HSSFCell cell1= rowx.createCell((short) 0);
		HSSFCell cell2= rowx.createCell((short) 1);
		HSSFCell cell3= rowx.createCell((short) 2);
		HSSFCell cell4= rowx.createCell((short) 3);
		HSSFCell cell5= rowx.createCell((short) 4);
		HSSFCell cell6= rowx.createCell((short) 5);

		// style
		HSSFCellStyle cellt= book.createCellStyle();
	    cellt.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	   		
		cell1.setCellValue(this.libellefichier);	
		cell1.setCellStyle(cellt);
		
		cell2.setCellValue(this.libelleannee1);
		
		cell2.setCellStyle(cellt);
		
		cell3.setCellValue(this.libelleannee2);
		
		cell3.setCellStyle(cellt);
		
		cell4.setCellValue(this.libelleannee3);
		
		cell4.setCellStyle(cellt);
		
		cell5.setCellValue(this.libellebudget1);
		
		cell5.setCellStyle(cellt);
		
		cell6.setCellValue(this.libellebudget2);
		
		cell6.setCellStyle(cellt);
		
		
		int totaldepenseannee1 = 0;
		int totaldepenseannee2 = 0;
		int totaldepenseannee3 = 0;
		int totaldepensebudget1 = 0;
		int totaldepensebudget2 = 0;
		int totalressourceannee1 = 0;
		int totalressourceannee2 = 0;
		int totalressourceannee3 = 0;
		int totalressourcebudget1 = 0;
		int totalressourcebudget2 = 0;
		
		
		//Classe Fonctionnement
		int compteurligne = 1;
		for(int i =0; i<classeCompteFonctionnement.getTableauLigneCompte().size();i++){

			//Création d'une ligne
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);


			//Ajout dans les colonnes
			Iterator<HSSFCell> cellIt= rowx.cellIterator();
			while (cellIt.hasNext()){
				HSSFCell cell = cellIt.next();
				cell1.setCellValue(classeCompteFonctionnement.getTableauLigneCompte().get(i).getLibelleCentreGension()+" " +classeCompteFonctionnement.getTableauLigneCompte().get(i).getCodecompte()+"-"+classeCompteFonctionnement.getTableauLigneCompte().get(i).getLibelleCompte());
				cell2.setCellValue(classeCompteFonctionnement.getTableauLigneCompte().get(i).getAnnee1());
				cell3.setCellValue(classeCompteFonctionnement.getTableauLigneCompte().get(i).getAnnee2());
				cell4.setCellValue(classeCompteFonctionnement.getTableauLigneCompte().get(i).getAnnee3());
				cell5.setCellValue(classeCompteFonctionnement.getTableauLigneCompte().get(i).getBudget1());
				cell6.setCellValue(classeCompteFonctionnement.getTableauLigneCompte().get(i).getBudget2());
			}
			

		}
		
		//ligne total fonctionnement
		rowx= sheetecriture.createRow(compteurligne);
		compteurligne++;
		
					
		HSSFCellStyle mystyle=book.createCellStyle();
		//mystyle.setFont(myfont);
		mystyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		//mystyle.setFillPattern(HSSFCellStyle.ALT_BARS);
	   	mystyle.setFillForegroundColor(HSSFColor.WHITE.index);
	   
	   	HSSFCellStyle cells= null;
	    cells= book.createCellStyle();
	    cells.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
	    cells.setBottomBorderColor(HSSFColor.RED.index);
	    cells.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
	    cells.setLeftBorderColor(HSSFColor.RED.index);
	    cells.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
	    cells.setRightBorderColor(HSSFColor.RED.index);
	    cells.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
	    cells.setTopBorderColor(HSSFColor.RED.index);
	   
	   	
	 
	//	cellstyle.setFont(HSSFFont.BOLDWEIGHT_NORMAL);
		
		//Création des cellules
		
		cell1= rowx.createCell((short) 0);
		
		
		//attribution d'une couleur
		
		
		
		cell2= rowx.createCell((short) 1);
		cell3= rowx.createCell((short) 2);
		cell4= rowx.createCell((short) 3);
		cell5= rowx.createCell((short) 4);
		cell6= rowx.createCell((short) 5);
         
		cell1.setCellValue(classeCompteFonctionnement.getLibelle());
		
		cell1.getRichStringCellValue().applyFont(HSSFFont.COLOR_RED);
		cell1.setCellStyle(cells);
		
		
		
		
		cell2.setCellValue(classeCompteFonctionnement.getTotalAnnee1());
		
				cell2.setCellStyle(cells);
		
		cell3.setCellValue(classeCompteFonctionnement.getTotalAnnee2());
		
				cell3.setCellStyle(cells);
		
		cell4.setCellValue(classeCompteFonctionnement.getTotalAnnee3());
		
			cell4.setCellStyle(cells);
		
		cell5.setCellValue(classeCompteFonctionnement.getTotalBudget1());
		
				cell5.setCellStyle(cells);
		
		cell6.setCellValue(classeCompteFonctionnement.getTotalBudget2());
		
				cell6.setCellStyle(cells);
		
		totaldepenseannee1 +=classeCompteFonctionnement.getTotalAnnee1();
		totaldepenseannee2 +=classeCompteFonctionnement.getTotalAnnee2();
		totaldepenseannee3 +=classeCompteFonctionnement.getTotalAnnee3();
		totaldepensebudget1+=classeCompteFonctionnement.getTotalBudget1();
		totaldepensebudget2+=classeCompteFonctionnement.getTotalBudget2(); 

		
		// Classe Locaux
				
		for(int i =0; i<classeCompteLocaux.getTableauLigneCompte().size();i++){

			//Création d'une ligne
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);


			//Parcourt des colonnes
			Iterator<HSSFCell> cellIt= rowx.cellIterator();
			while (cellIt.hasNext()){
				HSSFCell cell = cellIt.next();
				cell1.setCellValue(classeCompteLocaux.getTableauLigneCompte().get(i).getLibelleCentreGension()+" " +classeCompteLocaux.getTableauLigneCompte().get(i).getCodecompte()+"-"+classeCompteLocaux.getTableauLigneCompte().get(i).getLibelleCompte());
				cell2.setCellValue(classeCompteLocaux.getTableauLigneCompte().get(i).getAnnee1());
				cell3.setCellValue(classeCompteLocaux.getTableauLigneCompte().get(i).getAnnee2());
				cell4.setCellValue(classeCompteLocaux.getTableauLigneCompte().get(i).getAnnee3());
				cell5.setCellValue(classeCompteLocaux.getTableauLigneCompte().get(i).getBudget1());
				cell6.setCellValue(classeCompteLocaux.getTableauLigneCompte().get(i).getBudget2());
			}
			
		}
		
		//ligne total locaux
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);

			cell1.setCellValue(classeCompteLocaux.getLibelle());
			
			cell1.getRichStringCellValue().applyFont(HSSFFont.COLOR_RED);
				cell1.setCellStyle(cells);
			
			cell2.setCellValue(classeCompteLocaux.getTotalAnnee1());
				cell2.setCellStyle(cells);
			
			cell3.setCellValue(classeCompteLocaux.getTotalAnnee2());
				cell3.setCellStyle(cells);
			
			cell4.setCellValue(classeCompteLocaux.getTotalAnnee3());
				cell4.setCellStyle(cells);
			
			cell5.setCellValue(classeCompteLocaux.getTotalBudget1());
				cell5.setCellStyle(cells);
			cell6.setCellValue(classeCompteLocaux.getTotalBudget2());
				cell6.setCellStyle(cells);
			
			totaldepenseannee1 +=classeCompteLocaux.getTotalAnnee1();
			totaldepenseannee2 +=classeCompteLocaux.getTotalAnnee2();
			totaldepenseannee3 +=classeCompteLocaux.getTotalAnnee3();
			totaldepensebudget1+=classeCompteLocaux.getTotalBudget1();
			totaldepensebudget2+=classeCompteLocaux.getTotalBudget2(); 
				
	// Classe Secours
		
			for(int i =0; i<classeCompteSecours.getTableauLigneCompte().size();i++){

			//Création d'une ligne
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);


			//Parcourt des colonnes
			Iterator<HSSFCell> cellIt= rowx.cellIterator();
			while (cellIt.hasNext()){
				HSSFCell cell = cellIt.next();
				cell1.setCellValue(classeCompteSecours.getTableauLigneCompte().get(i).getLibelleCentreGension()+" "+classeCompteSecours.getTableauLigneCompte().get(i).getCodecompte()+"-"+classeCompteSecours.getTableauLigneCompte().get(i).getLibelleCompte());
				cell2.setCellValue(classeCompteSecours.getTableauLigneCompte().get(i).getAnnee1());
				cell3.setCellValue(classeCompteSecours.getTableauLigneCompte().get(i).getAnnee2());
				cell4.setCellValue(classeCompteSecours.getTableauLigneCompte().get(i).getAnnee3());
				cell5.setCellValue(classeCompteSecours.getTableauLigneCompte().get(i).getBudget1());
				cell6.setCellValue(classeCompteSecours.getTableauLigneCompte().get(i).getBudget2());
			}


		}
		
			//ligne total secours
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);

			cell1.setCellValue(classeCompteSecours.getLibelle());
			
			cell1.getRichStringCellValue().applyFont(HSSFFont.COLOR_RED);
				cell1.setCellStyle(cells);
			
			cell2.setCellValue(classeCompteSecours.getTotalAnnee1());
				cell2.setCellStyle(cells);
			
			cell3.setCellValue(classeCompteSecours.getTotalAnnee2());
				cell3.setCellStyle(cells);
			
			cell4.setCellValue(classeCompteSecours.getTotalAnnee3());
				cell4.setCellStyle(cells);
			
			cell5.setCellValue(classeCompteSecours.getTotalBudget1());
				cell5.setCellStyle(cells);
			
			cell6.setCellValue(classeCompteSecours.getTotalBudget2());
				cell6.setCellStyle(cells);
			
			
			totaldepenseannee1 += classeCompteSecours.getTotalAnnee1();
			totaldepenseannee2 += classeCompteSecours.getTotalAnnee2();
			totaldepenseannee3 += classeCompteSecours.getTotalAnnee3();
			totaldepensebudget1 += classeCompteSecours.getTotalBudget1();
			totaldepensebudget2 += classeCompteSecours.getTotalBudget2(); 
			
		//Classe Activités
						
			for(int i =0; i<classeCompteActivites.getTableauLigneCompte().size();i++){

				//Création d'une ligne
				rowx= sheetecriture.createRow(compteurligne);
				compteurligne++;
				//Création des cellules
				cell1= rowx.createCell((short) 0);
				cell2= rowx.createCell((short) 1);
				cell3= rowx.createCell((short) 2);
				cell4= rowx.createCell((short) 3);
				cell5= rowx.createCell((short) 4);
				cell6= rowx.createCell((short) 5);


				//Parcourt des colonnes
				Iterator<HSSFCell> cellIt= rowx.cellIterator();
				while (cellIt.hasNext()){
					HSSFCell cell = cellIt.next();
					cell1.setCellValue(classeCompteActivites.getTableauLigneCompte().get(i).getLibelleCentreGension()+" "+classeCompteActivites.getTableauLigneCompte().get(i).getCodecompte()+"-"+classeCompteActivites.getTableauLigneCompte().get(i).getLibelleCompte());
					cell2.setCellValue(classeCompteActivites.getTableauLigneCompte().get(i).getAnnee1());
					cell3.setCellValue(classeCompteActivites.getTableauLigneCompte().get(i).getAnnee2());
					cell4.setCellValue(classeCompteActivites.getTableauLigneCompte().get(i).getAnnee3());
					cell5.setCellValue(classeCompteActivites.getTableauLigneCompte().get(i).getBudget1());
					cell6.setCellValue(classeCompteActivites.getTableauLigneCompte().get(i).getBudget2());
				}


			}	
			
			
			//ligne total activites
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);  
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);

			cell1.setCellValue(classeCompteActivites.getLibelle());
			
			cell1.getRichStringCellValue().applyFont(HSSFFont.COLOR_RED);
				cell1.setCellStyle(cells);
			
			cell2.setCellValue(classeCompteActivites.getTotalAnnee1());
				cell2.setCellStyle(cells);
			
			cell3.setCellValue(classeCompteActivites.getTotalAnnee2());
				cell3.setCellStyle(cells);
			
			cell4.setCellValue(classeCompteActivites.getTotalAnnee3());
				cell4.setCellStyle(cells);
			
			cell5.setCellValue(classeCompteActivites.getTotalBudget1());
				cell5.setCellStyle(cells);
			
			cell6.setCellValue(classeCompteActivites.getTotalBudget2());
				cell6.setCellStyle(cells);
			
			
			totaldepenseannee1 += classeCompteActivites.getTotalAnnee1();
			totaldepenseannee2 += classeCompteActivites.getTotalAnnee2();
			totaldepenseannee3 += classeCompteActivites.getTotalAnnee3();
			totaldepensebudget1 += classeCompteActivites.getTotalBudget1();
			totaldepensebudget2 += classeCompteActivites.getTotalBudget2(); 
			
			//ligne total DEPENSE
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);
			
			cell1.setCellValue("TOTAL DEPENSES");
			
			cell1.getRichStringCellValue().applyFont(HSSFFont.COLOR_RED);
				cell1.setCellStyle(cells);
			
			cell2.setCellValue(totaldepenseannee1);
				cell2.setCellStyle(cells);
			
			cell3.setCellValue(totaldepenseannee2);
				cell3.setCellStyle(cells);
			
			cell4.setCellValue(totaldepenseannee3);
				cell4.setCellStyle(cells);
			
			cell5.setCellValue(totaldepensebudget1);
				cell5.setCellStyle(cells);
			
			cell6.setCellValue(totaldepensebudget2);
				cell6.setCellStyle(cells);
			
			
			// Classe Subventions
			for(int i =0; i<classeCompteSubvention.getTableauLigneCompte().size();i++){

				//Création d'une ligne
				rowx= sheetecriture.createRow(compteurligne);
				compteurligne++;
				//Création des cellules
				cell1= rowx.createCell((short) 0);
				cell2= rowx.createCell((short) 1);
				cell3= rowx.createCell((short) 2);
				cell4= rowx.createCell((short) 3);
				cell5= rowx.createCell((short) 4);
				cell6= rowx.createCell((short) 5);


				//Parcourt des colonnes
				Iterator<HSSFCell> cellIt= rowx.cellIterator();
				while (cellIt.hasNext()){
					HSSFCell cell = cellIt.next();
					cell1.setCellValue(classeCompteSubvention.getTableauLigneCompte().get(i).getCodecompte()+"-"+classeCompteSubvention.getTableauLigneCompte().get(i).getLibelleCompte());
					cell2.setCellValue(classeCompteSubvention.getTableauLigneCompte().get(i).getAnnee1());
					cell3.setCellValue(classeCompteSubvention.getTableauLigneCompte().get(i).getAnnee2());
					cell4.setCellValue(classeCompteSubvention.getTableauLigneCompte().get(i).getAnnee3());
					cell5.setCellValue(classeCompteSubvention.getTableauLigneCompte().get(i).getBudget1());
					cell6.setCellValue(classeCompteSubvention.getTableauLigneCompte().get(i).getBudget2());
				}


			}	
			
			
			// ligne total ressource + dépense
			
			
			

			//ligne total activites
			rowx= sheetecriture.createRow(compteurligne);
			compteurligne++;
			//Création des cellules
			cell1= rowx.createCell((short) 0);
			cell2= rowx.createCell((short) 1);
			cell3= rowx.createCell((short) 2);
			cell4= rowx.createCell((short) 3);
			cell5= rowx.createCell((short) 4);
			cell6= rowx.createCell((short) 5);

			cell1.setCellValue(classeCompteSubvention.getLibelle());
			
			cell1.getRichStringCellValue().applyFont(HSSFFont.COLOR_RED);
			
			cell2.setCellValue(classeCompteSubvention.getTotalAnnee1());
			cell3.setCellValue(classeCompteSubvention.getTotalAnnee2());
			cell4.setCellValue(classeCompteSubvention.getTotalAnnee3());
			cell5.setCellValue(classeCompteSubvention.getTotalBudget1());
			cell6.setCellValue(classeCompteSubvention.getTotalBudget2());
			
			totalressourceannee1 += classeCompteSubvention.getTotalAnnee1();
			totalressourceannee2 += classeCompteSubvention.getTotalAnnee2();
			totalressourceannee3 += classeCompteSubvention.getTotalAnnee3();
			totalressourcebudget1+= classeCompteSubvention.getTotalBudget1();
			totalressourcebudget2 += classeCompteSubvention.getTotalBudget2(); 
			
			
			// somme de dépense + ressources
			
			
			HSSFCellStyle cellst= null;
		    cellst= book.createCellStyle();
		    cellst.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		    cellst.setBottomBorderColor(HSSFColor.BLUE.index);
		    cellst.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		    cellst.setLeftBorderColor(HSSFColor.BLUE.index);
		    cellst.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		    cellst.setRightBorderColor(HSSFColor.BLUE.index);
		    cellst.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		    cellst.setTopBorderColor(HSSFColor.BLUE.index);
			
			
			/*
			totalressourceannee1+=totaldepenseannee1;
			totalressourceannee2+=totaldepenseannee2;
			totalressourceannee3+=totaldepenseannee3;
			totalressourcebudget1+=totaldepensebudget1;
			totalressourcebudget2+=totaldepensebudget2;
			*/
			
			cell1.setCellValue("TOTAL RESSOURCES");
			
			cell1.getRichStringCellValue().applyFont(HSSFFont.COLOR_NORMAL);
				cell1.setCellStyle(cellst) ;
									
			cell2.setCellValue(totalressourceannee1);
				cell2.setCellStyle(cellst) ;
			
			cell3.setCellValue(totalressourceannee2);
				cell3.setCellStyle(cellst) ;
			
			cell4.setCellValue(totalressourceannee3);
				cell4.setCellStyle(cellst) ;
			
			cell5.setCellValue(totalressourcebudget1);
				cell5.setCellStyle(cellst) ;
			
			cell6.setCellValue(totalressourcebudget2);
				cell6.setCellStyle(cellst) ;
			
		



		try {
			fichierecriture = new FileOutputStream(cheminfichierlecture);
			book.write(fichierecriture);
			fichierecriture.close();
		} 

		catch (Exception e) {
			
			e.printStackTrace();
			fichierecriture.close();
		}
	
	}


	private void initHashMap_Libelles() {
		//Initialisation des libelles
		/*int anneencours = Calendar.getInstance().get(Calendar.YEAR);
		this.setLibelleannee1(String.valueOf(anneencours-2));
		this.setLibelleannee2(String.valueOf(anneencours-1));
		this.setLibelleannee3(String.valueOf(anneencours));
		this.setLibellebudget1("Budget:exercice-Complet"+"-"+String.valueOf(anneencours));
		this.setLibellebudget2(String.valueOf(anneencours+1));*/
		
		//init hashmap
		correspondanceCodeCompta_ClasseCompta = new HashMap<Integer,Integer>();
		
		//Classe fonctionnement
		correspondanceCodeCompta_ClasseCompta.put(606101, 1);correspondanceCodeCompta_ClasseCompta.put(606102, 1);
		correspondanceCodeCompta_ClasseCompta.put(606103, 1);correspondanceCodeCompta_ClasseCompta.put(606104, 1);
		correspondanceCodeCompta_ClasseCompta.put(606300, 1);correspondanceCodeCompta_ClasseCompta.put(606318, 1);
		correspondanceCodeCompta_ClasseCompta.put(606400, 1);correspondanceCodeCompta_ClasseCompta.put(606800, 1);
		correspondanceCodeCompta_ClasseCompta.put(615501, 1);correspondanceCodeCompta_ClasseCompta.put(625100, 1);	
		correspondanceCodeCompta_ClasseCompta.put(625700, 1);correspondanceCodeCompta_ClasseCompta.put(626500, 1);	
		

		// Classe Locaux

		correspondanceCodeCompta_ClasseCompta.put(613200,2);correspondanceCodeCompta_ClasseCompta.put(614001,2);
		correspondanceCodeCompta_ClasseCompta.put(615201,2);correspondanceCodeCompta_ClasseCompta.put(615600,2);
		correspondanceCodeCompta_ClasseCompta.put(615200,2);correspondanceCodeCompta_ClasseCompta.put(635120,2);
		correspondanceCodeCompta_ClasseCompta.put(218101,2);correspondanceCodeCompta_ClasseCompta.put(628201,2);

		//Classe Secour
		correspondanceCodeCompta_ClasseCompta.put(657100,3);correspondanceCodeCompta_ClasseCompta.put(657900,3);

		//Classe Activités 
		correspondanceCodeCompta_ClasseCompta.put(606802,4);correspondanceCodeCompta_ClasseCompta.put(625800,4);
		correspondanceCodeCompta_ClasseCompta.put(628800,4);correspondanceCodeCompta_ClasseCompta.put(628100,4);
		correspondanceCodeCompta_ClasseCompta.put(628800,4);

		//Classe Subventions
		correspondanceCodeCompta_ClasseCompta.put(758801,5);correspondanceCodeCompta_ClasseCompta.put(758802,5);
		correspondanceCodeCompta_ClasseCompta.put(740000,5);

	}




	public String getLibellecompte() {
		return Libellecompte;
	}

	public void setLibellecompte(String libellecompte) {
		Libellecompte = libellecompte;
	}

	public String getLibelleannee1() {
		return libelleannee1;
	}

	public void setLibelleannee1(String libelleannee1) {
		this.libelleannee1 = libelleannee1;
	}

	public String getLibelleannee2() {
		return libelleannee2;
	}

	public void setLibelleannee2(String libelleannee2) {
		this.libelleannee2 = libelleannee2;
	}

	public String getLibelleannee3() {
		return libelleannee3;
	}

	public void setLibelleannee3(String libelleannee3) {
		this.libelleannee3 = libelleannee3;
	}

	public String getLibellebudget1() {
		return libellebudget1;
	}

	public void setLibellebudget1(String libellebudget1) {
		this.libellebudget1 = libellebudget1;
	}

	public String getLibellebudget2() {
		return libellebudget2;
	}

	public void setLibellebudget2(String libellebudget2) {
		this.libellebudget2 = libellebudget2;
	}



}