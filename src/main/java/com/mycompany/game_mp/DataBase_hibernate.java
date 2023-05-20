/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.game_mp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.*;
/**
 *
 * @author vovab
 */
public class DataBase_hibernate implements DB {
    SessionFactory sf = HibernateSF.getSessionFactory();
    public DataBase_hibernate(){}
    
    @Override
    public void addPlayer(PlayerEntity p) {
        Session s = sf.openSession();
        Transaction tx1 = s.beginTransaction();
        s.saveOrUpdate(p);
        tx1.commit();
        s.close();
    }
    
    @Override
    public boolean findPlayer(String name) {
        Session s = sf.openSession();
        Transaction tx1 = s.beginTransaction();
        PlayerEntity pe = s.get(PlayerEntity.class, name);
        if (pe != null) return true;
        else return false;
    }
    
    @Override
    public PlayerEntity getPlayerWins(String name) {
        Session s = sf.openSession();
        Transaction tx1 = s.beginTransaction();
        PlayerEntity e = s.get(PlayerEntity.class, name);
        tx1.commit();
        s.close();
        return e;
    }
    
    @Override
    public void setPlayerWins(PlayerEntity e) {
        Session s = sf.openSession();
        Transaction tx1 = s.beginTransaction();
        s.update(e);
        tx1.commit();
        s.close();
    }
    
    @Override
    public ArrayList<PlayerEntity> getAllPlayers() {
        ArrayList<PlayerEntity> res = new ArrayList<>();
        List<PlayerEntity> list = (List<PlayerEntity>)sf.openSession().
                createQuery("From PlayerEntity").list();
        res.addAll(list);
        return res;
    }

}
