package cjb.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cjb.api.common.CJBMod;
import cjb.api.common.ServerProxy;
import cjb.api.network.PacketCustomServer;
import cjb.api.common.Option;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="CJBAPI", version="2.0.0.1", dependencies="required-after:Forge@[10.13.0.1180,);")
public class CJBAPI {
	
	@SidedProxy(clientSide="cjb.api.client.ClientProxy", serverSide="cjb.api.common.ServerProxy")
	public static ServerProxy proxy;
	
	public static Map<String, CJBMod> cjbmods = new HashMap();
	private static Map<String, Properties> propslist = new HashMap();
	private static Map<String, Map<String, String>> optionlist = new HashMap();
	public static List<CJBMod> mods = new ArrayList();
	private static File cfgdir;
	public static boolean configDirty = false;
	public static CJBAPI INSTANCE;
	public static SimpleNetworkWrapper network;
	private static Logger logger = LogManager.getLogger("CJB Mods");
	
	@EventHandler
	public void init(FMLPreInitializationEvent event) {		
		INSTANCE = this;
		network = NetworkRegistry.INSTANCE.newSimpleChannel("CJBAPI");
		network.registerMessage(PacketCustomServer.Handler.class, PacketCustomServer.class, 0, Side.CLIENT);
		network.registerMessage(PacketCustomServer.Handler.class, PacketCustomServer.class, 1, Side.SERVER);
		proxy.initProxy();
	}
	
	private static void addCJBMod(CJBMod cjbmod, String name) {
		cjbmods.put(name, cjbmod);
		mods.add(cjbmod);
		propslist.put(name, new Properties());
		optionlist.put(name, new HashMap<String, String>());
		loadConfig(cjbmod.getClass().getSimpleName(), true);
		loadOptions(cjbmod);
		cjbmod.load();
	}
	public static int getInt(String mod, String s, int i) {
		if (propslist.get(mod).containsKey(s)) {
			optionlist.get(mod).put(s, propslist.get(mod).getProperty(s));
			return Integer.parseInt(optionlist.get(mod).get(s));
		} else {
			setInt(mod, s, i);
		}
		return i;
	}
	public static String getString(String mod, String s, String s1) {
		if (propslist.get(mod).containsKey(s)) {
			optionlist.get(mod).put(s, propslist.get(mod).getProperty(s));
			return optionlist.get(mod).get(s);
		} else {
			setString(mod, s, s1);
		}
		return s1;
	}
	public static void loadConfig(String mod, boolean startup) {
		try {
			cfgdir = proxy.getConfigDir(startup);
	        cfgdir.mkdirs();
	        File cfgfile = new File(cfgdir, mod + ".cfg");
	        if (cfgfile.exists() || cfgfile.createNewFile()) {
	            if (cfgfile.canRead()) {
	                FileInputStream var0 = new FileInputStream(cfgfile);
	                propslist.get(mod).load(var0);
	                var0.close();
	            }
	        }
		} catch (Throwable e) {e.printStackTrace();}
    }
	private static void loadOptions(CJBMod cjbmod) {
		for ( Option option : cjbmod.getOptions()) {
			if (option.getDefaultValue() instanceof Integer) {
				option.getInt();
			} else if (option.getDefaultValue() instanceof String) {
				option.getString();
			} else if (option.getDefaultValue() instanceof Boolean) {
				option.isTrue();
			}
		}
	}
	
	public static void registerCJBmod(Class c) {
		if (CJBMod.class.isAssignableFrom(c)) {
			try {
				CJBMod cjbmod = (CJBMod)c.newInstance();
				addCJBMod(cjbmod, cjbmod.getClass().getSimpleName());
				logger.info("Loaded mod: " + cjbmod.getClass().getSimpleName());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	public static void saveConfig(String mod) {
		try {
			cfgdir = proxy.getConfigDir(false);
	        cfgdir.mkdirs();
	        File cfgfile = new File(cfgdir, mod + ".cfg");
	        if (cfgfile.exists() || cfgfile.createNewFile()) {
	            if (cfgfile.canWrite()) {
	                FileOutputStream var0 = new FileOutputStream(cfgfile);
	                propslist.get(mod).store(var0, mod);
	                var0.close();
	            }
	        }
		} catch (Throwable e) {e.printStackTrace();}
    }
	
	public static void setInt(String mod, String s, int i) {
		propslist.get(mod).setProperty(s, Integer.toString(i));
		optionlist.get(mod).put(s, Integer.toString(i));
		saveConfig(mod);
	}
	public static void setString(String mod, String s, String s1) {
		propslist.get(mod).setProperty(s, s1);
		optionlist.get(mod).put(s, s1);
		saveConfig(mod);
	}
	
	public static int updateInt(String mod, String s, int max) {
		
		int i = Integer.parseInt(optionlist.get(mod).get(s));
		
		if (++i < max)
			setInt(mod, s, i);
		else
			i = 0;
		
		setInt(mod, s, i);
		return i;
	}
}
