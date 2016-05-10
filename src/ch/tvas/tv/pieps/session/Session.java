package ch.tvas.tv.pieps.session;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Session {
	private static Session session;
	
	@XmlElement
	private double levelLength=100;
	@XmlElement
	private double sectionLength=25;
	@XmlElement
	private double initVelocity=5;
	@XmlElement
	private double incrVelocity=3;
	@XmlElement
	private double terminalVelocity=15;
	
	private String path="";
	
	private SessionEventTable eventTable;
	private double startTime;
	private boolean running = false;
	
	private SessionThread sessionThread;
	private Thread thread;
	
	
	private Session(){}
	
	/**
	 * getInstance()
	 * @return
	 */
	public static Session getInstance(){
		if(null==session){
			session = new Session();
			loadDefault();
		}
		
		return session;
	}
	
	private static void loadDefault() {
		load("session_default.pieps");
	}
	
	public static void newSession() {
		loadDefault();
		Session.setPath("");
	}

	/**
	 * initFromFile
	 * @param path
	 */
	public static void load(String path){
		session = null;
		
		try {
			JAXBContext context = JAXBContext.newInstance(Session.class);
			Unmarshaller um = context.createUnmarshaller();
			session = (Session) um.unmarshal(new FileReader(path));
		}
		catch(Exception e){
			e.printStackTrace();
			session = new Session();
		}
		
		Session.buildEventTable();
	}
	
	/**
	 * save
	 * @throws Exception
	 */
	public static void save() throws Exception {
		Session.saveAs(Session.getPath());
	}
	
	/**
	 * saveToFile
	 */
	public static void saveAs(String path) throws Exception{
		Session.setPath(path);
		
		JAXBContext context = JAXBContext.newInstance(Session.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		Writer w= new FileWriter(path);
		m.marshal(session, w);
		w.close();
	}
	
	/**
	 * Fills the the event table according to the configuration
	 */
	public static void buildEventTable(){
		getInstance().eventTable = new SessionEventTable();
		
		/* Number of increments */
		int numIncrements = (int) Math.ceil((getInstance().terminalVelocity-getInstance().initVelocity)/getInstance().incrVelocity);
		/* Number of levels per section */
		int numLevels = (int) Math.floor(getInstance().levelLength/getInstance().sectionLength);
		
		double curVelocity = getInstance().initVelocity;
		double nextIncTime = 0;
		
		System.out.println("BUILDING EVENT TABLE:\ntime\tevent\tcomment");
		
		/* Loop through all events */
		for(int incPos = 0; incPos<numIncrements; incPos++){
			curVelocity = getInstance().initVelocity+incPos*getInstance().incrVelocity;
			
			getInstance().eventTable.addEvent(nextIncTime, SessionEventType.SECTION);
			
			System.out.println(Math.round(nextIncTime*10)/10+"\t"+"SECTION\t"+curVelocity+"m/s");
			
			for(int levPos=1; levPos<numLevels; levPos++){
				getInstance().eventTable.addEvent(nextIncTime+levPos*getInstance().sectionLength/curVelocity, SessionEventType.LEVEL);
				System.out.println(Math.round((nextIncTime+levPos*getInstance().sectionLength/curVelocity)*10)/10+"\t"+"LEVEL");
			}
			
			nextIncTime += getInstance().levelLength/curVelocity;
		}
		
		getInstance().eventTable.addEvent(nextIncTime, SessionEventType.END);
		System.out.println(Math.round(nextIncTime*10)/10+"\t"+"END");
		
	}
	
	/**
	 * getEventTable
	 */
	static public SessionEventTable getEventTable(){
		return getInstance().eventTable;
	}
	
	/** 
	 * getEndTime
	 */
	public static double getEndTime(){
		return getInstance().eventTable.getEndTime();
	}
	
	public static SessionEventType getNextEvent(){
		return getInstance().eventTable.getNextEvent(System.currentTimeMillis()/1E3-Session.getStartTime());
	}
	
	/** 
	 * getTimeToNextEvent
	 * @return
	 */
	public static double getTimeToNextEvent(){
		return getInstance().eventTable.getTimeToNextEvent(System.currentTimeMillis()/1E3-Session.getStartTime());
	}
	
	/** 
	 * getTimeToNextEvent
	 * @return
	 */
	public static double getTimeToNextEvent(SessionEventType eventType){
		return getInstance().eventTable.getTimeToNextEvent(System.currentTimeMillis()/1E3-Session.getStartTime(), eventType);
	}
	
	/**
	 * start();
	 * @return
	 */
	public static void start(){
		if(getInstance().running){
			System.err.println("Session allready running: Can not start a new one");
			return;
		}
		
		getInstance().running  = true;
		getInstance().sessionThread = new SessionThread(getEventTable());
		getInstance().thread  = new Thread(getInstance().sessionThread);
		getInstance().thread.start();
	}
	
	public static void stop(){
		if(!getInstance().running){
			System.out.println("Session not running: Nothing to stop");
			return;
		}
		
		getInstance().thread.interrupt();
		getInstance().running = false;
		
		Session.setStartTime(Double.NaN);
	}
	
	public static void hasStopped(){
		getInstance().running = false;
	}
	

	public static double getLevelLength() {
		return getInstance().levelLength;
	}

	public static void setLevelLength(double levelLength) {
		getInstance().levelLength = levelLength;
	}

	public static double getSectionLength() {
		return getInstance().sectionLength;
	}

	public static void setSectionLength(double sectionLength) {
		getInstance().sectionLength = sectionLength;
	}

	public static double getInitVelocity() {
		return getInstance().initVelocity;
	}
	
	public static double getInitVelocityKmH() {
		return getInstance().initVelocity*3.6;
	}

	public static void setInitVelocity(double initVelocity) {
		getInstance().initVelocity = initVelocity;
	}
	
	public static void setInitVelocityKmH(double initVelocity) {
		getInstance().initVelocity = initVelocity/3.6;
	}

	public static double getIncrVelocity() {
		return getInstance().incrVelocity;
	}
	
	public static double getIncrVelocityKmH() {
		return getInstance().incrVelocity*3.6;
	}

	public static void setIncrVelocity(double incrVelocity) {
		getInstance().incrVelocity = incrVelocity;
	}
	
	public static void setIncrVelocityKmH(double incrVelocity) {
		getInstance().incrVelocity = incrVelocity/3.6;
	}

	public static double getTerminalVelocity() {
		return getInstance().terminalVelocity;
	}
	
	public static double getTerminalVelocityKmH() {
		return getInstance().terminalVelocity*3.6;
	}
	
	public static void setTerminalVelocity(double terminalVelocity) {
		getInstance().terminalVelocity = terminalVelocity;
	}
	public static void setTerminalVelocityKmH(double terminalVelocity) {
		getInstance().terminalVelocity = terminalVelocity/3.6;
	}
		
	
	public static String getPath() {
		return getInstance().path;
	}

	public static void setPath(String path) {
		getInstance().path = path;
	}

	public static double getStartTime() {
		return getInstance().startTime;
	}
	
	public static void setStartTime(double startTime) {
		getInstance().startTime = startTime;
	}

	public static boolean isRunning() {
		return getInstance().running;
	}
	
	

}
