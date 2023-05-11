/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;
import java.io.*;
import java.net.Socket;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vovab
 */
public class Client implements Runnable{
    SocketMessage sm;
    MainServer ms;
//    InputStream is;
//    OutputStream os;
//    DataInputStream dis;
//    DataOutputStream dos;
    Gson gson = new Gson();
    Model m = Build_Model.build();
    ClientInfo info;
    
    
    public Client(SocketMessage sm, MainServer ms, String name)  {
        this.sm = sm;
        this.ms = ms;
        info = new ClientInfo(name);
    }

    public String getPlayerName() {
        return info.getPlayerName();
    }
    
    public void sendInfoToClient() {
        try {
            ServerResp serverResp = new ServerResp();
            serverResp.clientArrayList = m.getClientArrayList();
            serverResp.arrowArrayList = m.getArrowArrayList();
            //serverResp.circleArrayList = model.getTargetArrayList();
            serverResp.big = m.big;
            serverResp.small = m.small;
            serverResp.winner = m.getWinner();

            sm.writeData(gson.toJson(serverResp));
        } catch (IOException ex) {
        }
    }
//    
//    public void sendInfoToClient() {
//        try {
//            ServerResp serverResp = new ServerResp();
//            serverResp.clientArrayList = model.getClientArrayList();
//            serverResp.targetArrayList = model.getArrowsArrayList();
//            serverResp.circleArrayList = model.getTargetArrayList();
//            serverResp.theWinnerIs = model.getWinner();
//
//            socketMesWrapper.writeData(gson.toJson(serverResp));
//        } catch (IOException ex) {
//        }
//    }
    
    @Override
    public void run() {
        try {

            System.out.println("Cilent thread " + info.getPlayerName() + " started");

            m.addClient(info);
            ms.bcast();

            while(true)
            {
                String s = sm.getData();
                System.out.println("Msg: " + s);


                ClientReq msg = gson.fromJson(s, ClientReq.class);

                if(msg.getClientActions() == ClientActions.READY)
                {
                    System.out.println("READY " + getPlayerName());
                    m.ready(ms, this.getPlayerName());
                }

                if(msg.getClientActions() == ClientActions.STOP)
                {
                    m.requestPause(getPlayerName());
                }
                if (msg.getClientActions() == ClientActions.SHOOT) {
                    m.requestShoot(getPlayerName());
                }


            }
        } catch (IOException ignored) {

        }
    }
//    public ClientInfo getClientData() {
//        return clientInfo;
//    }

}
