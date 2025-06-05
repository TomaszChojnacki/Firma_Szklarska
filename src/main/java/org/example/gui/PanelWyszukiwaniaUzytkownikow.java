package org.example.gui;

import javax.swing.*;
import java.awt.*;

public class PanelWyszukiwaniaUzytkownikow extends JPanel {

    private JPanel panelCenter;

    public PanelWyszukiwaniaUzytkownikow() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Tytuł
        JLabel naglowek = new JLabel("Wyszukiwanie użytkowników", JLabel.CENTER);
        naglowek.setFont(new Font("SansSerif", Font.BOLD, 22));
        naglowek.setForeground(new Color(63, 81, 181));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(naglowek, BorderLayout.NORTH);

        // Przycisk Klienci / Pracownicy
        JPanel panelPrzyciski = new JPanel(new FlowLayout());
        JButton btnKlienci = new JButton("Klienci");
        JButton btnPracownicy = new JButton("Pracownicy");

        btnKlienci.setPreferredSize(new Dimension(150, 40));
        btnPracownicy.setPreferredSize(new Dimension(150, 40));

        panelPrzyciski.add(btnKlienci);
        panelPrzyciski.add(btnPracownicy);
        add(panelPrzyciski, BorderLayout.PAGE_START);

        // Panel, do którego będą się ładować listy
        panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(panelCenter, BorderLayout.CENTER);

        // Akcje
        btnKlienci.addActionListener(e -> wyswietlListe("KLIENT"));
        btnPracownicy.addActionListener(e -> wyswietlListe("PRACOWNICY"));
    }

    private void wyswietlListe(String rola) {
        panelCenter.removeAll();
        panelCenter.add(new PanelListaUzytkownikow(rola), BorderLayout.CENTER);
        panelCenter.revalidate();
        panelCenter.repaint();
    }
}
