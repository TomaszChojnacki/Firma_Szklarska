CREATE TABLE uzytkownicy (
    id SERIAL PRIMARY KEY,
    login VARCHAR(50) NOT NULL UNIQUE,
    haslo VARCHAR(50) NOT NULL,
    rola VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DOBRY'
);

INSERT INTO uzytkownicy (login, haslo, rola, status) VALUES
('tomasz', '123', 'ADMIN', 'DOBRY'),
('maciek', '123', 'PRACOWNIK', 'DOBRY'),
('klient', 'klient123', 'KLIENT', 'DOBRY'),
('biuro', '123', 'PRACOWNIK_OBSLUGI_KLIENTA', 'DOBRY');
