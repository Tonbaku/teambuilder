package control;

import java.util.Comparator;
import model.Pokemon;
import model.Stat;

public class SpeedComparator<T> implements Comparator<Pokemon> {
	
	public int compare(Pokemon p1, Pokemon p2) {
		Stat speed1 = p1.getStat("spd");
		Stat speed2 = p2.getStat("spd");
		if(speed1 != null && speed2 != null){
			if(speed1.getRealValue()>speed2.getRealValue()){
				return 1;
			} else if(speed1.getRealValue()<speed2.getRealValue()){
				return -1;
			}
		}
		return 0;
	}
	
	

}
