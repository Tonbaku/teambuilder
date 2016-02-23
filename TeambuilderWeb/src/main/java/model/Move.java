package model;

import java.util.Set;

import model.enums.MoveProperty;
import model.enums.SingleType;
import model.enums.Spectrum;

public class Move {
	private String name;
	private int basepower;
	private SingleType type;
	private Set<MoveProperty> properties;
	private int priorityLevel;
	private Spectrum spectrum;
	
	public int getBasePower() {
		return basepower;
	}
	
	public void setBasePower(int bp){
		this.basepower = bp;
	}

	public Spectrum getSpectrum() {
		return spectrum;
	}

	public void setSpectrum(Spectrum spectrum) {
		this.spectrum = spectrum;
	}
	
	public void setSpectrum(String spectrum) {
		for(Spectrum s : Spectrum.values()){
			if(s.toString().equalsIgnoreCase(spectrum)){
				this.spectrum = s;
			}
		}
	}

	public SingleType getType() {
		return type;
	}

	public void setType(SingleType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<MoveProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<MoveProperty> properties) {
		this.properties = properties;
	}
}
