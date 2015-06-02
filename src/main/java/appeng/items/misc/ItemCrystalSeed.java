package appeng.items.misc;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import appeng.api.implementations.items.IGrowableCrystal;
import appeng.items.AEBaseItem;

public class ItemCrystalSeed extends AEBaseItem implements IGrowableCrystal {

	@Override
	public float getMultiplier(Block paramBlock, Material paramMaterial) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack triggerGrowth(ItemStack paramItemStack) {
		// TODO Auto-generated method stub
		return null;
	}

}
