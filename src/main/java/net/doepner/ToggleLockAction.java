package net.doepner;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Toggles the lock
 */
public class ToggleLockAction extends AbstractAction implements LockListener {

    private final static Icon LOCKED_ICON;
    private final static Icon UNLOCKED_ICON;

    static {
        final Class<?> cl = LockWindow.class;
        try {
            LOCKED_ICON = new ImageIcon(ImageIO.read(cl.getResource("locked.png")));
            UNLOCKED_ICON = new ImageIcon(ImageIO.read(cl.getResource("unlocked.png")));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private final ManagedLock managedLock;

    ToggleLockAction(ManagedLock managedLock) {
        this.managedLock = managedLock;
        updateIcon();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        managedLock.toggle();
        updateIcon();
    }

    private void updateIcon() {
        putValue(Action.LARGE_ICON_KEY, managedLock.isAcquired()
                ? LOCKED_ICON : UNLOCKED_ICON);
    }

    @Override
    public void onChange() {
        updateIcon();
    }
}
