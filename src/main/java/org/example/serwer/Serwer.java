package org.example.serwer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Serwer {

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serwerSocket = new ServerSocket(port)) {
            System.out.println("Serwer działa na porcie " + port);

            while (true) {
                Socket klientSocket = serwerSocket.accept();
                System.out.println("Nowy klient połączony: " + klientSocket);

                // Tworzymy nowy wątek dla każdego klienta
                new MenadzerKlienta(klientSocket).start();
            }

        } catch (IOException e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        }
    }
}
