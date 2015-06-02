package cjb.cheats.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;
import tconstruct.smeltery.logic.SmelteryLogic;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.wands.ItemWandCasting;
import thaumcraft.common.lib.research.PlayerKnowledge;
import appeng.items.misc.ItemCrystalSeed;
import cjb.api.CJB;
import cjb.api.CJBAPI;
import cjb.cheats.Cheats;
import cjb.api.common.Option;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import exnihilo.blocks.tileentities.TileEntityCrucible;
import exnihilo.blocks.tileentities.TileEntitySieve;
import exnihilo.registries.HeatRegistry;

public class CheatProxyCommon {
	
	public static String mod = CheatsMod.class.getSimpleName();
	
	public static Option DEFAULT = new Option(mod, "General", false, "", false, false);
	public static Option IC2 = new Option(mod, "Industrial Craft 2", false, "", false, false);
	public static Option THAUMCRAFT = new Option(mod, "Thaumcraft 4", false, "", false, false);
	public static Option ENVIROMINE = new Option(mod, "EnviroMine", false, "", false, false);
	public static Option AE = new Option(mod, "Applied Energistics 2", false, "", false, false);
	public static Option TC = new Option(mod, "Tinkers Construct", false, "", false, false);
	public static Option EXNIHILO = new Option(mod, "Ex Nihilo", false, "", false, false);
	
	
	public static Option DAMAGE = new Option(mod, "", false, "nodamage", true);
	public static Option ONEHITKILL = new Option(mod, "", false, "onehitkill", true);
	public static Option FOOD = new Option(mod, "", false, "infinitefood", true);
	public static Option INVISIBLE = new Option(mod, "", false, "invisible", true);
	public static Option PICKUP = new Option(mod, "", false, "pickupradius", true);
	public static Option PICKUPORBS = new Option(mod, "", false, "pickupradiusorbs", true);
	public static Option FASTFURNACE = new Option(mod, "", false, "fastfurnace", true);
	public static Option XP = new Option(mod, "", false, "infinitexp", true);
	public static Option ITEMDAMAGE = new Option(mod, "", false, "noitemdamage", true);
	public static Option INFINITEARROWS = new Option(mod, "", false, "infinitearrows", true);
	public static Option AUTOJUMP = new Option(mod, "", false, "autojump", true);
	public static Option TIME = new Option(mod, "", 0, "time", true, new String[] {"cjb.options.time.normal", "cjb.options.time.alwaysday", "cjb.options.time.alwaysnight"});
	public static Option ADULT = new Option(mod, "", false, "adult", true);
	public static Option FASTGROWTH = new Option(mod, "", false, "fastgrowth", true);
	public static Option BLOCKBREAKSPEED = new Option(mod, "", 0, "blockbreakspeed", true, new String[] {"cjb.options.blockbreakspeed.normal", "cjb.options.blockbreakspeed.fast", "cjb.options.blockbreakspeed.veryfast"});
	public static Option NOPICKAXE = new Option(mod, "", false, "nopickaxe", true);
	public static Option WEATHER = new Option(mod, "", false, "weather", true);
	public static Option AIR = new Option(mod, "", false, "air", true);
	public static Option KEEPITEMS = new Option(mod, "", false, "keepitems", true);
	public static Option NOVOIDFOG = new Option(mod, "", false, "novoidfog", false);
	public static Option FASTLEAVEDECAY = new Option(mod, "", false, "fastleavedecay", true);
	public static Option REMOVEDEBUFFS = new Option(mod, "", false, "removedebuffs", true);
	
	public static Option IC2ENERGY = new Option(mod, "", false, "ic2itemenergy", true);
	public static Option RESEARCHASPECT = new Option(mod, "", false, "researchaspect", true);
	public static Option WANDVIS = new Option(mod, "", false, "infinitewandvis", true);
	public static Option CAMELPACK = new Option(mod, "", false, "infinitecamelpack", true);
	public static Option CRYSTALGROWTH = new Option(mod, "", false, "fastcrystalgrowth", true);
	public static Option INSTANTSMELTSMELTERY = new Option(mod, "", false, "instantsmeltsmeltery", true);
	public static Option INSTANTSIEVE = new Option(mod, "", false, "instantsieve", true);
	public static Option FASTERCRUCIBLES = new Option(mod, "", false, "fastercrucibles", true);
	
	private int posx = 0;
	private int posy = 0;
	private int posz = 0;
	
	public void initProxy() {
		CJBAPI.registerCJBmod(CheatsMod.class);
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void playerTick(PlayerTickEvent event) {
		EntityPlayer plr = event.player;
		World w = plr.worldObj;
		
		if (plr == null || w == null) 
			return;
		
		posx = MathHelper.floor_double(plr.posX);
		posy = MathHelper.floor_double(plr.posY);
		posz = MathHelper.floor_double(plr.posZ);
			
		if (w instanceof WorldServer) {
			if (PICKUP.isTrue()) {
				pickupItems(plr, w);
			}
			if (PICKUPORBS.isTrue()) {
				pickupOrbs(plr, w);
			}
			if(Cheats.aemod && CRYSTALGROWTH.isTrue()) {
				fastCrystalGrowth(w,plr);
			}
		}
		
		if (DAMAGE.isTrue()) {
			plr.setHealth(plr.getMaxHealth());
		}
			
		if (XP.isTrue())
			infiniteXP(plr);

		noItemDamage(plr);
			
		if (INFINITEARROWS.isTrue())
			infiniteArrows(plr, w);
			
		if (FOOD.isTrue()) {
			float food = plr.getFoodStats().getFoodLevel() - 10f;
			if (food > 0f) {
				plr.heal(food);
			}
			plr.getFoodStats().setFoodLevel(10);
		}
			
		autoJump(plr);
		
		if (FASTGROWTH.isTrue())
			fastGrowth(w);
			
		if (AIR.isTrue())
			infiniteAir(plr);

		if (REMOVEDEBUFFS.isTrue()) {
			removeDebuffs(plr);
		}
			
		if (Cheats.thaumcraftinstalled && RESEARCHASPECT.isTrue())
			infiniteResearchAspects(plr);
			
		if (Cheats.exnihilo) {
			updateCrucible();
		}
	}
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent event) {
		
		World w = event.world;
		
		if (w.isRemote)
			return;
			
		CJB.FASTLEAVEDECAY = FASTLEAVEDECAY.isTrue();
		CJB.NOVOID = NOVOIDFOG.isTrue();
			
		List<TileEntity> tiles = new ArrayList();
		tiles.addAll(w.loadedTileEntityList);
		for (TileEntity tile : tiles) {
			if (FASTFURNACE.isTrue() && tile instanceof TileEntityFurnace) {
				updateFurnace((TileEntityFurnace) tile);
			}
			
			if (tile instanceof IInventory) {
				noItemDamageTE((IInventory)tile);
			}
			
			if (Cheats.tconstructinstalled && INSTANTSMELTSMELTERY.isTrue() && tile instanceof SmelteryLogic) {
				updateSmeltery((SmelteryLogic)tile);
			}
			
			if (Cheats.exnihilo && INSTANTSIEVE.isTrue() && tile instanceof TileEntitySieve) {
				updateSieve((TileEntitySieve)tile);
			}
		}
		
		if (INVISIBLE.isTrue()) {
			List<Entity> entities = new ArrayList();
			entities.addAll(w.loadedEntityList);
			for (Entity ent : entities) {
				if (ent instanceof EntityCreature) {
					if (((EntityCreature) ent).getAttackTarget() instanceof EntityPlayer || ((EntityCreature) ent).getEntityToAttack() instanceof EntityPlayer) {
						((EntityCreature) ent).setAttackTarget(null);
						((EntityCreature) ent).setTarget(null);
					}
				}
			}
		}
		
		if (TIME.getInt() > 0) {
			changeTime(w);
		}
		
		if (ADULT.isTrue()) {
			instantAdult(w);
		}
		
		if (WEATHER.isTrue()) {
			disableWeather(w);
		}
		
		keepItemsOnDeath(w);
	}
	
	public void autoJump(EntityPlayer plr) {
		if (AUTOJUMP.isTrue() && plr.stepHeight != 1F) {
			plr.stepHeight = 1F;
		} else if (!AUTOJUMP.isTrue() && plr.stepHeight != 0.5F) {
			plr.stepHeight = 0.5F;
		}
	}
	
	public void changeTime(World worldObj) {
		if (worldObj.isRemote)
			return;
		
		int i = TIME.getInt();
		
		long time = worldObj.provider.getWorldTime() % 24000L;
		long ctime = worldObj.provider.getWorldTime();
		if (i == 1 && time > 12000L) {
			worldObj.provider.setWorldTime(ctime + 24000L - time);
		}
		if (i == 2 && (time < 14000L || time > 22000L)) {
			worldObj.provider.setWorldTime(ctime + 15000L - time);
		}
	}
	
	public void disableWeather(World w) {
		if (w.getWorldInfo().isThundering())
			w.getWorldInfo().setThundering(false);
		
		if (w.getWorldInfo().isRaining())
			w.getWorldInfo().setRaining(false);
	}
	
	public void fastCrystalGrowth(World w, EntityPlayer plr) {
		if (w.isRemote)
			return;
		
		List<EntityItem> items = w.getEntitiesWithinAABB(EntityItem.class, plr.boundingBox.copy().expand(8, 8, 8));
		
		for (EntityItem item : items) {
			if (item.getEntityItem() != null && item.getEntityItem().getItem() instanceof ItemCrystalSeed) {
				ItemCrystalSeed seed = (ItemCrystalSeed) item.getEntityItem().getItem();
				item.setEntityItemStack(seed.triggerGrowth(item.getEntityItem()));
			}
		}
	}
	
	public void fastGrowth(World worldObj) {
		if (worldObj.isRemote)
			return;
		
		int radius = 32;
		
		for (int y = posy - radius ; y < posy + radius ; ++y) {
			for (int x = posx - radius ; x < posx + radius ; ++x) {
				for (int z = posz - radius ; z < posz + radius ; ++z) {
					
					Block blk = worldObj.getBlock(x, y, z);
					
					if (blk instanceof IPlantable) {
						if (worldObj.rand.nextInt(20) == 0)
						blk.updateTick(worldObj, x, y, z, worldObj.rand);
					}
				}
			}
		}
	}
	
	public void infiniteAir(EntityPlayer plr) {	
		plr.setAir(300);
	}
	
	public void infiniteArrows(EntityPlayer plr, World w) {		
		boolean arrowsFound = false;
		for (int i = plr.inventory.mainInventory.length-1 ; i >= 0 ; --i) {
			ItemStack itemStack = plr.inventory.mainInventory[i];
			if (itemStack != null && itemStack.getItem() != null) {
				if (itemStack.getItem() == Items.arrow) {
					if (!arrowsFound) {
						itemStack.stackSize = 32;
						arrowsFound = true;
					} else {
						plr.inventory.mainInventory[i] = null;
					}
				}
			}
		}
		
		List<Entity> ents = new ArrayList();
		ents.addAll(w.loadedEntityList);
		for (Entity ent : ents) {
			if (ent instanceof EntityArrow) {
				EntityArrow arrow = (EntityArrow) ent;
				arrow.canBePickedUp = 1;
			}
		}
	}
	
	public void infiniteHealth(EntityPlayer plr) {
		plr.setHealth(plr.getMaxHealth());
	}
	
	public void infiniteResearchAspects(EntityPlayer plr) {
		if (!Cheats.thaumcraftinstalled || Thaumcraft.proxy == null)
			return;
		
		PlayerKnowledge pk = Thaumcraft.proxy.getPlayerKnowledge();
		
		if (pk == null || Aspect.aspects == null)
			return;
		
		for (Aspect aspect : Aspect.aspects.values()) {
			pk.addDiscoveredAspect(plr.getCommandSenderName(), aspect);
			pk.setAspectPool(plr.getCommandSenderName(), aspect, (short)999);
		}
		CJB.ToggleOption(CheatProxyCommon.RESEARCHASPECT);
	}
	
	public void infiniteXP(EntityPlayer plr) {	
		plr.experienceLevel = 999;
	}
	
	public void instantAdult(World worldObj) {
		if (worldObj.isRemote)
			return;
		
		List<Entity> ents = new ArrayList();
		ents.addAll(worldObj.loadedEntityList);
		for (Entity ent : ents) {
			if (ent instanceof EntityAgeable) {
				EntityAgeable entage = (EntityAgeable) ent;
				if (entage.getGrowingAge() != 0 && new Random().nextInt(50) == 0) {
					int age = (int) (entage.getGrowingAge() / 2F);
					entage.setGrowingAge(age);
				}
			}
		}
	}
	
	public void keepItemsOnDeath(World w) {
		if (CheatProxyCommon.KEEPITEMS.isTrue()) {
			w.getGameRules().setOrCreateGameRule("keepInventory", "true");
		} else {
			w.getGameRules().setOrCreateGameRule("keepInventory", "false");
		}
	}
	
	public void noItemDamage(EntityPlayer plr) {
		if (plr.openContainer instanceof Container && plr.openContainer.inventorySlots != null) {
			List<Slot> slots = plr.openContainer.inventorySlots;
			
			boolean inventoryChanged = false;
			
			for (Slot slot : slots) {
				if (slot != null && slot.getStack() != null && slot.getStack().getItem() != null) {
					if (setItemDamage(slot.getStack(), plr))
						inventoryChanged = true;
				}
			}
			
			if (inventoryChanged)
				plr.inventory.markDirty();
		}
	}
	
	public void noItemDamageTE(IInventory inv) {
		int size = inv.getSizeInventory();
		for (int i = 0 ; i < size ; i++) {
			ItemStack item = inv.getStackInSlot(i);
			if (item != null)
				setItemDamage(item, null);
		}
	}
	
	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed event) {
		int cheat = CheatProxyCommon.BLOCKBREAKSPEED.getInt();
		
		if (cheat > 0) {
			if (event.block.getUnlocalizedName().equalsIgnoreCase("tile.blockbarrel")) {
				return;
			}
			
			int multiplier = 25;
			
			if (cheat == 2)
				multiplier = 100;
			
			if (event.block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z) > 0)
				event.newSpeed = event.block.getBlockHardness(event.entityPlayer.worldObj, event.x, event.y, event.z) * multiplier * cheat;
		}
	}
	
	@SubscribeEvent
	public void onHarvestCheck(HarvestCheck event) {
		if (CheatProxyCommon.NOPICKAXE.isTrue())
			event.success = true;
	}
	
	@SubscribeEvent
	public void onLivingHurtEvent(LivingHurtEvent event) {
		
		if (event.entityLiving instanceof EntityPlayer) {
			if (DAMAGE.isTrue()) {
				if (event.source != DamageSource.starve)
					event.setCanceled(true);
			}
			
			if (FOOD.isTrue()) {
				if (event.source == DamageSource.starve)
					event.setCanceled(true);
			}
		} else {
			if (ONEHITKILL.isTrue() && (event.source.getSourceOfDamage() instanceof EntityPlayer || event.source.getSourceOfDamage() instanceof EntityArrow) && !(event.entityLiving instanceof EntityPlayer))
				event.ammount = 9999;
		}
	}
	
	@SubscribeEvent
	public void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event) {
		if (INVISIBLE.isTrue() && (event.entityLiving instanceof EntityCreature)) {
			if (event.target instanceof EntityPlayer) {
				((EntityLiving) event.entityLiving).setAttackTarget(null);
				event.entityLiving.setRevengeTarget(null);
			}
		}
	}
	
	public void pickupItems(EntityPlayer plr, World w) {
		
		if (w.isRemote)
			return;
		
		if (plr.isDead || plr.isSneaking()) 
			return;
		
		List<EntityItem> items = w.getEntitiesWithinAABB(EntityItem.class, plr.boundingBox.copy().expand(8, 8, 8));
		
		for (EntityItem item : items) {			
			if (item.lifespan == Integer.MAX_VALUE) {
				continue;
			}
			
			if (item.isDead) 
				continue;
			
			item.onCollideWithPlayer(plr);
		}
	}
	
	public void pickupOrbs(EntityPlayer plr, World w) {
		if (w.isRemote)
			return;
		
		if (plr.isDead || plr.isSneaking()) return;
		
		List<EntityXPOrb> orbs = w.getEntitiesWithinAABB(EntityXPOrb.class, plr.boundingBox.copy().expand(32, 32, 32));
		
		for (EntityXPOrb orb : orbs) {
			if (orb.isDead) continue;
			
			orb.onCollideWithPlayer(plr);
		}
	}
	
	public void removeDebuffs(EntityPlayer plr) {
		List<PotionEffect> effects = new ArrayList<PotionEffect>();
		effects.addAll(plr.getActivePotionEffects());
		
		for (Iterator iterator = plr.getActivePotionEffects().iterator(); iterator.hasNext();) {
            PotionEffect pe = (PotionEffect)iterator.next();
            if (pe == null)
            	continue;
            
            effects.add(pe);
		}
		
		for (PotionEffect pe : effects) {
			if (Potion.potionTypes[pe.getPotionID()] != null) {
            	if (Potion.potionTypes[pe.getPotionID()].isBadEffect()) {
            		plr.removePotionEffect(pe.getPotionID());
            	}
            }
		}
	}
	
	
	
	public boolean setItemDamage(ItemStack item, EntityLivingBase entity) {
		
		if (WANDVIS.isTrue() && Cheats.thaumcraftinstalled && item.getItem() instanceof ItemWandCasting) {
			ItemWandCasting iwc = (ItemWandCasting) item.getItem();
			
			for (Aspect aspect : Aspect.getPrimalAspects()) {
				iwc.storeVis(item, aspect, iwc.getMaxVis(item));
			}
		}
		
		if (ITEMDAMAGE.isTrue() && item.isItemStackDamageable() && item.isItemDamaged() && !(item.getItem() instanceof ItemBlock)) {
			NBTTagCompound tags = item.getTagCompound();
			if (tags != null && tags.hasKey("Energy") && tags.hasKey("EnergyMax")) {
				tags.setInteger("Energy", tags.getInteger("EnergyMax"));
				return true;
			} else if (tags != null && tags.hasKey("Energy")) {
				tags.setInteger("Energy", item.getMaxDamage());
				return true;
			} else {
				item.setItemDamage(0);
			}
			
			return true;
		}
		
		return false;
	}
	
	public void updateFurnace(TileEntityFurnace tile) {
		while(true) {
			if (tile.furnaceBurnTime > 0 && tile.furnaceCookTime < 199) {
				tile.furnaceBurnTime--;
				tile.furnaceCookTime++;
			} else {
				break;
			}
		}
	}
		
	private void updateSmeltery(SmelteryLogic tile) {		
		for (int i = 0; i < tile.maxBlockCapacity; i++) {
			if (tile.isStackInSlot(i) && tile.meltingTemps[i] > 200)
			{
				if (tile.activeTemps[i] < tile.meltingTemps[i]) {
					tile.activeTemps[i] = tile.meltingTemps[i];
				}
			}
		}
	}
	
	public void updateSieve(TileEntitySieve tile) {
		if (tile.mode == TileEntitySieve.SieveMode.FILLED) {
			tile.ProcessContents(true);
		}
	}
	
	public void updateCrucible() {
		if (FASTERCRUCIBLES.isTrue()) {
			if (HeatRegistry.getSpeed(Blocks.torch, 0) == 0.1F) {
				for (int i = 0 ; i <= 15 ; i++) {
					HeatRegistry.modify(Blocks.torch, i, 10.0F);
				}
			}
		} else {
			if (HeatRegistry.getSpeed(Blocks.torch, 0) == 10.0F) {
				for (int i = 0 ; i <= 15 ; i++) {
					HeatRegistry.modify(Blocks.torch, i, 0.1F);
				}
			}
		}
	}

	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent.Pre event) {
		if (FOOD.isTrue() && Minecraft.getMinecraft().ingameGUI instanceof GuiIngameForge) {
			GuiIngameForge gui = (GuiIngameForge) Minecraft.getMinecraft().ingameGUI;
			gui.renderFood = false;
		}
	}
}
