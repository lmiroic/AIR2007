package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "korisnik")
public class Korisnik {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String ime;
    private String prezime;
    private String email;
    private String lozinka;
    private String google_ID;

    public Korisnik()  {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getGoogle_ID() {
        return google_ID;
    }

    public void setGoogle_ID(String google_ID) {
        this.google_ID = google_ID;
    }
}
