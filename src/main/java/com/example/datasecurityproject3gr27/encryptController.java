package com.example.datasecurityproject3gr27;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.ResourceBundle;

public class encryptController implements Initializable {
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
    private ComboBox<String> combo;

    @FXML
    void encryptBtnClick(ActionEvent event) throws Exception {
        plainText = plainTextField.getText();
        subkeyString = subKeyField.getText();

        if (subkeyString.length() < 8) {
            int paddingLength = 8 - subkeyString.length();
            subkeyString = subkeyString + new String(new char[paddingLength]).replace('\0', (char) paddingLength);
            // Display the key in binary format to see the number padding
            for (int i = 0; i < subkeyString.length(); i++) {
                char c = subkeyString.charAt(i);
                String binary = String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0');
                System.out.print(binary + " ");
            }
        }

        byte[] subkey = subkeyString.getBytes(StandardCharsets.UTF_8);
        KeySpec keySpec = new DESKeySpec(subkey);
        byte[] iv = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        SecretKey secretKey = new SecretKeySpec(subkey, "DES");

        String mode = "CBC"; // options: ECB, CBC
        String padding = "PKCS5Padding"; // options: PKCS5Padding,

        Cipher cipher1 = Cipher.getInstance("DES/" + mode + "/" + padding);
        String s = combo.getSelectionModel().getSelectedItem().toString();
        if (s.equals("CBC")) {

            cipher1.init(cipher1.ENCRYPT_MODE, secretKey);
            byte[] ciphertext = cipher1.doFinal(plainText.getBytes());
            cipherTextField.setText(Hex.encodeHexString(ciphertext));
        } else {
            cipher1.init(cipher1.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] ciphertext1 = cipher1.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            cipherTextField.setText(Hex.encodeHexString(ciphertext1));
        }
       // else {
          //  cipher1.init(cipher1.ENCRYPT_MODE, secretKey);
            //byte[] ciphertext = cipher1.doFinal(plainText.getBytes());
            //cipherTextField.setText(Hex.encodeHexString(ciphertext));

    }

    @FXML
    void goDecryptionClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("decrypt.fxml"));
        Parent root = loader.load();
        Scene manageScene = new Scene(root);
        Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        primaryStage.setScene(manageScene);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> list = FXCollections.observableArrayList("ECB", "CBC", "PCKS5Padding");
        combo.setItems(list);
    }
}
