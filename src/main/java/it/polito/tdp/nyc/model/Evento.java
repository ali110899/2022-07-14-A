package it.polito.tdp.nyc.model;

public class Evento implements Comparable<Evento> {

	public enum EventType {
		
		SHARE,
		STOP
	}
	
	private EventType type;
	private int time;
	private NTA nta;
	private int durata;
	
	public Evento(EventType type, int time, NTA nta, int durata) {
		super();
		this.type = type;
		this.time = time;
		this.nta = nta;
		this.durata = durata;
	}

	public EventType getType() {
		return type;
	}

	public int getTime() {
		return time;
	}

	public NTA getNta() {
		return nta;
	}

	public int getDurata() {
		return durata;
	}

	@Override
	public int compareTo(Evento o) {
		return this.time-o.time;
	}
	
	
	
}
