package GrupaTestowa;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class test {
    static long l = 0;
    static long i;
    public static void main(String[] args){
        File file = new File("C:\\Users\\Bartek\\Desktop\\Archiwum\\Blizzzer\\Urabe2.jpg");
        try {
            i=FileUtils.checksumCRC32(file);
        } catch (IOException ioe){System.out.println("nie pyklo");}
        System.out.println("checksum: "+i);
    }
}
