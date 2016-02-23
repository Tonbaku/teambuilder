package model.enums;

import com.google.common.collect.ImmutableMap;

import model.Stat;

public enum Weather {
	CLEARSKY(null, null, null, 0),
	SUN(null, ImmutableMap.of(SingleType.FIRE, 1.5), ImmutableMap.of(SingleType.WATER, 0.5), 0),
	HAIL(null, null, null, 0.625),
	RAIN(null, ImmutableMap.of(SingleType.WATER, 1.5), ImmutableMap.of(SingleType.FIRE, 0.5), 0),
	SAND(ImmutableMap.of(SingleType.ROCK, ImmutableMap.of("sdef", 1.5)), null, null, 0.625);
	
	private ImmutableMap<SingleType, ImmutableMap<String, Double>> manipulatedTypes;
	private ImmutableMap<SingleType, Double> amplifiedDamage;
	private ImmutableMap<SingleType, Double> reducedDamage;
	private double residualDamage;
	
	
	private Weather(
			ImmutableMap<SingleType, ImmutableMap<String, Double>> manipulatedTypes,
			ImmutableMap<SingleType, Double> amplifiedDamage,
			ImmutableMap<SingleType, Double> reducedDamage,
			double residualDamage
			)
	{
		this.manipulatedTypes = manipulatedTypes;
		this.amplifiedDamage = amplifiedDamage;
		this.reducedDamage = reducedDamage;
		this.residualDamage = residualDamage;
	}


	public ImmutableMap<SingleType, ImmutableMap<String, Double>> getManipulatedTypes() {
		return manipulatedTypes;
	}


	public void setManipulatedTypes(ImmutableMap<SingleType, ImmutableMap<String, Double>> manipulatedTypes) {
		this.manipulatedTypes = manipulatedTypes;
	}


	public ImmutableMap<SingleType, Double> getAmplifiedDamage() {
		return amplifiedDamage;
	}


	public void setAmplifiedDamage(ImmutableMap<SingleType, Double> amplifiedDamage) {
		this.amplifiedDamage = amplifiedDamage;
	}


	public ImmutableMap<SingleType, Double> getReducedDamage() {
		return reducedDamage;
	}


	public void setReducedDamage(ImmutableMap<SingleType, Double> reducedDamage) {
		this.reducedDamage = reducedDamage;
	}


	public double getResidualDamage() {
		return residualDamage;
	}


	public void setResidualDamage(double residualDamage) {
		this.residualDamage = residualDamage;
	}
}
