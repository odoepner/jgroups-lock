package net.doepner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class LockWindow extends JFrame {

    public LockWindow(ManagedLock managedLock) {
        super("distributed lock : " + System.currentTimeMillis() % 10000);
        final ToggleLockAction action = new ToggleLockAction(managedLock);
        managedLock.addListener(action);
        add(new JButton(action));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
