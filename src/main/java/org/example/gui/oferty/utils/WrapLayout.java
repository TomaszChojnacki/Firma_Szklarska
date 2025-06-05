package org.example.gui.oferty.utils;

import java.awt.*;
import javax.swing.*;

public class WrapLayout extends FlowLayout {

    public WrapLayout() {
        super();
    }

    public WrapLayout(int align) {
        super(align);
    }

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        return layoutSize(target, false);
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        synchronized (target.getTreeLock()) {
            int targetWidth = target.getParent() instanceof JScrollPane
                    ? target.getParent().getWidth()
                    : target.getWidth();

            if (targetWidth == 0) targetWidth = Integer.MAX_VALUE;

            Insets insets = target.getInsets();
            int maxWidth = targetWidth - insets.left - insets.right - (getHgap() * 2);

            int x = 0, y = getVgap(), rowHeight = 0;

            for (Component comp : target.getComponents()) {
                if (!comp.isVisible()) continue;
                Dimension d = preferred ? comp.getPreferredSize() : comp.getMinimumSize();

                if ((x == 0) || (x + d.width <= maxWidth)) {
                    if (x > 0) x += getHgap();
                    x += d.width;
                    rowHeight = Math.max(rowHeight, d.height);
                } else {
                    x = d.width;
                    y += getVgap() + rowHeight;
                    rowHeight = d.height;
                }
            }

            y += rowHeight + getVgap();
            return new Dimension(targetWidth, y);
        }
    }
}
