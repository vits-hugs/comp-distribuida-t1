
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
        frame.setSize(600, 600);

        JTextField textArea = new JTextField("Read Only JTextField");
        textArea.setEditable(false);

        //Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JButton post = new JButton("post muntjac");
        JButton getMuntjac = new JButton("get a muntjac");
        JButton modMuntjac = new JButton("modify muntjac");
        JTextField inputID = new JTextField(10);

        CircuitBreaker getCircuitBreaker = new CircuitBreaker();
        getMuntjac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSONObject object;
                int id = Integer.parseInt(inputID.getText());
                Request request = makeRequestGetIDMunt(URL,id);
                object = getCircuitBreaker.ExecuteAction(request);

                textArea.setText(object.toString());
            }
        });
        post.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Muntjac muntjac = new Muntjac("novo","chapeu novo",90);
                Request request = makeRequestPostMunt(URL,muntjac);
                JSONObject object = getCircuitBreaker.ExecuteAction(request);
                textArea.setText(object.toString());
            }
        });

        modMuntjac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Muntjac muntjac = new Muntjac("modificado","modificado chapeu",1);
                int id = Integer.parseInt(inputID.getText());
                Request request = makeRequestModifyMunt(URL,id,muntjac);
                JSONObject object = getCircuitBreaker.ExecuteAction(request);
                textArea.setText(object.toString());
            }
        });

        panel.add(post);
        panel.add(getMuntjac);
        panel.add(modMuntjac);
        panel.add(inputID);

        BufferedImage myPicture = ImageIO.read(Front.class.getResource("/muntjac.jpeg"));
        Image newImage = myPicture.getScaledInstance(300, 300, Image.SCALE_DEFAULT);
        JLabel ta = new JLabel(new ImageIcon(newImage));
        frame.getContentPane().add(BorderLayout.SOUTH,panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.getContentPane().add(BorderLayout.NORTH, textArea);
        frame.setVisible(true);
    }
}