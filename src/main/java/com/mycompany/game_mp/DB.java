/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;

import java.util.*;
/**
 *
 * @author vovab
 */
public interface DB {
    void addPlayer(PlayerEntity entity);
    public boolean findPlayer(String name);
    PlayerEntity getPlayerWins(String name);
    void setPlayerWins(PlayerEntity entity);
    //void incrementPlayerWins(PlayerEntity entity);
    ArrayList<PlayerEntity> getAllPlayers();
}
