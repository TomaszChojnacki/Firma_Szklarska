package org.example.gui.oferty;

import org.example.gui.oferty.utils.WrapLayout;

import javax.swing.*;
import java.awt.*;

public class PanelOferty extends JPanel {

    public PanelOferty() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 🔵 Nagłówek
        JLabel naglowek = new JLabel("Wybierz rodzaj szkła", JLabel.CENTER);
        naglowek.setFont(new Font("Segoe UI", Font.BOLD, 30));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(naglowek, BorderLayout.NORTH);

        // 🔵 Prawdziwa galeria
        JPanel galeria = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        galeria.setBackground(new Color(245, 245, 255));
        galeria.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 📸 Dodaj oferty
        galeria.add(new OfertowaKarta("pojedyncze.png", "Pojedyncze przeszklenie"));
        galeria.add(new OfertowaKarta("podwojne.png", "Podwójne szkło"));
        galeria.add(new OfertowaKarta("potrojne.png", "Potrójne przeszklenie"));
        galeria.add(new OfertowaKarta("voorzetraam.png", "Voorzetraam"));


        // 🔵 Bardzo WAŻNE:
        galeria.setPreferredSize(new Dimension(800, 1200));
        // rozmiar galerii większy niż widok, by wymusić scroll

        // 🔽 Scrollowanie TYLKO galerii
        JScrollPane scrollPane = new JScrollPane(galeria);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 🔵 Osobny panel środkowy
        JPanel panelSrodkowy = new JPanel(new BorderLayout());
        panelSrodkowy.add(scrollPane, BorderLayout.CENTER);

        add(panelSrodkowy, BorderLayout.CENTER);
    }
}
