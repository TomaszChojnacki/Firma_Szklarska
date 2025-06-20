package org.example.gui.oferty;

import org.example.gui.Okno;
import org.example.gui.oferty.utils.WrapLayout;

import javax.swing.*;
import java.awt.*;

public class PanelOferty extends JPanel {

    public PanelOferty() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naglowek = new JLabel("Wybierz rodzaj szkła", JLabel.CENTER);
        naglowek.setFont(new Font("Segoe UI", Font.BOLD, 28));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(naglowek, BorderLayout.NORTH);

        // Galeria z WrapLayout
        JPanel galeria = new JPanel(new WrapLayout(FlowLayout.LEFT, 20, 20));
        galeria.setBackground(new Color(245, 245, 255));

        galeria.add(new OfertowaKarta("pojedyncze.png", "Pojedyncze przeszklenie", () -> otworzPanel(new PanelZamowieniaPojedyncze())));
        galeria.add(new OfertowaKarta("podwojne.png", "Podwójne szkło", () -> otworzPanel(new PanelZamowieniaPodwojne())));
        galeria.add(new OfertowaKarta("potrojne.png", "Potrójne przeszklenie", () -> otworzPanel(new PanelZamowieniaPotrojne())));
        galeria.add(new OfertowaKarta("voorzetraam.png", "Voorzetraam", () -> otworzPanel(new PanelZamowieniaVoorzetraam())));

        //JScrollPane otaczający galerię
        JScrollPane scrollPane = new JScrollPane(galeria,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(245, 245, 255));

        //Najważniejsze: ustawiamy preferowaną wysokość, by ScrollPane miał co przewijać
        galeria.setPreferredSize(new Dimension(800, 600)); // W razie potrzeby możesz zmieniać

        add(scrollPane, BorderLayout.CENTER);
    }

    private void otworzPanel(JPanel panelDocelowy) {
        JFrame okno = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (okno instanceof Okno oknoGlowne) {
            oknoGlowne.wyswietlPanel(panelDocelowy);
        }
    }
}
