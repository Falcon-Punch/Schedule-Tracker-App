/*
*    File Name  : User.java
*    Author     : Joseph Schell
*    Date       : 12/22/2018
*/
package scheduletrackerapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class User
{
    private final IntegerProperty userId;
    private final StringProperty userName;
    private final StringProperty password;

    public User(int userId, String username, String password)
    {
        this.userId = new SimpleIntegerProperty(userId);
        this.userName = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public int getID()
    {
        return this.userId.get();
    }

    public IntegerProperty IDProperty()
    {
        return userId;
    }

    public void setUsername(String username)
    {
        this.userName.set(username);
    }

    public String getUsername()
    {
        return this.userName.get();
    }

    public StringProperty usernameProperty()
    {
        return userName;
    }

    public void setPassword(String password)
    {
        this.password.set(password);
    }

    public String getPassword()
    {
        return this.password.get();
    }

    public StringProperty passwordProperty()
    {
        return password;
    }
}
