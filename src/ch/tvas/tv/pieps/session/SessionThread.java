package ch.tvas.tv.pieps.session;

import ch.tvas.tv.pieps.sounds.Player;

public class SessionThread implements Runnable{
	private SessionEventTable eventTable;
	volatile private Boolean stop = false;
	
	/**
	 * SessionRun 
	 */
	public SessionThread(SessionEventTable eventTable){
		this.eventTable = eventTable;
	}
	
	/**
	 * terminate
	 */
	public void terminate(){
		stop = true;
		System.out.println("Terminating session run");
	}
	
	/**
	 * start
	 * @throws InterruptedException 
	 */
	public void run(){
		
		if(null==eventTable){
			System.err.println("SessionThread: No event table defined!");
			return;
		}
		
		// Play start sound
		try {
			Player.playTone(SessionEventType.START);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		SessionEventType nextEvent = eventTable.getNextEvent(0); 
		// Set new start time
		Session.setStartTime(System.currentTimeMillis()/1E3);
		// Loop over all event
		while(null!=nextEvent & !stop){
			try {
				Player.playTone(nextEvent);
			
				Thread.sleep(100);
				nextEvent = Session.getNextEvent();
				
				double sleepTime = Session.getTimeToNextEvent();
				if(Double.isNaN(sleepTime))
					break;
				
				Thread.sleep((long) (sleepTime*1000));
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}	
		
		Session.setStartTime(Double.NaN);
		Session.hasStopped();
	}
	
	/**
	 * getTime
	 * 
	 * time [s] since start of run
	 * @return
	 */
	public double getTime(){
		double time = System.currentTimeMillis()/1E3-Session.getStartTime();
		return time;
	}
}
