package model;

public class Stat {
	protected String statname;
	protected int evs;
	protected int dvs;
	protected int base;
	
	public Stat(String name){
		this.statname = name;
		this.base = 0;
		this.evs = 0;
		this.dvs = 0;
	}

	public Stat(String name, int base) {
		this.statname = name;
		this.base = base;
		this.evs = 0;
		this.dvs = 0;
	}

	public Integer getBase() {
		return this.base;
	}
	
	public String getName(){
		return this.statname;
	}
	
	public int getRealValue(){
		int gesamtwert = 0;
		
		if("kp".equals(this.statname)){
			gesamtwert = 2 * this.base + dvs + (evs/4) + 110;
		} else {
			gesamtwert = 2 * this.base + dvs + (evs/4) + 5; 
		}
		return gesamtwert;
	}
	
	/*
	 * convenience method for dividing stats with each other
	 */
	public double divide(Stat defensive){
		return this.getRealValue()/defensive.getRealValue();
	}

	public int getMaxValue() {
		int gesamtwert = 0;
		
		if("kp".equals(this.statname)){
			gesamtwert = 2 * this.base + 204;
		} else {
			gesamtwert = (int)Math.floor((2 * this.base + 99) * 1.1); 
		}
		return gesamtwert;
	}
}
