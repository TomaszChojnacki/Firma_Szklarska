package testy;

import org.example.baza.BazaDanych;
import org.example.model.Uzytkownik;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class BazaDanychStatusTest {

    private static final String LOGIN = "testowy";
    private static final String HASLO = "123";
    private static final String ROLA = "MAGAZYNIER";

    @BeforeEach
    public void przygotuj() {
        BazaDanych.usunUzytkownika(LOGIN);
        BazaDanych.dodajUzytkownika(LOGIN, HASLO, ROLA);
    }

    @Test
    public void testUstawienieStatusuZwolniony() {
        boolean sukces = BazaDanych.ustawStatus(LOGIN, "ZWOLNIONY");
        assertTrue(sukces, "Status powinien zostać zmieniony");

        String status = BazaDanych.sprawdzStatus(LOGIN);
        assertEquals("ZWOLNIONY", status, "Status powinien być ustawiony na 'ZWOLNIONY'");
    }

    @Test
    public void testUstawienieStatusuDobry() {
        BazaDanych.ustawStatus(LOGIN, "ZWOLNIONY");
        BazaDanych.ustawStatus(LOGIN, "DOBRY");

        String status = BazaDanych.sprawdzStatus(LOGIN);
        assertEquals("DOBRY", status, "Status powinien być przywrócony do 'DOBRY'");
    }

    @AfterEach
    public void posprzataj() {
        BazaDanych.usunUzytkownika(LOGIN);
    }
}
