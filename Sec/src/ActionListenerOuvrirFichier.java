import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;


public class ActionListenerOuvrirFichier implements ActionListener {
	private JFrame frameParent;
	public ActionListenerOuvrirFichier(JFrame fenetre){
		
		this.frameParent=fenetre;
	}

	//private final FileNameExtensionFilter txt = new FileNameExtensionFilter("Fichiers texte (*.txt)", "txt");
	private final FileNameExtensionFilter xsl = new FileNameExtensionFilter("Fichiers excel (*.xls)", "xls");
	//private  final FileNameExtensionFilter xml = new FileNameExtensionFilter("Fichiers xml (*.xml)", "xml");
	//private  final FileNameExtensionFilter tout = new FileNameExtensionFilter("Fichiers cibles (*.xml, *.xls, *.txt)", "xml,xls,txt");
	@Override
	public void actionPerformed(ActionEvent e) {

		JFileChooser fic= new JFileChooser(new File ("."));
		fic.setFileFilter(xsl);
		// Enleve le filtre tout
		fic.setAcceptAllFileFilterUsed(false);

		if( fic.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
	
			try {
				
				FileInputStream	f = new FileInputStream (fic.getSelectedFile().getAbsolutePath());
				traitementFichierXSL test;
				try {
					JOptionPane.showConfirmDialog(frameParent, "debut traitement","Info", JOptionPane.YES_OPTION,1);
					test = new traitementFichierXSL(fic.getSelectedFile().getAbsolutePath());
					test.lecture();
					test.ecriture();
					JOptionPane.showConfirmDialog(frameParent, "Traitement du fichier terminé avec succès","Info", JOptionPane.YES_OPTION,1);
					
				} catch (IOException e1) {
					
					
					JOptionPane.showConfirmDialog(frameParent, "IOException","Erreur", JOptionPane.YES_OPTION,1);
					
					
					e1.printStackTrace();
				}
				
				
				
				
				
				
			} catch (FileNotFoundException e1) {
				JOptionPane.showConfirmDialog(frameParent, "FileNotFoundException","Erreur", JOptionPane.YES_OPTION,1);
				e1.printStackTrace();
			}
			




		}

	}




}

