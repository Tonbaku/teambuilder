package model.enums;

import java.util.HashSet;
import java.util.Set;

public enum MoveProperty {
	MULTIHIT, RECOIL, PUNCH, BITE, CONTACT, LAUNCH, SIDEEFFECT;
	
	public static Set<MoveProperty> resolveMoveProperty(boolean sideeffect, boolean contact, boolean punch, boolean bitemove, boolean recoil, boolean launch, boolean multihit){
		Set<MoveProperty> ret = new HashSet<MoveProperty>();
		if(sideeffect)
			ret.add(SIDEEFFECT);
		if (contact)
			ret.add(CONTACT);
		if (punch)
			ret.add(PUNCH);
		if (bitemove)
			ret.add(BITE);
		if (recoil)
			ret.add(RECOIL);
		if (launch)
			ret.add(LAUNCH);
		if (multihit)
			ret.add(MULTIHIT);
		return ret;
	}
}
