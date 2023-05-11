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
public class ClientInfo {
    private String playerName;
    private int arrowsShoot = 0;
    private int points = 0;
    
    public ClientInfo(String playerName) {
        this.playerName = playerName;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void increasePoints(int a) {
        this.points += a;
    }
    
    public void increaseArrowsShoot(int a) {
        this.arrowsShoot += a;
    }
    
    public int getPoints() {
        return points;
    }
    
    public int getArrowsShoot() {
        return arrowsShoot;
    }
    
    public void reset() {
        arrowsShoot = 0;
        points = 0;
    }
}
