package ch.tvas.tv.pieps.sounds;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ch.tvas.tv.pieps.session.SessionEventType;

@XmlRootElement
public class Player {
	
	@XmlElement
	protected Tone toneEnd;
	@XmlElement
	protected Tone toneLevel;
	@XmlElement
	protected Tone toneSection;
	@XmlElement
	protected Tone toneStart;
	
	protected static String PATH = "tone__config.xml";  
	
	protected static Player player = null;
	
	private Player(){}
	
	public static Player getInstance(){
		if(null==player) {
			try {
				Player.load();
			}
			catch(Exception e){
				System.err.println("Player: failed to load config. Using default values.");
				e.printStackTrace();
				player      = new Player();
				player.toneEnd     = new Tone(444, 2000);
				player.toneLevel   = new Tone(444, 500);
				player.toneSection = new Tone(600, 1000);
				player.toneStart   = new Tone(444, 500, 3);
				try {
					Player.save();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		
		return player;
	}
	
	public static void save() throws Exception {
		JAXBContext context = JAXBContext.newInstance(Player.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		Writer w= new FileWriter(PATH);
		m.marshal(player, w);
		w.close();
	}
	
	public static void load() throws Exception{
		player = null;
		
		try {
			JAXBContext context = JAXBContext.newInstance(Player.class);
			Unmarshaller um = context.createUnmarshaller();
			player = (Player) um.unmarshal(new FileReader(PATH));
		}
		catch(Exception e){
			throw new Exception();
		}
	}
	
	public static Tone getTone(SessionEventType event){
		switch(event){
		case END:
			return getInstance().toneEnd;
		case LEVEL:
			return getInstance().toneLevel;
		case SECTION:
			return getInstance().toneSection;
		case START:
			return getInstance().toneStart; 
		}
		
		return null;
	}
	
	
	public static void playTone(SessionEventType event) throws Exception{
		System.out.println("Playing soud for event '"+event.toString()+"'");
		
		SoundUtils.playTone(getTone(event));
	}
	
	public static void setTone(SessionEventType event, Tone tone){
		switch(event){
		case END:
			getInstance().toneEnd = tone;
			break;
		case LEVEL:
			getInstance().toneLevel = tone;
			break;
		case SECTION:
			getInstance().toneSection = tone;
			break;
		case START:
			getInstance().toneStart = tone;
			break;
		default:
			System.err.println("Player: setTone: unknown event type: "+event.toString());
			break;
		
		}
	}
}
