/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequence;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Trees<T> {

    Node<T> root;
    Graphics Gp;
    JPanel jp;
    

    public Trees(T rootData) {

        root = new Node<T>(0, null);
        root.edgeValue = null;
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();


    }

    void init() {
        JFrame f = new JFrame("Graph");
        f.setLayout(new BorderLayout());
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jp = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                setPreferredSize(new Dimension(5000, 1000)); // change this to adjust width and height of graph draw area
                repainter(g);

            }
        };
         final JScrollPane jsp = new JScrollPane(jp,   
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,   
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
        f.add(jsp,BorderLayout.CENTER);
        f.setSize(512, 512);
        f.setVisible(true);
    } 

    void repainter(Graphics Gp) {

        int w = jp.getWidth();
        int h = jp.getHeight();
        Gp.setColor(Color.ORANGE);
        Gp.setFont(new Font("arial",0,12));
        jp.setBackground(new Color(0,51,51));
        Gp.drawString("" + w, 10, 10);
        Gp.fillOval((w / 2) - 10, 50, 20, 20);
        Gp.setColor(Color.WHITE);
        Gp.drawString(root.data+"", (w/2)-60, 40);
        Gp.setColor(Color.ORANGE);
        if (root.children.size() > 0) {
            recursion(Gp, root.children, 0, w, 150, (w / 2), 60);
        }
    }

    void recursion(Graphics Gp, ArrayList<Node<T>> children, int x1, int x2, int h, int px1, int px2) {

       // System.err.println("Recursion");
        int diff = x2 - x1;
       // System.err.println(" Diff " + diff);
        int dx = diff / (children.size());
       // System.err.println(" dx " + dx);
        int Sx = x1 + dx;
        int prevX = x1;

        //System.err.println(" sx " + Sx);
        int offset=1;
        for (Node<T> n : children) {
            //System.err.println(" sx-10 " + (Sx - 10));
            Gp.drawLine((Sx + prevX) / 2, h, px1, px2);
            Gp.fillOval(((Sx + prevX) / 2) - 10, h - 10, 20, 20);
            Gp.setColor(Color.WHITE);
            Gp.drawString(n.data+"", ((Sx + prevX) / 2)-50+offset, h -15);
            int ex = ((Sx + prevX) / 2) + px1;
            int ey = h - px2;
            Gp.drawString(n.edgeValue+"", (ex / 2) + 5, h - 50);
            Gp.setColor(Color.ORANGE);
            if (n.children.size() > 0) {
                recursion(Gp, n.children, prevX, Sx, h + 100, ((Sx + prevX) / 2), h);
            }
            prevX = Sx;
            Sx += (dx);
            offset+=35;
        }
    }

    public static class Node<T> {

        int NodeId;
        T data;
        Node<T> parent;
        ArrayList<Node<T>> children;
        T edgeValue;

        Node(int id, Node p) {
            NodeId = id;
            parent = p;
            children = new ArrayList<Node<T>>();

        }

    }

    public static void main(String args[]) {

        Trees<String> t = new Trees<String>("root");
        Trees.Node<String> N = new Trees.Node<String>(1, t.root);
        N.children.add(new Trees.Node<String>(4, N));
        N.children.add(new Trees.Node<String>(5, N));
        N.children.add(new Trees.Node<String>(6, N));
        Trees.Node<String> N2 = new Trees.Node<String>(2, t.root);
        N2.children.add(new Trees.Node<String>(7, N2));
        N2.children.add(new Trees.Node<String>(8, N2));
        N2.children.add(new Trees.Node<String>(9, N2));
        Trees.Node<String> N3 = new Trees.Node<String>(3, t.root);
        N3.children.add(new Trees.Node<String>(10, N3));
        N3.children.add(new Trees.Node<String>(11, N3));
        Trees.Node<String> N4 = new Trees.Node<String>(12, t.root);
        N4.children.add(new Trees.Node<String>(13, N4));
        N4.children.add(new Trees.Node<String>(14, N4));
        t.root.children.add(N);
        t.root.children.add(N2);
        t.root.children.add(N3);
        t.root.children.add(N4);
        System.out.print(" " + t.root.children.size());
        t.init();

    }
}
