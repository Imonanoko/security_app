package org.example;

import org.example.DSAES.DSAES;
import org.example.FDAES.FDAES;
import org.example.lib.library;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.IOException;


public class Main {
    public static List<Integer> ByteTOInt(byte[] buffer) {
        List<Integer> outcome = new ArrayList<>();
        for (byte b : buffer) {
            outcome.add(Byte.toUnsignedInt(b));
        }
        return outcome;
    }

    public static byte[] IntTOByte(List<Integer> outcome) {
        byte[] buffer = new byte[outcome.size()];
        for (int i = 0; i < outcome.size(); i++) {
            buffer[i] = outcome.get(i).byteValue();
        }
        return buffer;
    }

    private static String FileNameSplit(String filePath) {
        String[] FilePathSplit = filePath.split("\\\\");
        return FilePathSplit[FilePathSplit.length - 1];
    }

    public static void FileEncryption(String filePath, String PW, boolean type) {
        String EncFile;
        if (type) {
            EncFile = ".\\src\\main\\java\\org\\example\\FDAESEncryption\\" + FileNameSplit(filePath);
        }
        else {
            EncFile = ".\\src\\main\\java\\org\\example\\DSAESEncryption\\" + FileNameSplit(filePath);
        }

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            try (FileOutputStream outputStream = new FileOutputStream(EncFile)) {
                byte[] buffer = new byte[16];
                int bytesRead;
                int total = 0;
                long filesizeByte = FileSize(filePath);
                long filesize;
                if(filesizeByte%16!=0){
                    filesize = filesizeByte/16+1;
                }
                else {
                    filesize = filesizeByte/16;
                }
                System.out.println(total + "/" + filesize);
                if (type) {
                    FDAES algorithm = new FDAES(PW);
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        List<Integer> FileBlock = new ArrayList<>(ByteTOInt(buffer));
                        while (FileBlock.size()<16){
                            FileBlock.add(0);
                        }
                        List<Integer> enc = algorithm.Encryption(library.IntToString(FileBlock));
                        outputStream.write(IntTOByte(enc));
                        total++;
                        System.out.println(total + "/" + filesize);
                    }
                } else {
                    DSAES algorithm = new DSAES(PW);
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        List<Integer> FileBlock = new ArrayList<>(ByteTOInt(buffer));
                        while (FileBlock.size()<16){
                            FileBlock.add(0);
                        }
                        List<Integer> enc = library.StringToInt(algorithm.Encryption(library.IntToString(FileBlock)));
                        outputStream.write(IntTOByte(enc));
                        total++;
                        System.out.println(total + "/" + filesize);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long FileSize(String Path) {
        File file = new File(Path);
        if (file.exists()) {
            return file.length();
        } else {
            System.out.println("File does not exist.");
        }
        return 0;
    }

    public static void FileDecryption(String filePath, String PW, boolean type) {
        String DecFile;
        if(type){
            DecFile = ".\\src\\main\\java\\org\\example\\FDAESDecryption\\" + FileNameSplit(filePath);
        }
        else {
            DecFile = ".\\src\\main\\java\\org\\example\\DSAESDecryption\\" + FileNameSplit(filePath);
        }

        try (FileInputStream inputStream = new FileInputStream(filePath)) {
            try (FileOutputStream outputStream = new FileOutputStream(DecFile)) {
                byte[] buffer = new byte[16];
                int bytesRead;
                int total = 0;
                long filesizeByte = FileSize(filePath);
                long filesize;
                if(filesizeByte%16!=0){
                    filesize = filesizeByte/16+1;
                }
                else {
                    filesize = filesizeByte/16;
                }
                System.out.println(total + "/" + filesize);
                if (type) {
                    FDAES algorithm = new FDAES(PW);
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        List<Integer> FileBlock = new ArrayList<>(ByteTOInt(buffer));
                        List<Integer> dec = algorithm.Decryption(library.IntToString(FileBlock));
                        if(filesize-1==total){
                            while (dec.get(dec.size()-1)==0){
                                dec.remove(dec.size()-1);
                            }
                        }
                        outputStream.write(IntTOByte(dec));
                        total++;
                        System.out.println(total + "/" + filesize);
                    }
                } else {
                    DSAES algorithm = new DSAES(PW);
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        List<Integer> FileBlock = new ArrayList<>(ByteTOInt(buffer));
                        List<Integer> dec = library.StringToInt(algorithm.Decryption(library.IntToString(FileBlock)));
                        if(filesize-1==total){
                            while (dec.get(dec.size()-1)==0){
                                dec.remove(dec.size()-1);
                            }
                        }
                        outputStream.write(IntTOByte(dec));
                        total++;
                        System.out.println(total + "/" + filesize);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        //加密
        String filePath = ".\\src\\main\\java\\org\\example\\test.txt";
        FileEncryption(filePath,"jfirowlskdvjoujaoiwfjorinvoa",true);
        FileEncryption(filePath,"jfirowlskdvjoujaoiwfjorinvoa",false);
        //解密
        filePath = ".\\src\\main\\java\\org\\example\\FDAESEncryption\\test.txt";
        FileDecryption(filePath,"jfirowlskdvjoujaoiwfjorinvoa",true);
        filePath = ".\\src\\main\\java\\org\\example\\DSAESEncryption\\test.txt";
        FileDecryption(filePath,"jfirowlskdvjoujaoiwfjorinvoa",false);

    }


}