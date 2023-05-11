/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;

/**
 *
 * @author vovab
 */
public class ClientReq {
    ClientActions clientActions;
    public ClientReq(ClientActions clientActions) {
        this.clientActions = clientActions;
    }
    public ClientActions getClientActions() {
        return clientActions;
    }
}
