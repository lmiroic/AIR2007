package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity (tableName = "korisnik")
public class Korisnik {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;
    @SerializedName("ime")
    private String ime;
    @SerializedName("prezime")
    private String prezime;
    @SerializedName("email")
    private String email;
    @SerializedName("lozinka")
    private String lozinka;
    @SerializedName("google_ID")
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

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", email='" + email + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", google_ID='" + google_ID + '\'' +
                '}';
    }
}
