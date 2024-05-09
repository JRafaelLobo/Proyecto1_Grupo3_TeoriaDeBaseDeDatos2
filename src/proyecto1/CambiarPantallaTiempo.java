/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

/**
 *
 * @author rinal
 */
public class CambiarPantallaTiempo extends Thread {

    private JFrame Cambio, Anterior;
    private int milisegundos;
    private Clip Music;
    private boolean Extended, personalizado=false,finalizar;
    private Main main;

    public CambiarPantallaTiempo(JFrame Cambio, JFrame Anterior, int milisegundos) {
        this.Cambio = Cambio;
        this.Anterior = Anterior;
        this.milisegundos = milisegundos;
        Extended = false;
    }

    public CambiarPantallaTiempo(JFrame Cambio, JFrame Anterior, int milisegundos, boolean Extended) {
        this.Cambio = Cambio;
        this.Anterior = Anterior;
        this.milisegundos = milisegundos;
        this.Extended = Extended;
    }

    public CambiarPantallaTiempo(JFrame Cambio, JFrame Anterior, boolean personalizado,Main main) {
        this.Cambio = Cambio;
        this.Anterior = Anterior;
        this.personalizado = personalizado;
    }

    public void setFinalizar(boolean finalizar) {
        this.finalizar = finalizar;
    }
    

    @Override
    public void run() {

        Anterior.setVisible(false);
        Cambio.pack();
        Cambio.setLocationRelativeTo(Anterior);
        Cambio.setVisible(true);
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException ex) {
        }
        Cambio.setVisible(false);
        Anterior.pack();
        Anterior.setLocationRelativeTo(Cambio);
        if (Extended) {
            Anterior.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        Anterior.setVisible(true);
    }

    public void playMusic(String filepath) {
        try {
            File music = new File(filepath);
            AudioInputStream AudioImput = AudioSystem.getAudioInputStream(music);
            Music = AudioSystem.getClip();
            Music.open(AudioImput);
        } catch (Exception e) {
            System.out.println("El Archivo no Existe");
            e.printStackTrace();
        }
    }
}

