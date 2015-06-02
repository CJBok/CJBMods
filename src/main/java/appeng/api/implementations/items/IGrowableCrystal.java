package appeng.api.implementations.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public abstract interface IGrowableCrystal {
	
	public abstract float getMultiplier(Block paramBlock, Material paramMaterial);
	
	public abstract ItemStack triggerGrowth(ItemStack paramItemStack);
}
