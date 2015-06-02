package cjb.moreinfo.common;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cjb.moreinfo.MoreInfo;
import cjb.moreinfo.network.PacketMoreInfoClient;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ProxyTickServer {
	
	private int posx, posy, posz = 0;
	private long secondTick = 0;
	private int tickCounter;

	public void getRainTime(EntityPlayerMP plr, World worldObj) {
		int rt = worldObj.getWorldInfo().getRainTime() / 20 / 60;
		int tt = worldObj.getWorldInfo().getThunderTime() / 20 / 60;
		
		try {
			ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            DataOutputStream var5 = new DataOutputStream(var4);
            var5.writeBoolean(worldObj.getWorldInfo().isRaining());
            var5.writeBoolean(worldObj.getWorldInfo().isThundering());
			var5.writeShort(rt);
			var5.writeShort(tt);
			MoreInfo.network.sendToDimension(new PacketMoreInfoClient("CJBMI|weather", var4.toByteArray()), worldObj.getWorldInfo().getVanillaDimension());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getVillageInfo(EntityPlayerMP plr, WorldServer w) {
		Village vil = w.villageCollectionObj.findNearestVillage(posx, posy, posz, 16);
		if (vil != null) {
			try {
				ByteArrayOutputStream var4 = new ByteArrayOutputStream();
	            DataOutputStream var5 = new DataOutputStream(var4);
	            var5.writeBoolean(true);
				var5.writeShort(vil.getNumVillagers());
				var5.writeShort(vil.getNumVillageDoors());
				var5.writeShort(vil.getVillageRadius());
				var5.writeShort(vil.getReputationForPlayer(plr.getDisplayName()));
				MoreInfo.network.sendTo(new PacketMoreInfoClient("CJBMI|village", var4.toByteArray()), plr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				vil = w.villageCollectionObj.findNearestVillage(posx, posy, posz, 2000);
				int i = -1;
				
				if (vil != null) {
					ChunkCoordinates c = vil.getCenter();
					int x = posx - c.posX;
					int z = posz - c.posZ;
					i = MathHelper.floor_double(Math.sqrt(x*x+z*z));
				}
				
				ByteArrayOutputStream var4 = new ByteArrayOutputStream();
	            DataOutputStream var5 = new DataOutputStream(var4);
	            var5.writeBoolean(false);
	            var5.writeShort(i);
	            MoreInfo.network.sendTo(new PacketMoreInfoClient("CJBMI|village", var4.toByteArray()), plr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void isPlayerInsideSlimeChunk(EntityPlayerMP plr, World w) {
		try {
			ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            DataOutputStream var5 = new DataOutputStream(var4);
            var5.writeBoolean(w.getChunkFromBlockCoords(posx, posz).getRandomWithSeed(987234911L).nextInt(10) == 0);
            MoreInfo.network.sendTo(new PacketMoreInfoClient("CJBMI|slime", var4.toByteArray()), plr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ;
	}
	
	public void sendSkyLightValue(EntityPlayerMP plr, WorldServer worldObj) {
		try {
			ByteArrayOutputStream var4 = new ByteArrayOutputStream();
            DataOutputStream var5 = new DataOutputStream(var4);
            var5.writeInt(worldObj.skylightSubtracted);
            MoreInfo.network.sendTo(new PacketMoreInfoClient("CJBMI|skylight", var4.toByteArray()), plr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public void tickEnd(TickEvent evt) {
		if (evt.phase == TickEvent.Phase.START) return;
		
		if (evt.type == TickEvent.Type.SERVER) {
			
			MinecraftServer server = MinecraftServer.getServer();
			
			Integer[] ids = DimensionManager.getIDs(this.tickCounter++ % 200 == 0);
	        for (int x = 0; x < ids.length; x++)
	        {
	            int id = ids[x];
	            long var2 = System.nanoTime();

	            if (id == 0 || server.getAllowNether())
	            {
	                WorldServer w = DimensionManager.getWorld(id);

	                if (this.tickCounter % 20 == 0) {
	                	getRainTime(null, w);
	                	List<EntityPlayerMP> plrs =  w.playerEntities;
	                	for (EntityPlayerMP plr : plrs) {
	                		posx = MathHelper.floor_double(plr.posX);
	            			posy = MathHelper.floor_double(plr.posY);
	            			posz = MathHelper.floor_double(plr.posZ);
	                		this.isPlayerInsideSlimeChunk(plr, w);
	                		this.sendSkyLightValue(plr, w);
	                	}
	                }
	                
	                if (this.tickCounter % 5 == 0) {
	                	getRainTime(null, w);
	                	List<EntityPlayerMP> plrs =  w.playerEntities;
	                	for (EntityPlayerMP plr : plrs) {
	                		posx = MathHelper.floor_double(plr.posX);
	            			posy = MathHelper.floor_double(plr.posY);
	            			posz = MathHelper.floor_double(plr.posZ);
	                		this.getVillageInfo(plr, w);
	                	}
	                }
	            }
	        }
		}
 	}
}
