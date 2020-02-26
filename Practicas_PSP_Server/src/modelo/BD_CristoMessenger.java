/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import Vista.vista_server;
import controlador.Protocol;
import controlador.Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lolac
 */
public class BD_CristoMessenger {
    
    public Protocol p;
    
    public BD_CristoMessenger(Protocol pro){
        
        this.p = pro;
    }
    
    public Connection connect_to_db(){
        
        Connection conexion = null;
    
        try {
            
            Properties propiedades_de_la_conexion;
            propiedades_de_la_conexion = new Properties();
            /*propiedades_de_la_conexion.put("user", "clasedam2");
            propiedades_de_la_conexion.put("password", "root");*/
            
            propiedades_de_la_conexion.put("user", "root");
            propiedades_de_la_conexion.put("password", "root");          
//System.out.println("Hemos creado las propiedades");            
            
            //conexion = DriverManager.getConnection("jdbc:mysql://clasedam2.ddns.net:3306/", propiedades_de_la_conexion);
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/", propiedades_de_la_conexion);
            
//System.out.println("Hemos asignado el valor de la conexion del obejto driver manager");  
            
        } catch (SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return conexion;
        
    }
    
    public Statement creacion_solo_statement() throws SQLException{
        Connection conexion_con_db = connect_to_db();
        
        Statement state_de_conexion = null;      

        state_de_conexion = conexion_con_db.createStatement();
        
        return state_de_conexion;
    }
    
    
    public boolean checkUserLoginAndPasswd(String new_id_user, String new_passwd) throws SQLException{
        //vista_server.debug("Entramos en el comprobar el nombre y la passwd");
        boolean loged_or_not = false;
        
        Statement stado = this.creacion_solo_statement();
        
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        String query = "SELECT * FROM user WHERE id_user='" + new_id_user + "' AND password='" + new_passwd + "';";
        
vista_server.debug("DEBUG: QUERY PARA OBTENER : " + query);

        ResultSet resultado = null;

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
        
        while(resultado.next()){
            
            vista_server.debug("Entramos en el bucle de la BD");
            
            String id_user = resultado.getString("id_user");
            String passwd_user = resultado.getString("password");
            int connected = resultado.getInt("state");
            
            vista_server.debug("ID_user_BD: " + id_user + "\tID_user_Cliente: " + new_id_user);
            vista_server.debug("PASWD_user_BD: " + passwd_user + "\tPASSWD_user_Cliente: " + new_passwd);
            if(connected == 0){
                if(id_user.equals(new_id_user) && passwd_user.equals(new_passwd)){
                    vista_server.debug("Entramos en el if que comprueba el nombre y la passwd");
                    loged_or_not = true;
                    stado.close();
                    resultado.close();
                    return loged_or_not;
                }
            }
            
        }
        
        stado.close();
        resultado.close();
        return loged_or_not;
        
    }
    
    public String returnFriendsOfUser(String user_login) throws SQLException{
        
        //Date fecha = new Date();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        
        String devolver = "PROTOCOLCRISTOMESSENGER1.0#" + ts + "#SERVER#LOGIN_CORRECT#" + user_login + "#FRIENDS#";
        
        Statement stado = this.creacion_solo_statement();
        
        String db = "USE local_cm;";
        //String db = "USE cristomessenger;";
        String query = "SELECT * FROM friend WHERE id_user_orig='" + user_login + "';";
        
vista_server.debug("DEBUG: QUERY PARA OBTENER : " + query);

        ResultSet resultado = null;

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
        
        int number_of_friends = 0;
        
        String friends = "#";
        String friend = "";
        
        
        while(resultado.next()){
            
            String id_user_orig = resultado.getString("id_user_orig");
            String id_user_dest = resultado.getString("id_user_dest");
            
            
            if(id_user_orig.equals(user_login)){
                
                number_of_friends++;
                
                if(this.returnIfUserConnected(id_user_dest) == true){
                    
//vista_server.debug("Entramos en el comprobar si el amigo esta conectado o desconectado");
                    
                    friend = id_user_dest + "#CONNECTED#";
//vista_server.debug("Valor del subString que vamos a concatenar: " + friend);
                    friends += friend;
                    
//vista_server.debug("COMO VA POR AHORA LA QUERY: " + friends);

                }else{
                    
                   friend = id_user_dest + "#DISCONNECTED#";
//vista_server.debug("Valor del subString que vamos a concatenar: " + friend);
                    friends += friend;
                    
//vista_server.debug("COMO VA POR AHORA LA QUERY: " + friends);
                }
            }
        }
        
        stado.close();
        resultado.close();
        
        devolver += String.valueOf(number_of_friends);
        devolver += friends;
        
vista_server.debug("Amigos del usuario " + user_login + ": " + devolver);
        
        return devolver;
        
    }
    
    public boolean returnIfUserConnected(String user_login) throws SQLException{
        
        
        boolean conectado = false;
                
        Statement stado = this.creacion_solo_statement();
        
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        String query = "SELECT * FROM user WHERE id_user='" + user_login + "';";
        
vista_server.debug("DEBUG: QUERY PARA OBTENER : " + query);

        ResultSet resultado = null;

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
        
        while(resultado.next()){
            
            String id_user_orig = resultado.getString("id_user");
            int connected = resultado.getInt("state");
            
            if(id_user_orig.equals(user_login)){
                if(connected == 1){
//vista_server.debug("El usuario amigo " + user_login + " está conectado");
                    conectado = true;
                    return conectado;
                    
                }else if(connected == 0){
                    
                    conectado = false;
                    return conectado;
                }
            }
        } 
        
        resultado.close();
        return conectado;
    }
    
    public int bringTotalMessages(String s) throws SQLException{
//vista_server.debug("Entramos en el metodo de obetener todos los mensajes de un usuario");
        String[] ss = s.split("#");
        
        Statement stado = this.creacion_solo_statement();
       
        
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        String query = "SELECT * FROM message WHERE (id_user_orig='" + ss[4] + "' AND id_user_dest='" + ss[5] + "') OR (id_user_orig='" + ss[5] + "' AND id_user_dest='" + ss[4] + "');";
        
vista_server.debug("DEBUG: QUERY PARA OBTENER : " + query);

        ResultSet resultado = null;

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
        
vista_server.debug("Hemos conseguido ejecutar la query");
        
        int contador_mensajes = 0;
        
        while(resultado.next()){
            
vista_server.debug("Entramos en el bucle de los cojones");
            contador_mensajes++;
            String texto = resultado.getString("text");
            vista_server.debug("DEBUG: valor de lo que nos traemos: " + texto);
        }
        stado.close();
        resultado.close();
       
        return contador_mensajes;
    }
    
    public int bringMessageFromUsersInADate(String s) throws SQLException{
        
        String[] ss = s.split("#");
        
        Statement stado = this.creacion_solo_statement();
        
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        
        int contador_mensajes = 0;
        
        ResultSet resultado = null;
                
vista_server.debug("VALOR DE LAFECHA CUANDO VAMOS A HACER LA QUERY: " + ss[6]);

        vista_server.debug("Dia: " + this.p.getDayOfTheMessageDate(ss[6]) + "\nMes: " + this.p.getMonthOfTheMessageDate(ss[6]) + "\nAño: " + this.p.getYearOfTheMessageDate(ss[6]));

        String year = this.p.getYearOfTheMessageDate(ss[6]);
        String month = this.p.getMonthOfTheMessageDate(ss[6]);
        String day = this.p.getDayOfTheMessageDate(ss[6]);
        
        String query = "SELECT * FROM message WHERE ((id_user_orig='" + ss[4] + "' AND id_user_dest='" + ss[5] + "') OR (id_user_orig='" + ss[5] + "' AND id_user_dest='" + ss[4] + "')) AND (YEAR(datetime)='" + year +  "' AND MONTH(datetime)='" + month +  "' AND DAY(datetime)='" + day + "');";

vista_server.debug("DEBUG: query que vamos a ejecutar en la base de datos sobre los mensajes en una fecha: " + query);

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
//vista_server.debug("Hemos conseguido ejecutar la query");

        while(resultado.next()){

            String cosa = resultado.getString("text");
            Timestamp tiempo = resultado.getTimestamp("datetime");

vista_server.debug("Hemo conseguido el mensaje : " + cosa + "\nEn la fecha: " + tiempo);

            if(cosa != null){
                
                contador_mensajes++;
            }

//vista_server.debug("DEBUG: Mesaje que nos traemos que envía alexinio en la fecha determinada: " + texto);
        }
        
        if(contador_mensajes > 0){
            this.setMessageAsRead(ss[4], ss[5], year, month, day);
        }
       
        stado.close();
        resultado.close();
        
        return contador_mensajes;
    }
    
    public void setMessageAsRead(String id_orig, String id_dest, String year, String month, String day) throws SQLException{
        
        Statement stado = this.creacion_solo_statement();
        
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        
        String query = "UPDATE messange SET read='1' WHERE ((id_user_orig='" + id_orig + "' AND id_user_dest='" + id_dest + "') OR (id_user_orig='" + id_dest + "' AND id_user_dest='" + id_orig + "')) AND (YEAR(datetime)='" + year +  "' AND MONTH(datetime)='" + month +  "' AND DAY(datetime)='" + day + "');";

vista_server.debug("DEBUG: query que vamos a ejecutar en la base de datos sobre los mensajes en una fecha: " + query);

        try{
            stado.executeQuery(db);
            stado.executeUpdate(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
       
        stado.close();
    }
    
    public String bringAllUserData(String id) throws SQLException{
        
        Statement stado = this.creacion_solo_statement();
        
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        String query = "SELECT * FROM user WHERE id_user='" + id + "';";
        
//vista_server.debug("DEBUG: query que vamos a ejecutar en la base de datos: " + query);

        ResultSet resultado = null;

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
        
        String datos = "";
        
        while(resultado.next()){
            
            
            String texto = resultado.getString("name");
            String texto1 = resultado.getString("surname1");
            String texto2 = resultado.getString("surname2");
            
            datos += id + "#" + texto + "#" + texto1 + "#" + texto2;
            vista_server.debug("EN DIRECTO DESDE LA BASE DE DATOS, TENEMOS UNA EXCLUSIVA DE L AQUERY QUE HEMOS OBTENIDO: " + datos);
            
        }
        
        stado.close();
        
        resultado.close();
        
        vista_server.debug("Salimos del método que nos trae toda la info del usuario");
        
        return datos;
    }
       
    
    public String bringMessage(int index, String id_user_orig, String id_user_dest) throws SQLException{
        
        
        Statement stado = this.creacion_solo_statement();
       
        ResultSet resultado = null;
        
        
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        String query = "SELECT * FROM message WHERE id_user_orig='" + id_user_orig + "' AND id_user_dest='" + id_user_dest + "' OR id_user_orig='" + id_user_dest + "' AND id_user_dest='" + id_user_orig + "';";
        
//vista_server.debug("DEBUG: query que vamos a ejecutar en la base de datos: " + query);

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
        
//vista_server.debug("Hemos conseguido ejecutar la query");
        
        int contador_mensajes = 0;
        String untreated_query = "";
        
        
        while(resultado.next()){
            
//vista_server.debug("Entramos en el bucle de los cojones");
            if(contador_mensajes == index){
                
                String texto = resultado.getString("text");
                String orig = resultado.getString("id_user_orig");
                String dest = resultado.getString("id_user_dest");
                Timestamp fechia = resultado.getTimestamp("datetime");
//vista_server.debug("DEBUG: entramos en el if que dice el inidice es igual que el contraod de mensjaes");

                
                untreated_query += orig + "#" + dest + "#" + fechia + "#" + texto;
            }
            
            contador_mensajes++;
        }
        stado.close();
        resultado.close();
        
        return untreated_query;
    }
    
    public String bringUserState(String id) throws SQLException{
        
        Statement stado = this.creacion_solo_statement();
       
        ResultSet resultado = null;
        //String db = "USE cristomessenger;";
        String db = "USE local_cm;";
        String query = "SELECT * FROM user WHERE id_user='" + id + "';";
        
//vista_server.debug("DEBUG: query que vamos a ejecutar en la base de datos: " + query);

        try{
            resultado = stado.executeQuery(db);
            resultado = stado.executeQuery(query);
        }catch(Exception e){
            vista_server.debug(e.toString());
        }
        
        String untreated_query = "";
        
        
        while(resultado.next()){
            
//vista_server.debug("Entramos en el bucle de los cojones");
            int estado = resultado.getInt("state");
            vista_server.debug("Valor del estado que nos traemos: " + estado);
            if(estado == 0){
                untreated_query = "NOT_CONNECTED";
            }else{
                untreated_query = "CONNECTED";
            }
        }
        stado.close();
        resultado.close();
        
        return untreated_query;
    }
}
