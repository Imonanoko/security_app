package org.example.threeDtwoB;

import org.example.lib.library;

import java.util.ArrayList;
import java.util.List;


public class ThreeDTwoB {
    private static final List<Integer> S_Box = new ArrayList<>(
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
    public static List<Integer> Encryption(List<Integer> P, List<Integer> EK1, List<Integer> EK2, List<Integer> EK3) {
        List<Integer> D1_Box = new ArrayList<>(library.GBT1(library.BinaryAdd(EK1, EK2), S_Box));
        List<Integer> D2_Box = new ArrayList<>(library.GBT2(EK3, D1_Box));
        List<Integer> C;
        int n0 = (EK1.size() + 1) / 2, n1 = 0, n2 = 0;
        for (int i = 0; i < n0; i++) {
            n1 += EK1.get(2 * i);
            n2 += EK1.get(2 * i + 1);
        }
        n1 %= 64;
        n2 %= 64;
        List<Integer> C1 = new ArrayList<>(library.SubBytes(library.DBR(P, n1), D1_Box));
//        System.out.println("C1: "+C1);
        List<Integer> C2 = new ArrayList<>(library.XOR(library.BinaryAdd(library.Rotate(C1, EK1), EK2), EK3));
        System.out.println("C2: "+C2);
        C = new ArrayList<>(library.SubBytes(library.DBR(C2, n2), D2_Box));
        return C;
    }

    public static List<Integer> Decryption(List<Integer> C, List<Integer> EK1, List<Integer> EK2, List<Integer> EK3) {
        List<Integer> D1_Box = new ArrayList<>(library.GBT1(library.BinaryAdd(EK1, EK2), S_Box));
        List<Integer> D2_Box = new ArrayList<>(library.GBT2(EK3, D1_Box));
        List<Integer> I_D1_Box = new ArrayList<>(library.InverseBox(D1_Box));
        List<Integer> I_D2_Box = new ArrayList<>(library.InverseBox(D2_Box));
        List<Integer> P;
        int n0 = (EK1.size() + 1) / 2, n1 = 0, n2 = 0;
        for (int i = 0; i < n0; i++) {
            n1 += EK1.get(2 * i);
            n2 += EK1.get(2 * i + 1);
        }
        n1 %= 64;
        n2 %= 64;
        List<Integer> C2 = new ArrayList<>(library.InverseDBR(library.SubBytes(C,I_D2_Box),n2));
        List<Integer> C1 = new ArrayList<>(library.InverseRotate(library.BinarySub(library.XOR(C2,EK3),EK2),EK1));
        P = new ArrayList<>(library.InverseDBR(library.SubBytes(C1,I_D1_Box),n1));
        return P;
    }
    public static void main(String [] args){
//        String str = "xjisowomriwuejtx";
//        List<Integer> P = new ArrayList<>(library.StringToInt(str));
//        List<Integer> EK1 = new ArrayList<>(library.StringToInt("1111222233334444"));
//        List<Integer> EK2 = new ArrayList<>(library.StringToInt("2222333344445555"));
//        List<Integer> EK3 = new ArrayList<>(library.StringToInt("3333444455556666"));
//        List<Integer> C = Encryption(P,EK1,EK2,EK3);
//        System.out.println(library.IntToString(C));
//        System.out.println(library.IntToString(Decryption(C,EK1,EK2,EK3)));
//
//        String str1 = "ifku2258aplijruj";
//        List<Integer> P1 = new ArrayList<>(library.StringToInt(str1));
//        List<Integer> EK11 = new ArrayList<>(library.StringToInt("1111222233334444"));
//        List<Integer> EK21 = new ArrayList<>(library.StringToInt("2222333344445555"));
//        List<Integer> EK31 = new ArrayList<>(library.StringToInt("3333444455556666"));
//        List<Integer> C1 = Encryption(P1,EK11,EK21,EK31);
//        System.out.println(library.IntToString(C1));
//        System.out.println(library.IntToString(Decryption(C1,EK11,EK21,EK31)));

        //test
//        String str = "xjisowomriwuejtx";
//        List<Integer> P = new ArrayList<>(library.StringToInt(str));
//        for(int i=0;i<64;i++){
//            List<Integer> C = library.DBR(P, i);
//            if(!P.equals(library.InverseDBR(C,i))){
//                System.out.println(false+" : "+i);
//            }
//        }
    }
}
