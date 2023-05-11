/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;
import java.io.*;
import java.net.Socket;

/**
 *
 * @author vovab
 */
public class SocketMessage {
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    
    public SocketMessage(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            dataInputStream =  new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getMessage() {
        try {
            return in.readLine();
        } catch (IOException ignored) {}
        return null;
    }
    public String getData() throws IOException {
        return dataInputStream.readUTF();
    }

    public void writeData(String message) throws IOException {
        dataOutputStream.writeUTF(message);
    }

    public void sendMessage(String str){
        out.println(str);
    }

    public void close() throws IOException {
        in.close();
        out.close();
    }
}
