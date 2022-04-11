package com.kikegg.remote.shutdown;

import java.awt.*;

public class IconImage {

    private static final Image image;

    static {
        image = Toolkit.getDefaultToolkit().getImage(IconImage.class.getResource("/remote-control_16x16.png"));
    }

    public static Image getIcon() {
        return image;
    }
}
