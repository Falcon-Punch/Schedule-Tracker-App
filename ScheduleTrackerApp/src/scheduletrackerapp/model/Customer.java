/*
*    File Name  : Customer.java
*    Author     : Joseph Schell
*    Date       : 12/21/2018
*/
package scheduletrackerapp.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Customer
{
    private final IntegerProperty customerId;
    private final StringProperty name;
    private final IntegerProperty addressId;
    private final StringProperty address;
    private final StringProperty address2;
    private final StringProperty city;
    private final StringProperty postalCode;
    private final StringProperty country;
    private final StringProperty phone;
    

    public Customer(int customerId, String name, int addressId, String address,
            String address2, String city, String postalCode, String phone, String country)
    {
        this.customerId = new SimpleIntegerProperty(customerId);
        this.name = new SimpleStringProperty(name);
        this.addressId = new SimpleIntegerProperty(addressId);
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.city = new SimpleStringProperty(city);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.country = new SimpleStringProperty(country);
        this.phone = new SimpleStringProperty(phone);        
    }

    public int getID()
    {
        return this.customerId.get();
    }

    public IntegerProperty IDProperty()
    {
        return customerId;
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public String getName()
    {
        return this.name.get();
    }

    public StringProperty nameProperty()
    {
        return name;
    }

    public void setAddressID(int addressID)
    {
        this.addressId.set(addressID);
    }

    public int getAddressID()
    {
        return this.addressId.get();
    }

    public IntegerProperty addressIDProperty()
    {
        return addressId;
    }

    public void setAddress(String address)
    {
        this.address.set(address);
    }

    public String getAddress()
    {
        return this.address.get();
    }

    public StringProperty addressProperty()
    {
        return address;
    }

    public void setAddress2(String address2)
    {
        this.address2.set(address2);
    }

    public String getAddress2()
    {
        return this.address2.get();
    }

    public StringProperty address2Property()
    {
        return address2;
    }

    public void setCity(String city)
    {
        this.city.set(city);
    }

    public String getCity()
    {
        return this.city.get();
    }

    public StringProperty cityProperty()
    {
        return city;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode.set(postalCode);
    }

    public String getPostalCode()
    {
        return this.postalCode.get();
    }

    public StringProperty postalCodeProperty()
    {
        return postalCode;
    }

    public void setCountry(String country)
    {
        this.country.set(country);
    }

    public String getCountry()
    {
        return this.country.get();
    }

    public StringProperty countryProperty()
    {
        return country;
    }
    
    public void setPhone(String phone)
    {
        this.phone.set(phone);
    }

    public String getPhone()
    {
        return this.phone.get();
    }

    public StringProperty phoneProperty()
    {
        return phone;
    }
}
