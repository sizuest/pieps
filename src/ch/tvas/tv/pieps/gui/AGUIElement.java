package ch.tvas.tv.pieps.gui;

import org.eclipse.swt.widgets.Composite;

public abstract class AGUIElement extends Composite{

	public AGUIElement(Composite parent, int style) {
		super(parent, style);
	}

	
	public abstract void update();
	
	public abstract void run(boolean running);
	
	public abstract void init();
}
