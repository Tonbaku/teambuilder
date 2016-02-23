package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import control.LiteAccess;
import model.enums.SingleType;

public class Type extends HashSet<SingleType>{
	private static final long serialVersionUID = 667940131534780356L;
	private final int maxEntries = 2;
	private LiteAccess lacc;
	private List<Integer> weaknesses;
	private List<Integer> doubleWeaknesses;
	private List<Integer> resistances;
	private List<Integer> doubleResistances;
	
	
	public Type(SingleType st1, SingleType st2){
		super.add(st1);
		super.add(st2);
		weaknesses = new ArrayList<Integer>();
		doubleWeaknesses = new ArrayList<Integer>();
		resistances = new ArrayList<Integer>();
		doubleResistances = new ArrayList<Integer>();
	}
	
	@Override
	public boolean add(SingleType st){
		if(this.size()<maxEntries){
			return super.add(st);
		} else {
			return false;
		}
	}
	
	
	
	
	
	/**
	 * matches the Weaknesses of this type against his Resistences
	 * @return
	 */
	public void matchRelations(){
		if(this.size()==2){
			Iterator<Integer> it = this.weaknesses.iterator();
			while(it.hasNext()){
				int weak = it.next();
				if(this.resistances.contains(weak)){
					this.resistances.remove((Integer)weak);
					it.remove();
				}
			}
		}
	}

	public double getEffectiveness(Move m){
		return getEffectiveness(m.getType());
	}

	public double getEffectiveness(SingleType t){
		if(this.getWeaknesses().contains(t))
			return 2;
		if(this.getDoubleWeaknesses().contains(t))
			return 4;
		if(this.getResistances().contains(t))
			return 0.5;
		if(this.getDoubleResistances().contains(t))
			return 0.25;
		return 1;
	}
	
	public LiteAccess getLacc() {
		return lacc;
	}

	public void setLacc(LiteAccess lacc) {
		this.lacc = lacc;
	}

	public List<Integer> getDoubleResistances() {
		return doubleResistances;
	}

	public void setDoubleResistances(List<Integer> doubleResistances) {
		this.doubleResistances = doubleResistances;
	}

	public List<Integer> getDoubleWeaknesses() {
		return doubleWeaknesses;
	}

	public void setDoubleWeaknesses(List<Integer> doubleWeaknesses) {
		this.doubleWeaknesses = doubleWeaknesses;
	}

	public List<Integer> getResistances() {
		return resistances;
	}

	public void setResistances(List<Integer> resistances) {
		this.resistances = resistances;
	}

	public List<Integer> getWeaknesses() {
		return weaknesses;
	}

	public void setWeaknesses(List<Integer> weaknesses) {
		this.weaknesses = weaknesses;
	}
	
	public String asString(){
		String ret = "";
		for(SingleType st : this){
			ret += st.getName() + " ";
		}
		return ret;
	}
}
