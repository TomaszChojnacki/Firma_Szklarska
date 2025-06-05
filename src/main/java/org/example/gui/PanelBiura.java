package org.example.gui;

import org.example.baza.BazaDanych;
import javax.swing.*;
import java.awt.*;

public class PanelBiura extends JPanel {

    public PanelBiura() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Nagłówek
        JLabel naglowek = new JLabel("Dodawanie nowych klientów", JLabel.CENTER);
        naglowek.setFont(new Font("SansSerif", Font.BOLD, 26));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        add(naglowek, BorderLayout.NORTH);

        // Główna część formularza
        JPanel panelFormularz = new JPanel(new GridBagLayout());
        panelFormularz.setBackground(Color.WHITE);
        panelFormularz.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pola
        JLabel labelLogin = new JLabel("Login klienta:");
        JTextField poleLogin = new JTextField(20);

        JLabel labelHaslo = new JLabel("Hasło klienta:");
        JTextField poleHaslo = new JTextField(20);

        JButton przyciskDodaj = new JButton("Dodaj klienta");
        JLabel labelInfo = new JLabel("", JLabel.CENTER);

        labelLogin.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelHaslo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelInfo.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelInfo.setForeground(Color.RED);

        // Dodawanie komponentów
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormularz.add(labelLogin, gbc);
        gbc.gridx = 1;
        panelFormularz.add(poleLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormularz.add(labelHaslo, gbc);
        gbc.gridx = 1;
        panelFormularz.add(poleHaslo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelFormularz.add(przyciskDodaj, gbc);

        gbc.gridy = 3;
        panelFormularz.add(labelInfo, gbc);

        add(panelFormularz, BorderLayout.CENTER);

        // Styl przycisku
        przyciskDodaj.setBackground(new Color(33, 150, 243));
        przyciskDodaj.setForeground(Color.WHITE);
        przyciskDodaj.setFocusPainted(false);
        przyciskDodaj.setFont(new Font("SansSerif", Font.BOLD, 16));
        przyciskDodaj.setPreferredSize(new Dimension(200, 40));

        // Akcja przycisku
        przyciskDodaj.addActionListener(e -> {
            String login = poleLogin.getText().trim();
            String haslo = poleHaslo.getText().trim();

            if (login.isEmpty() || haslo.isEmpty()) {
                labelInfo.setForeground(Color.RED);
                labelInfo.setText("Login i hasło nie mogą być puste!");
                return;
            }

            boolean sukces = BazaDanych.dodajUzytkownika(login, haslo, "KLIENT");
            if (sukces) {
                labelInfo.setForeground(new Color(76, 175, 80)); // Zielony
                labelInfo.setText("Klient dodany pomyślnie.");
                poleLogin.setText("");
                poleHaslo.setText("");
            } else {
                labelInfo.setForeground(Color.RED);
                labelInfo.setText("Błąd podczas dodawania klienta.");
            }
        });
    }
}
