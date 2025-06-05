package org.example.gui.oferty;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;

public class OfertowaKarta extends JPanel {

    public OfertowaKarta(String nazwaObrazu, String tytul) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(250, 250, 255));
        setPreferredSize(new Dimension(270, 300));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel obrazek = new JLabel();
        obrazek.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            URL imageURL = Objects.requireNonNull(getClass().getClassLoader().getResource("oferty/" + nazwaObrazu));
            BufferedImage img = ImageIO.read(imageURL);
            Image scaled = img.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaled);
            obrazek.setIcon(icon);
            obrazek.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true));
        } catch (IOException e) {
            obrazek.setText("Brak zdjęcia");
            obrazek.setForeground(Color.RED);
        }

        JLabel tytulLabel = new JLabel(tytul);
        tytulLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tytulLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tytulLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        add(obrazek);
        add(tytulLabel);
    }
}
