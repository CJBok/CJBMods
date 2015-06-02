package cjb.teleport.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldProvider;
import cjb.teleport.utils.HomeUtil;

public class CommandHome extends CommandBase
{
    @Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

    @Override
	public String getCommandName()
    {
        return "home";
    }
    
    @Override
	public String getCommandUsage(ICommandSender sender)
    {
        return "cjb.homes.commands.home.usage";
    }

    @Override
	public void processCommand(ICommandSender sender, String[] args)
    {    	
    	if (args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
    		func_152373_a(sender, this, getCommandUsage(sender));
    		return;
    	}
    	
        if(args.length <= 1) {
        	EntityPlayer plr = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
        	if (plr != null) {
        		
        		NBTTagCompound tpdata = HomeUtil.loadTeleportLocations(plr);
        		NBTTagList tptaglist = tpdata.getTagList("cjbtp", 10);
        		
        		if (tptaglist != null) {
        			if (args.length <= 0) {
        				String tps = "";
        				
        				for (int i = 0 ; i < tptaglist.tagCount() ; i++) {
        					NBTTagCompound tptag = tptaglist.getCompoundTagAt(i);
        					tps += EnumChatFormatting.GREEN + tptag.getString("name") + EnumChatFormatting.WHITE + (i < tptaglist.tagCount() - 1 ? ", " : "");
        				}
        				
        				if (tps.equalsIgnoreCase("")) {
        					func_152373_a(sender, this, "cjb.homes.commands.home.nohomes");
        				} else {
        					func_152373_a(sender, this, "cjb.homes.commands.home.list", new Object[]{tps});
        				}
        				return;
        			}
        			
        			if (tptaglist.tagCount() <= 0 ) {
        				func_152373_a(sender, this, "cjb.homes.commands.home.nohomes");
        			} else if (tptaglist.tagCount() >= 1) {
        				NBTTagCompound tptagto = null;
        				for (int i = 0 ; i < tptaglist.tagCount() ; i++) {
        					NBTTagCompound tptag = tptaglist.getCompoundTagAt(i);
        					if (args[0].equalsIgnoreCase(tptag.getString("name"))) {
        						tptagto = tptag;
        						break;
        					}
        				}
        				
        				if (tptagto != null) {
        					
        					int dimension = 0;
        					
        					if (tptagto.hasKey("dimension")) {
        						dimension = tptagto.getInteger("dimension");
        					}
        					
        					if (plr.dimension == dimension) {
        						plr.rotationPitch = 0;
            					plr.rotationYaw = tptagto.getInteger("yaw");
            					plr.setPositionAndUpdate(tptagto.getInteger("x") + 0.5D, tptagto.getInteger("y"), tptagto.getInteger("z") + 0.5D);
            					func_152373_a(sender, this, "cjb.homes.commands.home.teleportedto", new Object[] {EnumChatFormatting.GREEN + tptagto.getString("name") + EnumChatFormatting.WHITE});
        					} else {
        						WorldProvider wFrom = WorldProvider.getProviderForDimension(plr.dimension);
            					WorldProvider wTo = WorldProvider.getProviderForDimension(dimension);
            					
            					HomeUtil.transferPlayerToDimension((EntityPlayerMP) plr, tptagto.getInteger("x"), tptagto.getInteger("y"), tptagto.getInteger("z"), tptagto.getInteger("yaw"), dimension);
            					func_152373_a(sender, this, "cjb.homes.commands.home.teleportedto", new Object[] {EnumChatFormatting.GREEN + tptagto.getString("name") + EnumChatFormatting.RED + " [" + wFrom.getDimensionName() + " -> "  + wTo.getDimensionName() + "]"});
        					}
        				} else {
        					func_152373_a(sender, this, "cjb.homes.commands.home.notfound", new Object[]{EnumChatFormatting.RED + args[0] + EnumChatFormatting.WHITE});
        				}
        			}
        		}
        	}
        }
    }
}
