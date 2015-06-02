package cjb.moreinfo.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import tconstruct.library.tools.ToolCore;
import thaumcraft.common.items.wands.ItemWandCasting;
import cjb.moreinfo.MoreInfo;
import cjb.moreinfo.common.MoreInfoMod;
import cjb.moreinfo.client.Renderer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class ProxyTickClient implements Runnable {
	
	private Minecraft mc = Minecraft.getMinecraft();
	
	private int posx, posy, posz = 0;
	private String[] romannumbers = new String[]{"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", 
													"XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX"}; 
	private long oreTick = 0;
	private long spawnTick = 0;
	private boolean isSSP = true;
	
	public Thread thread;
	
	public boolean renderSpawnAreas = false;

	public List<String> getArmorDamage(EntityPlayer plr) {
		List<String> armors = new ArrayList<String>();
		int damage = 0;
		int maxdamage = 0;
		
		if (MoreInfoMod.ARMORHEAD.isTrue()) {
			ItemStack armor = plr.getCurrentArmor(3);
			
			
			if (armor != null) {
				maxdamage = armor.getMaxDamage();
				damage = maxdamage - armor.getItemDamage();
				
				String color = "\u00a72";
				if ((float)damage / (float)maxdamage <= 0.1f) {color = "\u00a7c";}
				armors.add((String) armor.getTooltip(plr, false).get(0) + ": " + color + damage + "/" + maxdamage);
			}
		}
		
		if (MoreInfoMod.ARMORCHEST.isTrue()) {
			ItemStack armor = plr.getCurrentArmor(2);
			
			
			if (armor != null) {
				maxdamage = armor.getMaxDamage();
				damage = maxdamage - armor.getItemDamage();
				
				String color = "\u00a72";
				if ((float)damage / (float)maxdamage <= 0.1f) {color = "\u00a7c";}
				armors.add((String) armor.getTooltip(plr, false).get(0) + ": " + color + damage + "/" + maxdamage);
			}
		}
		
		if (MoreInfoMod.ARMORLEGS.isTrue()) {
			ItemStack armor = plr.getCurrentArmor(1);
			
			
			if (armor != null) {
				maxdamage = armor.getMaxDamage();
				damage = maxdamage - armor.getItemDamage();
				
				String color = "\u00a72";
				if ((float)damage / (float)maxdamage <= 0.1f) {color = "\u00a7c";}
				armors.add((String) armor.getTooltip(plr, false).get(0) + ": " + color + damage + "/" + maxdamage);
			}
		}
		
		if (MoreInfoMod.ARMORFEET.isTrue()) {
			ItemStack armor = plr.getCurrentArmor(0);
			
			
			if (armor != null) {
				maxdamage = armor.getMaxDamage();
				damage = maxdamage - armor.getItemDamage();
				
				String color = "\u00a72";
				if ((float)damage / (float)maxdamage <= 0.1f) {color = "\u00a7c";}
				armors.add((String) armor.getTooltip(plr, false).get(0) + ": " + color + damage + "/" + maxdamage);
			}
		}
		
		return armors;
	}
	
	public String getArrowAmount(EntityPlayer plr) {
		if (!MoreInfoMod.ARROWS.isTrue())
			return "";
		
		if (plr.getCurrentEquippedItem() != null && plr.getCurrentEquippedItem().getItem() instanceof ItemBow) {
			int amount = 0;
			for (ItemStack stack : plr.inventory.mainInventory) {
				if (stack != null && stack.getItem() == Items.arrow) {
					amount += stack.stackSize;
				}
			}
			if (amount > 0)
				return "" + amount;
		}
		return "";
	}
	
	public String getBiome(World w) {
		if (!MoreInfoMod.BIOME.isTrue())
			return "";
		
		return "" + w.getBiomeGenForCoords(posx, posz).biomeName;
	}
	
	public boolean getCanSpawnHere(double d, double d1, double d2) {
		AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(d, d1, d2, d + 1, d1 + 1, d2 +1);
        return mc.theWorld.checkBlockCollision(boundingBox) && !mc.theWorld.isAnyLiquid(boundingBox);
    }
	
	public List<String> getItemInfo(EntityPlayer plr) {
		List<String> iteminfo = new ArrayList<String>();
		
		if (!MoreInfoMod.ITEMINFO.isTrue())
			return iteminfo;
		
		if (plr.getCurrentEquippedItem() != null) {
			ItemStack item = plr.getCurrentEquippedItem();
			int id = Item.getIdFromItem(item.getItem());
			int damage = item.getItemDamage();
			int maxdamage = item.getMaxDamage() + 1;
			
			String s = id + (item.getItem().getHasSubtypes()? ":" + damage : "");
			
			iteminfo.add(s + " : " + (String) item.getTooltip(plr, false).get(0));

			s = "";
			if (item.isItemStackDamageable()) {
				if (MoreInfo.tconstruct && item.getItem() instanceof ToolCore) {
					NBTTagCompound tags = item.getTagCompound();
					if (tags.hasKey("InfiTool")) {
						damage = tags.getCompoundTag("InfiTool").getInteger("Damage");
						maxdamage = tags.getCompoundTag("InfiTool").getInteger("TotalDurability") + 1;
						s = "" + (maxdamage - damage);
						if (tags.getCompoundTag("InfiTool").getBoolean("Broken")) {
							s = "";
						}
					}
					if (tags.hasKey("Energy")) {
						damage = tags.getInteger("Energy");
						if (tags.hasKey("EnergyMax")) {
							maxdamage = tags.getInteger("EnergyMax");
						} else {
							maxdamage = damage;
						}
						s = damage + " / " + maxdamage;
					}
				} else {
					s = (maxdamage - damage) + " / " + maxdamage;
				}
			}
			
			if (item.getItem() instanceof ItemFood) {
				
				float food = ((ItemFood)item.getItem()).func_150905_g(item) / 2f;
				float sat = ((ItemFood)item.getItem()).func_150906_h(item);
				
				s += "Food: " + food + " Saturation: " + sat;
			}
			
			if (MoreInfo.thaumcraft && item.getItem() instanceof ItemWandCasting) {
				item.getItem().addInformation(item, plr, iteminfo, false);
			}
			
			if (!s.isEmpty())
				iteminfo.add(s);
		}
		return iteminfo;
	}
	
	public String getLightLevel(World w) {
		if (!MoreInfoMod.LIGHTLEVEL.isTrue())
			return "";
		int ll = w.getBlockLightValue(posx, posy, posz);
		return (ll > 7 ? "\u00a72" : "\u00a7c") + "" + ll;
	}
	
	public void getMobs(EntityPlayer plr, World w) {
		Renderer.entities.clear();
		List<Entity> entities = new ArrayList<Entity>();
		entities.addAll(w.loadedEntityList);
		
		for (Entity ent : entities) {
			if (ent instanceof EntityLiving && ent != plr && ent.getDistanceToEntity(plr) <= 64)
				Renderer.entities.add((EntityLiving) ent);
		}
	}
	
	public List<String> getPlayerDebuffs(EntityPlayer plr) {
		List<String> debuffs = new ArrayList<String>();
		
		if (!MoreInfoMod.DEBUFFS.isTrue()) {
			return debuffs;
		}
		
		for (Iterator iterator = plr.getActivePotionEffects().iterator(); iterator.hasNext();) {
            PotionEffect pe = (PotionEffect)iterator.next();
            if (pe == null)
            	continue;
            
            if (pe.getAmplifier() >= 0 && pe.getAmplifier() < 20)
            	debuffs.add("" + Potion.getDurationString(pe) + " - " + StatCollector.translateToLocal(pe.getEffectName()) + " " + romannumbers[pe.getAmplifier()]);
            else
            	debuffs.add("" + Potion.getDurationString(pe) + " - " + StatCollector.translateToLocal(pe.getEffectName()) + " " + pe.getAmplifier());
        }
		
		return debuffs;
	}
	
	public String getRainTime(World w) {
		if (!MoreInfoMod.WEATHER.isTrue())
			return "";
		
		String s = "";
		if (MoreInfo.isRaining)
			s += "\u00a73";
		s += "R:" + MoreInfo.rainTime + "m\u00a7F";
		if (MoreInfo.isThundering)
			s += "\u00a73";
		s += " T:" + MoreInfo.thunderTime + "m\u00a7F";
		
		return s;
	}
	
	public void getSpawnAreas(EntityPlayer plr, World w) {
		
		if (System.currentTimeMillis() - spawnTick < 100 || w == null) 
			return;
		
		spawnTick = System.currentTimeMillis();
		
		List<RenderData> temp = new ArrayList<RenderData>();
		
		AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0);
		
		int radius = 32;
		int curmode = 2;
		for (int x = posx-radius ; x < posx+radius ; x++) {
			for (int z = posz-radius ; z < posz+radius ; z++) {
				
				Chunk chunk = w.getChunkFromBlockCoords(x, z);
				BiomeGenBase biome = w.getBiomeGenForCoords(x, z);
				
				if (!biome.getSpawnableList(EnumCreatureType.monster).isEmpty() && biome.getSpawningChance() > 0) {
					for (int y = posy-8 ; y < posy+4 ; y++) {
						
						int mode = getSpawnMode(chunk, aabb, x, y, z);
						
						if (mode != 0) {
							if (mode == 1) {
								temp.add(new RenderData(x,y,z,0x00FF00));
							} else {
								temp.add(new RenderData(x,y,z,0xFF0000)); 
							}
						}
					}
				}
			}
		}
		
		Renderer.spawns.clear();
		Renderer.spawns.addAll(temp);
	}
	
	private int getSpawnMode(Chunk chunk, AxisAlignedBB aabb, int x, int y, int z) {
		
		if (!SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, chunk.worldObj, x, y, z) || 
		   chunk.getSavedLightValue(EnumSkyBlock.Block, x & 0xF, y, z & 0xF) >= 8) {
			return 0;
		}
		
		aabb.minX = x + 0.2d;
		aabb.maxX = x + 0.8d;
		aabb.minY = y + 0.01d;
		aabb.maxY = y + 1.8d;
		aabb.minZ = z + 0.2d;
		aabb.maxZ = z + 0.8d;
		
		Block block = chunk.worldObj.getBlock(x, y, z);
		
		if (block == Blocks.air || !block.getMaterial().isSolid()) {
			block = chunk.worldObj.getBlock(x, y-1, z);
			if (block == Blocks.air || !block.getMaterial().isSolid()) 
				return 0;
			
			block = chunk.worldObj.getBlock(x, y+1, z);
			if (block != Blocks.air && block.getMaterial().isSolid()) 
				return 0;
		}
		
		if (/*!chunk.worldObj.checkNoEntityCollision(aabb) || !chunk.worldObj.getCollidingBlockBounds(aabb).isEmpty() ||*/ chunk.worldObj.isAnyLiquid(aabb))
			return 0;
		
		if (chunk.getSavedLightValue(EnumSkyBlock.Sky, x & 0xF, y, z & 0xF) >= 8) {
			return 1;
		}
		
		return 2;
	}
	
	public List<String> getToggles(EntityPlayer plr) {
		
		List<String> toggles = new ArrayList<String>();
		
		if (MoreInfoMod.renderSpawnAreas)
			toggles.add(I18n.format("cjb.moreinfo.tab.toggles.spawnarea"));
		if (plr.capabilities.isFlying)
			toggles.add(I18n.format("cjb.moreinfo.tab.toggles.flying"));

		return toggles;
	}
	
	public List<String> getVillageInfo(EntityPlayer plr, World w) {
		
		List<String> village = new ArrayList<String>();
		
		if (!MoreInfoMod.VILLAGE.isTrue()) {
			return village;
		}
		
		if (MoreInfo.villageinfo) {
			village.add(I18n.format("cjb.moreinfo.tab.village.villagers") + ": " + MoreInfo.villagevillagers);
			village.add(I18n.format("cjb.moreinfo.tab.village.doors") + ": " + MoreInfo.villagedoors);
			village.add(I18n.format("cjb.moreinfo.tab.village.size") + ": " + MoreInfo.villagesize);
			village.add(I18n.format("cjb.moreinfo.tab.village.reputation") + ": " + MoreInfo.villagereputation);
		} else if (mc.isSingleplayer() && MoreInfo.villagevillagers > -1){
			village.add(I18n.format("cjb.moreinfo.tab.village.distance") + ": " + MoreInfo.villagevillagers);
		}
		return village;
	}
	
	public String getWorldDay(World w) {
		long time = w.getWorldTime();
        int day = (int) (time / 24000) + 1;
		return "" + day;
	}
	
	public String getWorldTime(World w) {
		long time = w.getWorldTime();
        int day = (int) (time / 24000) + 1;
        int hours = (int) (time / 1000 % 24 + 6);
        if (hours > 23) hours = (hours - 29 + 5);
        float mins = time % 24000;
        mins %= 1000;
        mins = mins / 1000 * 60;
        
        String clock = "";
        String daytime = (w.calculateSkylightSubtracted(1f) < 4) ? "(\u00a72Day\u00a7f)" : "(\u00a7cNight\u00a7f)";
        
        if (MoreInfoMod.H12.getInt() == 1){
        	clock = "AM";
        	if (hours >= 12) {
        		hours -= 12;
        		clock = "PM";
        	}
        	if (hours == 0) hours = 12;
        }
		
		return " " + (Integer.toString(hours).length() == 1 ? "0" : "") + hours + ":" + (Integer.toString((int)mins).length() == 1 ? "0" : "") + (int)mins + clock + " " + daytime;
	}
	
	public String getXPInfo(EntityPlayer plr) {
		if (!MoreInfoMod.XP.isTrue())
			return "";
		return MathHelper.floor_float(plr.xpBarCap() * plr.experience) + " / " + plr.xpBarCap();
	}
	
	@Override
	public void run() {
		while (true) {
			if (mc.theWorld != null && mc.thePlayer != null) {
					getSpawnAreas(mc.thePlayer, mc.theWorld);
			}
		}
	}

	@SubscribeEvent
	public void tickEnd(TickEvent event) {
		if(event.phase == TickEvent.Phase.END && event.side == Side.CLIENT) {
			isSSP = mc.isSingleplayer();
			
			EntityPlayer plr = mc.thePlayer;
			
			if (plr == null || plr.worldObj == null) {
				Renderer.spawns.clear();
				Renderer.entities.clear();
				return;
			}
			
			World w = plr.worldObj;
			
			posx = MathHelper.floor_double(plr.posX);
			posy = MathHelper.floor_double(plr.posY);
			posz = MathHelper.floor_double(plr.posZ);
			
			InfoTab.debuffs.setTextList(this.getPlayerDebuffs(plr));
			InfoTab.village.setTextList(this.getVillageInfo(plr, w));
			InfoTab.daytime.setText(MoreInfoMod.DAYTIME.isTrue() ? getWorldDay(w) + getWorldTime(w) : "");
			InfoTab.lightlevel.setText(getLightLevel(w));
			InfoTab.biome.setText(getBiome(w));
			InfoTab.arrows.setText(getArrowAmount(plr));
			InfoTab.iteminfo.setTextList(getItemInfo(plr));
			InfoTab.slimechunk.setText(MoreInfoMod.SLIMECHUNK.isTrue() ? MoreInfo.slimechunk : "");
			InfoTab.weather.setText(getRainTime(w));
			InfoTab.xp.setText(getXPInfo(plr));
			InfoTab.fps.setText(MoreInfoMod.FPS.isTrue() ? mc.debug.split(" ")[0] : "");
			InfoTab.coords.setText(MoreInfoMod.COORDS.isTrue() ? posx + ", " + posy + ", " + posz : "");
			InfoTab.toggles.setTextList(getToggles(plr));
			InfoTab.armor.setTextList(this.getArmorDamage(plr));
			
			if (thread == null || !thread.isAlive()) {
				thread = new Thread(this);
				thread.setName("Spawn Area");
				thread.setPriority(3);
				thread.setDaemon(true);
				thread.start();
			}
			//getMobs(plr, w);
		}
	}
}
