package model;

public class ShedinjaStats extends Stat {

	public ShedinjaStats(String name, int base) {
		super(name, base);
	}

	public ShedinjaStats(String name) {
		super(name);
	}
	
	@Override
	public int getRealValue(){
		int gesamtwert = 0;
		
		if("kp".equals(this.statname)){
			//SHITTY SPECIAL HANDLING FOR SHEDINJAS FRIGGIN >>1HP-STAT<<
			gesamtwert = 1;
		} else {
			gesamtwert = 2 * this.base + dvs + (evs/4) + 5; 
		}
		return gesamtwert;
	}

	@Override
	public int getMaxValue() {
		int gesamtwert = 0;
		
		if("kp".equals(this.statname)){
			gesamtwert = 1;
		} else {
			gesamtwert = (int)Math.floor((2 * this.base + 99) * 1.1); 
		}
		return gesamtwert;
	}
}
