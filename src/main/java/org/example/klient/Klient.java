package org.example.klient;

import java.io.*;
import java.net.Socket;

public class Klient {
    public static String zalogowanyLogin;
    public static String zalogowanaRola;

    public static String logowanie(String login, String haslo) {
        try (Socket socket = new Socket("127.0.0.1", 1234);
             BufferedReader wejscie = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter wyjscie = new PrintWriter(socket.getOutputStream(), true)) {

            wejscie.readLine(); // Witaj
            wyjscie.println("LOGIN;" + login + ";" + haslo);
            String odpowiedz = wejscie.readLine();

            if (odpowiedz.startsWith("Zalogowano")) {
                zalogowanyLogin = login;
                zalogowanaRola = odpowiedz.split("\\[")[1].replace("]", ""); // np. "ADMIN"
            }

            return odpowiedz;

        } catch (IOException e) {
            return "Błąd połączenia z serwerem: " + e.getMessage();
        }
    }
}
