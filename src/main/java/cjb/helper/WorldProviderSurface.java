package cjb.helper;

import cjb.api.CJB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.WorldProvider;

public class WorldProviderSurface extends WorldProvider
{

	@Override
	public String getDimensionName() {
		return null;
	}
	
    public boolean getWorldHasVoidParticles()
    {
		if(CJB.NOVOID) {
			return false;
		}
		
        return super.getWorldHasVoidParticles();
    }
	
    public double getVoidFogYFactor()
    {
		if(CJB.NOVOID) {
			return 1.0D;
		}
        return super.getVoidFogYFactor();
    }
    
}