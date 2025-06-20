package org.example.gui.zamowienia;

import org.example.baza.BazaDanych;
import org.example.klient.Klient;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelListaZamowien extends JPanel {

    public PanelListaZamowien() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Nagłówek
        JLabel naglowek = new JLabel("Twoje zamówienia", JLabel.CENTER);
        naglowek.setFont(new Font("Segoe UI", Font.BOLD, 26));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(naglowek, BorderLayout.NORTH);

        // Panel z listą zamówień
        JPanel panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);

        List<String[]> zamowienia = BazaDanych.pobierzZamowieniaGrupowane(Klient.zalogowanyLogin);

        if (zamowienia.isEmpty()) {
            JLabel brak = new JLabel("Brak zamówień", JLabel.CENTER);
            brak.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            brak.setForeground(Color.GRAY);
            panelLista.add(Box.createVerticalStrut(30));
            panelLista.add(brak);
        } else {
            for (String[] zamowienie : zamowienia) {
                String id = zamowienie[0];
                String status = zamowienie[1];

                // Zewnętrzna "karta" zamówienia z marginesem
                JPanel karta = new JPanel(new BorderLayout());
                karta.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
                karta.setOpaque(false);

                // Wewnętrzna ramka
                JPanel panelZamowienia = new JPanel(new BorderLayout());
                panelZamowienia.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                        BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
                panelZamowienia.setBackground(new Color(250, 250, 250));
                panelZamowienia.setMaximumSize(new Dimension(700, 100));
                panelZamowienia.setAlignmentX(Component.CENTER_ALIGNMENT);

                // Tekst zamówienia
                JTextArea tekst = new JTextArea("Zamówienie #" + id + "\nStatus: " + status);
                tekst.setWrapStyleWord(true);
                tekst.setLineWrap(true);
                tekst.setEditable(false);
                tekst.setOpaque(false);
                tekst.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                tekst.setMargin(new Insets(0, 0, 0, 10));
                panelZamowienia.add(tekst, BorderLayout.CENTER);

                // Przycisk szczegółów
                JButton btnSzczegoly = new JButton("Szczegóły");
                btnSzczegoly.setPreferredSize(new Dimension(100, 30));
                btnSzczegoly.setBackground(new Color(33, 150, 243));
                btnSzczegoly.setForeground(Color.WHITE);
                btnSzczegoly.setFocusPainted(false);

                btnSzczegoly.addActionListener(e -> {
                    JFrame okno = (JFrame) SwingUtilities.getWindowAncestor(this);
                    if (okno instanceof org.example.gui.Okno) {
                        ((org.example.gui.Okno) okno).wyswietlPanel(new PanelSzczegolyZamowienia(id));
                    }
                });

                panelZamowienia.add(btnSzczegoly, BorderLayout.EAST);

                karta.add(panelZamowienia, BorderLayout.CENTER);
                panelLista.add(karta);
            }
        }

        JScrollPane scrollPane = new JScrollPane(panelLista);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
}
