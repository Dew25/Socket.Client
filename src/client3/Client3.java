/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client3;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author jvm
 */
public class Client3 extends JPanel implements Runnable, KeyListener {

    PrintWriter serverWriter;
    BufferedReader br;
    Socket socket;
    Map<String, int[]> locations = new HashMap<>();
    String message="";
    boolean working = true;

    public Client3() {
        addKeyListener(this);
        try {
            socket = new Socket("localhost", 3001);
            serverWriter = new PrintWriter(socket.getOutputStream(), true);
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            serverWriter.println("Juri");
            new Thread(this).start();
            setFocusable(true);
            requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(Client3.class.getName()).log(Level.SEVERE, "Не удалась запись в сокет", ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("Client");
        f.getContentPane().add(new Client3());// добавляем панель JPanel, коей является Client2
        f.setSize(400, 300);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            serverWriter.println("f");
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            serverWriter.println("r");
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            serverWriter.println("l");
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            serverWriter.println("b");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawString("Location", 100, 10);
        for (String userName : locations.keySet()) {
            int[] location = locations.get(userName);
            g.drawString(userName, location[0], location[1]);
            int step = 25;
            int lineX = (int) (location[0] + step * Math.cos(Math.toRadians(location[2])));
            int lineY = (int) (location[1] + step * Math.sin(Math.toRadians(location[2])));
            g.drawLine(location[0], location[1], lineX, lineY);
        }
        g.drawString(message, 100, 100);
    }

    @Override
    public void run() {
        try {
            while (working) {
                String line = br.readLine();
                String[] m1 = line.split(",");
                if(m1.length == 4){
                    locations.put(m1[0],new int[]{
                      Integer.parseInt(m1[1]),
                      Integer.parseInt(m1[2]),
                      Integer.parseInt(m1[3]),
                    });
                    repaint();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client3.class.getName()).log(Level.SEVERE, null, ex);
            message = "Server ended";
            repaint();
        }
    }

}
