package org.example.baza;

import org.example.model.Uzytkownik;

import java.sql.*;
import java.util.List;

public class BazaDanych {
    private static final String URL = "jdbc:postgresql://localhost:5432/firma_szklarska";
    private static final String UZYTKOWNIK = "postgres";
    private static final String HASLO = "uczen";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika PostgreSQL");
        }
    }

    public static Uzytkownik zalogujUzytkownika(String login, String haslo) {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "SELECT * FROM uzytkownicy WHERE login = ? AND haslo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, haslo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Uzytkownik(
                        rs.getInt("id"),
                        rs.getString("login"),
                        rs.getString("haslo"),
                        rs.getString("rola")
                );
            }
        } catch (SQLException e) {
            System.err.println("Błąd bazy: " + e.getMessage());
        }
        return null;
    }

    public static boolean dodajUzytkownika(String login, String haslo, String rola) {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "INSERT INTO uzytkownicy (login, haslo, rola, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, haslo);
            stmt.setString(3, rola.toUpperCase());
            stmt.setString(4, "DOBRY");

            int wstawione = stmt.executeUpdate();
            return wstawione > 0;

        } catch (SQLException e) {
            System.err.println("Błąd dodawania użytkownika: " + e.getMessage());
            return false;
        }
    }

    public static String sprawdzStatus(String login) {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "SELECT status FROM uzytkownicy WHERE login = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            System.err.println("Błąd sprawdzania statusu: " + e.getMessage());
        }
        return null;
    }

    public static boolean ustawStatus(String login, String nowyStatus) {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "UPDATE uzytkownicy SET status = ? WHERE login = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nowyStatus.toLowerCase());
            stmt.setString(2, login);
            int zmienione = stmt.executeUpdate();
            return zmienione > 0;
        } catch (SQLException e) {
            System.err.println("Błąd zmiany statusu: " + e.getMessage());
            return false;
        }
    }
    public static List<String[]> pobierzUzytkownikow(String rolaLubSpecjalne, String filtrTekstu) {
        List<String[]> lista = new java.util.ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql;

            if ("PRACOWNICY".equals(rolaLubSpecjalne)) {
                // Szukamy wszystkich poza klientami
                sql = "SELECT id, login, rola, status FROM uzytkownicy " +
                        "WHERE rola != 'KLIENT' AND (login ILIKE ? OR rola ILIKE ? OR status ILIKE ?)";
            } else {
                // Szukamy konkretną rolę
                sql = "SELECT id, login, rola, status FROM uzytkownicy " +
                        "WHERE rola = ? AND (login ILIKE ? OR rola ILIKE ? OR status ILIKE ?)";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);

            if ("PRACOWNICY".equals(rolaLubSpecjalne)) {
                stmt.setString(1, "%" + filtrTekstu + "%");
                stmt.setString(2, "%" + filtrTekstu + "%");
                stmt.setString(3, "%" + filtrTekstu + "%");
            } else {
                stmt.setString(1, rolaLubSpecjalne);
                stmt.setString(2, "%" + filtrTekstu + "%");
                stmt.setString(3, "%" + filtrTekstu + "%");
                stmt.setString(4, "%" + filtrTekstu + "%");
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("login"),
                        rs.getString("rola"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.err.println("Błąd pobierania użytkowników: " + e.getMessage());
        }
        return lista;
    }




}
