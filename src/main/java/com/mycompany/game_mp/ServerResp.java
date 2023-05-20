/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;
import java.util.ArrayList;
/**
 *
 * @author vovab
 */
public class ServerResp {
    public ArrayList<ClientInfo> clientArrayList;
    public ArrayList<MyPoint> arrowArrayList;
    public ArrayList<PlayerEntity> leadersArrayList;
    MyPoint big, small;
    //public ArrayList<MyPoint> targetArrayList;
    public String winner;
}
