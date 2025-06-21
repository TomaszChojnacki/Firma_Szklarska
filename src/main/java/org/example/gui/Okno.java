package org.example.gui;

import org.example.gui.admin.PanelAdministratora;
import org.example.gui.oferty.PanelOferty;
import org.example.gui.produkcja.PanelZarzadzaniaProdukcja;
import org.example.klient.Klient;
import org.example.baza.BazaDanych;
import org.example.gui.zamowienia.PanelListaZamowien;

import javax.swing.*;
import java.awt.*;

public class Okno extends JFrame {

    private JPanel panelContent;

    public Okno() {
        BazaDanych.usunStareZKoszyka();
        BazaDanych.usunZamowieniaZakonczonePoRoku();
        BazaDanych.usunWygasleKonta();
        setTitle("System firmy szklarskiej");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelMenu = new JPanel();
        panelMenu.setLayout(new BoxLayout(panelMenu, BoxLayout.Y_AXIS));
        panelMenu.setBackground(new Color(33, 150, 243));

        JButton btnOferty = new JButton("Przeglądaj Oferty");
        JButton btnKoszyk = new JButton("Koszyk");
        JButton btnListaZamowien = new JButton("Lista Zamówień");
        JButton btnZarzadzanieUzytkownikami = new JButton("Zarządzanie Użytkownikami");
        JButton btnWyloguj = new JButton("Wyloguj");

        // Dodanie nowego przycisku produkcji
        JButton btnPanelProdukcji = new JButton("Panel produkcji");

        // Styl dla wszystkich przycisków
        JButton[] przyciski = {
                btnOferty, btnKoszyk, btnListaZamowien, btnZarzadzanieUzytkownikami, btnPanelProdukcji, btnWyloguj
        };

        for (JButton btn : przyciski) {
            btn.setMaximumSize(new Dimension(200, 50));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setFocusPainted(false);
        }

        // Warunkowe przyciski zależne od roli
        if ("PRACOWNIK_OBSLUGI_KLIENTA".equals(Klient.zalogowanaRola)) {
            JButton btnPanelBiura = new JButton("Dodaj klienta");
            btnPanelBiura.setMaximumSize(new Dimension(250, 50));
            btnPanelBiura.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnPanelBiura.setFont(new Font("SansSerif", Font.BOLD, 14));
            btnPanelBiura.addActionListener(e -> wyswietlPanel(new PanelBiura()));
            panelMenu.add(Box.createVerticalStrut(10));
            panelMenu.add(btnPanelBiura);
        }

        // Ukryj zarządzanie użytkownikami jeśli nie admin
        if (!"ADMIN".equals(Klient.zalogowanaRola)) {
            btnZarzadzanieUzytkownikami.setVisible(false);
        }

        // Ukryj przycisk produkcji jeśli rola nie pasuje
        if (!java.util.Arrays.asList("ADMIN", "KIEROWNIK_PRODUKCJI", "MAGAZYNIER", "PRACOWNIK_PRODUKCJI", "PRACOWNIK_OBSLUGI_KLIENTA")
                .contains(Klient.zalogowanaRola)) {
            btnPanelProdukcji.setVisible(false);
        }

        // Dodanie do menu
        for (JButton btn : przyciski) {
            if (btn.isVisible()) {
                panelMenu.add(Box.createVerticalStrut(10));
                panelMenu.add(btn);
            }
        }

        add(panelMenu, BorderLayout.WEST);

        // Panel główny
        panelContent = new JPanel(new BorderLayout());
        panelContent.setBackground(new Color(0, 163, 241));
        add(panelContent, BorderLayout.CENTER);

        JLabel labelNaglowek = new JLabel("Witaj w systemie zarządzania firmą szklarską", JLabel.CENTER);
        labelNaglowek.setFont(new Font("SansSerif", Font.BOLD, 24));
        labelNaglowek.setOpaque(true);
        labelNaglowek.setBackground(new Color(0, 163, 255));
        labelNaglowek.setForeground(Color.WHITE);
        labelNaglowek.setPreferredSize(new Dimension(1000, 60));
        add(labelNaglowek, BorderLayout.NORTH);

        panelContent = new JPanel(new BorderLayout()) {
            private Image logoImage;

            {
                try {
                    ImageIcon logoIcon = new ImageIcon(getClass().getClassLoader().getResource("logo2.png"));
                    logoImage = logoIcon.getImage();
                } catch (Exception e) {
                    logoImage = null;
                }

                addComponentListener(new java.awt.event.ComponentAdapter() {
                    public void componentResized(java.awt.event.ComponentEvent evt) {
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (logoImage != null) {
                    g.drawImage(logoImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        add(panelContent, BorderLayout.CENTER);

        // Akcje
        btnOferty.addActionListener(e -> wyswietlPanel(new PanelOferty()));
        btnKoszyk.addActionListener(e -> wyswietlPanel(new org.example.gui.koszyk.PanelKoszyka()));
        btnListaZamowien.addActionListener(e -> wyswietlPanel(new PanelListaZamowien()));
        btnZarzadzanieUzytkownikami.addActionListener(e -> wyswietlPanel(new PanelAdministratora()));
        btnPanelProdukcji.addActionListener(e -> wyswietlPanel(new PanelZarzadzaniaProdukcja(Klient.zalogowanaRola)));
        btnWyloguj.addActionListener(e -> {
            dispose();
            new Logowanie().setVisible(true);
        });

        setVisible(true);
    }

    public void wyswietlPanel(JPanel panel) {
        panelContent.removeAll();
        panelContent.add(panel, BorderLayout.CENTER);
        panelContent.revalidate();
        panelContent.repaint();
    }
}
