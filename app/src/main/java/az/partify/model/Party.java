package az.partify.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by az on 21/05/16.
 */
@IgnoreExtraProperties
public class Party {

    public float latitude;
    public float longitude;
    public String name;

    public Party() {

    }

    public Party(float latitude, float longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }
}
