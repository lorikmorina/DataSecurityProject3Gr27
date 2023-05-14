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
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;

public class encryptController {
private String plainText;
private String subkeyString;
    @FXML
    private Button encryptBtn;

    @FXML
    private Button goDecryption;

    @FXML
    private TextArea plainTextField;
    @FXML
    private TextArea cipherTextField;

    @FXML
    private TextField subKeyField;

    @FXML
    void encryptBtnClick(ActionEvent event) throws Exception {
        plainText = plainTextField.getText();
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
        cipher.init(Cipher.ENCRYPT_MODE, key);


        byte[] ciphertext = cipher.doFinal(plainText.getBytes());
        cipherTextField.setText(Hex.encodeHexString(ciphertext));

    }

    @FXML
    void goDecryptionClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("decrypt.fxml"));
        Parent root = loader.load();
        Scene manageScene = new Scene(root);
        Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(manageScene);
    }

}
