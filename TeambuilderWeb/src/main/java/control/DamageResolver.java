package control;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableMap;

import model.EffectiveHealthRating;
import model.Move;
import model.Pokemon;
import model.Rating;
import model.Stat;
import model.Type;
import model.enums.AbilityModifiers;
import model.enums.MoveProperty;
import model.enums.SingleType;
import model.enums.Spectrum;
import model.enums.Weather;
import model.enums.WeatherInducers;

public class DamageResolver {
	private static final int OFFENSIVE = 0;
	private static final int DEFENSIVE = 1;
	
	//pseudo caching to boost performance a bit
	private Pokemon lastPokemon;
	private Stat physicalAttack;
	private Stat specialAttack;
	private Weather currentWeather;
	
	private Move currentMove;
	private Pokemon offensive;
	private Pokemon defensive;
	
	public HashMap<Move, Rating> rateMovePresence(Pokemon p){
		HashMap<Move, Rating> ret = new HashMap<Move, Rating>();
		this.offensive = p;
		for(Move m : p.getMoves()){
			Rating r = new Rating();
			this.currentMove = m;
			r.setRateValue(this.resolveVacuumDamage());
			ret.put(m, r);
		}
		return ret;
	}
	
	public Rating rateMovePresence(Pokemon p, Move m){
		if(p!=null && !p.equals(lastPokemon)){
			this.lastPokemon = p;
			this.physicalAttack = null;
			this.specialAttack = null;
			this.offensive = p;
		}
		this.currentMove = m;
		Rating ret = new Rating();
		ret.setMove(m.getName());
		ret.setPokemon(p.getName());
		ret.setRateValue(this.resolveVacuumDamage());
		ret.setAbility(p.getAbility());
		return ret;
	}
	
	public int resolveDamage(Pokemon offensive, Pokemon defensive, Move move){
		this.offensive = offensive;
		this.defensive = defensive;
		this.currentMove = move;
		if(this.offensive.getAbilityMods()==null){
			this.offensive.setAbilityMods(resolveAbilityModifier());
		}
		AbilityModifiers am = this.offensive.getAbilityMods();
		double modifier = resolveModifier(am);
		int basepower = this.applyTechnician(am);
		
		Stat offensiveStat = this.getOffensiveStat(am.getAttackModifier());
		int offensiveValue = offensiveStat.getMaxValue();
		offensiveValue = this.manipulateByWeather(offensiveValue, offensiveStat.getName(), this.offensive);
		
		Stat defensiveStat = this.getDefensiveStat(1);
		int defensiveValue = defensiveStat.getMaxValue();
		defensiveValue = this.manipulateByWeather(defensiveValue, defensiveStat.getName(), this.defensive);
		
		return solveDamageFormula(offensiveValue, defensiveValue, basepower, modifier);
	}
	
	private int resolveVacuumDamage(){
		if(offensive.getAbilityMods()==null){
			offensive.setAbilityMods(resolveAbilityModifier());
		}
		AbilityModifiers am = offensive.getAbilityMods();
		double modifier = resolveModifier(false, am);
		int basepower = this.applyTechnician(am);
		Stat offensiveStat = this.getOffensiveStat(am.getAttackModifier());
		int statValue = offensiveStat.getMaxValue();
		statValue = this.manipulateByWeather(statValue, offensiveStat.getName(), offensive);
		return solveDamageFormula(statValue, 1, basepower, modifier);
	}
	
	private int applyTechnician(AbilityModifiers am){
		int basepower = this.currentMove.getBasePower();
		if(basepower<61){
			if(am.equals(AbilityModifiers.TECHNICIAN)){
				basepower *= 1.5; 
			}
		}
		return basepower;
	}
	
	private void applyWeather(){
		// Order by Speed
		List<Pokemon> speedOrder = new ArrayList<Pokemon>();
		if(offensive!=null){
			speedOrder.add(offensive);
		}
		if(defensive!=null){
			speedOrder.add(defensive);
		}
		speedOrder.sort(new SpeedComparator<Pokemon>());
		WeatherInducers inducer;
		try{
			if(speedOrder.size()>0){
				inducer = WeatherInducers.instantiate(speedOrder.get(0).getAbility());
			} else {
				inducer = WeatherInducers.CLEARSKY;
			}
		} catch(IllegalArgumentException ex){
			System.err.println(speedOrder.get(0).getAbility() + " is not a weather-inducing Ability.");
			try {
				if(speedOrder.size()>1){
					inducer = WeatherInducers.instantiate(speedOrder.get(1).getAbility());
				} else {
					inducer = WeatherInducers.CLEARSKY;
				}
			} catch(IllegalArgumentException e){
				inducer = WeatherInducers.CLEARSKY;
			}
		}
		this.currentWeather = inducer.getInducedWeather();
		
		
	}
	
	private double applyWeatherEffects(double other){
		ImmutableMap<SingleType, Double> amplifications = this.currentWeather.getAmplifiedDamage();
		ImmutableMap<SingleType, Double> reductions = this.currentWeather.getReducedDamage();
		SingleType moveType = currentMove.getType();
		if(amplifications!=null && amplifications.containsKey(moveType)){
			other *= amplifications.get(moveType);
		}
		if(reductions!=null && reductions.containsKey(moveType)){
			other *= reductions.get(moveType);
		}
		return other;
	}
	
	private int manipulateByWeather(int realValue, String statname, Pokemon p){
		int ret = realValue;
		ImmutableMap<SingleType, ImmutableMap<String, Double>> manipulations = this.currentWeather.getManipulatedTypes();
		for(SingleType type : p.getType()){
			if(manipulations!=null && manipulations.containsKey(type)){
				if(manipulations.get(type).containsKey(statname)){
					ret *= manipulations.get(type).get(statname);
				}
			}
		}
		return ret;
	}
	
	private AbilityModifiers resolveAbilityModifier(){
		AbilityModifiers ret = null;
		try {
			ret = AbilityModifiers.valueOf(offensive.getAbility().toUpperCase().replace(" ", ""));
		} catch(Exception e){
			//System.out.println(offensive.getAbility() + " has no special Handling. Fallback to default.");
			ret = AbilityModifiers.DEFAULT;
		}
		//System.out.println(this.class + ":Processing : " + offensive.getName() + " with " + offensive.getAbility());
		//Thread.dumpStack();
		return ret;
	}
	private double resolveModifier(AbilityModifiers am){
		return this.resolveModifier(true, am);
	}
	
	private double resolveModifier(boolean useRandom, AbilityModifiers am){
		//we do not want to modify the original moves values -> work with a local copy
		Move movebuffer = currentMove;
		double modifier = 1;
		double random;
		if(useRandom){
			random = 1.0 - Math.random() * 0.15;
		} else {
			random = 1;
		}
		
		//Set Weather
		this.applyWeather();
		
		double effectiveness;
		if(defensive == null){
			effectiveness = 1;
		} else {
			effectiveness = defensive.getType().getEffectiveness(movebuffer);
		}
		
		//TODO implement items etc.
		double other = 1; 
		System.out.println(am.name());
		for(MoveProperty prop : movebuffer.getProperties()){
			if(am.getPropertieModifier() != null && am.getPropertieModifier().containsKey(prop)){
				other *= am.getPropertieModifier().get(prop);
			}
		}
		
		if(am.getTypeModifier() != null && am.getTypeModifier().containsKey(movebuffer.getType())){
			if(!am.isPinchCoupled()){
				other *= am.getTypeModifier().get(movebuffer.getType());
				if(am.getManipulatedTypes().containsKey(movebuffer.getType())){
					movebuffer.setType(am.getManipulatedTypes().get(movebuffer.getType()));
				}
			}
		}
		
		other = applyWeatherEffects(other);
		
		modifier = random * effectiveness * other;
		double stab = 1.5;
		if(am.equals(AbilityModifiers.ADAPTABILITY)){
			stab = 2;
		}
		if(offensive.getType().contains(movebuffer.getType())){
			modifier *= stab;
		}
		return modifier;
	}
	
	
	
	public Stat getOffensiveStat(double attackModifier){
		return getStatForSpectrum(offensive.getStats(), currentMove.getSpectrum(), OFFENSIVE, attackModifier);
	}
	
	public Stat getDefensiveStat(double modifier){
		return getStatForSpectrum(defensive.getStats(), currentMove.getSpectrum(), DEFENSIVE, modifier);
	}
	
	private Stat getStatForSpectrum(List<Stat> stats, Spectrum s, int switchOffOrDef, double modifier){
		if(switchOffOrDef==OFFENSIVE){
			if(s.equals(Spectrum.PHYSICAL)){
				if(this.physicalAttack!=null){
					return this.physicalAttack;
				}
			}
			if(s.equals(Spectrum.SPECIAL)){
				if(this.specialAttack!=null){
					return this.specialAttack;
				}
			}
		}
		String lookFor = defineStatForSearch(s, switchOffOrDef);
		for(Stat stat : stats){
			if(stat.getName().equalsIgnoreCase(lookFor)){
				return stat;
			}
		}
		return stats.get(0);
	}
	
	private String defineStatForSearch(Spectrum s, int mode){
		if(mode==OFFENSIVE){
			if(s.equals(Spectrum.PHYSICAL)){
				return "atk";
			}
			if(s.equals(Spectrum.SPECIAL)){
				return "satk";
			}
		}
		if(mode==OFFENSIVE){
			if(s.equals(Spectrum.PHYSICAL)){
				return "def";
			}
			if(s.equals(Spectrum.SPECIAL)){
				return "sdef";
			}
		}
		return "";
	}
	
	public EffectiveHealthRating getEffectiveHealth(Pokemon p){
		offensive = null;
		defensive = p;
		EffectiveHealthRating ret = new EffectiveHealthRating();
		ret.setPokemon(p.getName());
		double kp = 0;
		int def = 0;
		int sdef = 0;
		for(Stat s : p.getStats()){
			if("kp".equals(s.getName()))
				kp = s.getMaxValue();
			if("def".equals(s.getName()))
				def = s.getMaxValue();
			if("sdef".equals(s.getName()))
				sdef = s.getMaxValue();
		}
		double buffer = 0;
		applyWeather();
		def = manipulateByWeather(def, "def", p);
		sdef = manipulateByWeather(sdef, "sdef", p);
		buffer = kp / solveDamageFormula(300, def, 100, 1);
		ret.addEffectiveHealthValue(Spectrum.PHYSICAL, buffer);
		System.out.println("Physical Side: " + buffer);
		buffer = kp / solveDamageFormula(300, sdef, 100, 1);
		ret.addEffectiveHealthValue(Spectrum.SPECIAL, buffer);
		System.out.println("Special Side: " + buffer);
		buffer = kp / solveDamageFormula(300, (int)Math.floor(def*1.5), 100, 1);
		ret.addEvioliteHealthValue(Spectrum.PHYSICAL, buffer);
		buffer = kp / solveDamageFormula(300, (int)Math.floor(sdef*1.5), 100, 1);
		ret.addEvioliteHealthValue(Spectrum.SPECIAL, buffer);
		
		
		return ret;
	}
	
	private int solveDamageFormula(int offensiveStat, int defensiveStat, int basepower, double modifier){
		return (int) Math.floor((0.84 * ((double)offensiveStat/(double)defensiveStat) * basepower + 2) * modifier);
	}
	
}
