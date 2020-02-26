/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.Date;

/**
 *
 * @author lolac
 */
public class MessageController {
    
    String id_user_orig;
    String id_user_dest;
    String text;
    
    String fecha;
    
    int read;
    int sent;

    public MessageController(String id_user_orig, String id_user_dest, String text, String fecha, int read, int sent) {
        this.id_user_orig = id_user_orig;
        this.id_user_dest = id_user_dest;
        this.text = text;
        this.fecha = fecha;
        this.read = read;
        this.sent = sent;
    }

    public MessageController() {
        
        this.id_user_orig = "";
        this.id_user_dest = "";
        this.text = "";
        this.fecha = "";
        this.read = 0;
        this.sent = 0;
    }

    public MessageController(String id_user_orig, String id_user_dest, String text) {
        this.id_user_orig = id_user_orig;
        this.id_user_dest = id_user_dest;
        this.text = text;
    }

    public String getId_user_orig() {
        return id_user_orig;
    }

    public String getId_user_dest() {
        return id_user_dest;
    }

    public String getText() {
        return text;
    }

    public String getFecha() {
        return fecha;
    }

    public int getRead() {
        return read;
    }

    public int getSent() {
        return sent;
    }

    public void setId_user_orig(String id_user_orig) {
        this.id_user_orig = id_user_orig;
    }

    public void setId_user_dest(String id_user_dest) {
        this.id_user_dest = id_user_dest;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    @Override
    public String toString() {
        String mensaje = "Text: " + text + "\nFrom: " + id_user_orig + "\nTo: " + id_user_dest + "\nWhen: " + fecha + "\n\n";
        return mensaje;
    }
    
    
}
