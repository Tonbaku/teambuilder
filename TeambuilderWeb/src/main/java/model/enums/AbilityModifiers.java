package model.enums;

import com.google.common.collect.ImmutableMap;


public enum AbilityModifiers {
	DEFAULT(1.5, null, null, null, 1, false),
	//stab changes
	ADAPTABILITY(2, null, null, null, 1, false),
	PROTEAN(1.5, null, null, null, 1, false),
	
	//Typemodifiers (pain in the arse)
	PIXILATE(1.5, null, ImmutableMap.of(SingleType.NORMAL, 1.33), ImmutableMap.of(SingleType.NORMAL, SingleType.FAIRY), 1, false),
	AERILATE(1.5, null, ImmutableMap.of(SingleType.NORMAL, 1.33), ImmutableMap.of(SingleType.NORMAL, SingleType.FLYING), 1, false),
	REFRIGERATE(1.5, null, ImmutableMap.of(SingleType.NORMAL, 1.33), ImmutableMap.of(SingleType.NORMAL, SingleType.ICE), 1, false),
	
	//Attack modifiers
	PUREPOWER(1.5, null, null, null, 2, false),
	HUGEPOWER(1.5, null, null, null, 2, false),
	
	//Weather modifiers
	//TODO
	SANDFORCE(1.5, null, null, null, 1, false),
	//TODO
	SOLARPOWER(1.5, null, null, null, 1, false),
	
	//physical moves 1.5
	//TODO
	HUSTLE(1.5, null, null, null, 1, false),
	
	SHEERFORCE(1.5, ImmutableMap.of(MoveProperty.SIDEEFFECT, 1.33), null, null, 1, false),
	
	STRONGJAW(1.5, ImmutableMap.of(MoveProperty.BITE, 1.5), null, null, 1, false),
	
	IRONFIST(1.5, ImmutableMap.of(MoveProperty.PUNCH, 1.2), null, null, 1, false),
	
	TOUGHCLAWS(1.5, ImmutableMap.of(MoveProperty.CONTACT, 1.33), null, null, 1, false),

	RECKLESS(1.5, ImmutableMap.of(MoveProperty.RECOIL, 1.2), null, null, 1, false),
	//Modifier for basepower < 61
	//TODO
	TECHNICIAN(1.5, null, null, null, 1, false),
	
	//Modifier for resisted moves
	//TODO
	TINTEDLENS(1.5, null, null, null, 1, false),
	
	//Pinch moves (same as Typemodifiers)
	SWARM(1.5, null, ImmutableMap.of(SingleType.BUG, 1.5), null, 1, true),
	TORRENT(1.5, null, ImmutableMap.of(SingleType.WATER, 1.5), null, 1, true),
	BLAZE(1.5, null,ImmutableMap.of(SingleType.FIRE, 1.5), null, 1, true),
	OVERGROW(1.5, null, ImmutableMap.of(SingleType.GRASS, 1.5), null, 1, true),
	
	MEGALAUNCHER(1.5, ImmutableMap.of(MoveProperty.LAUNCH, 1.5), null, null, 1, false),
	
	//general 1.5 boost
	//TODO
	PARENTALBOND(1.5, null, null, null, 1, false),

	//situational boost 1.5 for 
	//TODO
	GUTS(1.5, null, null, null, 1, false);
	
	private double stab;
	private ImmutableMap<MoveProperty, Double> propertieModifier;
	private ImmutableMap<SingleType, Double> typeModifier; 
	private ImmutableMap<SingleType, SingleType> manipulatedTypes;
	private double attackModifier; 
	private boolean pinchCoupled;
	
	private AbilityModifiers(
			double stab, 
			ImmutableMap<MoveProperty, Double> propertieModifier, 
			ImmutableMap<SingleType, Double> typeModifier, 
			ImmutableMap<SingleType, SingleType> manipulatedTypes,
			double attackModifier, 
			boolean pinchCoupled)
	{
		this.stab = stab;
		this.propertieModifier = propertieModifier;
		this.typeModifier = typeModifier;
		this.setManipulatedTypes(manipulatedTypes);
		this.attackModifier = attackModifier;
		this.pinchCoupled = pinchCoupled;
	}

	public double getStab() {
		return stab;
	}

	public void setStab(double stab) {
		this.stab = stab;
	}

	public ImmutableMap<MoveProperty, Double> getPropertieModifier() {
		return propertieModifier;
	}

	public void setPropertieModifier(ImmutableMap<MoveProperty, Double> propertieModifier) {
		this.propertieModifier = propertieModifier;
	}

	public ImmutableMap<SingleType, Double> getTypeModifier() {
		return typeModifier;
	}

	public void setTypeModifier(ImmutableMap<SingleType, Double> typeModifier) {
		this.typeModifier = typeModifier;
	}

	public double getAttackModifier() {
		return attackModifier;
	}

	public void setAttackModifier(double attackModifier) {
		this.attackModifier = attackModifier;
	}

	public boolean isPinchCoupled() {
		return pinchCoupled;
	}

	public void setPinchCoupled(boolean pinchCoupled) {
		this.pinchCoupled = pinchCoupled;
	}

	public ImmutableMap<SingleType, SingleType> getManipulatedTypes() {
		return manipulatedTypes;
	}

	public void setManipulatedTypes(ImmutableMap<SingleType, SingleType> manipulatedTypes) {
		this.manipulatedTypes = manipulatedTypes;
	}
}
