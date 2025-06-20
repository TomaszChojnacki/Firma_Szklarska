package org.example.gui.admin;

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

        // Przycisk Klienci / Pracownicy / Usuwanie / Zarządzanie zamówieniami
        JPanel panelPrzyciski = new JPanel(new FlowLayout());

        JButton btnKlienci = new JButton("Klienci");
        JButton btnPracownicy = new JButton("Pracownicy");
        JButton btnUsuwanie = new JButton("Usuwanie użytkownika");
        JButton btnZarzadzanieZamowieniami = new JButton("Zarządzanie zamówieniami");

        Dimension btnSize = new Dimension(200, 40);
        btnKlienci.setPreferredSize(btnSize);
        btnPracownicy.setPreferredSize(btnSize);
        btnUsuwanie.setPreferredSize(btnSize);
        btnZarzadzanieZamowieniami.setPreferredSize(btnSize);

        panelPrzyciski.add(btnKlienci);
        panelPrzyciski.add(btnPracownicy);
        panelPrzyciski.add(btnUsuwanie);
        panelPrzyciski.add(btnZarzadzanieZamowieniami);

        add(panelPrzyciski, BorderLayout.PAGE_START);

        // Panel środkowy
        panelCenter = new JPanel(new BorderLayout());
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(panelCenter, BorderLayout.CENTER);

        // Akcje
        btnKlienci.addActionListener(e -> wyswietlListe("KLIENT"));
        btnPracownicy.addActionListener(e -> wyswietlListe("PRACOWNICY"));

        btnUsuwanie.addActionListener(e -> {
            panelCenter.removeAll();
            panelCenter.add(new PanelUsuwaniaUzytkownikow(), BorderLayout.CENTER);
            panelCenter.revalidate();
            panelCenter.repaint();
        });

        btnZarzadzanieZamowieniami.addActionListener(e -> {
            panelCenter.removeAll();
            panelCenter.add(new PanelZarzadzaniaZamowieniami(), BorderLayout.CENTER);
            panelCenter.revalidate();
            panelCenter.repaint();
        });

    }

    private void wyswietlListe(String rola) {
        panelCenter.removeAll();
        panelCenter.add(new PanelListaUzytkownikow(rola), BorderLayout.CENTER);
        panelCenter.revalidate();
        panelCenter.repaint();
    }
}
