package dev.kezuk.flodoria.enums;

public enum DelayerState {
	
	LOW(40, 2),
	NORMAL(80, 4),
	HIGHER(120, 6);
	
	public long litre;
	public int time;
	
	DelayerState(long litre, int time) {
		this.litre = litre;
		this.time = time;
	}
	
	public long getLitre() {
		return litre;
	}
	
	public int getTime() {
		return time;
	}

}
