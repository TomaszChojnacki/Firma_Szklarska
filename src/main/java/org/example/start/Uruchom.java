package org.example.start;

import com.formdev.flatlaf.FlatLightLaf;
import org.example.gui.Logowanie;

import javax.swing.*;

public class Uruchom {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Nie udało się załadować stylu.");
        }

        new Logowanie().setVisible(true);
    }
}
