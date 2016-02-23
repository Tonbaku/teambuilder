package model;

public class Rating {
	private String pokemon;
	private String move;
	private String ability;
	private String rateText;
	private double rateValue;
	
	public String getRateText() {
		if(this.rateText==null){
			this.rateText = this.pokemon + " - " + this.move + " - " + this.ability + " ~> " + this.rateValue;
		}
		return rateText;
	}
	public void setRateText(String rateText) {
		this.rateText = rateText;
	}
	public double getRateValue() {
		return rateValue;
	}
	public void setRateValue(double rateValue) {
		this.rateValue = rateValue;
	}
	
	public String getMove() {
		return move;
	}
	public void setMove(String move) {
		this.move = move;
	}
	public String getPokemon() {
		return pokemon;
	}
	public void setPokemon(String pokemon) {
		this.pokemon = pokemon;
	}
	
	public String getAbility() {
		return ability;
	}
	public void setAbility(String ability) {
		this.ability = ability;
	}
	
	
}