package model;

import java.util.List;
import java.util.Set;

import model.enums.AbilityModifiers;


public class Pokemon {
	private int number;
	private String name;
	private List<Stat> stats;
	private Type type;
	private Ability currentAbility;
	private AbilityModifiers abilityMods;
	private Set<Ability> abilities;
	private List<Move> moves;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Stat> getStats() {
		return stats;
	}
	public void setStats(List<Stat> stats) {
		this.stats = stats;
	}
	public Stat getStat(String name) {
		for(Stat s : stats){
			if(s.getName().equals(name)){
				return s;
			}
		}
		return null;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public void setAbilities(Set<Ability> abilities){
		this.abilities = abilities;
	}
	public Set<Ability> getAbilities(){
		return this.abilities;
	}
	public void setAbility(String ability) {
		for(Ability a : abilities){
			if(a.getName().equals(ability)){
				this.currentAbility = a;
				this.abilityMods = null;
			}
		}
	}
	public void setAbility(Ability ability) {
		for(Ability a : abilities){
			if(a.equals(ability)){
				this.currentAbility = a;
				this.abilityMods = null;
			}
		}
	}
	public String getAbility() {
		return this.currentAbility.getName();
	}
	public List<Move> getMoves() {
		return moves;
	}
	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	public AbilityModifiers getAbilityMods() {
		return abilityMods;
	}
	public void setAbilityMods(AbilityModifiers abilityMods) {
		this.abilityMods = abilityMods;
	}
}
