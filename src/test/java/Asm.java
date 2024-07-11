import top.yqingyu.common.asm.ClassWriter;
import top.yqingyu.common.asm.MethodVisitor;

import java.lang.reflect.Method;

import static top.yqingyu.common.asm.Opcodes.*;

public class Asm {
    public static void main(String[] args) throws Exception {
        // 创建类
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_8, ACC_PUBLIC + ACC_SUPER, "com/example/MyDynamicClass", null, "java/lang/Object", null);

        // 创建静态方法
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "sayHello", "()V", null, null);
        mv.visitCode();
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Hello from ASM!");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();

        // 结束类的定义
        cw.visitEnd();

        // 将字节码转换为字节数组
        byte[] classData = cw.toByteArray();

        // 使用Java的动态加载机制加载新创建的类
        Class<?> myClass = new MyClassLoader().defineClass("com.example.MyDynamicClass", classData);

        // 调用静态方法
        Method method = myClass.getMethod("sayHello");
        method.invoke(null);
    }

    private static class MyClassLoader extends ClassLoader {
        public Class<?> defineClass(String name, byte[] b) {
            return super.defineClass(name, b, 0, b.length);
        }
    }
}
