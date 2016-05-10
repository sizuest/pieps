package ch.tvas.tv.pieps.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import ch.tvas.tv.pieps.session.Session;
import ch.tvas.tv.pieps.session.SessionEventType;

public class SessionRunGUI extends AGUIElement {
	
	protected PiepsGUI mainGUI;
	protected Button buttonStart, buttonStop;
	protected Label labelTimeNext, labelTimeEnd;
	protected Label labelTimeNextValue, labelTimeEndValue;

	public SessionRunGUI(Composite parent, int style, PiepsGUI mainGUI) {
		super(parent, style);
		this.mainGUI = mainGUI;
		init();
	}

	@Override
	public void update() {
		double timeToNext = Session.getTimeToNextEvent(SessionEventType.SECTION);
		double timeToEnd  = Session.getTimeToNextEvent(SessionEventType.END);
		
		if(Double.isNaN(timeToNext))
			labelTimeNextValue.setText("-");
		else
			labelTimeNextValue.setText(Math.round(timeToNext*10)/10+" s");
		
		if(Double.isNaN(timeToEnd))
			labelTimeEndValue.setText(Math.round(Session.getEndTime()*10)/10+" s");
		else
			labelTimeEndValue.setText(Math.round(timeToEnd*10)/10+" s");
	}

	@Override
	public void run(boolean running) {
		buttonStart.setEnabled(!running);
		buttonStop.setEnabled(running);
	}

	@Override
	public void init() {
		this.setLayout(new GridLayout(2, false));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				
		labelTimeNext = new Label(this, SWT.NONE);
		labelTimeNext.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		labelTimeNext.setText("Zeit bis zum nächsten Abschnitt:");
		
		labelTimeNextValue = new Label(this, SWT.NONE);
		labelTimeNextValue.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		labelTimeNextValue.setText("******");
		labelTimeNextValue.setAlignment(SWT.RIGHT);
		
		labelTimeEnd = new Label(this, SWT.NONE);
		labelTimeEnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		labelTimeEnd.setText("Verbleibende Zeit:");
		
		labelTimeEndValue = new Label(this, SWT.NONE);
		labelTimeEndValue.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, true, true, 1, 1));
		labelTimeEndValue.setText("******");
		labelTimeEndValue.setAlignment(SWT.RIGHT);
		
		buttonStop = new Button(this, SWT.PUSH);
		buttonStop.setText("Stop");
		buttonStop.setEnabled(false);
		buttonStop.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
		buttonStop.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Session.stop();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not Used */}
		});
		
		buttonStart = new Button(this, SWT.PUSH);
		buttonStart.setText("Start");
		buttonStart.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
		buttonStart.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Session.buildEventTable();
				Session.start();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
		
	}
	

}
