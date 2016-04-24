package loc.developer.vladimiry.sunshine.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 20.04.2016.
 */
public class OpenWeatherMapParam implements Parcelable {

    private String location;
    private String mode;
    private String units;
    private int cnt;
    private String appid;

    /**
     *
     * @param q
     * @param mode
     * @param units
     * @param cnt
     * @param appid
     */
    public OpenWeatherMapParam(String location, String mode, String units, int cnt, String appid)
    {
        this.location = location;
        this.mode = mode;
        this.units = units;
        this.cnt = cnt;
        this.appid = appid;
    }

    //region Parcelable

    public static final Creator<OpenWeatherMapParam> CREATOR = new Creator<OpenWeatherMapParam>() {
        public OpenWeatherMapParam createFromParcel(Parcel in) { return new OpenWeatherMapParam(in); }
        public OpenWeatherMapParam[] newArray(int size) { return new OpenWeatherMapParam[size]; }
    };


    private OpenWeatherMapParam(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        location = in.readString();
        mode = in.readString();
        units = in.readString();
        cnt = in.readInt();
        appid = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.location);
        dest.writeString(this.mode);
        dest.writeString(this.units);
        dest.writeInt(this.cnt);
        dest.writeString(this.appid);
    }
    //endregion

    //region Getter and Setter

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    //endregion
}
