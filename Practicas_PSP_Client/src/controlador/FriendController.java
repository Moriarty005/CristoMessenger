/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

/**
 *
 * @author lolac
 */
public class FriendController {
    
    public String friend_login;
    public String friend_state;
    public String friend_name;
    public String friend_surname_1;
    public String friend_surname_2;

    public FriendController(String friend_login, String friend_state) {
        this.friend_login = friend_login;
        this.friend_state = friend_state;
    }

    public FriendController(String friend_login, String friend_state, String friend_name, String friend_surname_1, String friend_surname_2) {
        this.friend_login = friend_login;
        this.friend_state = friend_state;
        this.friend_name = friend_name;
        this.friend_surname_1 = friend_surname_1;
        this.friend_surname_2 = friend_surname_2;
    }

    public FriendController() {
        
        this.friend_login = "";
        this.friend_state = "";
    }

    public String getFriend_login() {
        return friend_login;
    }

    public String getFriend_state() {
        return friend_state;
    }

    public String getFriend_name() {
        return friend_name;
    }

    public String getFriend_surname_1() {
        return friend_surname_1;
    }

    public String getFriend_surname_2() {
        return friend_surname_2;
    }

    public void setFriend_login(String friend_login) {
        this.friend_login = friend_login;
    }

    public void setFriend_state(String friend_state) {
        this.friend_state = friend_state;
    }

    public void setFriend_name(String friend_name) {
        this.friend_name = friend_name;
    }

    public void setFriend_surname_1(String friend_surname_1) {
        this.friend_surname_1 = friend_surname_1;
    }

    public void setFriend_surname_2(String friend_surname_2) {
        this.friend_surname_2 = friend_surname_2;
    }
    
    public void setAllInfoFromStrings(String[] ss){
        
        this.setFriend_name(ss[4]);
        this.setFriend_surname_1(ss[5]);
        this.setFriend_surname_2(ss[6]);
    }
    
}
