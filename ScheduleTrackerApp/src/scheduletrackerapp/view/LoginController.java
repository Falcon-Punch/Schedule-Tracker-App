/*
*    File Name  : LoginController.java
*    Author     : Joseph Schell
*    Date       : 12/23/2018
*/
package scheduletrackerapp.view;

import scheduletrackerapp.ScheduleTrackerApp;
import scheduletrackerapp.model.User;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoginController
{
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Connection connection;
    private String auth = "";
    private Stage stage;
    
    @FXML private Label usernameLabel;
    @FXML private Label passwordLabel;
    @FXML private TextField usernameTextField;
    @FXML private TextField passwordField;
    @FXML private Button loginButton;

    @FXML private void initialize()
    {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locales/scheduler");
        usernameLabel.setText(resourceBundle.getString("username"));
        passwordLabel.setText(resourceBundle.getString("password"));
        loginButton.setText(resourceBundle.getString("login"));
    }
    
    /*
    *   Sets the connection
    *
    *   @param: Connection connection
    */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }
    
    /*
    *   Sets the stage
    *
    *   @param: Stage stage
    */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
    
    /*
    *   Gets authority
    *
    *   @param: none
    */
    public String getAuth()
    {
        return auth;
    }
    
    /*
    *   Gets users from Database
    *
    *   @param: Stage stage
    */
    private ObservableList<User> getUsers()
    {
        ObservableList<User> users = FXCollections.observableArrayList();
        
        try(Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                "Select userid, userName, password from user");)
        {
            try
            {
                while(resultSet.next())
                {
                    int userID = resultSet.getInt("userid");
                    String username = resultSet.getString("userName");
                    String password = resultSet.getString("password");
                    User user = new User(userID, username, password);
                    users.add(user);
                }
            } 
            catch(SQLException ex)
            {
                Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        
        return users;
    }
    
    /*
    *   Provides response for when the Login button is clicked.
    *
    *   @param: none
    */
    @FXML private void loginButtonClicked() throws IOException
    {
        auth = "";
        // For testing purposes use:
        // Username: test
        // Passowrd: test
        String username = usernameTextField.getText();
        String password = passwordField.getText();
        ObservableList<User> users = getUsers();
        
        for(User user : users)
        {
            if (user.getUsername().equals(username) && user.getPassword().equals(password))
            {
                auth = username;
            }
        }
        
        if("".equals(auth))
        {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("locales/scheduler");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(resourceBundle.getString("title"));
            alert.setHeaderText("");
            alert.setContentText(resourceBundle.getString("message"));
            alert.showAndWait();
        }
        else
        {
            Calendar cal = Calendar.getInstance();
            String time = DATE_FORMAT.format(cal.getTime());
            List<String> lines = Arrays.asList(time + " User: " + username + " logged in.");
            Path file = Paths.get("scheduler_logs.txt");
            
            try
            {
                Files.createFile(file);
            } 
            catch(FileAlreadyExistsException ex)
            {
                // Do not create file, already exists
            }
            
            Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            stage.close();
        }
    }
    
    /*
    *   Provides response for when the Exit button is clicked.
    *
    *   @param: none
    */
    @FXML private void exitButtonClicked()
    {
        stage.close();
    }
    
    /*
    *   Shows all UI settings on the Login stage
    *
    *   @param: Stage primaryStage
    *           Connection connection
    */
    public static String showUI(Stage primaryStage, 
            Connection connection) throws IOException
    {

        // Uncomment one line of code below for language testing purposes
        // Locale.setDefault(new Locale("german", "GERMAN"));
        // Locale.setDefault(new Locale("italian", "ITALIAN"));
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locales/scheduler");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleTrackerApp.class.getResource("view/Login.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage stage = new Stage();
        stage.setTitle(resourceBundle.getString("login"));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        stage.setScene(scene);

        LoginController loginController = loader.getController();
        loginController.setStage(stage);
        loginController.setConnection(connection);

        stage.showAndWait();

        return loginController.getAuth();
    }
    
    
}
