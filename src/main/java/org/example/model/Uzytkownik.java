package org.example.model;

public class Uzytkownik {
    private int id;
    private String login;
    private String haslo;
    private String rola;

    public Uzytkownik(int id, String login, String haslo, String rola) {
        this.id = id;
        this.login = login;
        this.haslo = haslo;
        this.rola = rola;
    }

    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getHaslo() { return haslo; }
    public String getRola() { return rola; }
}
