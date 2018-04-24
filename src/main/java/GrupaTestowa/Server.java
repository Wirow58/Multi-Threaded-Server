package GrupaTestowa;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import javax.swing.*;

public class Server extends JPanel {
public Server(){}
    public static void main(String args[]) {
        JFrame frame = new JFrame("Server");
        Server panel=new Server();
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        frame.getContentPane().add(panel, "Center");
        frame.setSize(panel.getPreferredSize());
        frame.setVisible(true);
        /*Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }
        c.format("Passwords don't match. Try again.%n");*/
        int portToSend = -1;
        ThreadHandler threadHandler = new ThreadHandler();
        threadHandler.start();

        List<ClientHandler> clientHandlerList = new ArrayList<ClientHandler>();

        for(int i=0; true; i++) {
            while(threadHandler.port!=-1){
            }
            clientHandlerList.add(new ClientHandler());
            clientHandlerList.get(i).start();
            while ((portToSend == -1) || (portToSend==0)) {
                portToSend = clientHandlerList.get(i).myPort;
            }
            threadHandler.port = portToSend;                //wysyłam port dla threadHandlera
        }
    }

}
