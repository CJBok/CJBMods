package tconstruct.library.tools;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.library.modifier.IModifyable;

public class ToolCore extends Item implements IModifyable {
	
	public int getItemDamageFromStackForDisplay(ItemStack stack) {
		return 0;
	}
	
	public int getItemMaxDamageFromStack(ItemStack stack) {
		return 0;
	}

}
