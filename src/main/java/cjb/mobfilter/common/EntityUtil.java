package cjb.mobfilter.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import cjb.mobfilter.client.GuiButtonMob;

public class EntityUtil {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	public static List<EntityData> entities = new ArrayList<EntityData>();
	
	private static HashMap<String,String> modSource_Name = new HashMap();
	private static HashMap<String,String> modSource_ID = new HashMap();
	
	private static boolean initialized = false;
	
	public static void Init() {
		
		if (initialized) return;
		
		modSource_Name = new HashMap();
		modSource_ID = new HashMap();
		
		modSource_Name.put("Minecraft", "Minecraft");
		modSource_Name.put("1.6.2.jar", "Minecraft");
	    modSource_Name.put("1.6.3.jar", "Minecraft");
	    modSource_Name.put("1.6.4.jar", "Minecraft");
	    modSource_Name.put("1.7.2.jar", "Minecraft");
	    modSource_Name.put("1.7.10.jar", "Minecraft");
	    modSource_Name.put("Forge", "Minecraft");
	    modSource_Name.put("forgeSrc", "Minecraft");
	    modSource_ID.put("Minecraft", "Minecraft");
	    modSource_ID.put("1.6.2.jar", "Minecraft");
	    modSource_ID.put("1.6.3.jar", "Minecraft");
	    modSource_ID.put("1.6.4.jar", "Minecraft");
	    modSource_ID.put("1.7.2.jar", "Minecraft");
	    modSource_ID.put("1.7.10.jar", "Minecraft");
	    modSource_ID.put("Forge", "Minecraft");
	    modSource_ID.put("forgeSrc", "Minecraft");
	    
	    for (ModContainer mod : Loader.instance().getModList()) {
			modSource_Name.put(mod.getSource().getName(), mod.getName());
			modSource_ID.put(mod.getSource().getName(), mod.getModId());
		}
		
		entities.clear();
		Map<Class, String> entitylist = EntityList.classToStringMapping;
		Iterator<String> it = entitylist.values().iterator();
		
		while (it.hasNext()) {
			String entityname = it.next();
			try {
				if (EntityCreature.class.isAssignableFrom((Class<?>) EntityList.stringToClassMapping.get(entityname)) || IMob.class.isAssignableFrom((Class<?>) EntityList.stringToClassMapping.get(entityname))){
					EntityData entdata = new EntityData();
					entdata.name = entityname;
					
					Entity ent = EntityList.createEntityByName(entdata.name, mc.theWorld);
					if (ent != null) {
						entities.add(entdata);
						ent.setDead();
					}
				}
			} catch (Throwable e) { }
		}
		
		for (EntityData entdata : entities) {
			String displayName = "";
        	if (I18n.format("entity." + entdata.name + ".name").contains(".")) {
        		displayName += entdata.name;
        	} else {
        		displayName += I18n.format("entity." + entdata.name + ".name");
        	}
        	
        	Entity ent = EntityList.createEntityByName(entdata.name, mc.theWorld);
        	
        	if (ent != null) {
        		entdata.displayName = "- " + ent.getCommandSenderName();
        		entdata.mod = nameFromObject(ent);
        		entdata.id = ent.getCommandSenderName();
            	ent.setDead();
        	}
		}
		
		Collections.sort(entities, new Comparator<EntityData>() {
			@Override
			public int compare(EntityData  ent1, EntityData  ent2) {
	            return  ent1.displayName.compareTo(ent2.displayName);
	        }
		});
				
		initialized = true;
	}
	
	public static String nameFromObject(Object obj) {
		String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		try {
			objPath = URLDecoder.decode(objPath, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		String modName = "<Unknown>";
		for (String s : modSource_Name.keySet()) {
			if (objPath.contains(s)) {
				modName = (String)modSource_Name.get(s);
				break;
			}
		}
		if (modName.equals("Minecraft Coder Pack")) {
			modName = "Minecraft";
		}
	    return modName;
	}
	
	public static String idFromObject(Object obj) {
		String objPath = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
		try {
			objPath = URLDecoder.decode(objPath, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		}
		String modName = "<Unknown>";
		for (String s : modSource_ID.keySet()) {
			if (objPath.contains(s)) {
				modName = (String)modSource_ID.get(s);
				break;
			}
		}
		if (modName.equals("Minecraft Coder Pack")) {
			modName = "Minecraft";
		}
		return modName;
	}
	
	public static List<String> GetUsedMods() {
		
		List<String> mods = new ArrayList<String>();
		for (EntityData entdata : entities) {
			if (!mods.contains(entdata.mod)) {
				mods.add(entdata.mod);
			}
		}
		
		return mods;
	}
}
