/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import Vista.vista_server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.BD_CristoMessenger;

/**
 *
 * @author lolac
 */
public class ServerThread extends Thread{
    
    private Socket socket = null;
    
    BD_CristoMessenger bd;
    
    int num_mensajes;
    
    String id_mes_dest;
    String id_mes_orig;
    
    Protocol kkp;
    
    public ServerThread(Socket socket){
        
        super("ServerThread");
        
        System.out.println("Entramos en el constructor de la hebra");
        
        this.socket = socket;
        
        num_mensajes = 0;
        
        this.kkp = new Protocol();
        
        bd = new BD_CristoMessenger(this.kkp);
    }
    
    @Override
    public void run() {

        try (
                
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            
//vista_server.debug("Entramos en el run de la hebra");
            
            String inputLine, outputLine;
//vista_server.debug("Vamos a entrar en el bucle");
            
//vista_server.debug("Valor de lo que nos pasan: " + in.readLine());
            
            while ((inputLine = in.readLine()) != null) {
         
                
//vista_server.debug("Login del user: " + user_login);
//vista_server.debug("Password del usuario: " + passwd);
                vista_server.debug("DEBUG_GORDO: LOQ UE ENVÍA EL CLIENTE CONSTANTEMENTE: " + inputLine);

                if(kkp.checkIfStartIsCorrect(inputLine)){
vista_server.debug("Entramo en el if general");
                    if(kkp.checkActionToDo(inputLine).equals("LOGIN")){
//vista_server.debug("Vamos a logear");
                        outputLine = kkp.checkLoginClue(inputLine);

                        String passwd = kkp.getPasswordFromRaw(outputLine);
                        String user_login = kkp.getUserLoginFromRaw(outputLine);
                        
                        if(bd.checkUserLoginAndPasswd(user_login, passwd)){
                            
//vista_server.debug("El usuario y la constraseña concuerdan");

                            String final_login_query = bd.returnFriendsOfUser(user_login);
                            out.println(final_login_query);
                        }else{
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            out.println("PROTOCOLCRISTOMESSENGER1.0#" + timestamp + "#SERVER#ERROR#BAD_LOGIN");
                            vista_server.debug("¡CASI CRACK!");
                        }
                        
                    }else if(kkp.checkActionToDo(inputLine).equals("MSGS")){
//vista_server.debug("DEBUG: CUANDO ENTRAMOS LA SEGUNDA VEZ LA QEURY VALE: " + inputLine);
vista_server.debug("DEBUG: entramos en el if de los mensajes YEYEY");
                        
                        if(kkp.checkMessageActionToDo(inputLine).equals("SEND")){
                            
//vista_server.debug("DEBUG: valor de las vence sque vamos a entrar en el bucle: " + num_mensajes);
                            
                            if(this.num_mensajes != 0){
                                vista_server.debug("Entramos en el enviar todos los mensajes");
                                vista_server.debug("Entramos en el if que comprueba que se han enviado todos los mensajes");
                                for(int index = 0; index < this.num_mensajes; index++){
//vista_server.debug("Entramos en el bucle");
                                    String lo_que_madar = bd.bringMessage(index, id_mes_orig, id_mes_dest);
                                    lo_que_madar = kkp.createSendMessageQuery(lo_que_madar);
vista_server.debug("DEBUG: valor del stirng que nos traemos de la base de datos con el mensaje: " + lo_que_madar);
                                    out.println(lo_que_madar);  
vista_server.debug("Ya hemos enviado el mensaje");
                                }
                            }
                            
                        }else if(kkp.checkMessageActionToDo(inputLine).equals("RECEIVED")){
                            
                            vista_server.debug("Ya hemos enviado todos los mensajes");
                            
                        }else{
                            this.id_mes_dest = kkp.getMessageUserDest(inputLine);
                            this.id_mes_orig = kkp.getMessageUserOrig(inputLine);
                            this.num_mensajes = bd.bringMessageFromUsersInADate(inputLine);
                            String lo_que_mandar = kkp.createQueryNumberOfMessages(num_mensajes, bd.bringTotalMessages(inputLine), inputLine);
vista_server.debug("DEBUG ANTES DE MANDAR: Valor de lo que va a enviar el server: " + lo_que_mandar);
                            out.println(lo_que_mandar);
                            //vista_server.debug("Vamos a enviar lso mensaguico ricos ahí to wenos de la mama");
                        }
                            
                        
                        //vista_server.debug("Entramos en el if que comprueba que vamos a tratar la query de mensajes");
                    }else if(kkp.checkActionToDo(inputLine).equals("ALLDATA_USER")){
                        //vista_server.debug("Entramos en el checkeaar que todo guay bro");
                        String algo_para_mandar = kkp.sendInfoOfUser(bd.bringAllUserData(kkp.treatQueryAllUserInfo(inputLine)));
                        //vista_server.debug("DEBUG: imprimimos lo que vmaos a mandar al cliente cuando cojemos toda su info: " + algo_para_mandar);
                        out.println(algo_para_mandar);
vista_server.debug("Terminamos el proceso de traer toda la info del usuario");
                    }else if(kkp.checkActionToDo(inputLine).equals("STATUS")){
                        
                        String friend_id = kkp.getIdOfTheFriendToGetStatus(inputLine);
                        
                        String query = null;
                        
                        String state = this.bd.bringUserState(friend_id);
                        
                        if(friend_id != null){
                            query = kkp.creteQueryOfStatus(friend_id, state);
                        }
                        
                        out.println(query);
                        
                        vista_server.debug("Entramos en el apartado de condeguir el estado de un amigwi");
                    }
                }else{
                    vista_server.debug("El comienzo de la cadena no es correcto");
                }
                
            }
            
            socket.close();
            
        }catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
