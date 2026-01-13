package Controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class logingControler {


    @FXML
    private Button btnCreateAccount;

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtPassword;

    public static String loggedInFirstName;

    @FXML
    void btnCreateAccountOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/createaccout.fxml"))));
            Stage satge1 = (Stage) txtEmail.getScene().getWindow();
            satge1.close();
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();

    }

    @FXML
    void btnLoginOnAction(ActionEvent event) {
        try {
            Connection con = DBConnection.getConnection();


            PreparedStatement ps = con.prepareStatement(
                    "SELECT frist_name, password FROM users WHERE email=?"
            );
            ps.setString(1, txtEmail.getText());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String dbHash = rs.getString("password");
                String firstName = rs.getString("frist_name");

                if (BCrypt.checkpw(txtPassword.getText(), dbHash)) {

                    loggedInFirstName = firstName;


                    Stage stage = new Stage();
                    stage.setScene(new Scene(
                            FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))
                    ));

                    Stage loginStage = (Stage) txtEmail.getScene().getWindow();
                    loginStage.close();

                    stage.show();
                    new Alert(Alert.AlertType.INFORMATION, "Login successful!").show();

                } else {
                    new Alert(Alert.AlertType.ERROR, "Invalid Password").show();
                }

            } else {
                new Alert(Alert.AlertType.ERROR, "Email not found").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


//        final String correctEmail = "test@gmail.com";
//        final String correctPassword = "test";
//
//        String enteredEmail = txtEmail.getText();
//        String enteredPassword = txtPassword.getText();
//
//        if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
//            new Alert(Alert.AlertType.ERROR,"Please enter both email and password").show();
//        }
//
//        if (enteredEmail.equals(correctEmail) && enteredPassword.equals(correctPassword)) {
//
//
//            Stage stage = new Stage();
//            try {
//                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
//                Stage satge1 = (Stage) txtEmail.getScene().getWindow();
//                satge1.close();
//            } catch (
//                    IOException e) {
//                throw new RuntimeException(e);
//            }
//            stage.show();
//            new Alert(Alert.AlertType.INFORMATION, "Login successful!").show();
//        } else {
//            new Alert(Alert.AlertType.ERROR, "Invalid email or password.").show();
//        }


    }
}
