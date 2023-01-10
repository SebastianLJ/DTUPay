
package org.dtu.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>Java class for user complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="user"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="cprNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
public class User {

    Name name;

    UserId userId;

    List<Payment> payments = new ArrayList<Payment>();

    public Name getName() {
        return name;
    }

    public UserId getUserId() {
        return userId;
    }

    public User(String firstName, String lastName) {
        this.userId = new UserId(UUID.randomUUID());
        this.firstName = new firstName;
        this.lastName = new lastName;
    }



    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
