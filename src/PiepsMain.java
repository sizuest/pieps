import java.util.Locale;

import org.eclipse.swt.widgets.Display;

import ch.tvas.tv.pieps.gui.PiepsGUI;



public class PiepsMain {
	
	public static void main(String[] args) {
		Display disp = new Display();
		
		Locale.setDefault(new Locale("de", "CH"));
		
		//start program
		new PiepsMain();
			
		System.out.println("PiepsMain called PiepsGUI");
		new PiepsGUI(disp);
		
		
		//shut down
		disp.dispose();
	}
	
	public PiepsMain() {
		System.out.println("Pieps started");
	}

}
