package cjb.api.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;;

@TransformerExclusions({"cjb.api.asm"})
@MCVersion("1.7.10")
public class CJBLoadingPlugin implements IFMLLoadingPlugin{
	
	public static boolean IN_MCP = false;
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {CJBClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return "cjb.api.asm.CJBASMModContainer";
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		IN_MCP = !(Boolean)data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return "cjb.api.asm.CJBAT";
	}

}
