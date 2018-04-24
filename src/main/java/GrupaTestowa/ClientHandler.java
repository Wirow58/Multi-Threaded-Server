package GrupaTestowa;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.IOUtils.toBufferedReader;

public class ClientHandler extends Thread {

  /*  public static final String LINE_SEPARATOR;

            static {
                // avoid security issues
                final StringBuilderWriter buf = new StringBuilderWriter(4);
                final PrintWriter out = new PrintWriter(buf);
               out.println();
                LINE_SEPARATOR = buf.toString();
               out.close();
            }




    public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
                                  final Charset encoding) throws IOException {
               if (lines == null) {
                        return;
                   }
                if (lineEnding == null) {
                        lineEnding = LINE_SEPARATOR;
                   }
               final Charset cs = Charsets.toCharset(encoding);
                for (final Object line : lines) {
                       if (line != null) {
                                output.write(line.toString().getBytes(cs));
                            }
                        output.write(lineEnding.getBytes(cs));
                   }
                   output.write(null);
            }*/








    private static InputStream inputStream = null;
    int myPort = 0;
    int command = 0;
    int noofUstreams = 0;
    int noofDstreams = 0;
    int streamPort = -1;

    File file;
    List<StreamHandler> uploadHandlerList = new ArrayList<StreamHandler>();
    List<StreamHandler> downloadHandlerList = new ArrayList<StreamHandler>();

    //public static File file = null;
    // public static String path = "C:\\Users\\kapol\\Desktop\\Server folder\\test.png";

    public ClientHandler(){
        //clientPort to port ServerSocket stworzonego sobie przez klienta - to tu powraca informacja zwrotna o porcie ClientHandlera
    }

    //public ClientHandler(String path){
    //    this.path = path;
    //}
    @Override
    public void run()  {
        try {
            ServerSocket threadSocket = new ServerSocket(0); //0 sprawia, że serwer słucha na każdym wolnym porcie serverSocket.getLocalPort() zwraca użyty port
            myPort = threadSocket.getLocalPort();
            System.out.println("CH port: " + myPort);
            Socket socket = threadSocket.accept();
            System.out.println("Client accepted!");
            String fromClient1 = null;
            String fromClient2 = null;
            //dalej jest sprawdzanie danych uzytkownika
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            PrintStream pw = new PrintStream(os, true);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            fromClient1 = br.readLine();
            fromClient2 = br.readLine();
            System.out.println(fromClient1);
            System.out.println(fromClient2);
            String username="default";
            FileInputStream fis=new FileInputStream(".\\ServerConfig\\config.txt");
            List<String> configList=IOUtils.readLines(fis,"UTF-8");
            String config[]=configList.toArray(new String[0]);
            int configLength=config.length;
            for(int i=0;i<configLength;i++) {
                if ((fromClient1.equals(config[i])) && (fromClient2.equals(config[i+1]))) {       //Nazwy uzytkownika sa na parzystych pozycjach listy (zaczynajac od 0) a hasla na nieparzystych
                    username = config[i];
                    pw.println(1);
                    System.out.println("Login i haslo zgodne");
                    break;
                } else {
                    if(i==(configLength-1)){
                        pw.println(0);
                        System.out.println("Zjebales Krzysiu!");
                        socket.close();
                        interrupt();
                    }
                }
            }
            System.out.println("Client connected!");

            while(true) {
                //System.out.println("Wszed do petli");
                while(command==0){
                    //System.out.println("przed sprawdzeniem command");
                    try {
                        command = Integer.parseInt(br.readLine());
                    }catch (NullPointerException npe){command=0;}
                    //System.out.println("po sprawdzeniu command");
                }
                switch (command) {
                    case 1:
                        System.out.println("Dostalem 1");
                        streamPort = -1;
                        uploadHandlerList.add(new StreamHandler(command,username));
                        uploadHandlerList.get(noofUstreams).start();
                        while ((streamPort == -1) || (streamPort == 0)) {
                            streamPort = uploadHandlerList.get(noofUstreams).sPort;
                        }
                        System.out.println("stream Port " + streamPort);
                        pw.println(streamPort);
                        noofUstreams++;
                        break;
                    case 2:
                        System.out.println("Dostalem 2");
                        streamPort = -1;
                        downloadHandlerList.add(new StreamHandler(command,username));
                        downloadHandlerList.get(noofDstreams).start();
                        while ((streamPort == -1) || (streamPort == 0)) {
                            streamPort = downloadHandlerList.get(noofDstreams).sPort;
                        }
                        System.out.println("stream Port " + streamPort);
                        pw.println(streamPort);
                        noofDstreams++;
                        break;
                    case 3:
                        System.out.println("Dostalem 3");
                        String toDelete=br.readLine();
                        System.out.println("Plik do usuniecia: "+toDelete);
                        file = new File(".\\ServerFiles\\"+username+"\\"+toDelete);
                        file.delete();
                        break;
                    case 4:
                        long x;
                        System.out.println("Dostalem 4");
                        file = new File(".\\ServerFiles\\"+username);
                        String s[] = file.list();
                        try {
                        System.out.println("za chwile length");
                            x = s.length;
                            if(x!=0) {
                                System.out.println("Now listing from Collection");
                                for (int i = 0; i < x; i++) {
                                    System.out.println(s[i]);
                                }
                            }else{
                                System.out.println("Folder jest pusty!");
                            }
                        } catch (NullPointerException npe){
                            System.out.println("Folder jest pusty!");
                            x = 0;
                        }
                        System.out.println("Sending started.");
                        if (x==0){
                            pw.println("NO_FILES");
                        }else {
                            for (int i = 0; i < x; i++) {
                                pw.println(s[i]);
                            }
                            pw.println("LINE_END");
                        }
                        System.out.println("Sending ended.");
                        //nie zamykam socketa i streamu, bo bedo potrzebne
                        break;
                    case 5:
                        System.out.println("Dostalem 5");
                        String toCheck=br.readLine();
                        Long checksumC = Long.parseLong(br.readLine());
                        System.out.println("Plik do sprawdzenia: "+toCheck);
                        file = new File(".\\ServerFiles\\"+username+"\\"+toCheck);
                        Long checksumS=FileUtils.checksumCRC32(file);
                        pw.println(checksumC-checksumS);
                        System.out.println("różnica checksumow: "+(checksumC-checksumS));
                    default://probably do nothing
                        break;
                }
                command=0;
            }
            //komendy: 1-sendingFile from c to s; 2-downloading from s to c; 3-deleting file from server; 4-filesUpdate



            /*Socket socket = new Socket("Client", clientPort);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.print(threadSocket.getLocalPort());
            socket.close();                                         //zamyka socket do wysłania portu klientowi


            Socket comSocket = threadSocket.accept();                  //czeka na połączenie klienta - tworzy dla niego comSocket na którym jest dalsza komunikacja

            */
        }catch (Exception fnfe) {
            System.err.println(fnfe);
        }
    }
}