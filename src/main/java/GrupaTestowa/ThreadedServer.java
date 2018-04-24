package GrupaTestowa;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.io.FileUtils;

public class ThreadedServer implements MyRunnable {
    private static InputStream inputStream = null;
    public static File file = null;
    public static String path = "C:\\Users\\kapol\\Desktop\\Server folder\\test.png";

    public ThreadedServer(){
    }

    public ThreadedServer(String path){
        this.path = path;
    }

    public void myRun() throws IOException{
        try {
            ServerSocket serverSocket = new ServerSocket(13000); //0 sprawia, że serwer słucha na każdym wolnym porcie serverSocket.getLocalPort() zwraca użyty port
            System.out.println("Server is turned on");
                Socket socket = serverSocket.accept();
            try {
                System.out.println("New Connection");
                inputStream = socket.getInputStream();
                file = new File(path);
                FileUtils.copyToFile(inputStream,file);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
            socket.close();
        }catch (FileNotFoundException fnfe) {
            System.err.println(fnfe);
        }finally {
            inputStream.close();
        }
    }
}