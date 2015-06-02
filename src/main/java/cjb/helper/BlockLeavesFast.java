package cjb.helper;

import cjb.api.CJB;
import cjb.api.asm.CJBClassTransformer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockLeavesFast extends Block {

	protected BlockLeavesFast(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	public void onNeighborBlockChange(World p_149695_1_, int p_149695_2_, int p_149695_3_, int p_149695_4_, Block p_149695_5_) {
		if (CJB.FASTLEAVEDECAY) {
			CJBClassTransformer.updateLeaveBlock(p_149695_1_, p_149695_2_, p_149695_3_, p_149695_4_, p_149695_5_);
		}
	}
}
