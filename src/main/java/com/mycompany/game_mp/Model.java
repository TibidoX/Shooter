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
public class Model {
    private final ArrayList<IObserver> observerArrayList = new ArrayList<>();
    private ArrayList<ClientInfo> clientArrayList = new ArrayList<>();
    MyPoint big = new MyPoint(275, 159, 20); 
    MyPoint small = new MyPoint(355, 159, 10);
    private volatile boolean isGameReset = true;
    private ArrayList<MyPoint> arrowArrayList = new ArrayList<>();
    private ArrayList<javax.swing.JLabel> labelArrayList = new ArrayList<>();
    
    String winner = null;
    private final ArrayList<String> readyList = new ArrayList<>();
    private final ArrayList<String> waitingList = new ArrayList<>();
    private final ArrayList<String> shootingList = new ArrayList<>();
    
    public void update() {
        for (IObserver o: observerArrayList) {
            o.update();
        }
    }
    
    public void moveBig(int mov) { //mov - napravlenie
      //this.big+=5*mov;
      this.big.setY(this.big.getY()+3*mov);
      this.update();
    }
    
    public void moveSmall(int mov) { //mov - napravlenie
      //this.big+=5*mov;
      this.small.setY(this.small.getY()+6*mov);
      this.update();
      System.out.println(this.big.getY());
    }
    
//    public void addTarget(MyPoint p) {
//        targetArrayList.add(p);
//        this.update();
//    }
    
    public void addTarget(MyPoint big, MyPoint small) {
        this.big = big;
        this.small = small;
    }
    
    public void addArrow(MyPoint p) {
        arrowArrayList.add(p);
        this.update();
    }
    
    public void addObserver(IObserver o) {
        observerArrayList.add(o);
    }
    
//    public ArrayList<MyPoint> getTargetArrayList() {
//        return targetArrayList;
//    }
    
    public ArrayList<MyPoint> getArrowArrayList() {
        return arrowArrayList;
    }
    
    public ArrayList<ClientInfo> getClientArrayList() {
        return clientArrayList;
    }
    
    public ArrayList<javax.swing.JLabel> getLabelArrayList() {
        return labelArrayList;
    }
    
    private synchronized void arrowsCountUpdate() {
        arrowArrayList.clear();
        //labelArrayList.clear();
        int clientsCount = clientArrayList.size();
        for (int i = 1; i <= clientsCount; i++) {
            int step = 300 / (clientsCount + 1);
            arrowArrayList.add(new MyPoint(0, step * i, 30));
            
//            javax.swing.JLabel l = new javax.swing.JLabel("Вы");
//            l.setLocation(0, step*i-20);
//            l.setSize(30, 16);
//            labelArrayList.add(l);
        }
    }
    
    public void ready(MainServer mc, String name) {
        if (readyList.isEmpty()) { readyList.add(name); return;}

        if (readyList.contains(name)) {readyList.remove(name); }
        else {readyList.add(name);}

        if (clientArrayList.size() > 1 && readyList.size() == clientArrayList.size()) {
            isGameReset = false;
            gameStart(mc);
        }
    }
    
    private void gameReset() {
        isGameReset = true;
        readyList.clear();
        //targetArrayList.clear();
        arrowArrayList.clear();
        waitingList.clear();
        shootingList.clear();
        clientArrayList.forEach(ClientInfo::reset);
        //this.addTarget(big, small);
        big = new MyPoint(275, 159, 20); 
        small = new MyPoint(355, 159, 10);
        arrowsCountUpdate();
    }
    
    public void gameStart(MainServer mc) {
        Thread thread = new Thread(
                ()->
                {
                    int movB = 1;
                    int movS = 1;
                    while (true) {
                        if (isGameReset) {
                            winner = null;
                            break;
                        }
                        if (waitingList.size() != 0) {
                            synchronized(this) {
                                try {
                                    wait();
                                } catch(InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        if (shootingList.size() != 0) {

                                for (int i = 0; i < shootingList.size(); i++) {
                                    int finalI = i;
                                    if (shootingList.get(finalI) == null) break;
                                    ClientInfo client = clientArrayList.stream()
                                            .filter(clientData -> clientData.getPlayerName().equals(shootingList.get(finalI)))
                                            .findFirst()
                                            .orElse(null);
                                    int index = clientArrayList.indexOf(client);
                                    MyPoint p = arrowArrayList.get(index);
                                    p.setX(p.getX() + 25);
                                    shootManager(p, client);
                                };

                        }
                        //MyPoint big = targetArrayList.get(0);
                        //MyPoint small = targetArrayList.get(1);

                        if (small.getY() <= small.getR() || 300 - small.getY()  <= small.getR()) {
                            movS *= -1;
                        }
                        //small.setY(small.getY() + sml_move);
                        moveSmall(movS);
                        if (big.getY() <= big.getR() || 300 - big.getY()  <= big.getR()) {
                            movB *= -1;
                        }
                        //big.setY(big.getY() + big_move);
                        moveBig(movB);
                        mc.bcast();

                        try {
                            Thread.sleep(25);//25
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
        );
        thread.start();

    }
    
    private synchronized void shootManager(MyPoint p, ClientInfo player) {
        ShootState shootState = check(p);
        System.out.println(shootState);
        if (shootState.equals(ShootState.FLYING)) return;
        if (shootState.equals(ShootState.BIG_SHOT)) player.increasePoints(1);
        if (shootState.equals(ShootState.SMALL_SHOT)) player.increasePoints(2);
        p.setX(0);
        if (shootingList.size() == 1) shootingList.clear();
        else {
            shootingList.remove(player.getPlayerName());
        }
        checkWinner();
    }
    
    public ShootState check(MyPoint p) {
        ShootState res = ShootState.FLYING;
        //if (shoot) {
            if (Math.sqrt(Math.pow(p.getX()+p.getR()-275, 2) + Math.pow(p.getY()-big.getY(), 2))<=big.getR()) {
                res = ShootState.BIG_SHOT;
                //count_col++;
                //jLabel2.setText(Integer.toString(count_col));
                //shoot = false;
            }
            if (Math.sqrt(Math.pow(p.getX()+p.getR()-355, 2) + Math.pow(p.getY()-small.getY(), 2))<=small.getR()){
                res = ShootState.SMALL_SHOT;
                //count_col+=2;
                //jLabel2.setText(Integer.toString(count_col));
                //shoot = false;
            }
            if (p.getX()>492) {
                res = ShootState.MISSED;
                //shoot = false;
            }
        //} else res = true;
        //boolean res = Math.sqrt(Math.pow(panel1.getPosArw()-275, 2) + Math.pow(159-panel1.getPosYB(), 2))<=panel1.getRB() || 
        //        Math.sqrt(Math.pow(panel1.getPosArw()-315, 2) + Math.pow(159-panel1.getPosYS(), 2))<=panel1.getRS() || 
        //        panel1.getPosArw()>492;
        return res;
    }
    
    public void requestPause(String name) {
        if (isGameReset) return;
        if (waitingList.contains(name)) {
            waitingList.remove(name);
            if (waitingList.size() == 0){
                int a = 0;
                synchronized(this) {
                    notifyAll();
                }
            }
        } else {
            waitingList.add(name);
        }
    }
    
    public void requestShoot(String playerName) {
        if (isGameReset) return;
        var player = clientArrayList.stream()
                .filter(clientData -> clientData.getPlayerName().equals(playerName))
                .findFirst()
                .orElse(null);
        assert player != null;
        if (! shootingList.contains(player.getPlayerName())){
            shootingList.add(player.getPlayerName());
            player.increaseArrowsShoot(1);
        }
    }
    
    private synchronized void checkWinner() {
        clientArrayList.forEach(clientDataManager -> {
            if (clientDataManager.getPoints() >= 10) {
                this.winner = clientDataManager.getPlayerName();
                gameReset();
                return;
            }
        });
    }
    
    public String getWinner() {
        return winner;
    }
    
    public void addClient(ClientInfo clientData) {
        clientArrayList.add(clientData);
        this.arrowsCountUpdate();
    }
    
    public void setClientArrayList(ArrayList<ClientInfo> clientArrayList) {
        this.clientArrayList = clientArrayList;
    }
    
    public void setArrowArrayList(ArrayList<MyPoint> arrowArrayList) {
        this.arrowArrayList = arrowArrayList;
    }
    
    public void setWinner(String winner) {
        this.winner = winner;
    }
}
