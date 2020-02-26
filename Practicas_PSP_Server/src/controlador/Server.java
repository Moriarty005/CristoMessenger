 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import Vista.vista_server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author lolac
 */
public class Server extends Thread{
    
    String black="\033[30m"; 
    String red="\033[31m"; 
    String green="\033[32m"; 
    String yellow="\033[33m"; 
    String blue="\033[34m"; 
    String purple="\033[35m"; 
    String cyan="\033[36m"; 
    String white="\033[37m";
    String reset="\u001B[0m";
    
    ArrayList<ServerThread> server_thread_servers;
    
    int portNumber;
    boolean listening;
    
    public Server(){
        
//vista_server.debug("entramos en el constructor del server");
        
        this.listening = true;
        
        server_thread_servers = new ArrayList<>();
    }
    
    public void setPort(int port_number){
        
        this.portNumber = port_number;
    }
    
    @Override
    public void run() {

vista_server.debug("Empezamos el servicio");
        
        this.listening = true;
        
        //vista_server.debug("yey bro whats up yeah");
        
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) { 
            
            //vista_server.debug("yey bro whats up yeah");
            
            while (listening) {
                //vista_server.debug("yey bro whatsup");
                this.server_thread_servers.add(new ServerThread(serverSocket.accept()));
                this.server_thread_servers.get(this.server_thread_servers.size() - 1).start();
            }
            
System.out.println("Paramos el servicio");
            
        } catch (IOException e) {
            
vista_server.debug("Could not listen on port " + portNumber);
            System.err.println(red + "Could not listen on port " + portNumber + reset);
            System.exit(-1);
        }
    }
    
    public void stopServer(){
        this.listening = false;
    }
    
}
