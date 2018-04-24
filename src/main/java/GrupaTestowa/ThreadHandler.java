package GrupaTestowa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadHandler extends Thread {
    //private static InputStream inputStream = null;
    int port = -1;

    @Override
    public void run() {
        while(true){
        try {
            ServerSocket handlerSocket = new ServerSocket(12129);
            System.out.println("Server is turned on");
            while (true) {
                while (port == -1) {
                    //tu odczytuje port z serwera
                }
                Socket socket = handlerSocket.accept();                     //czeka na clienta
                OutputStream os = socket.getOutputStream();                 //tutaj wysyłam numer portu do clienta
                System.out.println("Port na TH: " + port);
                PrintWriter pw = new PrintWriter(os, true);
                pw.println(port);
                socket.close();                                             //rozlaczam sie
                port = -1;
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }
    }
}
