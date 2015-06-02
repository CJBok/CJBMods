package cjb.teleport.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class HomeUtil {
	
	public static int isValidName(String s) {
		
		if (s.length() < 3) {
			return 1;
		}
		
		if (Character.isDigit(s.charAt(0))) {
			return 2;
		}
		
		return 0;
	}
	
	private static File getSaveDir(EntityPlayer plr) {
		return FMLCommonHandler.instance().getSavesDirectory();
	}

	public static NBTTagCompound loadTeleportLocations(EntityPlayer plr) {
		try {
            File file1 = new File(getSaveDir(plr), plr.worldObj.getSaveHandler().getWorldDirectoryName() + "/playerdata/CJBTP_" + plr.getCommandSenderName() + ".dat");

            if (file1.exists()) {
                return CompressedStreamTools.readCompressed(new FileInputStream(file1));
            } else {
            	return new NBTTagCompound();
            }
        } catch (Throwable exception) {
        	exception.printStackTrace();
        }
		return null;
	}
	
	public static void saveTeleportLocations(EntityPlayer plr, NBTTagCompound tpdata) {
		
		try {
			
			if (tpdata == null) {
				return;
			}
			
            File file1 = new File(getSaveDir(plr), plr.worldObj.getSaveHandler().getWorldDirectoryName() + "/playerdata/CJBTP_" + plr.getCommandSenderName() + ".dat.tmp");
            File file2 = new File(getSaveDir(plr), plr.worldObj.getSaveHandler().getWorldDirectoryName() + "/playerdata/CJBTP_" + plr.getCommandSenderName() + ".dat");
            
            CompressedStreamTools.writeCompressed(tpdata, new FileOutputStream(file1));

            if (file2.exists()) {
                file2.delete();
            }

            file1.renameTo(file2);
        } catch (Throwable exception) {
            exception.printStackTrace();
        }
	}
	
	public static void sendErrorMsg(ICommandSender sender, String s) {
		sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + s));
	}
	
	public static void sendMsg(ICommandSender sender, String s) {
		sender.addChatMessage(new ChatComponentText(s));
	}
	
	public static void transferEntityToWorld(Entity par1Entity, int par2, WorldServer par3WorldServer, WorldServer par4WorldServer)
    {
        WorldProvider pOld = par3WorldServer.provider;
        WorldProvider pNew = par4WorldServer.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double d0 = par1Entity.posX * moveFactor;
        double d1 = par1Entity.posZ * moveFactor;
        double d3 = par1Entity.posX;
        double d4 = par1Entity.posY;
        double d5 = par1Entity.posZ;
        float f = par1Entity.rotationYaw;
        par3WorldServer.theProfiler.startSection("moving");

        par3WorldServer.theProfiler.endSection();

            par3WorldServer.theProfiler.startSection("placing");
            d0 = MathHelper.clamp_int((int)d0, -29999872, 29999872);
            d1 = MathHelper.clamp_int((int)d1, -29999872, 29999872);

            if (par1Entity.isEntityAlive())
            {
                par4WorldServer.spawnEntityInWorld(par1Entity);
                par1Entity.setLocationAndAngles(d0, par1Entity.posY, d1, par1Entity.rotationYaw, par1Entity.rotationPitch);
                par4WorldServer.updateEntityWithOptionalForce(par1Entity, false);
                //teleporter.placeInPortal(par1Entity, d3, d4, d5, f);
            }

            par3WorldServer.theProfiler.endSection();
        

        par1Entity.setWorld(par4WorldServer);
    }
	
	public static void transferPlayerToDimension(EntityPlayerMP plr, int x, int y, int z, int yaw, int dimension) {
    	
    	ServerConfigurationManager manager = plr.mcServer.getConfigurationManager();
    	
        int j = plr.dimension;
        WorldServer worldserver = plr.mcServer.worldServerForDimension(plr.dimension);
        plr.dimension = dimension;
        WorldServer worldserver1 = plr.mcServer.worldServerForDimension(plr.dimension);
        plr.playerNetServerHandler.sendPacket(new S07PacketRespawn(plr.dimension, plr.worldObj.difficultySetting, plr.worldObj.getWorldInfo().getTerrainType(), plr.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(plr);
        plr.isDead = false;
        transferEntityToWorld(plr, j, worldserver, worldserver1);
        manager.func_72375_a(plr, worldserver);
        plr.playerNetServerHandler.setPlayerLocation(x + 0.5D, y, z + 0.5D, yaw, 0);
        plr.theItemInWorldManager.setWorld(worldserver1);
        manager.updateTimeAndWeatherForPlayer(plr, worldserver1);
        manager.syncPlayerInventory(plr);
        Iterator iterator = plr.getActivePotionEffects().iterator();

        while (iterator.hasNext())
        {
            PotionEffect potioneffect = (PotionEffect)iterator.next();
            plr.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(plr.getEntityId(), potioneffect));
        }

        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(plr, j, dimension);
    }
}
