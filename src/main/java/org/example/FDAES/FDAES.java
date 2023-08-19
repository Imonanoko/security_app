package org.example.FDAES;

import org.example.lib.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FDAES {
    private final List<Integer> I_Box = new ArrayList<>(
            List.of(52, 91, 130, 169, 208, 247, 30, 71, 112, 153, 194, 235, 20, 63, 106, 149, 192, 236, 23, 68,
                    114, 159, 204, 250, 39, 86, 134, 181, 228, 19, 70, 120, 170, 219, 12, 64, 117, 168, 221, 16, 73,
                    126, 180, 234, 34, 90, 146, 203, 4, 61, 123, 183, 242, 45, 105, 167, 230, 37, 100, 164, 229, 40,
                    104, 174, 241, 51, 122, 191, 5, 78, 148, 220, 38, 113, 189, 8, 85, 162, 244, 65, 144, 225, 53,
                    137, 217, 48, 136, 222, 55, 143, 237, 72, 161, 0, 95, 193, 29, 131, 231, 77, 178, 24, 129, 239,
                    88, 198, 49, 163, 18, 138, 254, 111, 233, 98, 216, 92, 213, 89, 215, 97, 232, 110, 253, 135, 21,
                    166, 56, 202, 101, 2, 156, 58, 214, 128, 36, 206, 125, 43, 223, 151, 76, 10, 197, 141, 80, 26,
                    238, 184, 142, 99, 62, 32, 9, 248, 218, 205, 196, 190, 195, 201, 212, 246, 11, 41, 75, 116, 160,
                    224, 31, 102, 175, 14, 107, 200, 66, 177, 59, 187, 93, 3, 165, 96, 42, 252, 207, 176, 171, 173,
                    199, 251, 44, 109, 186, 54, 158, 79, 1, 179, 140, 132, 147, 209, 25, 119, 17, 182, 133, 127, 172,
                    22, 139, 74, 60, 94, 227, 118, 108, 188, 81, 67, 154, 84, 152, 69, 150, 103, 6, 46, 50, 35, 28,
                    47, 145, 57, 157, 226, 7, 240, 87, 83, 82, 13, 210, 121, 15, 33, 27, 245, 211, 124, 115, 155, 185,
                    243, 249, 255));
    //RK is RK0~RK10
    private List<List<Integer>> RK = new ArrayList<>(Collections.nCopies(11, new ArrayList<>()));
    //D_Box is D0_Box~D10_Box
    private List<List<Integer>> D_Box = new ArrayList<>(Collections.nCopies(11, new ArrayList<>()));
    private List<List<Integer>> LP;
    //input 16~128 byte
    public FDAES(String PW) {
        List<Integer> P = library.StringToInt(PW);
        List<Integer> K_CK = new ArrayList<>();
        for (int i=0;i<16;i++){
            K_CK.add(i,P.get(i));
        }
        D_Box.set(0,new ArrayList<>(library.GBT1(library.DASM(P,I_Box),I_Box)));
        List<Integer> CK_E = library.DASMExpansion(library.DASM(P,I_Box),I_Box);
        List<Integer> KL = library.Middle(CK_E,40,16);
        List<Integer> KR = library.Middle(CK_E,72,16);
        RK.set(0,new ArrayList<>(library.XOR(library.BinaryAdd(K_CK,KL),KR)));
        D_Box.set(1,new ArrayList<>(library.GBT1(RK.get(0),D_Box.get(0))));
        for(int i=1;i<10;i++){
            RK.set(i,library.DASM(RK.get(i-1),D_Box.get(i)));
            D_Box.set(i+1,library.GBT2(RK.get(i),D_Box.get(i)));
        }
        RK.set(10,library.DASM(RK.get(9),D_Box.get(10)));
        LP =library.LP(RK);
    }
    //Plaintext only 16 byte
    public List<Integer> Encryption(String Plaintext){
        List<Integer> P = new ArrayList<>(library.StringToInt(Plaintext));
        List<Integer> tmp = new ArrayList<>(library.XOR(P,this.RK.get(0)));
        List<Integer> C;
        for(int i=1;i<10;i++){
            tmp = library.SubBytes(tmp,D_Box.get(i));
//            System.out.println("Sub Bytes"+tmp);
            tmp = library.DBR(tmp,this.LP.get(i-1).get(0));
//            System.out.println("DBR"+tmp);
            tmp = library.MixColumn(tmp,this.LP.get(i-1).get(1));
//            System.out.println("Mix Column"+tmp);
            tmp = library.XOR(tmp,this.RK.get(i));
//            System.out.println("Round Key"+tmp+"\n"+"\n");
        }
        tmp = library.SubBytes(tmp,D_Box.get(10));
        tmp = library.DBR(tmp,this.LP.get(9).get(0));
        C = library.XOR(tmp,this.RK.get(10));

        return C;
    }
    public List<Integer> Decryption(String Ciphertext){
        List<Integer> C = new ArrayList<>(library.StringToInt(Ciphertext));
        List<Integer> tmp = new ArrayList<>(library.XOR(C,this.RK.get(10)));
        tmp = library.InverseDBR(tmp,this.LP.get(9).get(0));
        tmp = library.SubBytes(tmp,library.InverseBox(D_Box.get(10)));
        for(int i=9;i>0;i--){
            tmp = library.XOR(tmp,this.RK.get(i));
            tmp = library.InverseMixColumn(tmp,this.LP.get(i-1).get(1));
            tmp = library.InverseDBR(tmp,this.LP.get(i-1).get(0));
            tmp = library.SubBytes(tmp,library.InverseBox(D_Box.get(i)));
        }
        List<Integer> P;
        P = library.XOR(tmp,this.RK.get(0));
        return P;
    }
        public static void main(String[] args) {
        FDAES x = new FDAES("sixkrudjsuitfjxuv");
        List<List<Integer>> C = new ArrayList<>();
        List<List<Integer>> P = new ArrayList<>();
        C.add(new ArrayList<>(x.Encryption("six square 10311")));
        C.add(new ArrayList<>(x.Encryption("sjiwurnv47@!#$%^")));
        C.add(new ArrayList<>(x.Encryption("xkigj574shtucjdu")));
        for(List<Integer> i:C){
            P.add(new ArrayList<>(x.Decryption(library.IntToString(i))));
        }
        FDAES y  = new FDAES("sixkrudjsuitfjxuv");
        for(List<Integer> j:P){
            System.out.println(library.IntToString(j));
        }
    }
}
