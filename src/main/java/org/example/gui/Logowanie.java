package org.example.gui;

import org.example.klient.Klient;

import javax.swing.*;

public class Logowanie extends JFrame {

    JPanel panel = new JPanel();

    public Logowanie() {
        setTitle("Logowanie");
        setSize(400, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void initComponents() {
        JButton przyciskZamknij = new JButton("Zamknij");
        JButton przyciskOK = new JButton("OK");
        JLabel loginLabel = new JLabel("Login: ");
        JLabel hasloLabel = new JLabel("Hasło: ");
        JTextField poleLogin = new JTextField(10);
        JPasswordField poleHaslo = new JPasswordField(10);

        panel.setLayout(null);

        // Ustawienia pozycji
        loginLabel.setBounds(50, 50, 100, 30);
        hasloLabel.setBounds(50, 90, 100, 30);
        poleLogin.setBounds(150, 50, 150, 30);
        poleHaslo.setBounds(150, 90, 150, 30);
        przyciskOK.setBounds(80, 150, 100, 30);
        przyciskZamknij.setBounds(200, 150, 100, 30);

        panel.add(loginLabel);
        panel.add(hasloLabel);
        panel.add(poleLogin);
        panel.add(poleHaslo);
        panel.add(przyciskOK);
        panel.add(przyciskZamknij);

        getContentPane().add(panel);

        przyciskZamknij.addActionListener(e -> System.exit(0));

        przyciskOK.addActionListener(e -> {
            String login = poleLogin.getText();
            String haslo = new String(poleHaslo.getPassword());

            String odpowiedz = Klient.logowanie(login, haslo);

            if (odpowiedz.startsWith("Zalogowano")) {
                JOptionPane.showMessageDialog(this, odpowiedz);
                new Okno(); // Twoje główne okno po zalogowaniu
                dispose(); // Zamknięcie okna logowania
            } else {
                JOptionPane.showMessageDialog(this, "Błąd logowania: " + odpowiedz);
            }
        });
    }
}
