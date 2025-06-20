package org.example.start;


import org.example.baza.BazaDanych;

public class DodajPierwszegoUzytkownika {
    public static void main(String[] args) {
        boolean sukces = BazaDanych.dodajUzytkownika("tomasz", "123", "ADMIN");
        if (sukces) {
            System.out.println("Uzytkownik dodany pomyslnie");
        } else {
            System.out.println("Nie udalo się dodac użytkownika");
        }
    }
}
