package com.vteba.service.json.jaxrs;

/**
 * Efficient key class, used instead of using <code>Class</code>.
 * The reason for having a separate key class instead of
 * directly using {@link Class} as key is mostly
 * to allow for redefining <code>hashCode</code> method --
 * for some strange reason, {@link Class} does not
 * redefine {@link Object#hashCode} and thus uses identity
 * hash, which is pretty slow. This makes key access using
 * {@link Class} unnecessarily slow.
 *<p>
 * Note: since class is not strictly immutable, caller must
 * know what it is doing, if changing field values.
 * @author yinlei
 * @since 2.2
 */
public final class ClassKey implements Comparable<ClassKey> {
    private String className;

    private Class<?> clazz;

    /**
     * Let's cache hash code straight away, since we are
     * almost certain to need it.
     */
    private int hashCode;

    public ClassKey() {
        clazz = null;
        className = null;
        hashCode = 0;
    }

    public ClassKey(Class<?> clz) {
        clazz = clz;
        className = clz.getName();
        hashCode = className.hashCode();
    }

    public void reset(Class<?> clz) {
        clazz = clz;
        className = clz.getName();
        hashCode = className.hashCode();
    }

    /*
    /**********************************************************
    /* Comparable
    /**********************************************************
     */

    // Just need to sort by name, ok to collide (unless used in TreeMap/Set!)
    @Override
    public int compareTo(ClassKey other) {
        return className.compareTo(other.className);
    }
    
    /*
    /**********************************************************
    /* Standard methods
    /**********************************************************
     */

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (o.getClass() != getClass()) return false;
        ClassKey other = (ClassKey) o;

        /* Is it possible to have different Class object for same name + class loader combo?
         * Let's assume answer is no: if this is wrong, will need to uncomment following functionality
         */
        /*
        return (other._className.equals(_className))
            && (other._class.getClassLoader() == _class.getClassLoader());
        */
        return other.clazz == clazz;
    }

    @Override public int hashCode() {
    	return hashCode;
    }

    @Override public String toString() {
    	return className;
    }    
}
