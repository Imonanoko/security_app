package org.example;


import org.example.lib.*;

public class test {
    public static void main(String []agrs){
        for(int i=0;i<256;i++){
            for(int j=0;j<256;j++){
                int add=-1;
                    if (i == 0) {
                        add = 0;
                    } else if (i == 1) {
                        add = j;
                    } else {
                        int P = i-2;
                        add = switch (P / 20) {
                            case 0 -> MixColumnTable0.MulTable.get(P % 20).get(j);
                            case 1 -> MixColumnTable1.MulTable.get(P % 20).get(j);
                            case 2 -> MixColumnTable2.MulTable.get(P % 20).get(j);
                            case 3 -> MixColumnTable3.MulTable.get(P % 20).get(j);
                            case 4 -> MixColumnTable4.MulTable.get(P % 20).get(j);
                            case 5 -> MixColumnTable5.MulTable.get(P % 20).get(j);
                            case 6 -> MixColumnTable6.MulTable.get(P % 20).get(j);
                            case 7 -> MixColumnTable7.MulTable.get(P % 20).get(j);
                            case 8 -> MixColumnTable8.MulTable.get(P % 20).get(j);
                            case 9 -> MixColumnTable9.MulTable.get(P % 20).get(j);
                            case 10 -> MixColumnTable10.MulTable.get(P % 20).get(j);
                            case 11 -> MixColumnTable11.MulTable.get(P % 20).get(j);
                            case 12 -> MixColumnTable12.MulTable.get(P % 20).get(j);
                            default -> add;
                        };
                    }
                    if(add != library.GF28Multiply(i,j)){
                        System.out.println("i = "+i+" ,j = "+j+" ,your ans: "+add+" ,ans:"+library.GF28Multiply(i,j));
                        break;
                    }
            }
        }
    }
}
