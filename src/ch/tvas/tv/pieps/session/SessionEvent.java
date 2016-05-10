package ch.tvas.tv.pieps.session;

public class SessionEvent {
	double time;
	SessionEventType event;
	
	public SessionEvent(double time, SessionEventType event){
		this.time = time;
		this.event = event;
	}
	
	public double getTime(){
		return time;
	}
	
	public SessionEventType getEvent(){
		return event;
	}
}
