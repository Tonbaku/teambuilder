package view;

import java.io.File;

import org.jsoup.helper.StringUtil;

import control.LiteAccess;

public class SpriteAccessBB {
	private LiteAccess lacc;

	public String getSpriteURLForName(String name){
		String url = "./resources/Sprites/"+name+".gif";
//		File f = new File(url);
//		if(!f.exists()){
//			lacc.getValueForSelect("select nummer from pokemon where name=\""+name+"\";");
//		}
		return url;
	}
	
	public String getRandomSpriteUrl(){
		String name = lacc.getValueForSelect("select name from pokemon where nummer="+Math.round(Math.random()*721)+";");
		return this.getSpriteURLForName(name);
	}
	
	public String getIconForName(String name){
		String url;
		String sqlReturn = lacc.getValueForSelect("select nummer from pokemon where name=\""+ name +"\";");
		if(StringUtil.isBlank(sqlReturn)){
			return "";
		}
		int numberForName = Integer.parseInt(sqlReturn);
		if(numberForName>649){
			url = "./resources/Icons/"+name+".png";
		} else {
			url = "./resources/Icons/"+name+".gif";
		}
		return url;
	}
	
	public LiteAccess getLacc() {
		return lacc;
	}

	public void setLacc(LiteAccess lacc) {
		this.lacc = lacc;
	}
	

}
