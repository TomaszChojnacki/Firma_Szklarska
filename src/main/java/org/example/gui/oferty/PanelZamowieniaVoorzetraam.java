package org.example.gui.oferty;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class PanelZamowieniaVoorzetraam extends JPanel {

    private JTextField poleSzerokosc, poleWysokosc;
    private JComboBox<String> comboInstalacja, comboKratka, comboTypRamki;
    private JCheckBox chkKoraliki, chkMaterialy;
    private JLabel labelCena;
    private double aktualnaCena = 0;

    public PanelZamowieniaVoorzetraam() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        //Lewa strona – obrazek
        JLabel obrazek = new JLabel();
        obrazek.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("oferty/voorzetraam.png"));
        Image scaled = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        obrazek.setIcon(new ImageIcon(scaled));
        JPanel panelObraz = new JPanel(new BorderLayout());
        panelObraz.setBackground(Color.WHITE);
        panelObraz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelObraz.add(obrazek, BorderLayout.CENTER);

        //Prawa strona – formularz
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int y = 0;

        JLabel naglowek = new JLabel("Voorzetraam");
        naglowek.setFont(new Font("Segoe UI", Font.BOLD, 18));
        naglowek.setForeground(new Color(33, 150, 243));
        gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2;
        panelForm.add(naglowek, gbc);

        JLabel opis = new JLabel("<html><small>Idealne rozwiązanie do renowacji okien. Dodatkowa izolacja bez demontażu.</small></html>");
        opis.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        gbc.gridy = y++;
        panelForm.add(opis, gbc);

        // Szerokość
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Szerokość [mm]:"), gbc);
        gbc.gridx = 1;
        poleSzerokosc = new JTextField("100", 10);
        panelForm.add(poleSzerokosc, gbc);
        y++;

        // Wysokość
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Wysokość [mm]:"), gbc);
        gbc.gridx = 1;
        poleWysokosc = new JTextField("100", 10);
        panelForm.add(poleWysokosc, gbc);
        y++;

        // Instalacja
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Instalacja:"), gbc);
        gbc.gridx = 1;
        comboInstalacja = new JComboBox<>(new String[]{"Nie (0,00 zł)", "Tak (500,00 zł)"});
        panelForm.add(comboInstalacja, gbc);
        y++;

        // Kratka
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Kratka wentylacyjna:"), gbc);
        gbc.gridx = 1;
        comboKratka = new JComboBox<>(new String[]{"Nie (0,00 zł)", "Tak (95,00 zł)"});
        panelForm.add(comboKratka, gbc);
        y++;

        // Typ ramki
        gbc.gridx = 0; gbc.gridy = y;
        panelForm.add(new JLabel("Typ ramki:"), gbc);
        gbc.gridx = 1;
        comboTypRamki = new JComboBox<>(new String[]{"Drewniane", "Aluminiowe", "PCV"});
        panelForm.add(comboTypRamki, gbc);
        y++;

        // Koraliki
        gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2;
        chkKoraliki = new JCheckBox("Koraliki szkliniące (110,00 zł)");
        chkKoraliki.setBackground(Color.WHITE);
        panelForm.add(chkKoraliki, gbc);

        // Materiały szklarskie
        gbc.gridy = y++;
        chkMaterialy = new JCheckBox("Materiały szklarskie (170,00 zł)");
        chkMaterialy.setBackground(Color.WHITE);
        panelForm.add(chkMaterialy, gbc);

        // Cena
        gbc.gridy = y++;
        labelCena = new JLabel("Cena: 0,00 zł");
        labelCena.setFont(new Font("Segoe UI", Font.BOLD, 14));
        labelCena.setForeground(new Color(76, 175, 80));
        panelForm.add(labelCena, gbc);

        // Przycisk
        JButton btnDodaj = new JButton("Dodaj do koszyka");
        btnDodaj.setBackground(new Color(33, 150, 243));
        btnDodaj.addActionListener(e -> {
            String szer = poleSzerokosc.getText();
            String wys = poleWysokosc.getText();
            String instalacja = (String) comboInstalacja.getSelectedItem();
            String kratka = (String) comboKratka.getSelectedItem();
            String ramka = (String) comboTypRamki.getSelectedItem();
            boolean koraliki = chkKoraliki.isSelected();
            boolean materialy = chkMaterialy.isSelected();

            String opisZamowienia = "Voorzetraam - Rozmiar: " + szer + "x" + wys + " mm, "
                    + "Instalacja: " + instalacja + ", "
                    + "Kratka: " + kratka + ", "
                    + "Ramka: " + ramka
                    + (koraliki ? ", Koraliki" : "")
                    + (materialy ? ", Materiały" : "");

            org.example.gui.koszyk.PanelKoszyka.dodajProdukt("Voorzetraam", opisZamowienia, aktualnaCena);

            JOptionPane.showMessageDialog(this, "Dodano do koszyka");
        });
        btnDodaj.setForeground(Color.WHITE);
        btnDodaj.setFocusPainted(false);
        gbc.gridy = y++;
        panelForm.add(btnDodaj, gbc);

        // Połączenie
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelObraz, panelForm);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerSize(2);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);

        dodajSluchaczy();
        przeliczCene();
    }

    private void przeliczCene() {
        try {
            int szer = Integer.parseInt(poleSzerokosc.getText());
            int wys = Integer.parseInt(poleWysokosc.getText());

            if (szer < 100 || wys < 100) {
                labelCena.setText("Minimalny rozmiar to 100 x 100 mm");
                aktualnaCena = 0;
                return;
            }

            double cenaZaM2 = 890.00;
            double powierzchnia = (szer / 1000.0) * (wys / 1000.0);
            double cena = cenaZaM2 * powierzchnia;

            if (comboInstalacja.getSelectedIndex() == 1) cena += 450.00;
            if (comboKratka.getSelectedIndex() == 1) cena += 90.00;
            if (chkKoraliki.isSelected()) cena += 100.80;
            if (chkMaterialy.isSelected()) cena += 150.0;

            aktualnaCena = cena;
            labelCena.setText(String.format("Cena: %.2f zł", cena));
        } catch (NumberFormatException e) {
            labelCena.setText("Nieprawidłowe dane");
            aktualnaCena = 0;
        }
    }

    private void dodajSluchaczy() {
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { przeliczCene(); }
            public void removeUpdate(DocumentEvent e) { przeliczCene(); }
            public void changedUpdate(DocumentEvent e) { przeliczCene(); }
        };
        poleSzerokosc.getDocument().addDocumentListener(docListener);
        poleWysokosc.getDocument().addDocumentListener(docListener);

        comboInstalacja.addItemListener(e -> przeliczCene());
        comboKratka.addItemListener(e -> przeliczCene());
        comboTypRamki.addItemListener(e -> przeliczCene());
        chkKoraliki.addItemListener(e -> przeliczCene());
        chkMaterialy.addItemListener(e -> przeliczCene());
    }
}
