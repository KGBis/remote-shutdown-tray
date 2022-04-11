package com.kikegg.remote.shutdown;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Log
public class TrayActionListener implements ActionListener {

    private final ShowIpFrame ipFrame;

    TrayActionListener() {
        ipFrame = new ShowIpFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        MenuItem mi = (MenuItem) source;
        PopupMenu parent = (PopupMenu) mi.getParent();
        parent.setEnabled(false); // Disable the tray temporally

        switch (e.getActionCommand()) {
            case "EXIT_CMD":
                System.exit(0);
                break;
            case "IP_CMD":
                ipFrame.show(showIp(), parent);
                break;
            default:
                break;
        }
    }

    public String showIp() {
        Enumeration<NetworkInterface> nets;
        List<String> addresses = new ArrayList<>();
        try {
            nets = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface netInterface : Collections.list(nets)) {
                if (!netInterface.isLoopback()) {
                    Optional<InetAddress> first = netInterface.inetAddresses().filter(inetAddress -> inetAddress instanceof Inet4Address).findFirst();
                    first.ifPresent(inetAddress -> addresses.add(netInterface.getName() + ": " + inetAddress.getHostAddress()));
                }
            }

            addresses.sort(Comparator.naturalOrder());

            String s = "<b>Detected non-loopback interfaces:</b><p>";
            s = s + StringUtils.join(addresses, "<br>");
            return s;

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
