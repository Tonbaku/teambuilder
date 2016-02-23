package view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import control.DamageResolver;
import control.LiteAccess;
import control.MoveFactory;
import control.PokemonFactory;
import model.Ability;
import model.Move;
import model.Pokemon;
import model.Rating;
import model.Stat;
import model.Team;

public class TeamBB {
	private Team actualTeam;
	private Pokemon currentMember;
	private LiteAccess lacc;
	private PokemonFactory pokemonFactory;
	private MoveFactory moveFactory;
	private DamageResolver damRes;
	
	public TeamBB(){
		
	}
	
	@PostConstruct
	public void init(){
		this.actualTeam = new Team(pokemonFactory, moveFactory);
		if(currentMember == null){
			currentMember = actualTeam.iterator().next();
		}
	}
	
	public void onNameChanged(){
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, String> parameterMap = (Map<String, String>) ctx.getRequestParameterMap();
		String param = parameterMap.get("inputPokemon");
		if(param != null){
			String newName = param;
			if(lacc.doesPokemonExist(newName)){
				if(currentMember == null){
					currentMember = actualTeam.iterator().next();
				}
				actualTeam.replaceMember(currentMember, pokemonFactory.createPokemon(newName));
				currentMember = actualTeam.iterator().next();
			}
		}
	}
	
	public void onAbilityChanged(ValueChangeEvent vce){
		this.currentMember.setAbility((String)vce.getNewValue());
	}
	
	public HashMap<Move, Rating> rateMovePresence(){
		return this.damRes.rateMovePresence(currentMember);
	}
	
	public List<Stat> getCurrentStats(){
		return this.currentMember.getStats();
	}
	
	public List<Move> getCurrentMoves(){
		return this.currentMember.getMoves();
	}
	
	public Integer getStat(String stat){
		for(Stat entry : this.currentMember.getStats()){
			if(entry.getName().equals(stat)){
				return entry.getBase();
			}
		}
		return 0;
	}
	
	public Team getTeam(){
		return this.actualTeam;
	}
	
	public String getName(){
		return this.currentMember.getName();
	}
	
	public void setName(String name){
		this.currentMember.setName(name);
	}
	
	public LiteAccess getLacc() {
		return lacc;
	}
	public void setLacc(LiteAccess lacc) {
		this.lacc = lacc;
	}
	
	public Set<Ability> getAbilities(){
		return this.currentMember.getAbilities();
	}
	
	public void setAbility(String ability){
		this.currentMember.setAbility(ability);
	}
	
	public String getAbility(){
		return this.currentMember.getAbility();
	}

	public PokemonFactory getPokemonFactory() {
		return pokemonFactory;
	}

	public void setPokemonFactory(PokemonFactory pokemonFactory) {
		this.pokemonFactory = pokemonFactory;
	}
	
	public MoveFactory getMoveFactory() {
		return moveFactory;
	}

	public void setMoveFactory(MoveFactory moveFactory) {
		this.moveFactory = moveFactory;
	}

	public Pokemon getCurrentMember() {
		return currentMember;
	}

	public void setCurrentMember(Pokemon currentMember) {
		this.currentMember = currentMember;
	}

	public DamageResolver getDamageResolver() {
		return damRes;
	}

	public void setDamageResolver(DamageResolver damRes) {
		this.damRes = damRes;
	}
}
