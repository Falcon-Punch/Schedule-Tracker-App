/*
*    File Name  : Type.java
*    Author     : Joseph Schell
*    Date       : 12/22/2018
*/
package scheduletrackerapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Type
{
    private final StringProperty type;
    private final IntegerProperty count;
    
    public Type(int count, String type)
    {
        this.type = new SimpleStringProperty(type);
        this.count = new SimpleIntegerProperty(count);
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

    public void setCount(int count)
    {
        this.count.set(count);
    }
    
    public int getCount()
    {
        return this.count.get();
    }

    public IntegerProperty countProperty()
    {
        return count;
    }
}
