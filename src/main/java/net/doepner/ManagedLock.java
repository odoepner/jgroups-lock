package net.doepner;

public interface ManagedLock {

    void toggle();

    void close();

    boolean isAcquired();

    void addListener(LockListener l);
}
