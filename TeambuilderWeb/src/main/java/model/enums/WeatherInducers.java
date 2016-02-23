package model.enums;

public enum WeatherInducers {
	CLEARSKY(Weather.CLEARSKY),
	DROUGHT(Weather.SUN), 
	DRIZZLE(Weather.RAIN), 
	SNOWWARNING(Weather.HAIL), 
	SANDSTREAM(Weather.SAND), 
	PRIMORDIALSEA(Weather.RAIN), 
	DESOLATELAND(Weather.SUN);
	
	private Weather inducedWeather;
	private WeatherInducers(Weather w){
		this.setInducedWeather(w);
	}
	
	public Weather getInducedWeather() {
		return inducedWeather;
	}
	public void setInducedWeather(Weather inducedWeather) {
		this.inducedWeather = inducedWeather;
	}
	
	public static WeatherInducers instantiate(String ability){
		return WeatherInducers.valueOf(ability.replaceAll(" ", "").toUpperCase());
	}
}
