package org.example.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class library {

    public static List<Integer> SubBytes(List<Integer> P, List<Integer> D_Box) {
        int size = P.size();
        List<Integer> outcome = new ArrayList<>(Collections.nCopies(size, -1));
        for (int i = 0; i < size; i++) {
            outcome.set(i, D_Box.get(P.get(i)));
        }
        return outcome;
    }

    public static List<Integer> InverseBox(List<Integer> Box) {
        int size = Box.size();
        int value;
        List<Integer> I_Box = new ArrayList<>(Collections.nCopies(size, -1));
        for (int i = 0; i < size; i++) {
            value = Box.get(i);
            I_Box.set(value, i);
        }
        return I_Box;
    }

    public static List<Integer> XOR(List<Integer> n1, List<Integer> n2) {
        int length = n1.size();
        List<Integer> n3 = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            n3.add(n1.get(i) ^ n2.get(i));
        }
        return n3;
    }

    protected static int Inverse_S_Box(int value) {
        return table.S_Box.indexOf(value);
    }

    public static List<Integer> BinaryAdd(List<Integer> n1, List<Integer> key) {
        int length = n1.size();
        List<Integer> n3 = new ArrayList<>(Collections.nCopies(length, null));
        int carry = 0;
        for (int i = length - 1; i >= 0; i--) {
            int sum = n1.get(i) + key.get(i) + carry;
            carry = sum >> 8;
            int index = sum % 256;
            n3.set(i, table.S_Box.get(index));
        }
        return n3;
    }

    public static List<Integer> BinarySub(List<Integer> n3, List<Integer> key) {
        int length = n3.size();
        List<Integer> n1 = new ArrayList<>(Collections.nCopies(length, null));
        int borrow = 0;
        for (int i = length - 1; i >= 0; i--) {
            int sub = Inverse_S_Box(n3.get(i)) - key.get(i) - borrow;
            if (sub < 0) {
                borrow = 1;
            } else {
                borrow = 0;
            }
            int tmp = sub + (borrow << 8);
            n1.set(i, tmp);
        }
        return n1;
    }

    public static List<Integer> Rotate(List<Integer> n1, List<Integer> key) {
        int length = n1.size();
        int tmpLength = length / 4;
        int[] tmp = new int[tmpLength];
        for (int i = length - tmpLength; i < length; i++) {
            int index = i - length + tmpLength;
            tmp[index] = n1.get(i);
        }
        for (int i = length - 1; i >= tmpLength; i--) {
            n1.set(i, n1.get(i - tmpLength));
        }
        for (int i = 0; i < tmpLength; i++) {
            n1.set(i, tmp[i]);
        }
        return XOR(n1, key);
    }

    public static List<Integer> InverseRotate(List<Integer> n3, List<Integer> key) {
        int length = n3.size();
        int tmpLength = length / 4;
        int[] tmp = new int[tmpLength];
        int eax;
        List<Integer> n1 = XOR(n3, key);
        for (int i = 0; i < tmpLength; i++) {
            tmp[i] = n1.get(i);
        }
        for (int i = 0; i < length - tmpLength; i++) {
            n1.set(i, n1.get(i + tmpLength));
        }
        for (int i = length - tmpLength; i < length; i++) {
            eax = i - length + tmpLength;
            n1.set(i, tmp[eax]);
        }

        return n1;
    }

    public static List<Integer> DASM(List<Integer> P, List<Integer> D_box) {
        int length = P.size();
        long[] vp = new long[length + 2];
        long dsc = 256;
        List<Integer> C = new ArrayList<>(Collections.nCopies(length, null));
        for (int i = 1; i <= length; i++) {
            vp[i] = P.get(i - 1);
            dsc = dsc + (vp[i] + 1) * i;
        }
//        System.out.println("dsc="+dsc);   //+i跟*i有差。+法有交換率
        vp[0] = vp[1] + vp[length] + dsc;
        vp[length + 1] = vp[length] + vp[length - 1];
        for (int i = 1; i <= length; i++) {
            dsc = (dsc + vp[i - 1] * vp[i] + vp[i + 1] + i) % 1048576;
            int vch = (int) ((vp[i] + dsc) % 256);
            C.set(i - 1, D_box.get(vch));
        }
        return C;
    }

    public static List<Integer> StringToInt(String n1) {
        List<Integer> c = new ArrayList<>(n1.length());
        for (int i = 0; i < n1.length(); i++) {
            c.add((int) n1.charAt(i));
        }
        return c;
    }

    public static String IntToString(List<Integer> numbers) {
        StringBuilder sb = new StringBuilder();
        for (int num : numbers) {
            sb.append((char) num);
        }
        return sb.toString();
    }

    public static List<Integer> Middle(List<Integer> CK_E, int Start, int length) {
        List<Integer> K = new ArrayList<>(Collections.nCopies(length, null));
        for (int i = Start; i < Start + length; i++) {
            K.set(i - Start, CK_E.get(i));
        }
        return K;
    }

    public static List<Integer> DASMExpansion(List<Integer> PW, List<Integer> D_box) {
        while (PW.size() < 1024) {
            List<Integer> P = DASM(PW, D_box);
            List<Integer> PWCopy = new ArrayList<>(PW);
            PW = new ArrayList<>(P);
            PW.addAll(PWCopy);
            PW.addAll(P);
        }
        PW = DASM(PW, D_box);
        List<Integer> outcome = new ArrayList<>(Collections.nCopies(128, null));
        for (int i = 127; i >= 0; i--) {
            outcome.set(i, PW.get(PW.size() - 128 + i));
        }
        return outcome;
    }

    private static List<List<Integer>> split128(List<Integer> PW) {
        int index = 0;
//        int PWL = PW.size();
        List<List<Integer>> outcome = new ArrayList<>();
        List<Integer> tmp = new ArrayList<>();
        for (Integer integer : PW) {
            tmp.add(integer);
            index++;
            if (index == 128) {
                outcome.add(new ArrayList<>(tmp));
                index = 0;
                tmp.clear();
            }
        }
        return outcome;
    }

    private static List<List<Integer>> GISK(List<Integer> PW, List<Integer> D_box) {
        List<Integer> ISK1;
        List<List<Integer>> outcome = new ArrayList<>();
        int PWLength = PW.size();
        if (PWLength < 128) {
            ISK1 = new ArrayList<>(DASMExpansion(PW, D_box));
        } else if (PWLength == 128) {
            ISK1 = new ArrayList<>(DASM(PW, D_box));
        } else {
            List<List<Integer>> splitPW = split128(PW);
            int m = splitPW.size() - 1;
            if (PWLength % 128 != 0) {
                splitPW.set(m, new ArrayList<>(DASMExpansion(splitPW.get(m), D_box)));
            }
            ISK1 = new ArrayList<>(splitPW.get(0));
            for (int i = 1; i < m + 1; i++) {
                ISK1 = XOR(ISK1, splitPW.get(i));
            }
        }
        outcome.add(new ArrayList<>(ISK1));
        outcome.add(new ArrayList<>(DASM(ISK1, D_box)));
        return outcome;
    }

    private static List<List<Integer>> GDK(List<Integer> ISK1, List<Integer> ISK2, List<Integer> D_box) {
        List<List<Integer>> outcome = new ArrayList<>();
        outcome.add(new ArrayList<>(DASM(DASM(XOR(ISK1, ISK2), D_box), D_box)));
        outcome.add(new ArrayList<>(DASM(DASM(BinaryAdd(ISK1, ISK2), D_box), D_box)));
        outcome.add(new ArrayList<>(DASM(DASM(Rotate(ISK1, ISK2), D_box), D_box)));
        return outcome;
    }

    private static List<Integer> DBG(List<Integer> KA, List<Integer> KB, List<Integer> KC) {
        List<Integer> SIA1, SIA2, SIA3, RIA, D_box;
        SIA1 = new ArrayList<>(Collections.nCopies(128, null));
        SIA2 = new ArrayList<>(Collections.nCopies(256, null));
        SIA3 = new ArrayList<>(Collections.nCopies(256, null));
        RIA = new ArrayList<>(Collections.nCopies(256, null));
        D_box = new ArrayList<>(Collections.nCopies(256, null));
        List<Boolean> FA = new ArrayList<>(Collections.nCopies(256, false));
        int AL1 = 0, AL2 = 0, ALR = 0;
        for (int i = 0; i < 128; i++) {
            if (!FA.get(KA.get(i))) {
                SIA1.set(AL1, KA.get(i));
                FA.set(KA.get(i), true);
                AL1++;
            }
        }
        for (int i = 0; i < 128; i++) {
            if (!FA.get(KB.get(i))) {
                SIA2.set(AL2, KB.get(i));
                FA.set(KB.get(i), true);
                AL2++;
            }
        }
        for (int i = 0; i < 128; i++) {
            if (!FA.get(KC.get(i))) {
                SIA2.set(AL2, KC.get(i));
                FA.set(KC.get(i), true);
                AL2++;
            }
        }
        for (int i = 0; i < 256; i++) {
            if (!FA.get(i)) {
                RIA.set(ALR, i);
                ALR++;
            }
        }
        for (int i = 0; i < ALR; i++) {
            SIA2.set(AL2 + i, RIA.get(i));
        }
        for (int i = 0; i < AL1; i++) {
            SIA2.set(AL2 + ALR + i, SIA1.get(i));
        }
        int h1 = 0, h2 = 0, h3 = 0, jup1 = 0, jup2 = 0, jup3 = 0;
        for (int i = 0; i < 64; i++) {
            h1 += KB.get(i);
            jup1 += KB.get(64 + i);
        }
        for (int i = 0; i < 32; i++) {
            h2 += KC.get(i);
            h3 += KC.get(32 + i);
            jup2 += KC.get(64 + i);
            jup3 += KC.get(96 + i);
        }
        h1 = h1 % 256;
        h2 = h2 % 256;
        h3 = h3 % 256;
        jup1 = jup1 % 128;
        if (jup1 % 2 == 0) {
            jup1 += 129;
        }
        jup2 = jup2 % 128;
        if (jup2 % 2 == 0) {
            jup2 += 129;
        }
        jup3 = jup3 % 128;
        if (jup3 % 2 == 0) {
            jup3 += 129;
        }
        for (int i = 0; i < 256; i++) {
            int tmp = (h1 + i * jup1) % 256;
            SIA3.set(i, SIA2.get(tmp));
        }
        for (int i = 0; i < 256; i++) {
            int tmp1 = (h3 + i * jup3) % 256;
            int tmp2 = (h2 + i * jup2) % 256;
            D_box.set(tmp1, SIA3.get(tmp2));
        }
        return D_box;
    }

    public static List<Integer> GBT1(List<Integer> PW, List<Integer> I_box) {
        List<List<Integer>> ISK = GISK(PW, I_box);
        List<List<Integer>> DK = GDK(ISK.get(0), ISK.get(1), I_box);
        //return D_box;
        return DBG(DK.get(0), DK.get(1), DK.get(2));
    }

    public static List<Integer> GBT2(List<Integer> K, List<Integer> I_box) {
        int h1 = 0, h2 = 0, jump1 = 0, jump2 = 0;
        List<Integer> D_box = new ArrayList<>(Collections.nCopies(256, null));
        for (int i = 0; i < 4; i++) {
            h1 += K.get(i);
            h2 += K.get(i + 4);
            jump1 += K.get(i + 8);
            jump2 += K.get(i + 12);
        }
        h1 = h1 % 256;
        h2 = h2 % 256;
        jump1 = jump1 % 128;
        jump2 = jump2 % 128;
        if (jump1 % 2 == 0) {
            jump1 += 129;
        }
        if (jump2 % 2 == 0) {
            jump2 += 129;
        }
        for (int i = 0; i < 256; i++) {
            int tmp = (h2 + jump2 * i) % 256;
            int tmp1 = (h1 + jump1 * i) % 256;
            D_box.set(tmp, I_box.get(tmp1));
        }
        return D_box;
    }

    public static List<Integer> DBR(List<Integer> P, int n) {
        List<Integer> C = new ArrayList<>(Collections.nCopies(16, -1));
        List<Integer> nth = new ArrayList<>(Collections.nCopies(16, -1));
        int index;
        if (n < 16) {
            C.clear();
            for (int i = 0; i < 16; i++) {
                index = (i + n) % 16;
                C.add(P.get(index));
            }
            return C;
        } else if (n < 40) {
            nth = new ArrayList<>(table.DBRArrayType2.get(n - 16));
        } else if (n < 64) {
            nth = new ArrayList<>(table.DBRArrayType3.get(n - 40));
        }
        for (int i = 0; i < 16; i++) {

            index = nth.get(i);
            C.set(i, P.get(index));
        }
        return C;
    }

    public static List<Integer> InverseDBR(List<Integer> C, int n) {
        List<Integer> P = new ArrayList<>(Collections.nCopies(16, -1));
        List<Integer> nth = new ArrayList<>(Collections.nCopies(16, -1));
        int index;
        if (n < 16) {
            P.clear();
            for (int i = 0; i < 16; i++) {
                index = (i - n + 16) % 16;
                P.add(C.get(index));
            }
            return P;
        } else if (n < 40) {
            nth = new ArrayList<>(table.InverseDBRArrayType2.get(n - 16));
        } else if (n < 64) {
            nth = new ArrayList<>(table.InverseDBRArrayType3.get(n - 40));
        }
        for (int i = 0; i < 16; i++) {
            index = nth.get(i);
            P.set(i, C.get(index));
        }
        return P;
    }

    private static int GF28Add(int a, int b) {
        return a ^ b;
    }

    //mod = x8+x4+x3+x+1 =>100011011
    public static int GF28Multiply(int a, int b) {
        int product = 0;
        //存取a的多項式乘1~x^7的結果
        int[] power = new int[8];
        String Binary_b = Integer.toBinaryString(b);
        int Binary_bLength = Binary_b.length();
        for (int i = 0; i < 8; i++) {
            power[i] = a << i;
        }
        for (int i = 0; i < Binary_bLength; i++) {
            if (Binary_b.charAt(Binary_bLength-1-i) == '1') {
                product = GF28Add(product, power[i]);
            }
        }

        int productLength = Integer.toBinaryString(product).length();
        while (productLength>8){
            product = GF28Add(product,table.GF28ModList[productLength-9]);
            productLength = Integer.toBinaryString(product).length();
        }//1110101010
        return product;
    }
    public static List<Integer> GF28MatrixMultiply(List<Integer> RightMatrix,List<Integer> LeftMatrix,int size){
        List<Integer> outcome = new ArrayList<>();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                int tmp = 0;
                for(int k=0;k<size;k++){
                    tmp = GF28Add(tmp,GF28Multiply(RightMatrix.get(j+4*k),LeftMatrix.get(4*i+k)));
                }
                outcome.add(tmp);
            }
        }
        return outcome;
    }
    //生成用可憐
//    public static int GF28Multiply(int a,int b){
//        int value= b;
//        if(a>=2){
//            value = b<<1;
//            if(value>255){
//                value^=283;
//            }
//            if(a==3){
//                value^=b;
//            }
//        }
//        return value;
//    }

    //舊版
    public static List<Integer> MixColumn(List<Integer> P, int n) {
        List<Integer> outcome = new ArrayList<>();
        List<Integer> matrix = new ArrayList<>(table.MixColumn.get(n));
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                int tmp = 0;
                for(int k=0;k<4;k++){
                    tmp = GF28Add(tmp,GF28Multiply(matrix.get(j+4*k),P.get(4*i+k)));
                }
                outcome.add(tmp);
            }
        }
        return outcome;
    }
    //舊版
    public static List<Integer> InverseMixColumn(List<Integer> C, int n) {
        List<Integer> outcome = new ArrayList<>();
        List<Integer> matrix = new ArrayList<>(table.InverseMixColumn.get(n));
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                int tmp = 0;
                for(int k=0;k<4;k++){
                    tmp = GF28Add(tmp,GF28Multiply(matrix.get(j+4*k),C.get(4*i+k)));
                }
                outcome.add(tmp);
            }
        }
        return outcome;
    }
    //新版
//    public static List<Integer> MixColumn(List<Integer> P, int n) {
//        List<Integer> outcome = new ArrayList<>();
//        List<Integer> matrix = new ArrayList<>(table.MixColumn.get(n));
//        for(int i=0;i<4;i++){
//            for(int j=0;j<4;j++){
//                int tmp = 0;
//                for(int k=0;k<4;k++){
//                    int number;
//                    if(matrix.get(j+4*k)==0){
//                        number=0;
//                    } else if (matrix.get(j+4*k)==1) {
//                        number = P.get(4*i+k);
//                    }else {
//                        if(matrix.get(j+4*k)<53){
//                            number = table2.Multiply_by_2TO255.get(matrix.get(j+4*k)-2).get(P.get(4*i+k));
//                        } else if (matrix.get(j+4*k)<104) {
//                            number = table3.Multiply_by_2TO255.get(matrix.get(j+4*k)-53).get(P.get(4*i+k));
//                        }
//                        else if (matrix.get(j+4*k)<155) {
//                            number = table4.Multiply_by_2TO255.get(matrix.get(j+4*k)-104).get(P.get(4*i+k));
//                        }
//                        else if (matrix.get(j+4*k)<206) {
//                            number = table5.Multiply_by_2TO255.get(matrix.get(j+4*k)-155).get(P.get(4*i+k));
//                        }
//                        else {
//                            number = table6.Multiply_by_2TO255.get(matrix.get(j+4*k)-206).get(P.get(4*i+k));
//                        }
//
//                    }
//                    tmp = GF28Add(tmp,number);
////                    tmp = GF28Add(tmp,GF28Multiply(matrix.get(j+4*k),P.get(4*i+k)));
//                }
//                outcome.add(tmp);
//            }
//        }
//        return outcome;
//    }
    public static List<List<Integer>> LP(List<List<Integer>> RK) {
        List<List<Integer>> LP = new ArrayList<>(Collections.nCopies(10, new ArrayList<>(Collections.nCopies(2, 0))));
        for (int i = 0; i < 10; i++) {
            List<Integer> tmp = new ArrayList<>(RK.get(i));
            int tmpLP0 = 0, tmpLP1 = 0;
            for (int j = 0; j < 8; j++) {
                tmpLP0 += tmp.get(2 * j);
                tmpLP1 += tmp.get(2 * j + 1);
            }
            tmpLP0 = tmpLP0 % 64;
            tmpLP1 = tmpLP1 % 80;
            LP.get(i).set(0, tmpLP0);
            LP.get(i).set(1, tmpLP1);
        }
        return LP;
    }
}
