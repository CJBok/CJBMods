package cjb.api.asm;

import java.util.Iterator;
import java.util.Random;

import cjb.api.CJB;
import net.minecraft.block.Block;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import cpw.mods.fml.common.Loader;
import static org.objectweb.asm.Opcodes.*;

public class CJBClassTransformer implements IClassTransformer {
	
	Logger coreLogger = LogManager.getLogger("<CJB ASM>");
	public static Random rnd = new Random();
	

	@Override
	public byte[] transform(String obfName, String transformedName, byte[] basicClass) {
		
		if (transformedName.equals("net.minecraft.block.BlockLeavesBase")) {
			return patchBlockLeavesBase(basicClass);
		}
		
		if (transformedName.equals("net.minecraft.world.WorldProviderSurface")) {
			return patchWorldProviderSurface(basicClass);
		}
		
		return basicClass;
	}
	
	private byte[] patchBlockLeavesBase(byte[] basicClass) {
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		coreLogger.log(Level.INFO, "Found Leave Class: " + classNode.name);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);

		String methodName = mcp() ? "onNeighborBlockChange" : "func_149695_a" ; //Block/onNeighborBlockChange
		String worldClass = "net/minecraft/world/World";
		String blockClass = "net/minecraft/block/Block";
		
		MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, methodName, "(L" + worldClass + ";IIIL" + blockClass + ";)V", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(17, l0);
		mv.visitFieldInsn(GETSTATIC, "cjb/api/CJB", "FASTLEAVEDECAY", "Z");
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(18, l2);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ILOAD, 2);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitVarInsn(ILOAD, 4);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitMethodInsn(INVOKESTATIC, "cjb/api/asm/CJBClassTransformer", "updateLeaveBlock", "(L" + worldClass + ";IIIL" + blockClass + ";)V", false);
		mv.visitLabel(l1);
		mv.visitLineNumber(20, l1);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitInsn(RETURN);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLocalVariable("this", "Lcjb/api/asm/CJBClassTransformer;", null, l0, l3, 0);
		mv.visitLocalVariable("p_149695_1_", "L" + worldClass + ";", null, l0, l3, 1);
		mv.visitLocalVariable("p_149695_2_", "I", null, l0, l3, 2);
		mv.visitLocalVariable("p_149695_3_", "I", null, l0, l3, 3);
		mv.visitLocalVariable("p_149695_4_", "I", null, l0, l3, 4);
		mv.visitLocalVariable("p_149695_5_", "L" + blockClass + ";", null, l0, l3, 5);
		mv.visitMaxs(5, 6);
		mv.visitEnd();
		
		return writer.toByteArray();
	}
	
	private byte[] patchWorldProviderSurface(byte[] basicClass) {
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(basicClass);
		classReader.accept(classNode, 0);
		coreLogger.log(Level.INFO, "Found WorldProviderSurface Class: " + classNode.name);

		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);

		String methodName = mcp() ? "getVoidFogYFactor" : "func_76565_k" ; //Block/onNeighborBlockChange
		String worldClass = "net/minecraft/world/World";
		String blockClass = "net/minecraft/block/Block";
			
		MethodVisitor mv = writer.visitMethod(ACC_PUBLIC, methodName, "()D", null, null);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(27, l0);
		mv.visitFieldInsn(GETSTATIC, "cjb/api/CJB", "NOVOID", "Z");
		Label l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(28, l2);
		mv.visitInsn(DCONST_1);
		mv.visitInsn(DRETURN);
		mv.visitLabel(l1);
		mv.visitLineNumber(30, l1);
		mv.visitFrame(F_NEW, 1, new Object[] {"cjb/helper/WorldProviderSurface"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/world/WorldProvider", methodName, "()D", false);
		mv.visitInsn(DRETURN);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLocalVariable("this", "Lnet/minecraft/world/WorldProviderSurface;", null, l0, l3, 0);
		mv.visitMaxs(2, 1);
		mv.visitEnd();
		
		
		methodName = mcp() ? "getWorldHasVoidParticles" : "func_76564_j";
		worldClass = "net/minecraft/world/World";
		blockClass = "net/minecraft/block/Block";
		
		mv = writer.visitMethod(ACC_PUBLIC, methodName, "()Z", null, null);
		mv.visitCode();
		l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(18, l0);
		mv.visitFieldInsn(GETSTATIC, "cjb/api/CJB", "NOVOID", "Z");
		l1 = new Label();
		mv.visitJumpInsn(IFEQ, l1);
		l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(19, l2);
		mv.visitInsn(ICONST_0);
		mv.visitInsn(IRETURN);
		mv.visitLabel(l1);
		mv.visitLineNumber(22, l1);
		mv.visitFrame(F_NEW, 1, new Object[] {"cjb/helper/WorldProviderSurface"}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 0);
		mv.visitMethodInsn(INVOKESPECIAL, "net/minecraft/world/WorldProvider", methodName, "()Z", false);
		mv.visitInsn(IRETURN);
		l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLocalVariable("this", "Lnet/minecraft/world/WorldProviderSurface;", null, l0, l3, 0);
		mv.visitMaxs(1, 1);
		mv.visitEnd();		
		
		return writer.toByteArray();
	}
	
	public static void updateLeaveBlock(World world , int x, int y, int z, Block block) {
		world.scheduleBlockUpdate(x, y, z, block, 7 + rnd.nextInt(5));
	}
	
	private static boolean mcp() {
		return CJBLoadingPlugin.IN_MCP;
	}
}
