package com.kikegg.remote.shutdown;

import lombok.extern.java.Log;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;


@Log
public class ShowIpFrame implements FocusListener {

    private final JFrame frame;
    private final JLabel label = new JLabel();
    private PopupMenu parentPopup;

    ShowIpFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}

        frame = new JFrame("Remote Shutdown");
        frame.setIconImage(IconImage.getIcon());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLayout(new BorderLayout(5, 5));
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
        frame.addFocusListener(this);
    }

    public void show(String ipText, PopupMenu parent) {
        this.parentPopup = parent;
        EventQueue.invokeLater(() -> {
            // remove old label, IP should not change but just in case...
            frame.remove(label);

            // set new label text and border
            label.setText("<html>" +
                    "<span style=\"text-align: center;\">" +
                    "By Enrique García (c) 2022  (kike.g.garcia at gmail.com)" + "<br>" +
                    "Based on the 'RemoteShutdownPCServer' by Isah Rikovic (https://github.com/rikovicisah) (rikovicisah at gmail.com)" +
                    "</span><p/><p/>" +
                    "<span>" +
                    ipText +"  <i>(copied to clipboard)</i>" +
                    "</span>" +
                    "</html>");
            label.setBorder(new EmptyBorder(10,10,10,10));

            // add the label, pack and make visible
            frame.add(label);
            frame.pack();
            frame.setVisible(true);
        });

        copyToClipboard(ipText);
    }

    private void copyToClipboard(String ip) {
        StringSelection stringSelection = new StringSelection(ip);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        log.info("IP " + ip + " copied to the clipboard.");
    }

    @Override
    public void focusGained(FocusEvent e) {}

    @Override
    public void focusLost(FocusEvent e) {
        this.parentPopup.setEnabled(true);
    }
}

