package org.example.baza;

import org.example.model.Uzytkownik;
import org.example.model.Hasher;

import java.sql.*;
import java.util.ArrayList;
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
            stmt.setString(2, Hasher.sha256(haslo)); // HASH hasła
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
            stmt.setString(2, Hasher.sha256(haslo)); // HASH hasła
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
            String sql;
            PreparedStatement stmt;

            if ("zwolniony".equalsIgnoreCase(nowyStatus)) {
                sql = """
                  UPDATE uzytkownicy
                  SET status = ?, data_wygasniecia = CURRENT_DATE + INTERVAL '30 days'
                  WHERE login = ?
                  """;
            } else {
                sql = """
                  UPDATE uzytkownicy
                  SET status = ?, data_wygasniecia = NULL
                  WHERE login = ?
                  """;
            }

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nowyStatus.toLowerCase());
            stmt.setString(2, login);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Błąd zmiany statusu: " + e.getMessage());
            return false;
        }
    }

    public static void usunWygasleKonta() {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = """
            DELETE FROM uzytkownicy
            WHERE status = 'zwolniony'
              AND data_wygasniecia IS NOT NULL
              AND data_wygasniecia < CURRENT_DATE
        """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            int usunietych = stmt.executeUpdate();
            System.out.println("Usunięto " + usunietych + " wygasłych kont.");
        } catch (SQLException e) {
            System.err.println("Błąd usuwania wygasłych kont: " + e.getMessage());
        }
    }

    public static boolean usunUzytkownika(String login) {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "DELETE FROM uzytkownicy WHERE login = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Błąd usuwania: " + e.getMessage());
            return false;
        }
    }

    public static List<String[]> pobierzUzytkownikow(String rolaLubSpecjalne, String filtrTekstu) {
        List<String[]> lista = new java.util.ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql;

            if ("PRACOWNICY".equals(rolaLubSpecjalne)) {
                sql = "SELECT id, login, rola, status FROM uzytkownicy " +
                        "WHERE rola != 'KLIENT' AND (login ILIKE ? OR rola ILIKE ? OR status ILIKE ?)";
            } else if ("WSZYSCY".equals(rolaLubSpecjalne)) {
                sql = "SELECT id, login, rola, status FROM uzytkownicy " +
                        "WHERE (login ILIKE ? OR rola ILIKE ? OR status ILIKE ?)";
            } else {
                sql = "SELECT id, login, rola, status FROM uzytkownicy " +
                        "WHERE rola = ? AND (login ILIKE ? OR rola ILIKE ? OR status ILIKE ?)";
            }

            PreparedStatement stmt = conn.prepareStatement(sql);

            if ("PRACOWNICY".equals(rolaLubSpecjalne) || "WSZYSCY".equals(rolaLubSpecjalne)) {
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


    public static void dodajDoKoszyka(String login, String opis, double cena) {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "INSERT INTO zamowienia (login, opis, cena, status) VALUES (?, ?, ?, 'w koszyku')";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, opis);
            stmt.setDouble(3, cena);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd zapisu koszyka: " + e.getMessage());
        }
    }

    public static List<String[]> pobierzKoszykUzytkownika(String login) {
        List<String[]> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "SELECT id, opis, cena FROM zamowienia WHERE login = ? AND status = 'w koszyku'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new String[] {
                        String.valueOf(rs.getInt("id")),
                        rs.getString("opis"),
                        String.format("%.2f", rs.getDouble("cena"))
                });
            }
        } catch (SQLException e) {
            System.err.println("Błąd pobierania koszyka: " + e.getMessage());
        }
        return lista;
    }

    public static void usunZamowienieZKoszyka(int idZamowienia) {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "DELETE FROM zamowienia WHERE id = ? AND status = 'w koszyku'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idZamowienia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd usuwania zamówienia z koszyka: " + e.getMessage());
        }
    }


    public static void usunStareZKoszyka() {
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "DELETE FROM zamowienia WHERE status = 'w koszyku' AND data_dodania < NOW() - INTERVAL '2 days'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd usuwania starych z koszyka: " + e.getMessage());
        }
    }

    public static void zlozZamowienie(String login) {
        String updateSQL = "UPDATE zamowienia SET status = 'rozpoczęcie prac', grupa_id = CAST(? AS UUID) WHERE login = ? AND status = 'w koszyku'";
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String grupaId = java.util.UUID.randomUUID().toString();
            PreparedStatement stmt = conn.prepareStatement(updateSQL);
            stmt.setString(1, grupaId);
            stmt.setString(2, login);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd składania zamówienia: " + e.getMessage());
        }
    }

    public static List<String[]> pobierzZamowieniaGrupowane(String login) {
        List<String[]> lista = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO)) {
            String sql = "SELECT grupa_id, status FROM zamowienia " +
                    "WHERE login = ? AND grupa_id IS NOT NULL " +
                    "GROUP BY grupa_id, status ORDER BY MIN(data_dodania) DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("grupa_id"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.err.println("Błąd pobierania zamówień: " + e.getMessage());
        }
        return lista;
    }

    public static List<String[]> pobierzPozycjeZamowienia(String grupaId) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT opis, cena FROM zamowienia WHERE grupa_id = CAST(? AS UUID)";
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, grupaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add(new String[] {
                        rs.getString("opis"),
                        String.format("%.2f", rs.getDouble("cena"))
                });
            }
        } catch (SQLException e) {
            System.err.println("Błąd pobierania pozycji zamówienia: " + e.getMessage());
        }
        return lista;
    }

    public static void usunZamowienieGrupowe(String grupaId) {
        String sql = "DELETE FROM zamowienia WHERE grupa_id = CAST(? AS UUID)";
        try (Connection conn = DriverManager.getConnection(URL, UZYTKOWNIK, HASLO);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, grupaId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd usuwania zamówienia: " + e.getMessage());
        }
    }


}
