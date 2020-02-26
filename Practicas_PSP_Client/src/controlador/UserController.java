/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.util.ArrayList;

/**
 *
 * @author lolac
 */
public class UserController {
    
    private String login;
    private String passwd;
    private String name;
    private String surname_1;
    private String surname_2;
    private int state;
    private String photo_location;
    
    ArrayList<FriendController> friend_list;
    ArrayList<MessageController> message_list;

    public UserController(String login, String passwd, String name, String surname_1, String surname_2, int state) {
        this.login = login;
        this.passwd = passwd;
        this.name = name;
        this.surname_1 = surname_1;
        this.surname_2 = surname_2;
        this.state = state;
        
        this.friend_list = new ArrayList<>();
        this.message_list = new ArrayList<>();
    }

    public UserController(String login, String passwd) {
        this.login = login;
        this.passwd = passwd;
        
        this.friend_list = new ArrayList<>();
        this.message_list = new ArrayList<>();
    }

    public UserController() {
        this.login = "";
        this.passwd = "";
        this.name = "";
        this.state = 0;
        this.photo_location = "";
        
        this.friend_list = new ArrayList<>();
        this.message_list = new ArrayList<>();
    }

    public String getSurname_1() {
        return surname_1;
    }

    public String getSurname_2() {
        return surname_2;
    }

    public String getLogin() {
        return login;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getName() {
        return name;
    }

    public int getState() {
        return state;
    }

    public String getPhoto_location() {
        return photo_location;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setPhoto_location(String photo_location) {
        this.photo_location = photo_location;
    }

    public void setSurname_1(String surname_1) {
        this.surname_1 = surname_1;
    }

    public void setSurname_2(String surname_2) {
        this.surname_2 = surname_2;
    }

    public ArrayList<FriendController> getFriend_list() {
        return friend_list;
    }
    
    public ArrayList<MessageController> getMessage_list() {
        return this.message_list;
    }
    
    public void addMessageToMessageList(MessageController mensaje){
        
        this.message_list.add(mensaje);
    }
    
    public void addUserToFriendList(FriendController amigo){
        
        this.friend_list.add(amigo);
    }
    
    public int getFriendListSIze(){
        return this.friend_list.size();
    }
    
    public int getMessageListSize(){
        return this.message_list.size();
    }
    
    public String allMessagesToString(String id_user_dest, String id_user_orig){
        
        String cosa = null;
        
        for(int index = 0; index < this.getMessage_list().size(); index++){
            
            if((this.message_list.get(index).getId_user_dest().equals(id_user_dest) && this.message_list.get(index).getId_user_orig().equals(id_user_orig)) || (this.message_list.get(index).getId_user_dest().equals(id_user_orig) && this.message_list.get(index).getId_user_orig().equals(id_user_dest))){
                cosa += this.message_list.get(index).toString();
            }
            
        }
        
        return cosa;
    }
   
}
