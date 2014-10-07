import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.transform.OutputKeys;


public class QuitterAppli implements ActionListener {
	private JFrame frameParent;
	public QuitterAppli(JFrame fenetre){
		this.frameParent=fenetre;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	
	int tot= JOptionPane.showConfirmDialog(frameParent, "Vouler vous quitter?","voulez-vous quitter", JOptionPane.YES_NO_OPTION,1);
	
	if(tot==0){
		System.exit(0);
		
	
	}
		
	}

}
