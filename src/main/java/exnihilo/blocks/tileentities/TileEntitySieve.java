package exnihilo.blocks.tileentities;

import net.minecraft.tileentity.TileEntity;

public class TileEntitySieve extends TileEntity {
	
	public SieveMode mode = SieveMode.EMPTY;
	
	public static enum SieveMode {
		EMPTY(0),  FILLED(1);
	    
		public int value;
	    
		private SieveMode(int v) {
			this.value = v;
		}
	}
	  
	public void ProcessContents(boolean creative) {}

}
