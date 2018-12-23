/*
*    File Name  : EditCustomerController.java
*    Author     : Joseph Schell
*    Date       : 12/23/2018
*/
package scheduletrackerapp.view;

import scheduletrackerapp.ScheduleTrackerApp;
import scheduletrackerapp.model.MessageAlert;
import scheduletrackerapp.model.Customer;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditCustomerController
{
    private String customerID;
    private String addressID;
    private String userName = "";
    private Connection connection;
    private Stage stage;

    @FXML private TextField nameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField address2TextField;
    @FXML private TextField cityTextField;
    @FXML private TextField postalCodeTextField;
    @FXML private TextField phoneTextField;
    @FXML private TextField countryTextField;

    /*
    *   Retrieves data from Database
    *
    *   @param: String query
    */
    public ResultSet getDataFromDataBase(String query) throws ClassNotFoundException
    {
        ResultSet resultSet = null;
        Statement statement;
        
        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        
        return resultSet;
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
    *   Sets the customer
    *
    *   @param: Customer customer
    */
    public void setCustomer(Customer customer)
    {
        if(customer == null)
        {
            this.customerID = null;
            this.addressID = null;
            nameTextField.setText("");
            addressTextField.setText("");
            address2TextField.setText("");
            cityTextField.setText("");
            postalCodeTextField.setText("");
            phoneTextField.setText("");
            countryTextField.setText("");
        }
        else
        {
            this.customerID = Integer.toString(customer.getID());
            this.addressID = Integer.toString(customer.getAddressID());
            nameTextField.setText(customer.getName());
            addressTextField.setText(customer.getAddress());
            address2TextField.setText(customer.getAddress2());
            cityTextField.setText(customer.getCity());
            postalCodeTextField.setText(customer.getPostalCode());
            phoneTextField.setText(customer.getPhone());
            countryTextField.setText(customer.getCountry());
        }
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
    *   Sets the username
    *
    *   @param: String userName
    */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    /*
    *   Checks if any input from the user is valid or not
    *
    *   @param: none
    */
    private boolean isInputValid()
    {
        String messageError = "";

        if(nameTextField.getText() == null 
                || nameTextField.getText().length() == 0)
        {
            messageError += "No valid name!\n";
        }

        if(countryTextField.getText() == null 
                || countryTextField.getText().length() == 0)
        {
            messageError += "No valid country!\n";
        }

        if(cityTextField.getText() == null 
                || countryTextField.getText().length() == 0)
        {
            messageError += "No valid city!\n";
        }

        if(addressTextField.getText() == null 
                || addressTextField.getText().length() == 0)
        {
            messageError += "No valid address!\n";
        }

        if(messageError.length() == 0)
        {
            return true;
        }
        else
        {
            MessageAlert.errorAlert(messageError);
            return false;
        }
    }
      
    /*
    *   Provides response for when the Save button is clicked.
    *
    *   @param: none
    */
    @FXML private void saveButtonClicked()
    {
        if(isInputValid())
        {
            if(this.customerID == null)
            {
                manageCustomer(true);
            } 
            else
            {
                manageCustomer(false);
            }
        }
    }
    
    /*
    *   Provides response for when the Cancel button is clicked.
    *
    *   @param: none
    */
    @FXML private void cancelButtonClicked()
    {
        stage.close();
    }

    /*
    *   Handles a new Customer event
    *
    *   @param: boolean newCustomer
    */
    private void manageCustomer(boolean newCustomer)
    {
        PreparedStatement preparedStatement;
        
        try
        {
            if(newCustomer)
            {
                int countryId = handleCountry();
                int cityId = handleCity(countryId);
                handleAddress(cityId, false);
                
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO customer (customerName, addressId, "
                        + "active, createDate, createdBy, lastUpdateBy) "
                        + "VALUES (?, ?, 1, CURDATE(), ?, ?)");
            } 
            else
            {
                int countryId = handleCountry();
                int cityId = handleCity(countryId);
                handleAddress(cityId, true);
                
                preparedStatement = connection.prepareStatement(
                        "UPDATE customer SET customerName=?, addressId=?, "
                        + "lastUpdateBy=? WHERE customerid = ?");
            }
            
            preparedStatement.setString(1, nameTextField.getText());
            preparedStatement.setString(2, addressID);
            preparedStatement.setString(3, this.userName);
            
            if(newCustomer)
            {
                preparedStatement.setString(4, this.userName);
            } 
            else
            {
                preparedStatement.setString(4, customerID);
            }
            preparedStatement.execute();
        } 
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        stage.close();
    }

    /*
    *   Handles new Country entry, by entering data into Database
    *
    *   @param: none
    */
    private int handleCountry() throws SQLException
    {
        String country = countryTextField.getText();
        String query
                = "INSERT INTO country (country, createDate, createdBy, lastUpdateBy) "
                + "SELECT ?, CURDATE(), ?, ? FROM country WHERE NOT EXISTS ( "
                + "  SELECT country FROM country WHERE country=? ) LIMIT 1;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, country);
        preparedStatement.setString(2, this.userName);
        preparedStatement.setString(3, this.userName);
        preparedStatement.setString(4, country);
        preparedStatement.execute();

        query = "SELECT countryid FROM country WHERE country=?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, country);

        ResultSet resultSet = preparedStatement.executeQuery();
        int countryId = 0;
        
        if(resultSet.next())
        {
            countryId = resultSet.getInt(1);
        }
        
        return countryId;
    }
    
    /*
    *   Handles new City entry, by entering data into Database
    *
    *   @param: none
    */
    private int handleCity(int countryID) throws SQLException
    {
        String city = cityTextField.getText();
        String query
                = "INSERT INTO city (city, countryId, createDate, createdBy, "
                + "lastUpdateBy) SELECT ?, ?, CURDATE(), ?, ? FROM city "
                + "WHERE NOT EXISTS ( SELECT city FROM city WHERE city=? ) LIMIT 1;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, city);
        preparedStatement.setString(2, Integer.toString(countryID));
        preparedStatement.setString(3, this.userName);
        preparedStatement.setString(4, this.userName);
        preparedStatement.setString(5, city);
        preparedStatement.execute();

        query = "SELECT cityid FROM city WHERE city=?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, city);

        ResultSet resultSet = preparedStatement.executeQuery();
        int cityId = 0;
        
        if(resultSet.next())
        {
            cityId = resultSet.getInt(1);
        }
        return cityId;
    }
    
    /*
    *   Handles new Address entry, by entering data into Database
    *
    *   @param: none
    */
    private void handleAddress(int cityID, boolean update) throws SQLException
    {
        String address = addressTextField.getText();
        String query;
        
        if(update)
        {
            query = "UPDATE address SET address=?, address2=?, cityId=?, "
                    + "postalCode=?, phone=?, lastUpdateBy=? WHERE addressid=?;";
        } 
        else
        {
            query = "INSERT INTO address (address, address2, cityId, postalCode, "
                    + "phone, createDate, createdBy, lastUpdateBy) SELECT ?, ?, "
                    + "?, ?, ?, CURDATE(), ?, ? FROM address WHERE NOT EXISTS ( "
                    + "SELECT address FROM address WHERE address=? ) LIMIT 1;";
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, address);
        preparedStatement.setString(2, address2TextField.getText());
        preparedStatement.setString(3, Integer.toString(cityID));
        preparedStatement.setString(4, postalCodeTextField.getText());
        preparedStatement.setString(5, phoneTextField.getText());
        preparedStatement.setString(6, this.userName);
        
        if(update)
        {
            preparedStatement.setString(7, addressID);
        } 
        else
        {
            preparedStatement.setString(7, this.userName);
            preparedStatement.setString(8, address);
        }
        
        preparedStatement.execute();

        if(!update)
        {
            query = "SELECT addressid FROM address WHERE address=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, address);

            ResultSet resultSet = preparedStatement.executeQuery();
            int id = 0;
            
            if (resultSet.next())
            {
                id = resultSet.getInt(1);
            }
            addressID = Integer.toString(id);
        }
    }
    
    /*
    *   Shows all UI settings on the Customer stage
    *
    *   @param: Stage primaryStage
    *           Connection connection
    *           Customer customer
    *           String title
    *           String userName
    */
    public static void showUI(Stage primaryStage, Connection connection, 
            Customer customer, String title, String userName) 
            throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleTrackerApp.class.getResource("view/EditCustomer.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        stage.setScene(scene);

        EditCustomerController editCustomerController = loader.getController();
        editCustomerController.setStage(stage);
        editCustomerController.setConnection(connection);
        editCustomerController.setUserName(userName);

        if(customer != null)
        {
            editCustomerController.setCustomer(customer);
        } 
        else
        {
            editCustomerController.setCustomer(null);
        }

        stage.showAndWait();
    }
}
