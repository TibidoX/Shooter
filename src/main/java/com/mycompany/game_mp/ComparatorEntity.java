/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;
import java.util.Comparator;

/**
 *
 * @author vovab
 */
class ComparatorEntity implements Comparator<PlayerEntity> {
    @Override
    public int compare(PlayerEntity p1, PlayerEntity p2) {
        return (p1.getWins() > p2.getWins()) ? -1 : ((p1.getWins() == p2.getWins()) ? 0 : 1);
    }
}
