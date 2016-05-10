package ch.tvas.tv.pieps.sounds;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Tone {
	@XmlElement
	private int frequency;
	@XmlElement
	private int duration;
	@XmlElement
	private int count;
	
	public Tone(){
	}
	
	public Tone(int frequency, int duration, int count){
		this.frequency = frequency;
		this.duration  = duration;
		this.count     = count;
	}
	
	public Tone(int frequency, int duration){
		this.frequency = frequency;
		this.duration  = duration;
		this.count     = 1;
	}
	

	/**
	 * @return the frequency
	 */
	public int getFrequency() {
		return frequency;
	}
	/**
	 * @param frequency the frequency to set
	 */
	@XmlTransient
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	@XmlTransient
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	@XmlTransient
	public void setCount(int count) {
		this.count = count;
	}

}
