package fr.istic.m2miage.heybuddy.firebase;

/**
 * Objet représentant la position GPS
 */
public class Position {

    private double latitude;
    private double longitude;

    /**
     * Constructeur par défaut, requis pour appeler DataSnapshot.getValue(Position.class)
     */
    public Position() {

    }

    /**
     * Constructeur
     * @param latitude Latitude de la position GPS
     * @param longitude Longitude de la position GPS
     */
    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Getter
     * @return La latitude de la position GPS
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter
     * @param latitude Nouvelle latitude à affecter à la position GPS
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Getter
     * @return La longitude de la position GPS
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter
     * @param longitude Nouvelle longitude à affecter à la position GPS
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
