package model;

public class Notification_model {
    int image;
    String tieude;
    String noidung;
    String time;
    String date;

    public Notification_model() {
    }

    public Notification_model(int image, String tieude, String noidung, String time, String date) {
        this.image = image;
        this.tieude = tieude;
        this.noidung = noidung;
        this.time = time;
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
