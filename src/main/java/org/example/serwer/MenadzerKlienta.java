package org.example.serwer;

import org.example.baza.BazaDanych;
import org.example.model.Uzytkownik;

import java.io.*;
import java.net.Socket;

public class MenadzerKlienta extends Thread {
    private final Socket socket;
    private BufferedReader wejscie;
    private PrintWriter wyjscie;

    public MenadzerKlienta(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            wejscie = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            wyjscie = new PrintWriter(socket.getOutputStream(), true);

            wyjscie.println("Witaj! Podaj komendę (LOGIN;nazwa;haslo):");

            String linia;
            while ((linia = wejscie.readLine()) != null) {
                System.out.println("Odebrano od klienta: " + linia);
                String[] dane = linia.split(";");

                switch (dane[0].toUpperCase()) {
                    case "LOGIN" -> {
                        if (dane.length < 3) {
                            wyjscie.println("Błąd: brak danych logowania.");
                            continue;
                        }
                        Uzytkownik u = BazaDanych.zalogujUzytkownika(dane[1], dane[2]);
                        if (u != null) {
                            String status = BazaDanych.sprawdzStatus(dane[1]);
                            if ("zwolniony".equalsIgnoreCase(status)) {
                                wyjscie.println("Pracownik " + dane[1] + " został zwolniony i nie może się zalogować.");
                            } else {
                                wyjscie.println("Zalogowano jako: " + u.getLogin() + " [" + u.getRola() + "]");
                            }
                        } else {
                            wyjscie.println("Nieprawidłowy login lub hasło.");
                        }
                    }

                    case "EXIT" -> {
                        wyjscie.println("Zamykanie połączenia...");
                        socket.close();
                        return;
                    }
                    default -> wyjscie.println("Nieznana komenda.");
                }
            }

        } catch (IOException e) {
            System.err.println("Błąd klienta: " + e.getMessage());
        }
    }
}
