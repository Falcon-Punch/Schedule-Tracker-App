/*
*    File Name  : Appointment.java
*    Author     : Joseph Schell
*    Date       : 12/21/2018
*/
package scheduletrackerapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Appointment
{
    private final IntegerProperty appointmentId;
    private final StringProperty title;
    private final StringProperty type;
    private final StringProperty description;
    private final StringProperty location;
    private final StringProperty contact;
    private final StringProperty URL;
    private final StringProperty customerName;
    private final IntegerProperty customerId;
    private final StringProperty userName;
    private final IntegerProperty userId;
    private final StringProperty start;
    private final StringProperty end;

    public Appointment(int appointmentId, String start, String title, String type, 
            String customerName, int customerId, String userName, int userId, 
            String description, String location, String contact, String URL, String end)
    {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.title = new SimpleStringProperty(title);
        this.type = new SimpleStringProperty(type);
        this.description = new SimpleStringProperty(description);
        this.location = new SimpleStringProperty(location);
        this.contact = new SimpleStringProperty(contact);
        this.URL = new SimpleStringProperty(URL);
        this.customerName = new SimpleStringProperty(customerName);
        this.customerId = new SimpleIntegerProperty(customerId);
        this.userName = new SimpleStringProperty(userName);
        this.userId = new SimpleIntegerProperty(userId);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
    }
    
    public int getID()
    {
        return this.appointmentId.get();
    }

    public IntegerProperty IDProperty()
    {
        return appointmentId;
    }

    public void setTitle(String title)
    {
        this.title.set(title);
    }

    public String getTitle()
    {
        return this.title.get();
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public void setType(String type)
    {
        this.type.set(type);
    }

    public String getType()
    {
        return this.type.get();
    }

    public StringProperty typeProperty()
    {
        return type;
    }

    public void setDescription(String description)
    {
        this.description.set(description);
    }

    public String getDescription()
    {
        return this.description.get();
    }

    public StringProperty descriptionProperty()
    {
        return description;
    }

    public void setLocation(String location)
    {
        this.location.set(location);
    }

    public String getLocation()
    {
        return this.location.get();
    }

    public StringProperty locationProperty()
    {
        return location;
    }

    public void setContact(String contact)
    {
        this.contact.set(contact);
    }

    public String getContact()
    {
        return this.contact.get();
    }

    public StringProperty contactProperty()
    {
        return contact;
    }

    public void setURL(String URL)
    {
        this.URL.set(URL);
    }

    public String getURL()
    {
        return this.URL.get();
    }

    public StringProperty URLProperty()
    {
        return URL;
    }

    public void setCustomerName(String customerName)
    {
        this.customerName.set(customerName);
    }

    public String getCustomerName()
    {
        return this.customerName.get();
    }

    public StringProperty customerNameProperty()
    {
        return customerName;
    }

    public void setCustomerID(int customerID)
    {
        this.customerId.set(customerID);
    }

    public int getCustomerID()
    {
        return this.customerId.get();
    }

    public IntegerProperty customerIDProperty()
    {
        return customerId;
    }

    public void setUserName(String userName)
    {
        this.userName.set(userName);
    }

    public String getUserName()
    {
        return this.userName.get();
    }

    public StringProperty userNameProperty()
    {
        return userName;
    }

    public void setUserID(int userID)
    {
        this.userId.set(userID);
    }

    public int getUserID()
    {
        return this.userId.get();
    }

    public IntegerProperty userIDProperty()
    {
        return userId;
    }
    
    public void setStart(String start)
    {
        this.start.set(start);
    }

    public String getStart()
    {
        return this.start.get();
    }
    
    public StringProperty startProperty()
    {
        return start;
    }
    
    public void setEnd(String end)
    {
        this.end.set(end);
    }

    public String getEnd()
    {
        return this.end.get();
    }

    public StringProperty endProperty()
    {
        return end;
    }
}
