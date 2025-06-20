package org.example.gui.koszyk;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.example.klient.Klient;
import org.example.baza.BazaDanych;


public class PanelKoszyka extends JPanel {

    private JPanel panelLista;
    private JLabel labelSuma;
    private static final List<ProduktKoszyka> produkty = new ArrayList<>();

    public PanelKoszyka() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel naglowek = new JLabel("Twój koszyk", JLabel.CENTER);
        naglowek.setFont(new Font("Segoe UI", Font.BOLD, 26));
        naglowek.setForeground(new Color(33, 150, 243));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(naglowek, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panelLista);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelDolny = new JPanel(new BorderLayout());
        panelDolny.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelDolny.setBackground(Color.WHITE);

        labelSuma = new JLabel("Łączna cena: 0,00 zł");
        labelSuma.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelSuma.setForeground(new Color(76, 175, 80));
        panelDolny.add(labelSuma, BorderLayout.WEST);

        JButton btnZamow = new JButton("Złóż zamówienie");
        btnZamow.setBackground(new Color(33, 150, 243));
        btnZamow.addActionListener(e -> {
            int decyzja = JOptionPane.showConfirmDialog(this, "Czy na pewno chcesz złożyć to zamówienie?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);
            if (decyzja == JOptionPane.YES_OPTION) {
                BazaDanych.zlozZamowienie(Klient.zalogowanyLogin);
                JOptionPane.showMessageDialog(this, "Zamówienie złożone. Status: rozpoczęcie prac.");
                odswiezKoszyk(); // wyczyść koszyk
            }
        });

        btnZamow.setForeground(Color.WHITE);
        panelDolny.add(btnZamow, BorderLayout.EAST);

        add(panelDolny, BorderLayout.SOUTH);

        odswiezKoszyk();
    }

    private void odswiezKoszyk() {
        panelLista.removeAll();
        double suma = 0;

        produkty.clear();
        List<String[]> dane = BazaDanych.pobierzKoszykUzytkownika(Klient.zalogowanyLogin);

        for (String[] rekord : dane) {
            String id = rekord[0];
            String opis = rekord[1];
            double cena = Double.parseDouble(rekord[2].replace(",", "."));

            produkty.add(new ProduktKoszyka("Produkt", opis, cena));

            //Karta zamówienia
            JPanel karta = new JPanel(new BorderLayout(10, 10));
            karta.setBackground(new Color(245, 245, 245));
            karta.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true)
            ));

            //Opis zamówienia
            JTextArea opisArea = new JTextArea(opis + " — " + String.format("%.2f zł", cena));
            opisArea.setWrapStyleWord(true);
            opisArea.setLineWrap(true);
            opisArea.setEditable(false);
            opisArea.setOpaque(false);
            opisArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            karta.add(opisArea, BorderLayout.CENTER);

            //Przycisk usuń
            JButton btnUsun = new JButton("Usuń");
            btnUsun.setPreferredSize(new Dimension(80, 30));
            btnUsun.setBackground(new Color(244, 67, 54));
            btnUsun.setForeground(Color.WHITE);
            btnUsun.setFocusPainted(false);
            btnUsun.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btnUsun.addActionListener(e -> {
                BazaDanych.usunZamowienieZKoszyka(Integer.parseInt(id));
                odswiezKoszyk();
            });

            JPanel panelPrzycisk = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            panelPrzycisk.setBackground(new Color(245, 245, 245));
            panelPrzycisk.add(btnUsun);

            karta.add(panelPrzycisk, BorderLayout.EAST);

            panelLista.add(Box.createVerticalStrut(10));
            panelLista.add(karta);

            suma += cena;
        }

        labelSuma.setText("Łączna cena: " + String.format("%.2f zł", suma));
        revalidate();
        repaint();
    }



    public static void dodajProdukt(String nazwa, String opis, double cena) {
        produkty.add(new ProduktKoszyka(nazwa, opis, cena));
        BazaDanych.dodajDoKoszyka(Klient.zalogowanyLogin, opis, cena);  // <-- dodaj to
    }


    private static class ProduktKoszyka {
        String nazwa;
        String opis;
        double cena;

        public ProduktKoszyka(String nazwa, String opis, double cena) {
            this.nazwa = nazwa;
            this.opis = opis;
            this.cena = cena;
        }
    }

}
