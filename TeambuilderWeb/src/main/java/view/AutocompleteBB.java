package view;

import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import control.LiteAccess;

public class AutocompleteBB {
	private LiteAccess lacc;
	private List<String> currentSuggestions;
	private SpriteAccessBB spriteAccess;
	
	public void onAjaxCall(){
		ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, String> parameterMap = (Map<String, String>) ctx.getRequestParameterMap();
		String param = parameterMap.get("autocompleteField");
		currentSuggestions = lacc.getValuesForSingleColumn("select name from "+param+" order by name;");
	}
	
	public String getSuggestions(){
		//Make the suggestions to a javascript-array-String
		if(currentSuggestions == null || currentSuggestions.isEmpty()){
			return "";
		}
		StringBuilder ret = new StringBuilder();
		ret.append('[');
		for(String entry : currentSuggestions){
			ret.append('{');
			ret.append("icon:");
			ret.append('"');
			ret.append("<img src='");
			ret.append(spriteAccess.getIconForName(entry)); 
			ret.append("'/>");
			ret.append('"');
			ret.append(',');
			ret.append("label:");
			ret.append('"');
			ret.append(entry); 
			ret.append('"');
			ret.append(',');
			ret.append("value:");
			ret.append('"');
			ret.append(entry);
			ret.append('"');
			ret.append('}');
			ret.append(",");
		}
		ret.setCharAt(ret.length()-1, ']');
		ret.append(';');
		return ret.toString();
	}
	
	public LiteAccess getLacc() {
		return lacc;
	}

	public void setLacc(LiteAccess lacc) {
		this.lacc = lacc;
	}

	public SpriteAccessBB getSpriteAccess() {
		return spriteAccess;
	}

	public void setSpriteAccess(SpriteAccessBB spriteAccess) {
		this.spriteAccess = spriteAccess;
	}

}
