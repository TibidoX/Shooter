/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;
import javax.persistence.*;

/**
 *
 * @author vovab
 */

@Entity
@Table(name = "players")
public class PlayerEntity {
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "name", nullable = false)
    private String name;
    @Basic
    @Column(name = "wins", nullable = false)
    private int wins;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
