package model;

import java.util.HashMap;

import model.enums.Spectrum;

public class EffectiveHealthRating extends Rating {
	private HashMap<Spectrum, Double> effectiveHealthValues;
	private HashMap<Spectrum, Double> evioliteHealthValues;
	
	public EffectiveHealthRating(){
		this.effectiveHealthValues = new HashMap<Spectrum, Double>();
		this.evioliteHealthValues = new HashMap<Spectrum, Double>();
	}
	
	@Override
	public String getRateText(){
		return this.getPokemon() + ":\n Physical: " + this.effectiveHealthValues.get(Spectrum.PHYSICAL) + " Special: " + this.effectiveHealthValues.get(Spectrum.SPECIAL)
		+ "\n EviolitePhysical: " + this.evioliteHealthValues.get(Spectrum.PHYSICAL) + " EvioliteSpecial: " + this.evioliteHealthValues.get(Spectrum.SPECIAL) + "\n";
	}

	public HashMap<Spectrum, Double> getEffectiveHealthValues() {
		return effectiveHealthValues;
	}

	public void setEffectiveHealthValues(HashMap<Spectrum, Double> effectiveHealthValues) {
		this.effectiveHealthValues = effectiveHealthValues;
	}
	
	public void addEffectiveHealthValue(Spectrum s, Double value){
		if(this.effectiveHealthValues.containsKey(s)){
			this.effectiveHealthValues.replace(s, value);
		} else {
			this.effectiveHealthValues.put(s, value);
		}
	}

	public void addEvioliteHealthValue(Spectrum s, Double value){
		if(this.evioliteHealthValues.containsKey(s)){
			this.evioliteHealthValues.replace(s, value);
		} else {
			this.evioliteHealthValues.put(s, value);
		}
	}
	
	public HashMap<Spectrum, Double> getEvioliteHealthValues() {
		return evioliteHealthValues;
	}

	public void setEvioliteHealthValues(HashMap<Spectrum, Double> evioliteHealthValues) {
		this.evioliteHealthValues = evioliteHealthValues;
	}
}
