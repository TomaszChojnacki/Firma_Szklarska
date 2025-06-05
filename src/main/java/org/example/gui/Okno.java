package org.example.gui;

import org.example.gui.oferty.PanelOferty;
import org.example.klient.Klient;

import javax.swing.*;
import java.awt.*;

public class Okno extends JFrame {

    private JPanel panelContent;

    public Okno() {
        setTitle("System firmy szklarskiej");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Lewa kolumna z przyciskami
        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(new Color(33, 150, 243)); // Niebieskie tło

        JButton btnOferty = new JButton("Przeglądaj Oferty");
        JButton btnNoweZamowienie = new JButton("Nowe Zamówienie");
        JButton btnListaZamowien = new JButton("Lista Zamówień");
        JButton btnZarzadzanieUzytkownikami = new JButton("Zarządzanie Użytkownikami");

        if ("PRACOWNIK_OBSLUGI_KLIENTA".equals(Klient.zalogowanaRola)) {
            JButton btnPanelBiura = new JButton("Dodaj klienta");
            btnPanelBiura.setMaximumSize(new Dimension(250, 50));
            btnPanelBiura.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnPanelBiura.setFont(new Font("SansSerif", Font.BOLD, 14));

            btnPanelBiura.addActionListener(e -> wyswietlPanel(new PanelBiura()));
            panelMenu.add(Box.createVerticalStrut(10));
            panelMenu.add(btnPanelBiura);
        }

        JButton btnWyloguj = new JButton("Wyloguj");

        JButton[] przyciski = {btnOferty, btnNoweZamowienie, btnListaZamowien, btnZarzadzanieUzytkownikami, btnWyloguj};

        for (JButton btn : przyciski) {
            btn.setMaximumSize(new Dimension(200, 50));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setFocusPainted(false);
            panelMenu.add(Box.createVerticalStrut(10));
            panelMenu.add(btn);
        }

        add(panelMenu, BorderLayout.WEST);

        // Panel główny (dynamiczny)
        panelContent = new JPanel();
        panelContent.setLayout(new BorderLayout());
        panelContent.setBackground(new Color(245, 245, 245)); // Jasnoszare tło
        add(panelContent, BorderLayout.CENTER);

        // Tytuł na górze
        JLabel labelNaglowek = new JLabel("Witaj w systemie zarządzania firmą szklarską", JLabel.CENTER);
        labelNaglowek.setFont(new Font("SansSerif", Font.BOLD, 24));
        labelNaglowek.setOpaque(true);
        labelNaglowek.setBackground(new Color(3, 169, 244)); // Inny odcień niebieskiego
        labelNaglowek.setForeground(Color.WHITE);
        labelNaglowek.setPreferredSize(new Dimension(1000, 60));
        add(labelNaglowek, BorderLayout.NORTH);

        // Działanie przycisków
        btnOferty.addActionListener(e -> wyswietlPanel(new PanelOferty()));
        btnNoweZamowienie.addActionListener(e -> wyswietlTrescTekst("Formularz dodawania nowego zamówienia."));
        btnListaZamowien.addActionListener(e -> wyswietlTrescTekst("Lista zamówień klienta/pracownika."));
        btnZarzadzanieUzytkownikami.addActionListener(e -> {
            if ("ADMIN".equals(Klient.zalogowanaRola)) {
                wyswietlPanel(new PanelAdministratora());
            } else {
                JOptionPane.showMessageDialog(this, "Brak dostępu!");
            }
        });
        btnWyloguj.addActionListener(e -> {
            dispose();
            new Logowanie().setVisible(true);
        });


        // Ukrycie przycisku "zarządzanie użytkownikami" jeśli nie admin
        if (!"ADMIN".equals(Klient.zalogowanaRola)) {
            btnZarzadzanieUzytkownikami.setVisible(false);
        }

        setVisible(true);
    }

    // Funkcja do wyświetlania zwykłego tekstu
    private void wyswietlTrescTekst(String tresc) {
        panelContent.removeAll();
        JLabel label = new JLabel(tresc, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        panelContent.add(label, BorderLayout.CENTER);
        panelContent.revalidate();
        panelContent.repaint();
    }

    public void wyswietlPanel(JPanel panel) {
        panelContent.removeAll();
        panelContent.add(panel, BorderLayout.CENTER);
        panelContent.revalidate();
        panelContent.repaint();
    }

}
