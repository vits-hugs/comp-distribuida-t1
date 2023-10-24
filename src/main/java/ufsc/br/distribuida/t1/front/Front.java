
package ufsc.br.distribuida.t1.front;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.json.JSONObject;
import ufsc.br.distribuida.t1.front.circuitbreaker.CircuitBreaker;
import ufsc.br.distribuida.t1.front.circuitbreaker.GetRequest;
import ufsc.br.distribuida.t1.front.circuitbreaker.Requester;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static ufsc.br.distribuida.t1.front.requestFunctions.makeRequestGetIDMunt;


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
        JButton send = new JButton("Send");
        JButton reset = new JButton("Send");
        JButton r1 = new JButton("Send");
        JButton r2 = new JButton("Send");

        CircuitBreaker getCircuitBreaker = new CircuitBreaker();
        send.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JSONObject object;
                Requester request = makeRequestGetIDMunt(URL,1);
                object = getCircuitBreaker.ExecuteAction(request);

                textArea.setText(object.toString());
            }
        });
        panel.add(send);
        panel.add(reset);
        panel.add(r2);panel.add(r1);
        // Text Area at the Center


        BufferedImage myPicture = ImageIO.read(Front.class.getResource("/muntjac.jpeg"));
        Image newImage = myPicture.getScaledInstance(300, 300, Image.SCALE_DEFAULT);
        JLabel ta = new JLabel(new ImageIcon(newImage));
        frame.getContentPane().add(BorderLayout.SOUTH,panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.getContentPane().add(BorderLayout.NORTH, textArea);
        frame.setVisible(true);
    }
}