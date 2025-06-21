package testy;

import org.example.baza.BazaDanych;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BazaDanychZamowienieTest {

    private static final String LOGIN = "test";

    @BeforeEach
    public void setUp() {
        BazaDanych.usunUzytkownika(LOGIN);
        BazaDanych.dodajUzytkownika(LOGIN, "haslo123", "KLIENT");

        // Dodajemy 2 produkty do koszyka
        BazaDanych.dodajDoKoszyka(LOGIN, "Szyba hartowana 5mm", 199.99);
        BazaDanych.dodajDoKoszyka(LOGIN, "Lustro na wymiar", 299.49);
    }

    @Test
    public void testZlozZamowienie() {
        // Przed złożeniem
        List<String[]> koszyk = BazaDanych.pobierzKoszykUzytkownika(LOGIN);
        assertEquals(2, koszyk.size(), "Koszyk powinien mieć 2 pozycje przed złożeniem");

        // Złożenie
        BazaDanych.zlozZamowienie(LOGIN);

        // Po złożeniu
        List<String[]> zamowienia = BazaDanych.pobierzZamowieniaGrupowane(LOGIN);
        assertFalse(zamowienia.isEmpty(), "Powinno istnieć przynajmniej jedno zamówienie");
        assertEquals("rozpoczęcie prac", zamowienia.get(0)[1], "Status zamówienia powinien być 'rozpoczęcie prac'");
        assertNotNull(zamowienia.get(0)[0], "Grupa ID zamówienia nie powinna być pusta");
    }

    @AfterEach
    public void tearDown() {
        // Usunięcie zamówień i użytkownika
        List<String[]> zamowienia = BazaDanych.pobierzZamowieniaGrupowane(LOGIN);
        for (String[] z : zamowienia) {
            BazaDanych.usunZamowienieGrupowe(z[0]);
        }
        BazaDanych.usunUzytkownika(LOGIN);
    }
}
