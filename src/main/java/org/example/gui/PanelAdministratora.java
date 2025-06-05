package org.example.gui;

import javax.swing.*;
import java.awt.*;

public class PanelAdministratora extends JPanel {

    public PanelAdministratora() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JLabel naglowek = new JLabel("Panel zarządzania użytkownikami", JLabel.CENTER);
        naglowek.setFont(new Font("SansSerif", Font.BOLD, 26));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(naglowek, BorderLayout.NORTH);

        JPanel panelFormularz = new JPanel(new GridBagLayout());
        panelFormularz.setBackground(Color.WHITE);
        panelFormularz.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelLogin = new JLabel("Login:");
        JTextField poleLogin = new JTextField(20);

        JLabel labelHaslo = new JLabel("Hasło:");
        JTextField poleHaslo = new JTextField(20);

        JLabel labelRola = new JLabel("Rola:");
        JComboBox<String> poleRola = new JComboBox<>(new String[]{"KLIENT", "PRACOWNIK", "PRACOWNIK_OBSLUGI_KLIENTA"});

        JButton przyciskDodaj = new JButton("Dodaj użytkownika");

        JLabel labelLoginStatus = new JLabel("Login pracownika:");
        JTextField poleLoginStatus = new JTextField(20);

        JLabel labelNowyStatus = new JLabel("Nowy status:");
        JComboBox<String> poleNowyStatus = new JComboBox<>(new String[]{"DOBRY", "ZWOLNIONY"});

        JButton przyciskZmienStatus = new JButton("Zmień status");

        JButton przyciskWyszukaj = new JButton("Wyszukaj użytkownika");

        JLabel labelInfo = new JLabel("", JLabel.CENTER);
        labelInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelInfo.setForeground(Color.RED);

        // Dodawanie komponentów
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormularz.add(labelLogin, gbc);
        gbc.gridx = 1;
        panelFormularz.add(poleLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormularz.add(labelHaslo, gbc);
        gbc.gridx = 1;
        panelFormularz.add(poleHaslo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormularz.add(labelRola, gbc);
        gbc.gridx = 1;
        panelFormularz.add(poleRola, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelFormularz.add(przyciskDodaj, gbc);

        gbc.gridy = 4;
        panelFormularz.add(new JSeparator(), gbc);

        gbc.gridy = 5; gbc.gridwidth = 1;
        gbc.gridx = 0;
        panelFormularz.add(labelLoginStatus, gbc);
        gbc.gridx = 1;
        panelFormularz.add(poleLoginStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        panelFormularz.add(labelNowyStatus, gbc);
        gbc.gridx = 1;
        panelFormularz.add(poleNowyStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        panelFormularz.add(przyciskZmienStatus, gbc);

        gbc.gridy = 8;
        panelFormularz.add(new JSeparator(), gbc);

        gbc.gridy = 9;
        panelFormularz.add(przyciskWyszukaj, gbc);

        gbc.gridy = 10;
        panelFormularz.add(labelInfo, gbc);

        add(panelFormularz, BorderLayout.CENTER);

        // Styl przycisków
        przyciskDodaj.setBackground(new Color(33, 150, 243));
        przyciskDodaj.setForeground(Color.WHITE);
        przyciskDodaj.setFocusPainted(false);
        przyciskDodaj.setFont(new Font("SansSerif", Font.BOLD, 16));
        przyciskDodaj.setPreferredSize(new Dimension(200, 40));

        przyciskZmienStatus.setBackground(new Color(255, 152, 0));
        przyciskZmienStatus.setForeground(Color.WHITE);
        przyciskZmienStatus.setFocusPainted(false);
        przyciskZmienStatus.setFont(new Font("SansSerif", Font.BOLD, 16));
        przyciskZmienStatus.setPreferredSize(new Dimension(200, 40));

        przyciskWyszukaj.setBackground(new Color(0, 188, 212));
        przyciskWyszukaj.setForeground(Color.WHITE);
        przyciskWyszukaj.setFocusPainted(false);
        przyciskWyszukaj.setFont(new Font("SansSerif", Font.BOLD, 16));
        przyciskWyszukaj.setPreferredSize(new Dimension(200, 40));

        // Akcje przycisków
        przyciskDodaj.addActionListener(e -> {
            String login = poleLogin.getText().trim();
            String haslo = poleHaslo.getText().trim();
            String rola = (String) poleRola.getSelectedItem();

            if (login.isEmpty() || haslo.isEmpty()) {
                labelInfo.setForeground(Color.RED);
                labelInfo.setText("Login i hasło nie mogą być puste!");
                return;
            }

            boolean sukces = org.example.baza.BazaDanych.dodajUzytkownika(login, haslo, rola);
            if (sukces) {
                labelInfo.setForeground(new Color(76, 175, 80));
                labelInfo.setText("Użytkownik dodany.");
                poleLogin.setText("");
                poleHaslo.setText("");
            } else {
                labelInfo.setForeground(Color.RED);
                labelInfo.setText("Błąd dodawania użytkownika.");
            }
        });

        przyciskZmienStatus.addActionListener(e -> {
            String login = poleLoginStatus.getText().trim();
            String nowyStatus = (String) poleNowyStatus.getSelectedItem();

            if (login.isEmpty()) {
                labelInfo.setForeground(Color.RED);
                labelInfo.setText("Podaj login pracownika!");
                return;
            }

            boolean sukces = org.example.baza.BazaDanych.ustawStatus(login, nowyStatus);
            if (sukces) {
                labelInfo.setForeground(new Color(76, 175, 80));
                labelInfo.setText("Status zmieniony pomyślnie.");
                poleLoginStatus.setText("");
            } else {
                labelInfo.setForeground(Color.RED);
                labelInfo.setText("Błąd zmiany statusu.");
            }
        });

        przyciskWyszukaj.addActionListener(e -> {
            JFrame okno = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (okno instanceof Okno) {
                ((Okno) okno).wyswietlPanel(new PanelWyszukiwaniaUzytkownikow());
            }
        });
    }
}
