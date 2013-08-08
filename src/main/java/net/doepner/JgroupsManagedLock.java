package net.doepner;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;

import org.jgroups.JChannel;
import org.jgroups.blocks.locking.LockService;
import org.jgroups.protocols.CENTRAL_LOCK;
import org.jgroups.stack.Protocol;

public class JgroupsManagedLock implements ManagedLock {

    private final JChannel channel;
    private final Lock lock;
    private final String name;

    private boolean acquired = false;

    public JgroupsManagedLock(String name) {
        this.name = name;
        final LockService lockService;
        try {
            channel = createChannel();
            lockService = new LockService(channel);
            channel.connect("LockCluster");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        lock = lockService.getLock(name);
    }

    private JChannel createChannel() throws Exception {
        final JChannel ch = new JChannel();
        final Protocol lockProtocol = new CENTRAL_LOCK();
        lockProtocol.init();
        ch.getProtocolStack().addProtocol(lockProtocol);
        return ch;
    }

    @Override
    public void toggle() {
        if (acquired) {
            lock.unlock();
        } else {
            lock.lock();
        }
        acquired = !acquired;
        notifyListeners();
    }

    @Override
    public boolean isAcquired() {
        return acquired;
    }

    @Override
    public void close() {
        lock.unlock();
        channel.close();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": name=" + name + ", acquired=" + acquired;
    }

    private final List<LockListener> listeners = new LinkedList<LockListener>();

    @Override
    public void addListener(LockListener l) {
        listeners.add(l);
    }

    private void notifyListeners() {
        for (LockListener listener : listeners) {
            listener.onChange();
        }
    }
}
