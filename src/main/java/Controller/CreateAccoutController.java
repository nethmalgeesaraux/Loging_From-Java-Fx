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
import java.sql.SQLException;

public class CreateAccoutController {

    @FXML
    private Button btnCreateAccount;

    @FXML
    private Button btnback;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    void btnBackOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml"))));
            Stage satge1 = (Stage) btnback.getScene().getWindow();
            satge1.close();
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();

    }

    @FXML
    void btnCreateAccountOnAction(ActionEvent event) {

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required!").show();
            return;
        }


        if (!isValidEmail(email)) {
            new Alert(Alert.AlertType.ERROR, "Please enter a valid email address!").show();
            return;
        }

        if (!isValidPassword(password)) {
            new Alert(Alert.AlertType.ERROR,
                    "Password must have add Uppercase letter , Lowercase letter and 8 characters").show();
            return;
        }


        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO users (frist_name, last_name, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, hashedPassword);

            stmt.executeUpdate();

            new Alert(Alert.AlertType.INFORMATION, "Account Created Successfully!").show();

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z]){8,}$";
        return password.matches(passwordRegex);
    }


}
