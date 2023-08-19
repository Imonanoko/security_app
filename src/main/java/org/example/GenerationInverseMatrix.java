package org.example;

import org.example.lib.library;
import org.example.lib.table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.Thread;

class t extends Thread {
    public static int GF28Multiply(int a, int b) {
        int value = b;
        if (a >= 2) {
            value = b << 1;
            if (value > 255) {
                value ^= 283;
            }
            if (a == 3) {
                value ^= b;
            }
        }
        return value;
    }

    private static List<List<Integer>> newtable = new ArrayList<>(List.of(
            List.of(2, 2, 1, 3, 3, 3, 3, 1, 1, 1, 3, 1, 1, 1, 3, 3),
            List.of(3, 2, 1, 2, 2, 3, 2, 3, 2, 1, 2, 1, 3, 1, 3, 1),
            List.of(3, 2, 1, 1, 3, 2, 2, 1, 1, 2, 2, 2, 3, 3, 2, 2),
            List.of(1, 1, 2, 1, 3, 2, 3, 1, 3, 2, 2, 2, 3, 1, 2, 1),
            List.of(1, 3, 3, 2, 1, 1, 3, 1, 1, 2, 2, 2, 1, 1, 2, 3),
            List.of(2, 3, 3, 2, 2, 2, 3, 3, 1, 1, 1, 3, 3, 1, 3, 1),
            List.of(1, 3, 3, 2, 1, 2, 1, 2, 3, 3, 3, 2, 2, 3, 3, 2),
            List.of(3, 3, 2, 3, 3, 1, 1, 1, 2, 3, 3, 1, 1, 2, 1, 2),
            List.of(3, 1, 3, 2, 2, 2, 2, 3, 2, 3, 2, 1, 2, 1, 1, 3),
            List.of(3, 2, 1, 2, 3, 1, 3, 2, 3, 1, 3, 1, 1, 1, 1, 1)

    ));
    private int num = 0;

    public t(int num) {
        this.num = num;
    }

    public static void appendListToFile(String filename, List<Integer> dataList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for (Integer item : dataList) {
                writer.write(item.toString() + " ");
            }
            writer.newLine();
            System.out.println("List已成功附加寫入檔案 " + filename + " 中。");
        } catch (IOException e) {
            System.out.println("寫入檔案時發生錯誤：" + e.getMessage());
        }
    }

    public void run() {
        List<Integer> outcome = new ArrayList<>();
        List<Integer> matrix = newtable.get(num);
        List<List<Integer>> RightMatrix = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            RightMatrix.add(new ArrayList<>(Collections.nCopies(4, null)));
        }
        int index = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                RightMatrix.get(j).set(i, matrix.get(index));
                index++;
            }
        }
        // find first column
        loop1:
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    for (int m = 0; m < 256; m++) {
                        int v1 = GF28Multiply(RightMatrix.get(0).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(0).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(0).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(0).get(3), m);
                        int v2 = GF28Multiply(RightMatrix.get(1).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(1).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(1).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(1).get(3), m);
                        int v3 = GF28Multiply(RightMatrix.get(2).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(2).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(2).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(2).get(3), m);
                        int v4 = GF28Multiply(RightMatrix.get(3).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(3).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(3).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(3).get(3), m);
                        if (v1 == 1 && v2 == 0 && v3 == 0 && v4 == 0) {
                            outcome.add(i);
                            outcome.add(j);
                            outcome.add(k);
                            outcome.add(m);
                            break loop1;
                        }
                    }
                }
            }
        }
        // find second column
        loop2:
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    for (int m = 0; m < 256; m++) {
                        int v1 = GF28Multiply(RightMatrix.get(0).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(0).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(0).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(0).get(3), m);
                        int v2 = GF28Multiply(RightMatrix.get(1).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(1).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(1).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(1).get(3), m);
                        int v3 = GF28Multiply(RightMatrix.get(2).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(2).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(2).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(2).get(3), m);
                        int v4 = GF28Multiply(RightMatrix.get(3).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(3).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(3).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(3).get(3), m);
                        if (v1 == 0 && v2 == 1 && v3 == 0 && v4 == 0) {
                            outcome.add(i);
                            outcome.add(j);
                            outcome.add(k);
                            outcome.add(m);
                            break loop2;
                        }
                    }
                }
            }
        }
        // find 3th column
        loop3:
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    for (int m = 0; m < 256; m++) {
                        int v1 = GF28Multiply(RightMatrix.get(0).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(0).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(0).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(0).get(3), m);
                        int v2 = GF28Multiply(RightMatrix.get(1).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(1).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(1).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(1).get(3), m);
                        int v3 = GF28Multiply(RightMatrix.get(2).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(2).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(2).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(2).get(3), m);
                        int v4 = GF28Multiply(RightMatrix.get(3).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(3).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(3).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(3).get(3), m);
                        if (v1 == 0 && v2 == 0 && v3 == 1 && v4 == 0) {
                            outcome.add(i);
                            outcome.add(j);
                            outcome.add(k);
                            outcome.add(m);
                            break loop3;
                        }
                    }
                }
            }
        }
        // find 4th column
        loop4:
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                for (int k = 0; k < 256; k++) {
                    for (int m = 0; m < 256; m++) {
                        int v1 = GF28Multiply(RightMatrix.get(0).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(0).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(0).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(0).get(3), m);
                        int v2 = GF28Multiply(RightMatrix.get(1).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(1).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(1).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(1).get(3), m);
                        int v3 = GF28Multiply(RightMatrix.get(2).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(2).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(2).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(2).get(3), m);
                        int v4 = GF28Multiply(RightMatrix.get(3).get(0), i)
                                ^ GF28Multiply(RightMatrix.get(3).get(1), j)
                                ^ GF28Multiply(RightMatrix.get(3).get(2), k)
                                ^ GF28Multiply(RightMatrix.get(3).get(3), m);
                        if (v1 == 0 && v2 == 0 && v3 == 0 && v4 == 1) {
                            outcome.add(i);
                            outcome.add(j);
                            outcome.add(k);
                            outcome.add(m);
                            break loop4;
                        }
                    }
                }
            }
        }
        String fileName = num + "80.txt";
        appendListToFile(fileName, outcome);
    }

}

public class GenerationInverseMatrix {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            t thread = new t(i);
            thread.start();
        }
    }
}

