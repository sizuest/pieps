package ch.tvas.tv.pieps.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ControlEditor;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ch.tvas.tv.pieps.session.SessionEventType;
import ch.tvas.tv.pieps.sounds.Player;
import ch.tvas.tv.pieps.sounds.SoundUtils;
import ch.tvas.tv.pieps.sounds.Tone;

public class EditSoundSettings extends Composite{
	
	protected Table table;
	protected Button close, save;

	public EditSoundSettings(Composite parent, int style) {
		super(parent, style);
		
		this.setLayout(new GridLayout(2, false));
		
		table = new Table(this, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
    	
    	String[] titles =  {"Ereignis", "Frequenz [Hz]", "Dauer [ms]", "Anzahl","     "};
		for(int i=0; i < titles.length; i++){
			TableColumn column = new TableColumn(table, SWT.NULL);
			column.setText(titles[i]);
		}
		
		for(SessionEventType e: SessionEventType.values())
			newInputSet(table, e);
		
		TableColumn[] columns = table.getColumns();
        for (int j = 0; j < columns.length; j++) {
          columns[j].pack();
        }
        
        addTableEditor(table);
        
        close = new Button(this, SWT.PUSH);
        close.setText("Abbrechen");
        close.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1));
        close.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
        
        save = new Button(this, SWT.PUSH);
        save.setText("Speichern");
        save.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true, 1, 1));
        save.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				save();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
	}
	
	public static void open(){
		final Shell shell = new Shell(Display.getCurrent());
		shell.setText("Einstellungen");
		shell.setLayout(new FillLayout());
		
		final EditSoundSettings gui = new EditSoundSettings(shell, SWT.NONE);
		gui.addDisposeListener(new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				gui.close();
				shell.dispose();
			}
		});

		
		shell.pack();
		shell.open();		
		
		
	}
	
	private void newInputSet(Table parent, final SessionEventType event){
		
		final TableItem item;
		Button play;
		TableEditor editor = new TableEditor(parent);
		
		item = new TableItem(parent, SWT.NONE);
		
		item.setText(0, event.toString());
		item.setText(1, Player.getTone(event).getFrequency()+"");
		item.setText(2, Player.getTone(event).getDuration()+"");
		item.setText(3, Player.getTone(event).getCount()+"");
		
		play = new Button(parent, SWT.PUSH);
		play.setText("▶");
		play.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, true, 1, 1));
		play.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					SoundUtils.playTone(getTone(item));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {/* Not used */}
		});
	
		play.pack();
		
		editor = new TableEditor(parent);
		editor.minimumWidth = play.getSize().x;
		editor.horizontalAlignment = SWT.LEFT;
        editor.setEditor(play, item, 4);
        
	}

	
	public void close(){
		try {
			Player.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.dispose();
	}
	
	public void save(){
		try {
			setValues();
			Player.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.dispose();
	}
	
	private void changeTableEntry(TableCursor cursor, Text text){
		TableItem row = cursor.getRow();
		int column = cursor.getColumn();
		int value;
		
		
		
		try{
			value = Integer.valueOf(text.getText());
			row.setText(column, value+"");
		}
		catch(Exception e){
			
		}
		
				
        text.dispose();
		this.update();
	}
	
	private void setValues(){
		SessionEventType event;
		
		for(TableItem ti: table.getItems()){
			event = SessionEventType.valueOf(ti.getText(0));
			
			Player.setTone(event, getTone(ti));
		}
	}
	
	private Tone getTone(TableItem ti){
		int freq, dur, count;
		
		freq  = Integer.valueOf(ti.getText(1));
		dur   = Integer.valueOf(ti.getText(2));
		count = Integer.valueOf(ti.getText(3));
		
		return new Tone(freq, dur, count);
	}
	
	private void addTableEditor(Table table){
		final TableCursor cursor = new TableCursor(table, SWT.NONE);
	    // create an editor to edit the cell when the user hits "ENTER"
	    // while over a cell in the table
	    final ControlEditor editor = new ControlEditor(cursor);
	    editor.grabHorizontal = true;
	    editor.grabVertical = true;
	   
	    cursor.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent e) {
	        	
	        	if(cursor.getColumn() == 0)
	        		return;
	        	
	            switch(e.keyCode) {
		            case SWT.ARROW_UP:
		            case SWT.ARROW_RIGHT:
		            case SWT.ARROW_DOWN:
		            case SWT.ARROW_LEFT:
		                break;
		               
		            default:
		                final Text text = new Text(cursor, SWT.NONE);
		                text.append(String.valueOf(e.character));
		                text.addKeyListener(new KeyAdapter() {
		                    public void keyPressed(KeyEvent e) {
		                        // close the text editor and copy the data over
		                        // when the user hits "ENTER"
		                        if (e.character == SWT.CR) {
		                        	changeTableEntry(cursor, text);
			                        text.dispose();
		                        }
		                        // close the text editor when the user hits "ESC"
		                        if (e.character == SWT.ESC) {
		                            text.dispose();
		                        }
		                    }
		                });
		                editor.setEditor(text);
		                text.setFocus();
		                break;
	            }  
	        }
	    });
	}
}
