package cjb.api.asm;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class CJBAT extends AccessTransformer {

	public CJBAT() throws IOException {
		super("cjb_at.cfg");
		
	}
}
