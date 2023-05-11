/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;
import java.util.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;

/**
 *
 * @author vovab
 */
public class MainServer {
    int port = 3124;
    InetAddress ip = null;
    ExecutorService service = Executors.newFixedThreadPool(4);
    ArrayList<Client> clientArrayList = new ArrayList<>();
    
    Model m = Build_Model.build();
    
//    void bcastAllPoints(){
//        for (Client client : clientArrayList) {
//            client.sendAllPointToClient();
//        }
//    }
    
    public void bcast(){
        clientArrayList.forEach(Client::sendInfoToClient);
    }
    
    public void ServerStart(){
        ServerSocket ss;
        try {  
            ip = InetAddress.getLocalHost();
            ss = new ServerSocket(port, 2, ip); //?2?
            System.out.append("Server start\n");
            m.addTarget(new MyPoint(275, 159, 20), new MyPoint(355, 159, 10));
            
            while(true)
            {
                Socket cs;
                cs = ss.accept();
                SocketMessage sm = new SocketMessage(cs);
                String name = sm.getMessage();
                
                if (tryAddClient(sm, name)) {
                    System.out.println(name + " Connected");
                } else {
                    cs.close();
                }
            }
            
        } catch (IOException ex) {}
    }
    
    private boolean tryAddClient(SocketMessage sm, String name) {
         if (clientArrayList.size() >= 4) {
             sm.sendMessage("Превышено число подключений");
             return false;
         }
         if (clientArrayList.isEmpty() ||
                 clientArrayList.stream()
                .filter(client -> client.getPlayerName().equals(name))
                .findFirst()
                .orElse(null) == null) {
             sm.sendMessage("ACCEPT");
             Client c = new Client(sm, this, name);
             clientArrayList.add(c);
             service.submit(c);
             System.out.println("RESPONSE ACCEPT");
             return true;
         }
        sm.sendMessage("Игрок с таким именем уже имеется");
        System.out.println("RESPONSE DECLINE");
        return false;
    }
    
    public static void main(String[] args) {
        new MainServer().ServerStart();
    }
}
