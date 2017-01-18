package fr.istic.m2miage.heybuddy.firebase;

/**
 * Objet représentant un utilisateur
 */
public class User {

    private String username;
    private String email;
    private String numero;
    private String token;

    /**
     * Constructeur par défaut, requis pour appeler DataSnapshot.getValue(User.class)
     */
    public User() {

    }

    /**
     * Constructeur
     * @param username Nom de l'utilisateur
     * @param email Adresse email de l'utilisateur
     */
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    /**
     * Getter
     * @return Nom de l'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter
     * @param username Nouveau nom à affecter à l'utilisateur
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter
     * @return Adresse email de l'utilisateur
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter
     * @param email Nouvelle adresse email de l'utilisateur
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter
     * @return Numéro de téléphone de l'utilisateur
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Setter
     * @param numero Nouveau numéro à affecter à l'utilisateur
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
