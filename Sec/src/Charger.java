
import java.awt.event.KeyEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;



//import fl.notebook.NoteBookGUI.changefiltre;


public class Charger extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	// créer des menus 
	static JMenuItem mnItemSauvegarder = new JMenuItem("Sauvegarder",KeyEvent.VK_S);
	static JMenuItem mnItemFichier = new JMenuItem("Ouvrir un Fichier",KeyEvent.VK_R);
	static JMenuItem mnItemSupprimer = new JMenuItem("Supprimer", KeyEvent.VK_S);
	static JFileChooser savedFile = new JFileChooser();
	
	
	public Charger (){
		// regalge de l'affichage de la fenêtre

		this.setTitle("Traitement");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setResizable(false);
	
		
		// Nom de fichier par défaut du sauvegarder
				savedFile.setSelectedFile(new File("*.xls"));
				
				//savedFile.addPropertyChangeListener(new changefiltre());
				
				// Création d'une barre de menus et d'un menu
				JMenuBar menubar = new JMenuBar();
				JMenu menuFichier ;
				JMenu menuAide;
				JMenu menuEdition;
				
				// creation de menu
				 menuFichier= new JMenu("Fichier");
				 menuAide = new JMenu("Aide");
				 menuEdition = new JMenu("Edition");
				 
				 menuFichier.setMnemonic(KeyEvent.VK_F);
				 menuEdition.setMnemonic(KeyEvent.VK_E);
				 menuAide.setMnemonic(KeyEvent.VK_A);
				 
				 // ajout des menu au menubar
				 menubar.add(menuFichier);
				 menubar.add(menuAide);
				 menubar.add(menuEdition);
				//  menuFichier.add(mnItemOvrir);
				// fournir à la fenêtre la barre de menus
					this.setJMenuBar(menubar);
				 // creation d'un radio bouton
				  
				 JMenuItem rdbt= new JMenuItem("ouvrir un Fichier");
				 JMenuItem rdbt1= new JMenuItem("ouvrir un repertoire");
				 JMenuItem ferme= new JMenuItem("Quitter");
	
				  menuFichier.add(rdbt);
				  menuFichier.add(rdbt1);
				  menuEdition.add(ferme);
				 
				  
				
				// Evenement	
				  
				rdbt.addActionListener(new ActionListenerOuvrirFichier(this));
				ferme.addActionListener(new QuitterAppli(this));
					
				
						

	}

}

