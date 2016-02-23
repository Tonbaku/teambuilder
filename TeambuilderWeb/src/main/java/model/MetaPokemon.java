package model;

import java.util.HashMap;

public class MetaPokemon {
	private String name;
	private HashMap<String, Double> moves;
	private HashMap<String, Double> abilities;
	private HashMap<String, Double> items;
	private HashMap<String, Double> mates;
	private HashMap<String, CounterInformation> counters;
	private HashMap<String, HashMap<String[], Double>> spreads;
	
	public MetaPokemon(){
		name = new String();
		moves = new HashMap<String, Double>();
		abilities = new HashMap<String, Double>();
		items = new HashMap<String, Double>();
		mates = new HashMap<String, Double>();
		counters = new HashMap<String, CounterInformation>();
		spreads = new HashMap<String, HashMap<String[], Double>>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public HashMap<String, Double> getMoves() {
		return moves;
	}
	
	public void setMoves(HashMap<String, Double> moves) {
		this.moves = moves;
	}
	
	public HashMap<String, Double> getAbilities() {
		return abilities;
	}
	
	public void setAbilities(HashMap<String, Double> abilities) {
		this.abilities = abilities;
	}
	
	public HashMap<String, Double> getItems() {
		return items;
	}
	
	public void setItems(HashMap<String, Double> items) {
		this.items = items;
	}
	
	public HashMap<String, Double> getMates() {
		return mates;
	}
	
	public void setMates(HashMap<String, Double> mates) {
		this.mates = mates;
	}
	
	public HashMap<String, CounterInformation> getCounters() {
		return counters;
	}
	
	public void setCounters(HashMap<String, CounterInformation> counters) {
		this.counters = counters;
	}

	public HashMap<String, HashMap<String[], Double>> getSpreads() {
		return spreads;
	}

	public void setSpreads(HashMap<String, HashMap<String[], Double>> spreads) {
		this.spreads = spreads;
	}
}
