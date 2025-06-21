package testy;

import org.example.baza.BazaDanych;
import org.example.model.Uzytkownik;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BazaDanychLogowanieTest {

    private static final String LOGIN = "testowy";
    private static final String HASLO = "123";
    private static final String ROLA = "PRACOWNIK_OBSLUGI_KLIENTA";

    @BeforeEach
    public void setUp() {
        BazaDanych.usunUzytkownika(LOGIN); // upewniamy się, że użytkownik nie istnieje
        BazaDanych.dodajUzytkownika(LOGIN, HASLO, ROLA);
    }

    @Test
    public void testLogowanieUzytkownikaZPoprawnymiDanymi() {
        Uzytkownik wynik = BazaDanych.zalogujUzytkownika(LOGIN, HASLO);
        assertNotNull(wynik, "Użytkownik powinien się zalogować z poprawnymi danymi");
    }

    @Test
    public void testLogowanieUzytkownikaZBlednymHaslem() {
        Uzytkownik wynik = BazaDanych.zalogujUzytkownika(LOGIN, "zleHaslo");
        assertNull(wynik, "Użytkownik nie powinien się zalogować z błędnym hasłem");
    }

    @AfterEach
    public void tearDown() {
        BazaDanych.usunUzytkownika(LOGIN); // sprzątanie po testach
    }
}
