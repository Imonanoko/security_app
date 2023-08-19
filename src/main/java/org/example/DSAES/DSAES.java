package org.example.DSAES;
import org.example.lib.library;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DSAES {
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
    protected List<List<Integer>> EK = new ArrayList<>();
    protected List<List<Integer>> D_box = new ArrayList<>();
    protected List<Integer> n = new ArrayList<>(Collections.nCopies(5,0));
    public DSAES(String PW){
        List<Integer> pw = new ArrayList<>(library.StringToInt(PW));
        pw = new ArrayList<>(library.DASM(pw,I_Box));
        List<Integer> KPW = new ArrayList<>();
        for(int i=0;i<16;i++){
            KPW.add(pw.get(i));
        }
        //D0_box
        D_box.add(new ArrayList<>(library.GBT1(pw,I_Box)));
        List<Integer> PW_E = new ArrayList<>(library.DASMExpansion(pw, D_box.get(0)));
        List<Integer> KL = new ArrayList<>(library.Middle(PW_E,40,16));
        List<Integer> KR = new ArrayList<>(library.Middle(PW_E,72,16));
        //EK0
        EK.add(library.XOR(library.BinaryAdd(KPW,KL),KR));
        //D1_box
        D_box.add(new ArrayList<>(library.GBT1(PW_E, D_box.get(0))));
        //D2_box,D3_box,EK1,EK2
        for(int i=1;i<3;i++){
            EK.add(library.DASM(EK.get(i-1),D_box.get(i)));
            D_box.add(library.GBT2(EK.get(i),D_box.get(i)));
        }
        //EK3
        EK.add(library.DASM(EK.get(2),D_box.get(3)));
        for(int i=0;i<8;i++){
            n.set(1,n.get(1)+EK.get(0).get(2*i));
            n.set(2,n.get(2)+EK.get(0).get(2*i+1));
            n.set(3,n.get(3)+EK.get(0).get(i));
            n.set(4,n.get(4)+EK.get(0).get(i+8));
        }
        n.set(1,n.get(1)%64);
        n.set(2,n.get(2)%64);
        n.set(3,n.get(3)%80);
        n.set(4,n.get(4)%80);
    }
    public String Encryption(String str){
        List<Integer> P = library.StringToInt(str);
        List<Integer> C1 = library.BinaryAdd(library.Rotate(P, EK.get(1)), EK.get(2));
        C1 = library.SubBytes(library.DBR(C1, n.get(1)), D_box.get(1));
        C1 = library.MixColumn(C1, n.get(3));
        List<Integer> C2 = library.XOR(library.Rotate(C1, EK.get(2)), EK.get(3));
        C2 = library.SubBytes(library.DBR(C2, n.get(2)), D_box.get(2));
        C2 = library.MixColumn(C2, n.get(4));
        String C = library.IntToString(library.SubBytes(library.XOR(library.BinaryAdd(C2, EK.get(3)),EK.get(1)), D_box.get(3)));
        return C;
    }
    public String Decryption(String str){
        List<Integer> C = new ArrayList<>(library.StringToInt(str));
        List<Integer> C2 = library.BinarySub(library.XOR(library.SubBytes(C,library.InverseBox(D_box.get(3))),EK.get(1)),EK.get(3));
        C2 =library.InverseMixColumn(C2, n.get(4));
        C2 = library.InverseDBR(library.SubBytes(C2, library.InverseBox(D_box.get(2))), n.get(2));
        List<Integer>C1 = library.InverseRotate(library.XOR(C2, EK.get(3)),EK.get(2));
        C1 = library.InverseMixColumn(C1, n.get(3));
        C1 = library.InverseDBR(library.SubBytes(C1, library.InverseBox(D_box.get(1))), n.get(1));
        List<Integer> P = library.InverseRotate(library.BinarySub(C1, EK.get(2)), EK.get(1));
        String outcome = library.IntToString(P);
        return outcome;
    }
    public static void main(String[] args){
        DSAES x = new DSAES("jfirowlskdvjoujaoiwfjorinvoa");
        String str = "sixsquare1234567";
        List<Integer> number = library.StringToInt(str);
        String y = x.Encryption(library.IntToString(number));
        DSAES newx = new DSAES("jfirowlskdvjoujaoiwfjorinvoa");
        System.out.println(newx.Decryption(y));
    }

}
