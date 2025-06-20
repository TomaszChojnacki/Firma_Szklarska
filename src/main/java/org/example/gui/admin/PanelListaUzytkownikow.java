package org.example.gui.admin;

import org.example.baza.BazaDanych;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelListaUzytkownikow extends JPanel {

    public PanelListaUzytkownikow(String rola) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelNaglowek = new JPanel();
        panelNaglowek.setLayout(new BoxLayout(panelNaglowek, BoxLayout.Y_AXIS));
        panelNaglowek.setBackground(Color.WHITE);

        // Tytuł
        JLabel label = new JLabel("Lista użytkowników – " + rola.toUpperCase(), JLabel.LEFT);
        label.setFont(new Font("SansSerif", Font.BOLD, 20));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Pasek wyszukiwania
        JPanel panelSzukaj = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSzukaj.setBackground(Color.WHITE);

        JTextField poleSzukaj = new JTextField(20);
        poleSzukaj.setToolTipText("Wyszukaj po loginie");

        JButton przyciskSzukaj = new JButton("Szukaj");

        panelSzukaj.add(poleSzukaj);
        panelSzukaj.add(przyciskSzukaj);

        panelNaglowek.add(label);
        panelNaglowek.add(Box.createVerticalStrut(8)); // odstęp
        panelNaglowek.add(panelSzukaj);

        // Tabela
        String[] kolumny = {"ID", "Login", "Rola", "Status"};
        DefaultTableModel model = new DefaultTableModel(kolumny, 0);
        JTable tabela = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tabela);

        add(panelNaglowek, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Wczytanie danych
        odswiezListe(model, rola, "");

        przyciskSzukaj.addActionListener(e -> {
            String fraza = poleSzukaj.getText().trim();
            odswiezListe(model, rola, fraza);
        });
    }

    private void odswiezListe(DefaultTableModel model, String rola, String filtr) {
        model.setRowCount(0);
        List<String[]> dane = BazaDanych.pobierzUzytkownikow(rola, filtr);

        for (String[] wiersz : dane) {
            model.addRow(wiersz);
        }
    }


}
