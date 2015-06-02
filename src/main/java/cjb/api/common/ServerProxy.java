package cjb.api.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;

import cjb.api.CJBAPI;
import cjb.api.network.PacketCustomServer;
import cjb.api.common.Option;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ServerProxy {
	
	public File getConfigDir(boolean startup) {
		return new File("./config/cjbserver/");
	}
	
	public void initProxy() {
		FMLCommonHandler.instance().bus().register(this);
	}
	
	@SubscribeEvent
	public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		for (CJBMod cjbmod : CJBAPI.mods) {
			for (Option option : cjbmod.getOptions()) {
				if (option.isPacket()) {
					try {
			            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
			            DataOutputStream output = new DataOutputStream(var4);
			            output.writeUTF(option.getMod());
			            output.writeUTF(option.getPropertyName());
			            
			            if (option.getDefaultValue() instanceof Integer) {
			            	output.writeInt(option.getInt());
			            	CJBAPI.network.sendToAll(new PacketCustomServer("CJBOptInt", var4.toByteArray()));
			            } else if (option.getDefaultValue() instanceof Boolean) {
			            	output.writeInt(option.getInt());
			            	CJBAPI.network.sendToAll(new PacketCustomServer("CJBOptInt", var4.toByteArray()));
			            } else if (option.getDefaultValue() instanceof String) {
			            	output.writeUTF(option.getString());
			            	CJBAPI.network.sendToAll(new PacketCustomServer("CJBOptStr", var4.toByteArray()));
			            }
			            
			        } catch (Throwable e) {
			            e.printStackTrace();
			        }
				}
			}
		}
	}

	@SubscribeEvent
	public void onTickServer(TickEvent.ServerTickEvent event) {
		if (CJBAPI.configDirty) {
			for (CJBMod cjbmod : CJBAPI.mods) {
				for (Option option : cjbmod.getOptions()) {
					if (option.isPacket()) {
						try {
				            ByteArrayOutputStream var4 = new ByteArrayOutputStream();
				            DataOutputStream output = new DataOutputStream(var4);
				            output.writeUTF(option.getMod());
				            output.writeUTF(option.getPropertyName());
				            
				            if (option.getDefaultValue() instanceof Integer) {
				            	output.writeInt(option.getInt());
				                CJBAPI.network.sendToAll(new PacketCustomServer("CJBOptInt", var4.toByteArray()));
				            } else if (option.getDefaultValue() instanceof Boolean) {
				            	output.writeInt(option.getInt());
				            	CJBAPI.network.sendToAll(new PacketCustomServer("CJBOptInt", var4.toByteArray()));
				            } else if (option.getDefaultValue() instanceof String) {
				            	output.writeUTF(option.getString());
				            	CJBAPI.network.sendToAll(new PacketCustomServer("CJBOptStr", var4.toByteArray()));
				            }
				            
				        } catch (Throwable e) {
				            e.printStackTrace();
				        }
					}
				}
			}
			CJBAPI.configDirty = false;
		}
	}
}
