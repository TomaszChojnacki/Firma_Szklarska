package org.example.gui.produkcja;

import org.example.baza.BazaDanych;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PanelZarzadzaniaProdukcja extends JPanel {

    private JPanel panelLista;
    private JComboBox<String> comboStatus;
    private JButton btnSzukaj, btnSort;
    private JLabel labelSortInfo;
    private boolean sortAsc = false;
    private String rolaUzytkownika;

    public PanelZarzadzaniaProdukcja(String rola) {
        this.rolaUzytkownika = rola;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Color.WHITE);
        comboStatus = new JComboBox<>(getWidoczneStatusy(rola));
        btnSzukaj = new JButton("Odśwież");
        btnSort = new JButton("Sortuj");
        labelSortInfo = new JLabel();

        top.add(new JLabel("Status:"));
        top.add(comboStatus);
        top.add(btnSzukaj);
        top.add(btnSort);
        top.add(labelSortInfo);
        add(top, BorderLayout.NORTH);

        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(Color.WHITE);
        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        btnSzukaj.addActionListener(e -> odswiez());
        btnSort.addActionListener(e -> {
            sortAsc = !sortAsc;
            odswiez();
        });

        odswiez();
    }

    private void odswiez() {
        panelLista.removeAll();
        String wybranyStatus = (String) comboStatus.getSelectedItem();
        List<String[]> zamowienia = BazaDanych.pobierzZamowieniaProdukcja(null, new String[]{wybranyStatus}, sortAsc);

        for (String[] p : zamowienia) {
            String grupaId = p[0];
            String status = p[1];
            String login = p.length > 2 ? p[2] : null;

            JPanel panelZamowienia = new JPanel(new BorderLayout());
            panelZamowienia.setMaximumSize(new Dimension(1000, 70));
            panelZamowienia.setBackground(new Color(245, 245, 245));
            panelZamowienia.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            JLabel label = new JLabel("Zamówienie #" + grupaId + " — Status: " + status);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            panelZamowienia.add(label, BorderLayout.CENTER);

            JPanel przyciski = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            przyciski.setOpaque(false);

            if (getDozwoloneStatusyDoZmiany(rolaUzytkownika).length > 0) {
                JButton btnZmienStatus = new JButton("Zmień status");
                btnZmienStatus.addActionListener(e -> zmienStatus(grupaId));
                przyciski.add(btnZmienStatus);
            }


            JButton btnSzczegoly = new JButton("Szczegóły");
            btnSzczegoly.addActionListener(e -> pokazSzczegolyZamowienia(grupaId, status, login));
            przyciski.add(btnSzczegoly);

            panelZamowienia.add(przyciski, BorderLayout.EAST);
            panelLista.add(panelZamowienia);
            panelLista.add(Box.createVerticalStrut(10));
        }

        labelSortInfo.setText("Sortowanie: " + (sortAsc ? "od najstarszych" : "od najnowszych"));
        revalidate();
        repaint();
    }

    private void zmienStatus(String grupaId) {
        String[] dozwolone = getDozwoloneStatusyDoZmiany(rolaUzytkownika);
        String nowy = (String) JOptionPane.showInputDialog(this, "Nowy status:", "Zmień status",
                JOptionPane.PLAIN_MESSAGE, null, dozwolone, dozwolone[0]);
        if (nowy != null) {
            BazaDanych.ustawStatusZamowieniaGrupowo(grupaId, nowy);
            odswiez();
        }
    }

    private void pokazSzczegolyZamowienia(String grupaId, String status, String login) {
        List<String[]> szczegoly = BazaDanych.pobierzPozycjeZamowienia(grupaId);
        double suma = 0;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 20));

        // Login użytkownika (gdy dotyczy)
        if ("gotowe do odbioru".equals(status) || "zakończone".equals(status)) {
            JLabel loginLabel = new JLabel("Zamówienie użytkownika: " + login);
            loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            loginLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            loginLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            panel.add(loginLabel);
        }

        // Nagłówek
        JLabel tytul = new JLabel("Szczegóły zamówienia #" + grupaId);
        tytul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tytul.setAlignmentX(Component.LEFT_ALIGNMENT);
        tytul.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(tytul);

        // Lista pozycji
        for (String[] p : szczegoly) {
            String opis = p[0];
            double cena = Double.parseDouble(p[1].replace(",", "."));
            suma += cena;

            JTextArea opisArea = new JTextArea(opis + "\nCena: " + String.format("%.2f zł", cena));
            opisArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            opisArea.setEditable(false);
            opisArea.setLineWrap(true);
            opisArea.setWrapStyleWord(true);
            opisArea.setOpaque(false);
            opisArea.setAlignmentX(Component.LEFT_ALIGNMENT);
            opisArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            panel.add(opisArea);
            panel.add(Box.createVerticalStrut(8));
        }

        // Łączna cena
        JLabel sumaLabel = new JLabel("Łączna cena: " + String.format("%.2f zł", suma));
        sumaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sumaLabel.setForeground(new Color(76, 175, 80));
        sumaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sumaLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.add(sumaLabel);

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(650, 500));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Szczegóły zamówienia", JOptionPane.PLAIN_MESSAGE);
    }



    private String[] getWidoczneStatusy(String rola) {
        switch (rola) {
            case "ADMIN":
                return new String[]{"rozpoczęcie prac", "realizacja", "kompletowanie materiałów", "obróbka końcowa", "gotowe", "gotowe do odbioru", "zakończone"};
            case "KIEROWNIK_PRODUKCJI":
                return new String[]{"rozpoczęcie prac", "realizacja", "kompletowanie materiałów", "gotowe"};
            case "MAGAZYNIER":
                return new String[]{"kompletowanie materiałów"};
            case "PRACOWNIK_PRODUKCJI":
                return new String[]{"obróbka końcowa"};
            case "PRACOWNIK_OBSLUGI_KLIENTA":
                return new String[]{"gotowe do odbioru"};
            default:
                return new String[]{};
        }
    }


    private String[] getDozwoloneStatusyDoZmiany(String rola) {
        switch (rola) {
            case "ADMIN":
                return new String[]{"rozpoczęcie prac", "realizacja", "kompletowanie materiałów", "obróbka końcowa", "gotowe", "gotowe do odbioru", "zakończone"};
            case "KIEROWNIK_PRODUKCJI":
                return new String[]{"rozpoczęcie prac", "realizacja", "kompletowanie materiałów", "obróbka końcowa", "gotowe", "gotowe do odbioru"};
            case "MAGAZYNIER":
                return new String[]{"obróbka końcowa"};
            case "PRACOWNIK_PRODUKCJI":
                return new String[]{"gotowe"};
            case "PRACOWNIK_OBSLUGI_KLIENTA":
                return new String[]{"zakończone"};
            default:
                return new String[]{};
        }
    }

}
