/*
*    File Name  : AppointmentController.java
*    Author     : Joseph Schell
*    Date       : 12/23/2018
*/
package scheduletrackerapp.view;

import scheduletrackerapp.ScheduleTrackerApp;
import scheduletrackerapp.model.MessageAlert;
import scheduletrackerapp.model.Appointment;
import scheduletrackerapp.model.DateTime;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AppointmentController
{
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String appointmentID;
    private String userName = "";
    private Connection connection;
    private Stage stage;
    
    @FXML private TextField titleTextField;
    @FXML private TextField typeTextField;
    @FXML private TextField descriptionTextField;
    @FXML private TextField locationTextField;
    @FXML private TextField contactTextField;
    @FXML private TextField URLTextField;
    @FXML private TextField startTextField;
    @FXML private TextField endTextField;
    @FXML private ComboBox<String> userNameComboBox;
    @FXML private ComboBox<String> customerNameComboBox;

    /*
    *   Handles a new appointment event
    *
    *   @param: Boolean newAppointment
    */
    private void manageAppointment(boolean newAppointment)
    {
        PreparedStatement preparedStatement;
        
        try
        {
            String userID = getID("user", "userid", "userName", 
                    userNameComboBox.getValue());
            String customerID = getID("customer", "customerid", 
                    "customerName", customerNameComboBox.getValue());
            
            if(newAppointment)
            {
                preparedStatement = connection.prepareStatement(
                        "INSERT INTO appointment (customerId, userId, title, "
                        + "description, location, contact, type, url, start, end, "
                        + "createDate, createdBy, lastUpdateBy) VALUES "
                        + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), ?, ?)");
            } 
            else
            {
                preparedStatement = connection.prepareStatement(
                        "UPDATE appointment SET customerId=?, userId=?, title=?, "
                        + "description=?, location=?, contact=?, type=?, url=?, "
                        + "start=?, end=?, lastUpdateBy=? WHERE appointmentid = ?");
            }

            preparedStatement.setString(1, customerID);
            preparedStatement.setString(2, userID);
            preparedStatement.setString(3, titleTextField.getText());
            preparedStatement.setString(4, descriptionTextField.getText());
            preparedStatement.setString(5, locationTextField.getText());
            preparedStatement.setString(6, contactTextField.getText());
            preparedStatement.setString(7, typeTextField.getText());
            preparedStatement.setString(8, URLTextField.getText());
            preparedStatement.setString(9, DateTime.setDateUTC(startTextField.getText()));
            preparedStatement.setString(10, DateTime.setDateUTC(endTextField.getText()));
            preparedStatement.setString(11, this.userName);
            
            if(newAppointment)
            {
                preparedStatement.setString(12, this.userName);
            } 
            else
            {
                preparedStatement.setString(12, this.appointmentID);
            }
            
            preparedStatement.execute();
            
        } 
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        stage.close();
    }

    /*
    *   Provides response for when the Save button is clicked.
    *
    *   @param: none
    */
    @FXML private void saveButtonClicked() throws ClassNotFoundException
    {
        
        if(this.appointmentID == null)
        {
            if(isInputValid())
            {
                manageAppointment(true);
            }
        } 
        else
        {
            if(isEditValid())
            {
                manageAppointment(false);
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
    *   Sets the appointment cells to the obtained data from the Database
    *
    *   @param: Appointment appointment
    */
    public void setAppointment(Appointment appointment)
    {
        if(appointment == null)
        {
            this.appointmentID = null;
            titleTextField.setText("");
            descriptionTextField.setText("");
            locationTextField.setText("");
            contactTextField.setText("");
            typeTextField.setText("");
            URLTextField.setText("");
            startTextField.setText("");
            endTextField.setText("");
        } 
        else
        {
            this.appointmentID = Integer.toString(appointment.getID());
            titleTextField.setText(appointment.getTitle());
            descriptionTextField.setText(appointment.getDescription());
            locationTextField.setText(appointment.getLocation());
            contactTextField.setText(appointment.getContact());
            typeTextField.setText(appointment.getType());
            URLTextField.setText(appointment.getURL());
            startTextField.setText(appointment.getStart());
            endTextField.setText(appointment.getEnd());
        }
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
    *   Sets the Stage
    *
    *   @param: Stage stage
    */
    public void setStage(Stage stage)
    {
        this.stage = stage;
    }
    
    /*
    *   Sets the Username
    *
    *   @param: String userName
    */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /*
    *   Fills combo boxes with options for user to select when creating new appointment
    *
    *   @param: Appointment appointment
    */    
    private void fillComboBoxes(Appointment appointment) throws ClassNotFoundException
    {
        userNameComboBox.getItems().clear();
        userNameComboBox.getItems().addAll(getComboBoxItems("user", "userName"));

        customerNameComboBox.getItems().clear();
        customerNameComboBox.getItems().addAll(getComboBoxItems("customer", "customerName"));

        if(appointment == null)
        {
            userNameComboBox.getSelectionModel().select(0);
            customerNameComboBox.getSelectionModel().select(0);
        } 
        else
        {
            userNameComboBox.getSelectionModel().select(appointment.getUserName());
            customerNameComboBox.getSelectionModel().select(appointment.getCustomerName());
        }
    }
    
    /*
    *   Gets ID from Database
    *
    *   @param: String table
    *           String idCol
    *           String nameCol
    *           String name
    */
    private String getID(String table, String idCol, String nameCol, 
            String name) throws SQLException
    {
        PreparedStatement preparedStatement;
        preparedStatement = connection.prepareStatement("SELECT " + idCol 
                + " FROM " + table + " WHERE " + nameCol + "='" + name + "' ");
        ResultSet resultSet = preparedStatement.executeQuery();
        
        int id = 0;
        
        if(resultSet.next())
        {
            id = resultSet.getInt(1);
        }
        
        return Integer.toString(id);
    }
    
    /*
    *   Checks if any input from the user is valid or not, during an entry addition
    *
    *   @param: none
    */
    private boolean isInputValid() throws ClassNotFoundException
    {
        String errorMessage = "";
        
        try
        {
            Date start = DATE_FORMAT.parse(startTextField.getText());
            Date end = DATE_FORMAT.parse(endTextField.getText());
            
            if(end.compareTo(start) <= 0)
            {
                errorMessage += "The end time must be after the start time!\n";
            }

            if(checkBusinessHours(startTextField.getText()))
            {
                errorMessage += "Meeting must start during buisness hours:\n"
                        + "8:00 AM to 5:00 PM and Monday through Friday only!\n";
            }

            if(checkBusinessHours(endTextField.getText()))
            {
                errorMessage += "Meeting must end during buisness hours:\n"
                        + "8:00 AM to 5:00 PM and Monday through Friday only!\n";
            }

            if(checkApptOverlap())
            {
                errorMessage += "An appointment has already been scheduled "
                        + "during this time!\n";
            }
        } 
        catch(ParseException ex)
        {
            errorMessage += "Invalid start or end date\n"
                    + "(please use proper format: yyyy-MM-dd HH:mm:ss)!\n";
        }

        if(errorMessage.length() == 0)
        {
            return true;
        } 
        else
        {
            MessageAlert.errorAlert(errorMessage);
            return false;
        }
    }

    /*
    *   Checks if any input from the user is valid or not, during an entry edit
    *
    *   @param: none
    */
    private boolean isEditValid() throws ClassNotFoundException
    {
        String errorMessage = "";
        
        try
        {
            Date start = DATE_FORMAT.parse(startTextField.getText());
            Date end = DATE_FORMAT.parse(endTextField.getText());
            
            if(end.compareTo(start) <= 0)
            {
                errorMessage += "The end time must be after the start time!\n";
            }

            if(checkBusinessHours(startTextField.getText()))
            {
                errorMessage += "Meeting must start during buisness hours:\n"
                        + "8:00 AM to 5:00 PM and Monday through Friday only!\n";
            }

            if(checkBusinessHours(endTextField.getText()))
            {
                errorMessage += "Meeting must end during buisness hours:\n"
                        + "8:00 AM to 5:00 PM and Monday through Friday only!\n";
            }
        } 
        catch(ParseException ex)
        {
            errorMessage += "Invalid start or end date\n"
                    + "(please use proper format: yyyy-MM-dd HH:mm:ss)!\n";
        }

        if(errorMessage.length() == 0)
        {
            return true;
        } 
        else
        {
            MessageAlert.errorAlert(errorMessage);
            return false;
        }
    }
    
    /*
    *   Checks a specific time is within the business hours of 8AM to 5PM
    *
    *   @param: String time
    */    
    private boolean checkBusinessHours(String time) throws ParseException
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DATE_FORMAT.parse(time));
        Calendar morning = Calendar.getInstance();
        morning.setTime(DATE_FORMAT.parse(time));
        Calendar night = Calendar.getInstance();
        night.setTime(DATE_FORMAT.parse(time));

        morning.set(Calendar.HOUR, 7);
        morning.set(Calendar.MINUTE, 59);
        morning.set(Calendar.AM_PM, Calendar.AM);

        night.set(Calendar.HOUR, 5);
        night.set(Calendar.MINUTE, 01);
        night.set(Calendar.AM_PM, Calendar.PM);

        return cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY 
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY 
                || cal.before(morning) || cal.after(night);
    }

    /*
    *   Checks if a new appointment is trying to overlap with a previously
    *   scheduled appointment
    *
    *   @param: none
    */
    private boolean checkApptOverlap() throws ClassNotFoundException, ParseException
    {
        boolean overlap = false;
        ObservableList<Appointment> calendar = FXCollections.observableArrayList();
        calendar.addAll(getAppointments(true));
        calendar.addAll(getAppointments(false));

        for(Appointment appointment : calendar)
        {
            try
            {
                Calendar starttime = Calendar.getInstance();
                starttime.setTime(DATE_FORMAT.parse(startTextField.getText()));
                Calendar endtime = Calendar.getInstance();
                endtime.setTime(DATE_FORMAT.parse(endTextField.getText()));
                Calendar morning = Calendar.getInstance();
                morning.setTime(DATE_FORMAT.parse(appointment.getStart()));
                Calendar night = Calendar.getInstance();
                night.setTime(DATE_FORMAT.parse(appointment.getEnd()));

                if(((starttime.compareTo(morning) >= 0) 
                        && (starttime.compareTo(night) <= 0)) 
                        || ((endtime.compareTo(morning) >= 0) 
                        && (endtime.compareTo(night) <= 0)))
                {
                    overlap = true;
                } 
            } 
            catch(ParseException ex)
            {
                Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return overlap;
    }

    /*
    *   Gets appointment from the Database
    *
    *   @param: boolean customers
    */
    private ObservableList<Appointment> getAppointments(boolean customers) 
            throws ClassNotFoundException, ParseException
    {
        ObservableList<Appointment> calendar = FXCollections.observableArrayList();
        ResultSet resultSet;
        
        if(customers)
        {
            resultSet = getDataFromDB("SELECT appointmentid, start, title, type, "
                    + "customerName, appointment.customerId, userName, appointment.userId, "
                    + "description, location, contact, url, end FROM appointment, customer, user "
                    + "WHERE customer.customerName='" + customerNameComboBox.getValue() + "' "
                    + "AND appointment.userId = user.userid AND appointment.customerId "
                    + "= customer.customerid ORDER BY start");
        } 
        else
        {
            resultSet = getDataFromDB("SELECT appointmentid, start, title, type, "
                    + "customerName, appointment.customerId, userName, appointment.userId, "
                    + "description, location, contact, url, end FROM appointment, customer, user "
                    + "WHERE user.userName='" + userNameComboBox.getValue() + "' AND "
                    + "appointment.userId = user.userid AND appointment.customerId = "
                    + "customer.customerid ORDER BY start");
        }

        try
        {
            while(resultSet.next())
            {
                int apptID = resultSet.getInt("appointmentid");
                String start = DateTime.setDateLocal(resultSet.getString("start"));
                String title = resultSet.getString("title");
                String type = resultSet.getString("type");
                String customerName = resultSet.getString("customerName");
                int customerID = resultSet.getInt("customerId");
                String username = resultSet.getString("userName");
                int userId = resultSet.getInt("userId");
                String description = resultSet.getString("description");
                String location = resultSet.getString("location");
                String contact = resultSet.getString("contact");
                String URL = resultSet.getString("url");
                String end = DateTime.setDateLocal(resultSet.getString("end"));
                Appointment appointment = new Appointment(apptID, start, title, type, 
                        customerName, customerID, username, userId, description, 
                        location, contact, URL, end);
                calendar.add(appointment);
            }
        } 
        catch(SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return calendar;
    }

    /*
    *   Provides reaction to combo box selections by getting data 
    *   from Database for specific items
    *
    *   @param: String table
    *           String column
    */
    public ArrayList<String> getComboBoxItems(String table, 
            String column) throws ClassNotFoundException
    {
        ResultSet resultSet = getDataFromDB("SELECT " + column 
                + " FROM " + table);
        ArrayList<String> items = new ArrayList<>();
        
        try
        {
            while(resultSet.next())
            {
                String item = resultSet.getString(column);
                items.add(item);
            }
        } 
        catch(SQLException ex)
        {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return items;
    }

    /*
    *   Retrieves data from Database
    *
    *   @param: String query
    */
    public ResultSet getDataFromDB(String query) throws ClassNotFoundException
    {
        Statement statement;
        ResultSet resultSet = null;
        
        try
        {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } 
        catch(SQLException ex)
        {
            ex.printStackTrace();
        }
        
        return resultSet;
    }

    /*
    *   Shows all UI settings on the Appointment stage
    *
    *   @param: Stage primaryStage
    *           Connection connection
    *           Appointment appointment
    *           String title
    *           String userName
    */
    public static void showUI(Stage primaryStage, Connection connection, 
            Appointment appointment, String title, String userName) 
            throws IOException, ClassNotFoundException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ScheduleTrackerApp.class.getResource(
                "view_controller/Appointment.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        stage.setScene(scene);

        AppointmentController appointmentController = loader.getController();
        appointmentController.setStage(stage);
        appointmentController.setConnection(connection);
        appointmentController.setUserName(userName);

        if(appointment != null)
        {
            appointmentController.setAppointment(appointment);
            appointmentController.fillComboBoxes(appointment);
        } 
        else
        {
            appointmentController.setAppointment(null);
            appointmentController.fillComboBoxes(null);
        }

        stage.showAndWait();
    }
}
