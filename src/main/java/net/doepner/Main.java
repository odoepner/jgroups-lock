package net.doepner;

import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) throws IOException {
        new Main();
    }

    public Main() throws IOException {
        final ManagedLock managedLock = new JgroupsManagedLock("myLock");

        if (!GraphicsEnvironment.isHeadless()) {
            gui(managedLock);
        }
        final Console console = System.console();
        if (console != null) {
            readInput(console, managedLock);
        }
    }

    private void readInput(Console console, final ManagedLock managedLock)
            throws IOException {
        final Reader in = console.reader();
        final PrintWriter out = console.writer();

        final LockListener listener = new LockListener() {
            @Override
            public void onChange() {
                out.println(managedLock);
                out.println("press key to toggle(t) or quit(q)");
            }
        };
        listener.onChange();
        managedLock.addListener(listener);

        for (char ch = 0; ch != -1; ) {
            ch = (char) in.read();
            switch (ch) {
                case 't': {
                    managedLock.toggle();
                    break;
                }
                case 'q': {
                    managedLock.close();
                    System.exit(0);
                }
            }
        }

    }

    private void gui(final ManagedLock managedLock) {
        try {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                final LockWindow lockWindow = new LockWindow(managedLock);
                lockWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        managedLock.close();
                    }
                });
            }
        });
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
