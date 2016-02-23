package model.enums;

public enum Natures {
	ADAMANT	("atk",	"satk"),
	BASHFUL	("satk","satk"),
	BOLD	("def",	"atk"),
	BRAVE	("atk",	"spd"),
	CALM	("sdef", "atk"),
	CAREFUL	("sdef","satk"),
	DOCILE	("def",	"def"),
	GENTLE	("sdef","def"),
	HARDY	("atk",	"atk"),
	HASTY	("spd",	"def"),
	IMPISH	("def",	"satk"),
	JOLLY	("spd",	"satk"),
	LAX		("def",	"sdef"),
	LONELY	("atk",	"def"),
	MILD	("satk","def"),
	MODEST	("satk","atk"),
	NAIVE	("spd",	"sdef"),
	NAUGHTY	("atk",	"sdef"),
	QUIET	("satk","spd"),
	QUIRKY	("sdef","sdef"),
	RASH	("satk","sdef"),
	RELAXED	("def",	"spd"),
	SASSY	("sdef","spd"),
	SERIOUS	("spd",	"spd"),
	TIMID	("spd",	"atk");
	
	private String boosted;
	private String lowered;
	
	private Natures(String boosted, String lowered){
		this.setBoosted(boosted);
		this.setLowered(lowered);
	}

	public String getBoosted() {
		return boosted;
	}

	public void setBoosted(String boosted) {
		this.boosted = boosted;
	}

	public String getLowered() {
		return lowered;
	}

	public void setLowered(String lowered) {
		this.lowered = lowered;
	}
	
	
}
