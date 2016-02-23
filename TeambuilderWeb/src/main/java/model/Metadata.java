package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


public class Metadata<T> extends HashSet<MetaPokemon> {
	public HashMap<String, CounterInformation> getCountersFor(String pokemon){
		Iterator<MetaPokemon> it = this.iterator();
		HashMap<String, CounterInformation> counter = new HashMap<String, CounterInformation>();
		while(it.hasNext()){
			MetaPokemon currentPokemon = it.next();
			if(currentPokemon.getName().trim().equals(pokemon)){
				counter = currentPokemon.getCounters();
			}
		}
		return counter;
	}
}
