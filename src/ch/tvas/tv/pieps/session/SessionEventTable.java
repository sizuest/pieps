package ch.tvas.tv.pieps.session;

import java.util.ArrayList;

public class SessionEventTable {
	ArrayList<SessionEvent> events;
	
	public SessionEventTable(){
		events = new ArrayList<SessionEvent>();
	}
	
	/**
	 * addEvent
	 * 
	 * @param time
	 * @param event
	 */
	public void addEvent(double time, SessionEventType event){
		addEvent(new SessionEvent(time, event));
	}
	
	/**
	 * addEvent
	 */
	public void addEvent(SessionEvent event){
		for(int i=0; i<events.size(); i++)
			if(events.get(i).getTime()>event.getTime()){
				events.add(i, event);
				return;
			}
		
		events.add(event);
	}
	
	/**
	 * getTimoOfNextEvent
	 * 
	 * @param now
	 * @return
	 */
	public double getTimeOfNextEvent(double now){
		int idx = getIndexOfNextEvent(now);
		
		if(idx<0)
			return Double.NaN;
		else
			return events.get(idx).getTime();
	}

	/**
	 * getTimeToNextEvent
	 * 
	 * @param now
	 * @return
	 */
	public double getTimeToNextEvent(double now){
		return getTimeOfNextEvent(now)-now;
	}
	
	/**
	 * getNextEvent
	 * 
	 * @param now
	 * @return
	 */
	public SessionEventType getNextEvent(double now){
		int idx = getIndexOfNextEvent(now);
		
		if(idx<0)
			return null;
		else
			return events.get(idx).getEvent();
	}

	/**
	 * getEndTime
	 * @return [s]
	 */
	public double getEndTime() {
		if(events.size()==0)
			return Double.NaN;
		return events.get(events.size()-1).getTime();
	}

	/**
	 * getTimeToNextEvent
	 * @param now
	 * @param eventType
	 * @return
	 */
	public double getTimeToNextEvent(double now, SessionEventType eventType) {
		return getTimeOfNextEvent(now, eventType)-now;
	}
	
	/**
	 * getTimeOfNextEvent
	 * @param now
	 * @param eventType
	 * @return
	 */
	public double getTimeOfNextEvent(double now, SessionEventType eventType) {
		int idx = getIndexOfNextEvent(now, eventType);
		
		if(idx<0)
			return Double.NaN;
		else
			return events.get(idx).getTime();
	}
	
	private int getIndexOfNextEvent(double now){
		for(int i=0; i<events.size(); i++){
			if(events.get(i).getTime()>now)
				return i;
		}
		
		return -1;
	}
	
	private int getIndexOfNextEvent(double now, SessionEventType event){		
		for(int i=0; i<events.size(); i++){
			if(events.get(i).getTime()>now & events.get(i).getEvent().equals(event))
				return i;
		}
		
		return -1;
	}
}
