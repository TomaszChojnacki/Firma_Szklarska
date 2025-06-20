package org.example.gui.zamowienia;

import org.example.baza.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelSzczegolyZamowienia extends JPanel {

    public PanelSzczegolyZamowienia(String grupaId) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Górny panel z przyciskiem powrotu
        JPanel panelGora = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGora.setBackground(Color.WHITE);
        panelGora.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        JButton btnPowrot = new JButton("← Powrót do listy zamówień");
        btnPowrot.setBackground(new Color(33, 150, 243));
        btnPowrot.setForeground(Color.WHITE);
        btnPowrot.setFocusPainted(false);
        btnPowrot.setPreferredSize(new Dimension(220, 35));
        btnPowrot.addActionListener(e -> {
            JFrame okno = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (okno instanceof org.example.gui.Okno) {
                ((org.example.gui.Okno) okno).wyswietlPanel(new PanelListaZamowien());
            }
        });

        panelGora.add(btnPowrot);
        add(panelGora, BorderLayout.NORTH);

        // Panel zawartości
        JPanel panelZawartosc = new JPanel();
        panelZawartosc.setLayout(new BoxLayout(panelZawartosc, BoxLayout.Y_AXIS));
        panelZawartosc.setBackground(Color.WHITE);
        panelZawartosc.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // Nagłówek pod przyciskiem
        JLabel tytul = new JLabel("Szczegóły zamówienia #" + grupaId);
        tytul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tytul.setForeground(new Color(33, 150, 243));
        tytul.setAlignmentX(Component.CENTER_ALIGNMENT);
        tytul.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        panelZawartosc.add(tytul);

        // Lista pozycji
        double suma = 0;
        List<String[]> pozycje = BazaDanych.pobierzPozycjeZamowienia(grupaId);

        if (pozycje.isEmpty()) {
            JLabel brak = new JLabel("Brak pozycji w tym zamówieniu.");
            brak.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            brak.setForeground(Color.GRAY);
            panelZawartosc.add(brak);
        } else {
            for (String[] p : pozycje) {
                String opis = p[0];
                double cena = Double.parseDouble(p[1].replace(",", "."));
                suma += cena;

                JPanel panelPozycja = new JPanel(new BorderLayout());
                panelPozycja.setMaximumSize(new Dimension(700, 90));
                panelPozycja.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelPozycja.setBackground(new Color(245, 248, 255));
                panelPozycja.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(220, 220, 220)),
                        BorderFactory.createEmptyBorder(10, 15, 10, 15)
                ));

                JTextArea opisArea = new JTextArea(opis + "\nCena: " + String.format("%.2f zł", cena));
                opisArea.setLineWrap(true);
                opisArea.setWrapStyleWord(true);
                opisArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                opisArea.setEditable(false);
                opisArea.setOpaque(false);

                panelPozycja.add(opisArea, BorderLayout.CENTER);
                panelZawartosc.add(panelPozycja);
                panelZawartosc.add(Box.createVerticalStrut(10));
            }

            //  Łączna cena
            JLabel sumaLabel = new JLabel("Łączna cena: " + String.format("%.2f zł", suma));
            sumaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            sumaLabel.setForeground(new Color(76, 175, 80));
            sumaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sumaLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
            panelZawartosc.add(sumaLabel);
        }

        JScrollPane scrollPane = new JScrollPane(panelZawartosc);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }
}
