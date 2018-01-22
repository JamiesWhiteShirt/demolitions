package com.jamieswhiteshirt.demolitions.core.plugin;

import com.jamieswhiteshirt.demolitions.core.SafeClassWriter;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.function.Consumer;

public class ClassTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
        }
        return basicClass;
    }

    private boolean equalsEither(String name, String srgName, String mcpName) {
        return name.equals(srgName) || name.equals(mcpName);
    }

    private byte[] transformClass(byte[] basicClass, Consumer<ClassNode> transformer) {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);

        transformer.accept(classNode);

        ClassWriter writer = new SafeClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private byte[] transformSingleMethod(byte[] basicClass, String srgName, String mcpName, String desc, Consumer<MethodNode> transformer) {
        return transformClass(basicClass, classNode -> {
            for (MethodNode methodNode : classNode.methods) {
                if (equalsEither(methodNode.name, srgName, mcpName) && methodNode.desc.equals(desc)) {
                    transformer.accept(methodNode);
                }
            }
        });
    }
}
