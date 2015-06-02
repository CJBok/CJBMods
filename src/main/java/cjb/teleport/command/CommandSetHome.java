package cjb.teleport.command;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import cjb.teleport.utils.HomeUtil;

public class CommandSetHome extends CommandBase
{
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

    @Override
    public String getCommandName()
    {
        return "sethome";
    }
    
    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "cjb.homes.commands.sethome.usage";
    }
    
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
    	if (args.length <= 0 || args.length > 1 || args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
    		func_152373_a(sender, this, getCommandUsage(sender));
    		return;
    	}
    	
        if(args.length == 1) {
        	
        	int stringmode = HomeUtil.isValidName(args[0]);
        	
        	if (stringmode > 0) {
        		if (stringmode == 1) {
        			func_152373_a(sender, this, "cjb.homes.commands.sethome.tooshort");
        		} else
        		if (stringmode == 2) {
        			func_152373_a(sender, this, "cjb.homes.commands.sethome.nonumber");
        		}
        		return;
        	}
        	
        	EntityPlayer plr = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
        	if (plr != null) {
        		
        		NBTTagCompound tpdata = HomeUtil.loadTeleportLocations(plr);
        		NBTTagList tptaglist = tpdata.getTagList("cjbtp", 10);
        		
        		if (tptaglist != null) {
        			for (int i = 0 ; i < tptaglist.tagCount() ; i++) {
    					NBTTagCompound tptag = tptaglist.getCompoundTagAt(i);
    					if (args[0].equalsIgnoreCase(tptag.getString("name")) || tptag.getString("name").equalsIgnoreCase("")) {
    						tptaglist.removeTag(i);
    					}
    				}
        			
        			double yaw = plr.rotationYaw % 360;
        			
        			if (yaw > 45 && yaw <= 135)
        				yaw = 90;
        			else if (yaw > 135 && yaw <= 225)
        				yaw = 180;
        			else if (yaw > 225 && yaw <= 315)
        				yaw = 270;
        			else if (yaw > 315)
        				yaw = 0;
        			
        			else if (yaw > -45 && yaw <= 45)
        				yaw = 0;
        			else if (yaw > -135 && yaw <= -45)
        				yaw = -90;
        			else if (yaw > -225 && yaw <= -135)
        				yaw = -180;
        			else if (yaw > -315 && yaw <= -225)
        				yaw = -270;
        			else if (yaw <= -315)
        				yaw = 0;
        			
        			NBTTagCompound tptag = new NBTTagCompound().getCompoundTag(args[0]);
        			tptag.setString("name", args[0]);
        			tptag.setInteger("x", MathHelper.floor_double(plr.posX));
        			tptag.setInteger("y", MathHelper.floor_double(plr.posY));
        			tptag.setInteger("z", MathHelper.floor_double(plr.posZ));
        			tptag.setInteger("yaw", MathHelper.floor_double(yaw));
        			tptag.setInteger("dimension", plr.dimension);
        			tptaglist.appendTag(tptag);
        			//HomeUtil.sendMsg(sender, I18n.format("cjb.homes.commands.sethome.created", new Object[] {EnumChatFormatting.GREEN + args[0] + EnumChatFormatting.WHITE}));
        			func_152373_a(sender, this, "cjb.homes.commands.sethome.created", new Object[] {EnumChatFormatting.GREEN + args[0] + EnumChatFormatting.WHITE});
        			tpdata.setTag("cjbtp", tptaglist);
        			HomeUtil.saveTeleportLocations(plr, tpdata);
        		} else {
        			
        		}
        	}
        }
    }
}
