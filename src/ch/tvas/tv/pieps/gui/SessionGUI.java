package ch.tvas.tv.pieps.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ch.tvas.tv.pieps.session.Session;

public class SessionGUI extends AGUIElement{
	
	/* Numerical inputs */
	private Text textLevelLength;
	private Text textSectionLength;
	private Text textInitVelocity;
	private Text textIncrVelocity;
	private Text textTerminalVelocity;
	private Text textTotalTime;
	
	/* Unit selectors */
	private Combo comboLevelLength;
	private Combo comboSectionLength;
	private Combo comboInitVelocity;
	private Combo comboIncrVelocity;
	private Combo comboTerminalVelocity;
	private Combo comboTotalTime;
	
	/* Labels */
	private Label labelLevelLength;
	private Label labelSectionLength;
	private Label labelInitVelocity;
	private Label labelIncrVelocity;
	private Label labelTerminalVelocity;
	private Label labelTotalTime;
	
	/* Buttons */
	private Button buttonSave;
	
	

	public SessionGUI(Composite parent, int style) {
		super(parent, style);
		
		init();
	}

	@Override
	public void update() {
		textLevelLength.setText(""+Session.getLevelLength());
		textSectionLength.setText(""+Session.getSectionLength());
		if(0==comboInitVelocity.getSelectionIndex())
			textInitVelocity.setText(""+Session.getInitVelocity());
		else
			textInitVelocity.setText(""+Session.getInitVelocityKmH());
			
		if(0==comboIncrVelocity.getSelectionIndex())
			textIncrVelocity.setText(""+Session.getIncrVelocity());
		else
			textIncrVelocity.setText(""+Session.getIncrVelocityKmH());
		
		if(0==comboTerminalVelocity.getSelectionIndex())
			textTerminalVelocity.setText(""+Session.getTerminalVelocity());
		else
			textTerminalVelocity.setText(""+Session.getTerminalVelocityKmH());
		
		textTotalTime.setText(""+Math.round(Session.getEndTime()*10)/10);
	}

	@Override
	public void run(boolean running) {
		textLevelLength.setEnabled(!running);
		textSectionLength.setEnabled(!running);
		textInitVelocity.setEnabled(!running);
		textIncrVelocity.setEnabled(!running);
		textTerminalVelocity.setEnabled(!running);
		textTotalTime.setEnabled(!running);
		
		comboLevelLength.setEnabled(!running);
		comboSectionLength.setEnabled(!running);
		comboInitVelocity.setEnabled(!running);
		comboIncrVelocity.setEnabled(!running);
		comboTerminalVelocity.setEnabled(!running);
		comboTotalTime.setEnabled(!running);
		
		buttonSave.setEnabled(!running);
	}

	@Override
	public void init() {
		this.setLayout(new GridLayout(3, false));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		String[] lengthUnits={"m"},
				 velocityUnits={"m/s", "km/h"},
			     timeUnits={"s"};
		
		/* LevelLength */
		labelLevelLength = new Label(this, SWT.NONE);
		labelLevelLength.setText("Abschnittslänge");
		labelLevelLength.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		textLevelLength = new Text(this, SWT.BORDER);
		textLevelLength.setText("*****");
		textLevelLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		comboLevelLength = new Combo(this, SWT.BORDER);
		comboLevelLength.setItems(lengthUnits);
		comboLevelLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		/* SectionLength */
		labelSectionLength = new Label(this, SWT.NONE);
		labelSectionLength.setText("Inkrementlänge");
		labelSectionLength.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		textSectionLength = new Text(this, SWT.BORDER);
		textSectionLength.setText("*****");
		textSectionLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		comboSectionLength = new Combo(this, SWT.BORDER);
		comboSectionLength.setItems(lengthUnits);
		comboSectionLength.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		/* InitVelocity */
		labelInitVelocity = new Label(this, SWT.NONE);
		labelInitVelocity.setText("Anfangsgeschwindigkeit");
		labelInitVelocity.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		textInitVelocity = new Text(this, SWT.BORDER);
		textInitVelocity.setText("*****");
		textInitVelocity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		comboInitVelocity = new Combo(this, SWT.BORDER);
		comboInitVelocity.setItems(velocityUnits);
		comboInitVelocity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		/* IncrVelocity */
		labelIncrVelocity = new Label(this, SWT.NONE);
		labelIncrVelocity.setText("Geschwindigkeit pro Abschnitt");
		labelIncrVelocity.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		textIncrVelocity = new Text(this, SWT.BORDER);
		textIncrVelocity.setText("*****");
		textIncrVelocity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		comboIncrVelocity = new Combo(this, SWT.BORDER);
		comboIncrVelocity.setItems(velocityUnits);
		comboIncrVelocity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		/* TerminalVelocity */
		labelTerminalVelocity = new Label(this, SWT.NONE);
		labelTerminalVelocity.setText("Endgeschwindigkeit");
		labelTerminalVelocity.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		textTerminalVelocity = new Text(this, SWT.BORDER);
		textTerminalVelocity.setText("*****");
		textTerminalVelocity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		comboTerminalVelocity = new Combo(this, SWT.BORDER);
		comboTerminalVelocity.setItems(velocityUnits);
		comboTerminalVelocity.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		/* Total Time */
		labelTotalTime = new Label(this, SWT.NONE);
		labelTotalTime.setText("Gesammtdauer");
		labelTotalTime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		
		textTotalTime = new Text(this, SWT.BORDER);
		textTotalTime.setText("*****");
		textTotalTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		textTotalTime.setEditable(false);
		
		comboTotalTime = new Combo(this, SWT.BORDER);
		comboTotalTime.setItems(timeUnits);
		comboTotalTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		/* Button */
		buttonSave = new Button(this, SWT.PUSH);
		buttonSave.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
		buttonSave.setText("Übernehmen");
		buttonSave.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Session.buildEventTable();
				update();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
		
		this.redraw();
		
		/* Default combo selection */
		comboIncrVelocity.select(0);
		comboInitVelocity.select(0);
		comboLevelLength.select(0);
		comboSectionLength.select(0);
		comboTerminalVelocity.select(0);
		comboTotalTime.select(0);
		
		update();
		
		/* Combo Listeners */
		SelectionListener comboListener = new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				update();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		};
		comboIncrVelocity.addSelectionListener(comboListener);
		comboInitVelocity.addSelectionListener(comboListener);
		comboLevelLength.addSelectionListener(comboListener);
		comboSectionLength.addSelectionListener(comboListener);
		comboTerminalVelocity.addSelectionListener(comboListener);
		
		/* Text Listener */
		KeyListener textListener = new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				setValues();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		textIncrVelocity.addKeyListener(textListener);
		textInitVelocity.addKeyListener(textListener);
		textLevelLength.addKeyListener(textListener);
		textSectionLength.addKeyListener(textListener);
		textTerminalVelocity.addKeyListener(textListener);
	}
	
	public void setValues(){
		double newValue;
		
		// Level
		try{
			newValue = Double.valueOf(textLevelLength.getText());
			Session.setLevelLength(newValue);
		}
		catch(Exception e1){}
		
		// Section
		try{
			newValue = Double.valueOf(textSectionLength.getText());
			Session.setSectionLength(newValue);
		}
		catch(Exception e1){}
		
		// IncrVelocity
		try{
			newValue = Double.valueOf(textIncrVelocity.getText());
			if(0 == comboIncrVelocity.getSelectionIndex())
				Session.setIncrVelocity(newValue);
			else
				Session.setIncrVelocityKmH(newValue);
		}
		catch(Exception e1){}
		
		// InitVelocity
		try{
			newValue = Double.valueOf(textInitVelocity.getText());
			if(0 == comboInitVelocity.getSelectionIndex())
				Session.setInitVelocity(newValue);
			else
				Session.setInitVelocityKmH(newValue);
		}
		catch(Exception e1){}
		
		//TerminalVelocity
		try{
			newValue = Double.valueOf(textTerminalVelocity.getText());
			if(0 == comboTerminalVelocity.getSelectionIndex())
				Session.setTerminalVelocity(newValue);
			else
				Session.setTerminalVelocityKmH(newValue);
		}
		catch(Exception e1){}
		
	}
	

}
