package cjb.teleport.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import cjb.teleport.utils.HomeUtil;

public class CommandDelHome extends CommandBase
{
    @Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

    @Override
	public String getCommandName()
    {
        return "delhome";
    }
    
    @Override
	public String getCommandUsage(ICommandSender sender)
    {
        return "cjb.homes.commands.delhome.usage";
    }

    @Override
	public void processCommand(ICommandSender sender, String[] args) {
	    if (args.length <= 0 || args.length > 1 || args.length <= 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
	    	func_152373_a(sender, this, getCommandUsage(sender));
			return;
		}
		
	    if(args.length == 1) {
	    	EntityPlayer plr = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
	    	if (plr != null) {
	    		NBTTagCompound tpdata = HomeUtil.loadTeleportLocations(plr);
        		NBTTagList tptaglist = tpdata.getTagList("cjbtp", 10);
	    		
	    		if (tptaglist != null) {
	    			boolean deleted = false;
	    			for (int i = 0 ; i < tptaglist.tagCount() ; i++) {
						NBTTagCompound tptag = tptaglist.getCompoundTagAt(i);
						if (args[0].equalsIgnoreCase(tptag.getString("name"))) {
							tptaglist.removeTag(i);
							deleted = true;
						}
					}
	    			
	    			if (deleted) {
	    				tpdata.setTag("cjbtp", tptaglist);
	    				func_152373_a(sender, this, "cjb.homes.commands.delhome.deleted", new Object[]{EnumChatFormatting.GREEN + args[0] + EnumChatFormatting.WHITE});
	    				HomeUtil.saveTeleportLocations(plr, tpdata);
	    			} else {
	    				func_152373_a(sender, this, "cjb.homes.commands.delhome.notfound", new Object[]{EnumChatFormatting.RED + args[0] + EnumChatFormatting.WHITE});
	    			}
	    		}
	    	}
	    }
    }
}
