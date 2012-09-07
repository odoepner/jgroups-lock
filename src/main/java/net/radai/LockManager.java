package net.radai;

public interface LockManager {
    void lock(String name);
    void release (String name);
}
