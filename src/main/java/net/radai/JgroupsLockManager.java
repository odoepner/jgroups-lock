package net.radai;

import org.jgroups.JChannel;
import org.jgroups.blocks.locking.LockService;

public class JgroupsLockManager implements LockManager{

    private JChannel channel;
    private LockService lockService;

    public JgroupsLockManager() {
        try {
            channel = new JChannel("udp-lock-stack.xml");
            lockService = new LockService(channel);
            channel.connect("LockCluster");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void close() {
        lockService.unlockAll();
        channel.close();
    }

    @Override
    public void lock(String name) {
        lockService.getLock(name).lock();
    }

    @Override
    public void release(String name) {
        lockService.getLock(name).unlock();
    }
}
