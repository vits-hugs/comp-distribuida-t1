
package ufsc.br.distribuida.t1.front;

import org.json.JSONObject;
import ufsc.br.distribuida.t1.Muntjac;
import ufsc.br.distribuida.t1.front.requests.Request;
import ufsc.br.distribuida.t1.front.circuitbreaker.CircuitBreaker;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ufsc.br.distribuida.t1.front.requestFunctions.*;


public class Front{
    final static String URL = "http://localhost:8080/api/muntjacs";
    public static void main(String args[]) throws IOException {

        //Creating the Frame
        JFrame frame = new JFrame("Muntjac");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        JTextField textArea = new JTextField("Read Only JTextField");
        textArea.setEditable(false);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton post = new JButton("post muntjac");
        JButton getMuntjac = new JButton("get a muntjac");
        JButton modMuntjac = new JButton("modify muntjac");

        CircuitBreaker getCircuitBreaker = new CircuitBreaker();
        getMuntjac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSONObject object;

                Request request = makeRequestGetIDMunt(URL,1);
                object = getCircuitBreaker.ExecuteAction(request);

                textArea.setText(object.toString());
            }
        });
        post.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Muntjac muntjac = new Muntjac("pastel","asr",40);
                Request request = makeRequestPostMunt(URL,muntjac);
                JSONObject object = getCircuitBreaker.ExecuteAction(request);
                textArea.setText(object.toString());
            }
        });

        modMuntjac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Muntjac muntjac = new Muntjac("novo","asr",45);
                Request request = makeRequestModifyMunt(URL,153,muntjac);
                JSONObject object = getCircuitBreaker.ExecuteAction(request);
                textArea.setText(object.toString());
            }
        });

        panel.add(post);
        panel.add(getMuntjac);
        panel.add(modMuntjac);

        BufferedImage myPicture = ImageIO.read(Front.class.getResource("/muntjac.jpeg"));
        Image newImage = myPicture.getScaledInstance(300, 300, Image.SCALE_DEFAULT);
        JLabel ta = new JLabel(new ImageIcon(newImage));
        frame.getContentPane().add(BorderLayout.SOUTH,panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.getContentPane().add(BorderLayout.NORTH, textArea);
        frame.setVisible(true);
    }
}