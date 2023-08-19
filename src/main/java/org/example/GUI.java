package org.example;

import org.example.lib.library;
import org.example.threeDtwoB.ThreeDTwoB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI extends JFrame {
    private JLabel pLabel, ek1Label, ek2Label, ek3Label;
    private JTextField pTextField, ek1TextField, ek2TextField, ek3TextField,enTextField,deTextField;
    private JButton enButton, deButton;

    public GUI() {
        // 設定視窗標題
        setTitle("Input GUI");

        // 初始化元件
        pLabel = new JLabel("P:");
        ek1Label = new JLabel("EK1:");
        ek2Label = new JLabel("EK2:");
        ek3Label = new JLabel("EK3:");

        pTextField = new JTextField(10);
        ek1TextField = new JTextField(10);
        ek2TextField = new JTextField(10);
        ek3TextField = new JTextField(10);
        enTextField = new JTextField(10);
        deTextField = new JTextField(10);


        enButton = new JButton("EN");
        deButton = new JButton("DE");

        // 設定按鈕事件監聽
        enButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pValue = pTextField.getText();
                String ek1Value = ek1TextField.getText();
                String ek2Value = ek2TextField.getText();
                String ek3Value = ek3TextField.getText();

                List<Integer> P = new ArrayList<>(library.StringToInt(pValue));
                List<Integer> EK1 = new ArrayList<>(library.StringToInt(ek1Value));
                List<Integer> EK2 = new ArrayList<>(library.StringToInt(ek2Value));
                List<Integer> EK3 = new ArrayList<>(library.StringToInt(ek3Value));
                enTextField.setText(library.IntToString(ThreeDTwoB.Encryption(P,EK1,EK2,EK3)));

            }
        });

        deButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String CValue = enTextField.getText();
                String ek1Value = ek1TextField.getText();
                String ek2Value = ek2TextField.getText();
                String ek3Value = ek3TextField.getText();

                List<Integer> C = new ArrayList<>(library.StringToInt(CValue));
                List<Integer> EK1 = new ArrayList<>(library.StringToInt(ek1Value));
                List<Integer> EK2 = new ArrayList<>(library.StringToInt(ek2Value));
                List<Integer> EK3 = new ArrayList<>(library.StringToInt(ek3Value));
                deTextField.setText(library.IntToString(ThreeDTwoB.Decryption(C,EK1,EK2,EK3)));
            }
        });

        // 設定視窗佈局
        setLayout(new GridLayout(5, 2));

        // 加入元件到視窗
        add(pLabel);
        add(pTextField);
        add(ek1Label);
        add(ek1TextField);
        add(ek2Label);
        add(ek2TextField);
        add(ek3Label);
        add(ek3TextField);
        add(enButton);
        add(deButton);
        add(enTextField);
        add(deTextField);

        // 設定視窗大小
        setSize(400, 200);

        // 設定視窗關閉動作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 顯示視窗
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUI();
            }
        });
    }
}

