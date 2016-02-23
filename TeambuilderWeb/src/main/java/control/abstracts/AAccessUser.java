package control.abstracts;

import control.LiteAccess;

public abstract class AAccessUser {
	protected LiteAccess lacc;
	
	public AAccessUser(LiteAccess lacc){
		this.lacc = lacc;
	}
	
	public AAccessUser(){
		
	}
	
	public LiteAccess getLacc() {
		return lacc;
	}

	public void setLacc(LiteAccess lacc) {
		this.lacc = lacc;
	}
}
