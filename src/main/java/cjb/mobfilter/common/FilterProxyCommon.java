package cjb.mobfilter.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.EnumSet;
import java.util.List;

import cjb.api.CJBAPI;
import cjb.mobfilter.MobFilter;


import cjb.mobfilter.network.PacketMobFilter;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class FilterProxyCommon {
	
	public void init() {
		MinecraftForge.EVENT_BUS.register(new FilterProxyCommon());
		FMLCommonHandler.instance().bus().register(this);
		CJBAPI.registerCJBmod(MobFilterMod.class);
	}
	
	@SubscribeEvent
	public void onLivingSpawnEvent(EntityJoinWorldEvent event) {
		
		if (event.entity instanceof EntityPlayerMP) {
			try{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	 	DataOutputStream output = new DataOutputStream(baos);
	    	 	output.writeUTF(MobFilterMod.filterlistoption.getString());
				MobFilter.network.sendTo(new PacketMobFilter("CJBMF|FilterList", baos.toByteArray()), (EntityPlayerMP) event.entity);
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		if (event.isCancelable() && event.entity instanceof EntityLivingBase) {
			if (event.entity instanceof EntityPlayer)
				return;
			
			if (event.entity instanceof EntityTameable && ((EntityTameable) event.entity).isTamed())
				return;
			
			if ( MobFilterMod.filterlist.containsKey("AGGRESIVE") && event.entity instanceof IMob)
				event.setCanceled(true);
			
			if ( MobFilterMod.filterlist.containsKey("PASSIVE") && event.entity instanceof IAnimals)
				event.setCanceled(true);
			
			if (MobFilterMod.filterlist.containsKey(EntityList.getEntityString(event.entity)))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void tickStart(WorldTickEvent event) {
		
		if (event.phase == TickEvent.Phase.START) {
			World world = (World) event.world;
			List<Entity> entities = world.loadedEntityList;
			
			for (Entity entity : entities) {
				if (entity instanceof EntityPlayer)
					continue;
				
				if (entity instanceof EntityLiving) {
					EntityLiving entityliving = (EntityLiving) entity;
					
					if (entityliving instanceof EntityTameable && ((EntityTameable) entityliving).isTamed())
						continue;
					
					if ( MobFilterMod.filterlist.containsKey("AGGRESIVE") && entityliving instanceof IMob)
						entityliving.setDead();
					
					if ( MobFilterMod.filterlist.containsKey("PASSIVE") && entityliving instanceof IAnimals)
						entityliving.setDead();
					
					if (MobFilterMod.filterlist.containsKey(EntityList.getEntityString(entity)))
						entityliving.setDead();
				}
			}
		}
	}

}
