package com.example.datasecurityproject3gr27;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

public class decryptController {
private String cipherText;
private String subkeyString;
    @FXML
    private Button decryptBtn;

    @FXML
    private Button goEncryption;

    @FXML
    private TextArea cipherTextField;
    @FXML
    private TextArea plainTextField;

    @FXML
    private TextField subKeyField;

    @FXML
    void decryptBtnClick(ActionEvent event) throws Exception {
        cipherText = cipherTextField.getText();
        subkeyString = subKeyField.getText();

        if (subkeyString.length() < 8) {
            int paddingLength = 8 - subkeyString.length();
            subkeyString = subkeyString + new String(new char[paddingLength]).replace('\0', (char)paddingLength);
            // Display the key in binary format to see the number padding
            for (int i = 0; i < subkeyString.length(); i++) {
                char c = subkeyString.charAt(i);
                String binary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
                System.out.print(binary + " ");
            }
        }
        byte[] subkey = subkeyString.getBytes(StandardCharsets.UTF_8);
        KeySpec keySpec = new DESKeySpec(subkey);
        //IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        System.out.println("test");
        cipher.init(Cipher.DECRYPT_MODE, key);
        Object Hex;
        byte[] decrypted = cipher.doFinal(Hex.decodeHex(cipherText.toCharArray()));
        plainTextField.setText(new String(decrypted));

    }

    @FXML


}
