/*
*    File Name  : MessageAlert.java
*    Author     : Joseph Schell
*    Date       : 12/22/2018
*/
package scheduletrackerapp.model;

import javafx.scene.control.Alert;

public class MessageAlert
{
    /*
    *   Sends user an alert to remind them of a specific event
    *
    *   @param: String reminder
    */
    public static void reminderAlert(String reminder)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Appointment Reminder!");
        alert.setContentText(reminder);
        alert.showAndWait();
    }
    
    /*
    *   Sends user an alert about a specific error
    *
    *   @param: String errorMessage
    */
    public static void errorAlert(String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Field(s)!");
        alert.setHeaderText("Please correct the invalid field(s).");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    /*
    *   Sends user an alert that notifies them that no row is selected
    *
    *   @param: String text
    */
    public static void noSelectionAlert(String text)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Nothing is selected!");
        alert.setHeaderText("No " + text + " selected.");
        alert.setContentText("Please select an " + text + " in the table.");
        alert.showAndWait();
    }
}
