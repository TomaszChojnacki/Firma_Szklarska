package org.example.gui.admin;

import org.example.baza.BazaDanych;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelUsuwaniaUzytkownikow extends JPanel {

    private DefaultTableModel model;
    private JTable tabela;
    private JTextField poleSzukaj;

    public PanelUsuwaniaUzytkownikow() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Nagłówek główny
        JLabel naglowek = new JLabel("Zarządzanie kontami – Usuwanie użytkownika", JLabel.CENTER);
        naglowek.setFont(new Font("SansSerif", Font.BOLD, 22));
        naglowek.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        naglowek.setForeground(new Color(63, 81, 181));
        add(naglowek, BorderLayout.NORTH);

        // Wyszukiwarka
        JPanel panelSzukaj = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        poleSzukaj = new JTextField(20);
        JButton btnSzukaj = new JButton("Szukaj");
        panelSzukaj.add(poleSzukaj);
        panelSzukaj.add(btnSzukaj);
        add(panelSzukaj, BorderLayout.BEFORE_FIRST_LINE);

        // Tabela
        model = new DefaultTableModel(new Object[]{"ID", "Login", "Rola", "Status", ""}, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };

        tabela = new JTable(model);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tabela.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        tabela.setBackground(Color.WHITE);
        tabela.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        add(scrollPane, BorderLayout.CENTER);

        // Kolumna "Usuń"
        tabela.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton btn = new JButton("Usuń");
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setBackground(new Color(244, 67, 54));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setMargin(new Insets(2, 10, 2, 10));
            return btn;
        });

        tabela.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            JButton button = new JButton("Usuń");
            int selectedRow;

            {
                button.setFont(new Font("SansSerif", Font.PLAIN, 12));
                button.setBackground(new Color(244, 67, 54));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.addActionListener(e -> {
                    String login = (String) model.getValueAt(selectedRow, 1);
                    int confirm = JOptionPane.showConfirmDialog(
                            null,
                            "Czy na pewno chcesz usunąć użytkownika: " + login + "?",
                            "Potwierdzenie usunięcia",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean usunieto = BazaDanych.usunUzytkownika(login);
                        if (usunieto) {
                            JOptionPane.showMessageDialog(null, "Użytkownik został usunięty.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Błąd podczas usuwania użytkownika.");
                        }
                        fireEditingStopped();  // Zakończ edycję
                        odswiezListe();        // Odśwież listę
                    }
                });
            }

            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                selectedRow = row;
                return button;
            }

            public Object getCellEditorValue() {
                return "";
            }
        });

        // Akcja przycisku
        btnSzukaj.addActionListener(e -> odswiezListe());

        // Wyświetl wszystkich na starcie
        odswiezListe();
    }

    private void odswiezListe() {
        model.setRowCount(0);
        String filtr = poleSzukaj.getText().trim();
        List<String[]> uzytkownicy = BazaDanych.pobierzUzytkownikow("WSZYSCY", filtr);

        for (String[] u : uzytkownicy) {
            model.addRow(new Object[]{u[0], u[1], u[2], u[3], "Usuń"});
        }
    }
}
