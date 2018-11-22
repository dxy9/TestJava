package dfw.asm._03method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * Created by dell on 2018/5/5
 */
public class StatelessChangeMethod {

    /**
     * public class C {
     *      public void m() throws Exception {
     *          Thread.sleep(100);
     *      }
     * }
     *
     * 转换为
     *
     * public class C {
     *    public static long timer;
     *    public void m() throws Exception {
     *         timer -= System.currentTimeMillis();
     *         Thread.sleep(100);
     *         timer += System.currentTimeMillis();
     *    }
     * }
     *
     *
     */

    public class AddTimerAdapter extends ClassVisitor {
        private String owner;
        private boolean isInterface;
        public AddTimerAdapter(ClassVisitor cv) {
            super(ASM4, cv);
        }
        @Override public void visit(int version, int access, String name,String signature, String superName, String[] interfaces) {
            cv.visit(version, access, name, signature, superName, interfaces);
            owner = name;
            isInterface = (access & ACC_INTERFACE) != 0;
        }
        @Override public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature,exceptions);
            if (!isInterface && mv != null && !name.equals("<init>")) {
                mv = new AddTimerMethodAdapter(mv);
            }
            return mv;
        }
        @Override public void visitEnd() {
            if (!isInterface) {
                FieldVisitor fv = cv.visitField(ACC_PUBLIC + ACC_STATIC, "timer","J", null, null);
                if (fv != null) {
                    fv.visitEnd();
                }
            }
            cv.visitEnd();
        }
        class AddTimerMethodAdapter extends MethodVisitor {
            public AddTimerMethodAdapter(MethodVisitor mv) {
                super(ASM4, mv);
            }
            @Override public void visitCode() {
                mv.visitCode();
                mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                mv.visitMethodInsn(INVOKESTATIC, "java/lang/System","currentTimeMillis", "()J");
                mv.visitInsn(LSUB);
                mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
            }
            @Override public void visitInsn(int opcode) {
                if ((opcode >= IRETURN && opcode <= RETURN) || opcode == ATHROW) {
                    mv.visitFieldInsn(GETSTATIC, owner, "timer", "J");
                    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System","currentTimeMillis", "()J");
                    mv.visitInsn(LADD);
                    mv.visitFieldInsn(PUTSTATIC, owner, "timer", "J");
                }
                mv.visitInsn(opcode);
            }
            @Override public void visitMaxs(int maxStack, int maxLocals) {
                mv.visitMaxs(maxStack + 4, maxLocals);
            }
        }
    }

}
