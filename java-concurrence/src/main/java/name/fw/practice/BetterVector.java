package name.fw.practice;

import name.fw.practice.annotations.ThreadSafe;

import java.util.Vector;

/**
 * BetterVector
 * <p/>
 * Extending Vector to have a put-if-absent _03method
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class BetterVector <E> extends Vector<E> {
    // When extending a serializable class, you should redefine serialVersionUID
    static final long serialVersionUID = -3963416950630760754L;

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent)
            add(x);
        return absent;
    }
}
