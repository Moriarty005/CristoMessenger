package controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import controlador.ClientProtocol;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import vista.MainUI;
import vista.loginCliente;

/**
 *
 * @author lolac
 */
public class Client{
    
    private Lock mutex;
    
    String hostName;
    int portNumber;
    
    Socket socket;
    
    loginCliente login;
    MainUI principal = null;
    
    UserController user;
    
    PrintWriter out;
    BufferedReader in;
    
    public ClientProtocol CP;
    
    public Client(String host_name, int port_number, loginCliente log, String new_nom, String new_pass) throws IOException{
        
        this.mutex = new ReentrantLock();
        
        this.hostName = "";
        this.portNumber = 0;
        
        this.login = log;
        
        CP = new ClientProtocol(this.user);
        
        this.user = new UserController(new_nom, new_pass);
        
        try{
            this.socket = new Socket(host_name, port_number);
           
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
    }

    
    public void logIn() throws IOException{
        
        //this.mutex.lock();
        
        try {
            
            String fromServer = null;
            if(this.user.getLogin() != null && this.user.getPasswd() != null){
                
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                
                
                String fromUser = "PROTOCOLCRISTOMESSENGER1.0#" + ts + "#CLIENT#LOGIN#" + this.user.getLogin() + "#" + this.user.getPasswd();
                
//System.out.println("DEBUG: Lo que vamos a enviar: " + fromUser);
                
                out.println(fromUser);
            }
            
//System.out.println("Lo que envia el server antes de entrar en ningun bucle: " + (fromServer = in.readLine()));
            if((fromServer = in.readLine()) != null){
                ClientProtocol CP = new ClientProtocol(this.user);
                System.out.println("Lo que envía el server: " + fromServer);

                if(CP.checkIfIsFromProctocol(fromServer)){
                    if(CP.checkAnctionToDoClient(fromServer).equals("LOGIN_CORRECT")){

//System.out.println("DEBUG: Entramos en el añadir amigis");
                        CP.addFriendsToUser(fromServer);

                        principal = new MainUI();
                        principal.setUser(this.user, this);
                        principal.setVisible(true);

                        this.login.setVisible(false);

                    }else if (CP.checkAnctionToDoClient(fromServer).equals("BAD_LOGIN")){
                        System.out.println("El usuario ya estaba conectado");
                    }
                }else{
                    System.out.println("ERROR EL MENSAJE ENVIADO NO ES DEL NUESTRO PROTOCOLO");
                }

            }   
            
            //this.mutex.unlock();
//System.out.println("Salimos del bucle"); 
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Excepcion en el login");
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
    
    public void sendMessagesRequest(String id_client_orig, String id_client_dest, Timestamp ts){
        
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        String timestamp = sdf.format(new Timestamp(System.currentTimeMillis()));
        
        String tiempo_query = ts.toString();
        
        String fromUser = "PROTOCOLCRISTOMESSENGER1.0#" + timestamp + "#CLIENT#MSGS#" + id_client_orig + "#" + id_client_dest + "#" + tiempo_query;
        
        MainUI.debugClient("Lo que va a evniar el cliente para obetener los emsajes: " + fromUser);
        //System.out.println("Lo uqe va a evniar el cliente para obetener los emsajes: " + fromUser);
        
        String fromServer = "";
        
        //int dias = 0;
        
        try  {
System.out.println("Vamos a enviar el mensaje al servidor");  

            this.mutex.lock();

            System.out.println("Vamos a enviar la query por primera vez ne los mensajes");
            out.println(fromUser);
            System.out.println("Ya lo hemos enciado");
            
System.out.println("Enviamos la query al servidor");

            int num = 0;
            int total_messages = 0;
            
            do{
                fromServer = in.readLine();
                System.out.println("Vamos a leer el número de mensajes");

                num = CP.countNumberMessages(fromServer);
                total_messages = CP.countTotalMessages(fromServer);
    System.out.println("LO HISIMOS");
                if(total_messages != 0){
                    if(num != 0){

        System.out.println("DEBUG: entramos en el contrador de mensajes");

                        fromUser = "PROTOCOLCRISTOMESSENGER1.0#" + timestamp + "#CLIENT#MSGS#OK_SEND!";
                        out.println(fromUser);

                        for(int index2 = 0; index2 < num; index2++){

                            fromServer = in.readLine();
        System.out.println("CHACHO: lo que nos trae el server bro: " + fromServer);
                            CP.proccessMEssageInfo(fromServer, this.user);
                        }

                        fromUser = "PROTOCOLCRISTOMESSENGER1.0#" + timestamp + "#CLIENT#MSGS#ALL_RECEIVED";
                        out.println(fromUser);
        //System.out.println("DEBUG: salimos del bucle que recibe los mensajes");
                        this.principal.setUser(this.user);
                    }else{

                        tiempo_query = sdf.format(Timestamp.valueOf(tiempo_query).getTime() - (24*60*60*1000));
                        fromUser = "PROTOCOLCRISTOMESSENGER1.0#" + timestamp + "#CLIENT#MSGS#" + id_client_orig + "#" + id_client_dest + "#" + tiempo_query;
                        out.println(fromUser);
                        System.out.println("Ya hemos restado un día: " + tiempo_query);
                        System.out.println("Lo que le enviamos al server: " + fromUser);
                        //dias++;
                    }    
                }else{
                    num = 1;
                    //System.out.println("Jemos entrao sahabalada");
                    //this.principal.thereAreNoMessages();
                }
                    
            }while(num == 0);
            
            this.mutex.unlock();
            
//System.out.println("Ya hemos terminado de traer los mensajes del colegui");
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Excepcion en el obtener mensajes");
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
    
    public String[] getAllInfoOfUser(String id){
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        String[] ss = null;
        
        try {
            
            //this.mutex.lock();
            
            out.println("PROTOCOLCRISTOMESSENGER1.0#" + timestamp + "#CLIENT#ALLDATA_USER#" + id);
            String fromServer = in.readLine();
            
            System.out.println("Valor de lo que envía el server " + fromServer);
            
            if(fromServer != null){
                ss = fromServer.split("#");
            }
            
            //this.mutex.unlock();
           // System.out.println("Hemos insertado todos los datos en el cliente");
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Excepcion en el obtener info del user");
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
System.out.println("Terminamos lo de traer la información");
        return ss;
    }

    public String askForStatusOfUser(String id_actual_user, String id_friend){
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        String fromUser = "PROTOCOLCRISTOMESSENGER1.0#" + timestamp + "#CLIENT#STATUS#" + id_actual_user + "#" + id_friend;
        
        //MainUI.debugClient("Lo que va a evniar el cliente para obetener los emsajes: " + fromUser);
        System.out.println("Lo que va a enviar el cliente para obtener el estado: " + fromUser);
        
        String fromServer = "";
        
        try {
            
            //this.mutex.lock();
            
            out.println(fromUser);
            
            fromServer = in.readLine();
            System.out.println("Enviamos la query para obtener si el amigo está conectado o no");
            
            String status = CP.getUserStatus(fromServer);
            
            //this.mutex.unlock();
            
            return status;
            
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Excepcion en el obtener estatus");
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
        
        return null;
    }
    
}

