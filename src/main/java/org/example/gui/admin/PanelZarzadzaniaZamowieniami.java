package org.example.gui.admin;

import org.example.baza.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelZarzadzaniaZamowieniami extends JPanel {

    private JPanel panelLista;
    private JTextField poleLogin;
    private String ostatnioWyszukanyLogin = "";

    public PanelZarzadzaniaZamowieniami() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Nagłówek
        JLabel naglowek = new JLabel("Zarządzanie zamówieniami użytkowników", JLabel.CENTER);
        naglowek.setFont(new Font("Segoe UI", Font.BOLD, 22));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(naglowek, BorderLayout.NORTH);

        // Panel loginu
        JPanel panelTop = new JPanel(new FlowLayout());
        panelTop.setBackground(Color.WHITE);
        JLabel labelLogin = new JLabel("Login użytkownika:");
        poleLogin = new JTextField(20);
        JButton btnWyszukaj = new JButton("Szukaj");

        panelTop.add(labelLogin);
        panelTop.add(poleLogin);
        panelTop.add(btnWyszukaj);
        add(panelTop, BorderLayout.PAGE_START);

        // Panel listy
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(panelLista);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Akcja wyszukiwania
        btnWyszukaj.addActionListener(e -> {
            ostatnioWyszukanyLogin = poleLogin.getText().trim();
            pokazZamowieniaUzytkownika(ostatnioWyszukanyLogin);
        });
    }

    private void pokazZamowieniaUzytkownika(String login) {
        panelLista.removeAll();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);

        List<String[]> zamowienia = BazaDanych.pobierzZamowieniaGrupowane(login);

        for (String[] z : zamowienia) {
            String grupaId = z[0];
            String status = z[1];

            JPanel panelZamowienia = new JPanel(new BorderLayout());
            panelZamowienia.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
            panelZamowienia.setBackground(Color.WHITE);

            // Panel opisu (po lewej)
            JPanel panelInfo = new JPanel();
            panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
            panelInfo.setBackground(Color.WHITE);
            JLabel labelZamowienie = new JLabel("Zamówienie #" + grupaId);
            labelZamowienie.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JLabel labelStatus = new JLabel("Status: " + status);
            labelStatus.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            panelInfo.add(labelZamowienie);
            panelInfo.add(labelStatus);

            panelZamowienia.add(panelInfo, BorderLayout.WEST);

            // Panel przycisków (po prawej)
            JPanel panelPrzyciski = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelPrzyciski.setOpaque(false);

            JButton btnSzczegoly = new JButton("Szczegóły");
            btnSzczegoly.setBackground(new Color(33, 150, 243));
            btnSzczegoly.setForeground(Color.WHITE);
            btnSzczegoly.addActionListener(e -> pokazSzczegoly(grupaId));

            JButton btnUsun = new JButton("Usuń");
            btnUsun.setBackground(new Color(244, 67, 54));
            btnUsun.setForeground(Color.WHITE);
            btnUsun.addActionListener(e -> {
                int potwierdzenie = JOptionPane.showConfirmDialog(
                        this, "Czy na pewno chcesz usunąć zamówienie?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
                if (potwierdzenie == JOptionPane.YES_OPTION) {
                    BazaDanych.usunZamowienieGrupowe(grupaId);
                    pokazZamowieniaUzytkownika(ostatnioWyszukanyLogin);
                }
            });

            panelPrzyciski.add(btnSzczegoly);
            panelPrzyciski.add(btnUsun);

            panelZamowienia.add(panelPrzyciski, BorderLayout.EAST);

            panelLista.add(Box.createVerticalStrut(10));
            panelLista.add(panelZamowienia);
        }


        panelLista.revalidate();
        panelLista.repaint();
    }


    private void pokazSzczegoly(String grupaId) {
        panelLista.removeAll();

        // Panel środkowy z centrowaniem
        JPanel panelSrodekWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSrodekWrapper.setBackground(Color.WHITE);

        // Główna zawartość w pionie
        JPanel panelSrodek = new JPanel();
        panelSrodek.setLayout(new BoxLayout(panelSrodek, BoxLayout.Y_AXIS));
        panelSrodek.setPreferredSize(new Dimension(600, 500));
        panelSrodek.setBackground(Color.WHITE);
        panelSrodek.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Powrót
        JButton btnPowrot = new JButton("Powrót");
        btnPowrot.setBackground(new Color(33, 150, 243));
        btnPowrot.setForeground(Color.WHITE);
        btnPowrot.setFocusPainted(false);
        btnPowrot.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPowrot.addActionListener(e -> pokazZamowieniaUzytkownika(ostatnioWyszukanyLogin));
        panelSrodek.add(btnPowrot);
        panelSrodek.add(Box.createVerticalStrut(15));

        // Nagłówek
        JLabel naglowek = new JLabel("Szczegóły zamówienia #" + grupaId);
        naglowek.setFont(new Font("Segoe UI", Font.BOLD, 18));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSrodek.add(naglowek);
        panelSrodek.add(Box.createVerticalStrut(20));

        // Pozycje zamówienia
        List<String[]> szczegoly = BazaDanych.pobierzPozycjeZamowienia(grupaId);
        double suma = 0;

        for (String[] p : szczegoly) {
            String opis = p[0];
            double cena = Double.parseDouble(p[1].replace(",", "."));
            suma += cena;

            JPanel panelPozycji = new JPanel(new BorderLayout());
            panelPozycji.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
            panelPozycji.setBackground(Color.WHITE);
            panelPozycji.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JTextArea area = new JTextArea(opis + " — " + String.format("%.2f zł", cena));
            area.setWrapStyleWord(true);
            area.setLineWrap(true);
            area.setEditable(false);
            area.setOpaque(false);
            area.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            panelPozycji.add(area, BorderLayout.CENTER);
            panelSrodek.add(panelPozycji);
            panelSrodek.add(Box.createVerticalStrut(10));
        }

        // Cena końcowa
        JLabel sumaLabel = new JLabel("Łączna cena: " + String.format("%.2f zł", suma));
        sumaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sumaLabel.setForeground(new Color(76, 175, 80));
        sumaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sumaLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        panelSrodek.add(sumaLabel);

        panelSrodekWrapper.add(panelSrodek);
        panelLista.add(panelSrodekWrapper);
        panelLista.revalidate();
        panelLista.repaint();
    }

}
