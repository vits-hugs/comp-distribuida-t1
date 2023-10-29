
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

    private static Muntjac showMuntjacDialog(JButton parent) {
        JPanel panel = new JPanel();
        JTextField nameField = new JTextField(20);
        JTextField hatField = new JTextField(20);
        JTextField happyField = new JTextField(20);

        panel.add(new JLabel("Nome:"));
        panel.add(nameField);
        panel.add(new JLabel("Chapéu:"));
        panel.add(hatField);
        panel.add(new JLabel("Felicidade:"));
        panel.add(happyField);
        int result = JOptionPane.showConfirmDialog(parent, panel, "Insira dados do muntjac", JOptionPane.OK_CANCEL_OPTION);

        String name = null;
        String hat = null;
        int happyness = 0;
        if (result == JOptionPane.OK_OPTION) {
            name = nameField.getText();
            hat = hatField.getText();
            happyness = Integer.parseInt(happyField.getText());
//            JOptionPane.showMessageDialog(parent, "Name: " + name + "\nAge: " + age);
        }

        return new Muntjac(name, hat, happyness);
    }

    private static int choiceID(JButton parent) {
        JPanel panel = new JPanel();
        JTextField idField = new JTextField(20);

        panel.add(new JLabel("ID:"));
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Insira ID do muntjac", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {

            int id = Integer.parseInt(idField.getText());
            return id;

        }
        return -1;
    }

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

        CircuitBreaker getCircuitBreaker = new CircuitBreaker();
        getMuntjac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSONObject object;
                int id;
                try {
                    id = choiceID(getMuntjac);
                } catch (NumberFormatException exception) {
                    textArea.setText("Insira um numero");
                    getMuntjac.doClick();
                    return;
                }
                if (id!=-1) {
                    Request request = makeRequestGetIDMunt(URL,id);
                    object = getCircuitBreaker.ExecuteAction(request);
                    textArea.setText(object.toString());

                }

            }
        });
        post.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Muntjac muntjac = showMuntjacDialog(post);
                Request request = makeRequestPostMunt(URL,muntjac);
                JSONObject object = getCircuitBreaker.ExecuteAction(request);
                textArea.setText(object.toString());
            }
        });

        modMuntjac.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id;
                try {
                    id = choiceID(getMuntjac);
                } catch (NumberFormatException exception) {
                    textArea.setText("Insira um numero");
                    getMuntjac.doClick();
                    return;
                }
                if(id!=-1) {
                    Muntjac muntjac = showMuntjacDialog(modMuntjac);
                    Request request = makeRequestModifyMunt(URL, id, muntjac);
                    JSONObject object = getCircuitBreaker.ExecuteAction(request);
                    textArea.setText(object.toString());
                }
            }
        });

        panel.add(post);
        panel.add(getMuntjac);
        panel.add(modMuntjac);

        BufferedImage myPicture = ImageIO.read(Front.class.getResource("/muntjac.jpeg"));
        Image newImage = myPicture.getScaledInstance(450, 450, Image.SCALE_DEFAULT);
        JLabel ta = new JLabel(new ImageIcon(newImage));
        frame.getContentPane().add(BorderLayout.SOUTH,panel);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.getContentPane().add(BorderLayout.NORTH, textArea);
        frame.setVisible(true);
    }
}