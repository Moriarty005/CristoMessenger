package controlador;

import Vista.vista_server;
import java.sql.Timestamp;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author lolac
 */
public class Protocol {

    
    public Protocol(){
        
    }
    
    public boolean checkIfStartIsCorrect(String s){
        boolean action = false;
        
        String[] query = s.split("#");
        
        if(query[0].equals("PROTOCOLCRISTOMESSENGER1.0")){
            action = true;
            return action;
        }
            
        return action;
    }
    
    /*
    *@brief This method is going to check the string passed through to detect if the first 40 characters of it is the clue for loggin in
    */
    public String checkLoginClue(String input){
                
        String[] ss = input.split("#");
        
        //vista_server.debug("Login: " + ss[4] + "\n" + "Passwd: " + ss[5]);
        
        return ss[4] + "#" + ss[5];
    }
    
    
    public String getUserLoginFromRaw(String raw_input){
        
        String output = null;
        
        String[] cosas = raw_input.split("#");
        output = cosas[0];
        
        return output;
    }
    
    public String getPasswordFromRaw(String raw_input){
        
        String output = null;
        
        String[] cosas = raw_input.split("#");
        output = cosas[1];
        
        return output;
    }
    
    public String checkActionToDo(String s){
        
        String action = null;
        
        String[] query = s.split("#");
        
        //vista_server.debug("Que vamos a hacer: " + query[3]);
        
        if(query[3].equals("LOGIN")){
            action = query[3];
            return action;
        }else if(query[3].equals("MSGS")){
            action = query[3];
            return action;
        }else if(query[3].equals("ALLDATA_USER")){
            action = query[3];
            return action;
        }else if(query[3].equals("STATUS")){
            action = query[3];
            return action;
        }
        
        return action;
    }
    
    
    
    public String checkMessageActionToDo(String s){
        
//vista_server.debug("ENTRAMOS EN EL MÉTODO DE DE CHECKEAR QUE ACCION TENEMOS QUE HACE CONS RESPECTO A LOS MENSAJES");
//vista_server.debug("VALOR DEL STRING QUE ENTRA EN EL MÉTODO DE CHECKEAR ACCION DE MENSAJES A  HACER: " + s);
        String[] split = s.split("#");
        
        String send = "";
        //vista_server.debug("Lo que nos llega al tener que hacer algo con los mensajes: " + s);
//vista_server.debug("DEBUG: valor de la cuarta celda del split: " + split[4]);
        if(split[4].equals("OK_SEND!")){
            send = "SEND";
            //return send;
        }else if(split[4].equals("ALL_RECEIVED")){
            send = "RECEIVED";
            //return send;
        }else{
            send = "PREP";
            //return send;
        }
        
        return send;
    }
    
    public String createQueryNumberOfMessages(int total_por_fecha, int total, String nombres){
        
        String[] ss = nombres.split("#");
        
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        
        String query = "PROTOCOLCRISTOMESSENGER1.0#" + ts +"#SERVER#MSGS#" +ss [4] + "#" + ss[5] + "#" + total + "#" + total_por_fecha;
        
//vista_server.debug("Query que hemos creado con todas las fechas: " + query);
        
        return query;
    }
    
    public String sendInfoOfUser(String s){
        
        String[] ss = s.split("#");
        
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        
        String query= "";
        
        if(s != null){
            query = "PROTOCOLCRISTOMESSENGER1.0#" + ts + "#SERVER#ALLDATA_USER#" + ss[0] + "#" + ss[1] + "#" + ss[2] + "#" + ss[3];
        }else{
            query = "PROTOCOLCRISTOMESSENGER1.0#" + ts +"#SERVER#BAD_DATA";
        }
        return query;
    }
    
    public String treatQueryAllUserInfo(String s){
        
        String[] ss = s.split("#");
        
        return ss[4];
    }
    
    public String getMessageUserDest(String s){
        
        String[] ss = s.split("#");
        
        return ss[5];
    }
    
    public String getMessageUserOrig(String s){
        
        String[] ss = s.split("#");
        
        return ss[4];
    }
    
    public boolean checkIfAllMessagesReceived(String s){
        
        if(this.checkIfStartIsCorrect(s)){
            
            String[] ss = s.split("#");

            if(ss[4].equals("ALL_RECEIVED")){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }
    
    
    public String createSendMessageQuery(String s){
        
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        
        String ss = "PROTOCOLCRISTOMESSENGER1.0#" + ts + "#SERVER#MSGS#" + s;
        
        return ss;
    }
    
    public String getIdOfTheFriendToGetStatus(String s){
        
        if(this.checkIfStartIsCorrect(s)){
            
            String[] ss = s.split("#");

            return ss[5];
        }else{
            return null;
        }
    }
    
    public String creteQueryOfStatus(String friend_id, String status){
        
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        
        String cosa = "PROTOCOLCRISTOMESSENGER1.0#" + ts +"#SERVER#STATUS#" + friend_id + "#" + status;
        
        return cosa;
        
    }
    
    public String getYearOfTheMessageDate(String cosa){
        
        String[] ss = cosa.split("-");
        
        return ss[0];
    }
    
    public String getMonthOfTheMessageDate(String cosa){
        
        String[] ss = cosa.split("-");
        
        return ss[1];
    }
    
    public String getDayOfTheMessageDate(String cosa){
        
        String[] ss = cosa.split("-");
        
        return ss[2].substring(0, 2);
    }
}
