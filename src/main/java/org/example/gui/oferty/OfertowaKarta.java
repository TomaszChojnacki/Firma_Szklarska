package org.example.gui.oferty;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class OfertowaKarta extends JPanel {

    public OfertowaKarta(String nazwaObrazu, String tytul, Runnable akcjaKlikniecia) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(255, 255, 255));
        setPreferredSize(new Dimension(220, 260));
        setMaximumSize(new Dimension(220, 260));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JLabel obrazek = new JLabel();
        obrazek.setAlignmentX(Component.CENTER_ALIGNMENT);
        obrazek.setHorizontalAlignment(JLabel.CENTER);
        obrazek.setVerticalAlignment(JLabel.CENTER);

        try {
            URL imageURL = Objects.requireNonNull(getClass().getClassLoader().getResource("oferty/" + nazwaObrazu));
            BufferedImage img = ImageIO.read(imageURL);
            Image scaled = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaled);
            obrazek.setIcon(icon);
        } catch (IOException | NullPointerException e) {
            obrazek.setText("Brak zdjęcia");
            obrazek.setForeground(Color.RED);
            obrazek.setHorizontalAlignment(JLabel.CENTER);
        }

        JLabel tytulLabel = new JLabel(tytul);
        tytulLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tytulLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tytulLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        tytulLabel.setHorizontalAlignment(SwingConstants.CENTER);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (akcjaKlikniecia != null) akcjaKlikniecia.run();
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
                setBackground(new Color(230, 240, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                setBackground(Color.WHITE);
            }
        });

        add(obrazek);
        add(Box.createVerticalStrut(6));
        add(tytulLabel);
    }
}
