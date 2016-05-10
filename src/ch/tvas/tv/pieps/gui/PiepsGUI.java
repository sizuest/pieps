package ch.tvas.tv.pieps.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import ch.tvas.tv.pieps.session.Session;

public class PiepsGUI {
	
	protected static Shell shell;
	protected Display disp;
	
	protected SessionGUI sessionGUI;
	protected SessionRunGUI sessionRunGUI;
	
	private Boolean running = false;
	
	public PiepsGUI(Display display) {
		disp = display;
		shell = new Shell(display);
		
		shell.setText("Pieps");
		
		//shell.setSize(300, 200);
		
		/* Put shell into center of the screen */
		Monitor primary = display.getPrimaryMonitor();
	    Rectangle bounds = primary.getBounds();
	    Rectangle rect = shell.getBounds();
	    
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    
	    shell.setLocation(x, y);
	    
	    /* Fill Layout */
	    shell.setLayout(new GridLayout(1, true));
	    
	    /* Menu */
	    initMenu();
	    
	    /* Content */
	    initContent();
	    
	    /* Open shell */
	    shell.pack();
	    shell.open();
	    
	    shell.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				Session.stop();
				while(Session.isRunning()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	    
	    
	    /* Stay open */
	    while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) {
				display.sleep();
			}
			else if(this.needsUpdate()){
				update();
				running = Session.isRunning();
			}
		}
		
	}
	
	private void initContent() {
		
		sessionGUI = new SessionGUI(shell, SWT.BORDER);
		sessionRunGUI = new SessionRunGUI(shell, SWT.BORDER, this);
		
	}

	private void initMenu(){
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		/* Menu: Datei */
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("Datei");
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);
		MenuItem fileNewItem = new MenuItem(fileMenu, SWT.PUSH);
		fileNewItem.setText("Neu");
		MenuItem fileOpenItem = new MenuItem(fileMenu, SWT.PUSH);
		fileOpenItem.setText("Öffnen");
		MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveItem.setText("Speichern");
		MenuItem fileSaveAsItem = new MenuItem(fileMenu, SWT.PUSH);
		fileSaveAsItem.setText("Speichern als");			
		MenuItem filePropertiesItem = new MenuItem(fileMenu, SWT.PUSH);
		filePropertiesItem.setText("Einstellungen");
		MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("Beenden");
		
		/* Hilfe */
		MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		helpMenuHeader.setText("Hilfe");
		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		helpMenuHeader.setMenu(helpMenu);
		MenuItem helpAboutItem = new MenuItem(helpMenu, SWT.PUSH);
		helpAboutItem.setText("Über");
		
		/* Listeners */
		fileNewItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Session.newSession();
				update();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
		
		fileOpenItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Session.load(getFilePath("Öffnen"));
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
		
		fileSaveItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					Session.save();
				} catch (Exception e1) {
					saveAs();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
		
		fileSaveAsItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				saveAs();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
		
		filePropertiesItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				EditSoundSettings.open();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used*/}
		});
		
		fileExitItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used*/}
		});
		
		shell.setMenuBar(menuBar);
		
	}

	public void update() {
		if(Session.isRunning()){
			sessionGUI.run(true);
			sessionRunGUI.run(true);
		}
		else {
			sessionGUI.run(false);
			sessionRunGUI.run(false);
		}		
		
		sessionGUI.update();
		sessionRunGUI.update();
	}
	
	private boolean needsUpdate(){
		return (Session.isRunning() | running);
	}
	
	private void saveAs(){
		try {
			Session.saveAs(getFilePath("Speichern als"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getFilePath(String titel){
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
        fd.setText(titel);
        fd.setFilterPath("C:/");
        fd.setFilterExtensions(new String[] { "*.pieps", "*.*" });
        String selected = fd.open();
        
        return selected;
	}
	
}
