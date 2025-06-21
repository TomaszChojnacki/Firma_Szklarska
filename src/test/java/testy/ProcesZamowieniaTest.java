package testy;

import org.example.baza.BazaDanych;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProcesZamowieniaTest {

    private static final String LOGIN = "test_klient";
    private static final String HASLO = "haslo123";
    private static String grupaIdZamowienia;

    @BeforeEach
    public void setUp() {
        // Usunięcie starego użytkownika i jego zamówień
        BazaDanych.usunUzytkownika(LOGIN);
        BazaDanych.dodajUzytkownika(LOGIN, HASLO, "KLIENT");

        // Dodanie produktów do koszyka
        BazaDanych.dodajDoKoszyka(LOGIN, "Szyba bezpieczna 6mm", 300.00);
        BazaDanych.dodajDoKoszyka(LOGIN, "Drzwi szklane z zawiasami", 799.99);
    }

    @Test
    public void testPelnyProcesZamowienia() {
        // 1. Weryfikacja koszyka
        List<String[]> koszyk = BazaDanych.pobierzKoszykUzytkownika(LOGIN);
        assertEquals(2, koszyk.size(), "Koszyk powinien zawierać 2 pozycje");

        // 2. Złożenie zamówienia
        BazaDanych.zlozZamowienie(LOGIN);

        // 3. Pobranie zamówień
        List<String[]> zamowienia = BazaDanych.pobierzZamowieniaGrupowane(LOGIN);
        assertFalse(zamowienia.isEmpty(), "Zamówienie powinno zostać utworzone");

        String status = zamowienia.get(0)[1];
        grupaIdZamowienia = zamowienia.get(0)[0];

        assertEquals("rozpoczęcie prac", status, "Status po złożeniu powinien być 'rozpoczęcie prac'");
        assertNotNull(grupaIdZamowienia, "Grupa ID zamówienia nie powinna być pusta");

        // 4. Sprawdzenie pozycji w zamówieniu
        List<String[]> pozycje = BazaDanych.pobierzPozycjeZamowienia(grupaIdZamowienia);
        assertEquals(2, pozycje.size(), "Zamówienie powinno mieć 2 pozycje");

        // 5. Zmiana statusu na "gotowe"
        boolean zmieniono = BazaDanych.ustawStatusZamowieniaGrupowo(grupaIdZamowienia, "gotowe");
        assertTrue(zmieniono, "Status powinien zostać zmieniony");

        List<String[]> poZmianie = BazaDanych.pobierzZamowieniaGrupowane(LOGIN);
        assertEquals("gotowe", poZmianie.get(0)[1], "Status powinien wynosić 'gotowe' po zmianie");
    }

    @AfterEach
    public void tearDown() {
        // Czyszczenie danych po teście
        if (grupaIdZamowienia != null) {
            BazaDanych.usunZamowienieGrupowe(grupaIdZamowienia);
        }
        BazaDanych.usunUzytkownika(LOGIN);
    }
}
