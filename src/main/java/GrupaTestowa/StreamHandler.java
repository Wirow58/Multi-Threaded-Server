package GrupaTestowa;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class StreamHandler extends Thread{


    int sPort=-1;
    int command = 0;
    String username = "default";
    private static InputStream inputStream = null;
    private static OutputStream outputStream = null;
    public static File file = null;
    String filename;
    public StreamHandler (int command, String username){
        this.command = command;
        this.username=username;
    }
    public void run(){
        switch (command) {
            case 1:
            try {
                ServerSocket clientSocket = new ServerSocket(0);
                sPort = clientSocket.getLocalPort();
                System.out.println("UH port: " + sPort);
                Socket socket = clientSocket.accept();
                System.out.println("UH is connected");

                try {
                    try {
                        inputStream = socket.getInputStream();
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(inputStream));
                        filename = br.readLine();
                        System.out.println(filename);
                        file = new File(".\\ServerFiles\\"+username+"\\" + filename);
                            FileUtils.copyToFile(inputStream, file);
                            System.out.println("File uploaded");

                    } catch (IOException ioe) {
                        System.err.println(ioe);
                    }
                    socket.close();
                } catch (FileNotFoundException fnfe) {
                    System.err.println(fnfe);
                } finally {
                    inputStream.close();
                    socket.close();
                    interrupt();
                }
            } catch (IOException ioe) {
                System.out.println("StreamHandler exception: " + ioe);
            }
            break;
            case 2:
                try {
                    ServerSocket clientSocket = new ServerSocket(0);
                    sPort = clientSocket.getLocalPort();
                    System.out.println("DH port: " + sPort);
                    Socket socket = clientSocket.accept();
                    System.out.println("DH is connected");

                    try {
                        try {
                            outputStream = socket.getOutputStream();
                            inputStream = socket.getInputStream();
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(inputStream));
                            filename = br.readLine();
                            file = new File(".\\ServerFiles\\"+username+"\\" + filename);
                            FileUtils.copyFile(file, outputStream);

                        } catch (IOException ioe) {
                            System.err.println(ioe);
                        }
                        socket.close();
                    } catch (FileNotFoundException fnfe) {
                        System.err.println(fnfe);
                    } finally {
                        inputStream.close();
                        socket.close();
                        interrupt();
                    }
                } catch (IOException ioe) {
                    System.out.println("StreamHandler exception: " + ioe);
                }
                break;
        }
    }
}
