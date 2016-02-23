package model;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import control.MoveFactory;
import control.PokemonFactory;

public class Team extends LinkedList<Pokemon> {
	private final int maxMembers = 6;
	private PokemonFactory pf;
	private MoveFactory mf;
	
	public Team(PokemonFactory pf, MoveFactory mf){
		super();
		this.pf = pf;
		this.mf = mf;
		Pokemon defaultPokemon = pf.createPokemon("Keldeo");
		List<Move> defaultSet = mf.createDefaultMoveSet();
		defaultPokemon.setMoves(defaultSet);
		this.add(defaultPokemon);
	}
	
	@Override
	public boolean add(Pokemon p){
		if(this.contains(p)){
			return false;
		}
		if(this.size()<this.maxMembers){
			return super.add(p);
		} else {
			System.out.println("Tried to add a member to a full Team");
			return false;
		}
		
	}
	
	public boolean replaceMember(Pokemon old, Pokemon replacement){
		
		for(Pokemon p : this){
			if(p.equals(old)){
				int pos = this.indexOf(p);
				this.add(pos, replacement);
				this.remove(pos+1);
				return true;
			}
		}
		return false;
	}
}
