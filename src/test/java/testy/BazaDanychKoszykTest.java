package testy;

import org.example.baza.BazaDanych;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BazaDanychKoszykTest {

    private static final String LOGIN = "testowy";
    private static final String HASLO = "123";
    private static final String ROLA = "KLIENT";

    @BeforeEach
    public void setUp() {
        BazaDanych.usunUzytkownika(LOGIN); // czyszczenie
        BazaDanych.dodajUzytkownika(LOGIN, HASLO, ROLA);
    }

    @Test
    public void testDodanieIPobranieZKoszyka() {
        // Dodaj do koszyka
        BazaDanych.dodajDoKoszyka(LOGIN, "Testowy produkt", 99.99);

        List<String[]> koszyk = BazaDanych.pobierzKoszykUzytkownika(LOGIN);

        assertNotNull(koszyk, "Koszyk nie powinien być nullem");
        assertFalse(koszyk.isEmpty(), "Koszyk nie powinien być pusty");

        String[] pozycja = koszyk.get(0);
        assertEquals("Testowy produkt", pozycja[1]);
        assertEquals(99.99, Double.parseDouble(pozycja[2].replace(",", ".")), 0.01);

    }

    @AfterEach
    public void tearDown() {
        // Usunięcie wszystkich pozycji w koszyku dla testowego użytkownika
        List<String[]> koszyk = BazaDanych.pobierzKoszykUzytkownika(LOGIN);
        for (String[] pozycja : koszyk) {
            int id = Integer.parseInt(pozycja[0]);
            BazaDanych.usunZamowienieZKoszyka(id);
        }
        BazaDanych.usunUzytkownika(LOGIN);
    }
}
