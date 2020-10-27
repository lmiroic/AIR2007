package air.foi.hr.core.entiteti;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "radnjaDnevnika")
public class RadnjaDnevnika {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String radnja;

    public RadnjaDnevnika() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRadnja() {
        return radnja;
    }

    public void setRadnja(String radnja) {
        this.radnja = radnja;
    }
}
