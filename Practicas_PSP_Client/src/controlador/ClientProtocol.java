package controlador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author lolac
 */
public class ClientProtocol {
    
    public UserController user;
    
    ClientProtocol(UserController user){
        
        this.user = user;
    }
    
    public boolean checkIfIsFromProctocol(String s){
        
        boolean yon = false;
        String[] ss = s.split("#");
//System.out.println("La primera casilla de lo que envía el server: " + ss[0]);
        if(ss[0].equals("PROTOCOLCRISTOMESSENGER1.0")){
            yon = true;
            return yon;
        }
        
        return yon;
    }
    
    
    
    public String checkAnctionToDoClient(String s){
        
        if(this.checkIfIsFromProctocol(s)){
            String[] cosas = s.split("#");
            
            if(cosas[3].equals("LOGIN_CORRECT")){
                return cosas[3];
            }else if(cosas[3].equals("BAD_LOGIN")){
                return cosas[3];
            }else if(cosas[3].equals("MSGS")){
                return cosas[3];
            }
        }
        
        return null;
    }
    
    public void addFriendsToUser(String s){
        
        if(this.checkIfIsFromProctocol(s)){
            System.out.println("DEBUG: entramos en el añadir amigis");

    //System.out.println("DEBUG: amigis: " + friends);

            String[] empty_friends = s.split("#");

    //System.out.println("DEUBG: valor de la primera celda de amigis: " + empty_friends[0]);
    //System.out.println("DEUBG: valor de la segunda celda de amigis: " + empty_friends[1]);


            FriendController amigo = null;

            System.out.println("Primer amigi: " + empty_friends[7]);

            for(int index = 7; index < empty_friends.length; index++){
    //System.out.println("DEBUG: entramos en el bucle de los amigis");
                if(index % 2 == 1){

                    amigo = new FriendController();
                    amigo.setFriend_login(empty_friends[index]);
    //System.out.println("DEBUG: nombre del amigo que acabamos de crear: " + amigo.getFriend_login());

                }else if(index % 2 == 0){

                    amigo.setFriend_state(empty_friends[index]);
    //System.out.println("DEBUG: estado del amigo que acabamos de crear: " + amigo.getFriend_state());
                    this.user.addUserToFriendList(amigo);
                }
            }

            for(int index = 0; index < this.user.friend_list.size(); index++){

                System.out.println("DEBUG: amigo que hemos añadido al usuario");
                System.out.println("Nombre: " + this.user.friend_list.get(index).getFriend_login());
                System.out.println("Estado: " + this.user.friend_list.get(index).getFriend_state());
            }
        }
        
        
    }
    
    public int countNumberMessages(String s){
        
        System.out.println("DEBUG: valor de lo que envía el server en el metodo de contar los usuarios: " + s);
        
        if(this.checkIfIsFromProctocol(s)){
            
            String[] ss = s.split("#");

            return Integer.valueOf(ss[7]);
        }
        return 0;
    }
    
    public int countTotalMessages(String s){
        
        if(this.checkIfIsFromProctocol(s)){
            
            String[] ss = s.split("#");

            return Integer.valueOf(ss[6]);
        }
        return 0;
    }
    
    public void proccessMEssageInfo(String s, UserController user){
        String[] ss = s.split("#");
        //System.out.println("Valor del string al entrar al método: " + s);
        //System.out.println("Numero de cosas que nos trae el string: " + ss.length);
        
        MessageController cosa = new MessageController();
        
        if(user.getMessageListSize() > 0){
            for(int index = 0; index < user.getMessageListSize(); index++){

                if(user.getMessage_list().get(index).getFecha() != ss[6]){
                    cosa.setId_user_orig(ss[4]);
                    cosa.setId_user_dest(ss[5]);
                    cosa.setFecha(ss[6]);
                    cosa.setText(ss[7]);
                    user.addMessageToMessageList(cosa);
                }else{
                    System.out.println("Este mensaje esta repetido");
                }
            }
        }else{
            cosa.setId_user_orig(ss[4]);
            cosa.setId_user_dest(ss[5]);
            cosa.setFecha(ss[6]);
            cosa.setText(ss[7]);
            user.addMessageToMessageList(cosa);
        }        
    }
    
    public void manageInfoOfUser(String[] s, UserController u){
        
        for (String ss : s){
            System.out.println(ss + "#");
        }
        
        if(s!= null){
            
            if(s[3] != "BAD_DATA"){
                
                u.setName(s[5]);
                u.setSurname_1(s[6]);
                u.setSurname_2(s[7]);
            }
        }
    }
    
    public String getUserStatus(String s){
        
        String[] ss = s.split("#");
        
        return ss[5];
    }
}
