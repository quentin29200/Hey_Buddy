package fr.istic.m2miage.heybuddy.firebase;

/**
 * Created by quentin on 18/01/17.
 */

// Contact
public class Contact {

    private long id;
    private String uid;
    private String name;
    private String lastName;
    private String image;
    private String numero;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getNumero() { return numero; }

    public void setNumero(String numero) { this.numero = numero; }
}