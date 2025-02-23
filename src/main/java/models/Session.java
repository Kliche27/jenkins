package models;

import java.sql.Timestamp;

public class Session {
    private String id ;
    private int userId ;
    private Timestamp login_time ;
    private Timestamp logout_time ;
    private boolean is_active;
    public Session() {}
    public Session(String id, int userId, Timestamp login_time, Timestamp logout_time, boolean is_active) {
        this.id = id;
        this.userId = userId;
        this.login_time = login_time;
        this.logout_time = logout_time;
        this.is_active = is_active;

    }
    public Session (String id ,Timestamp logout_time, boolean is_active) {
        this.id = id;
        this.logout_time = logout_time;
        this.is_active = is_active;
    }
    public Session(int userId, Timestamp login_time, Timestamp logout_time, boolean is_active) {
        this.userId = userId;
        this.login_time = login_time;
        this.logout_time = logout_time;
        this.is_active = is_active;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getLogin_time() {
        return login_time;
    }

    public void setLogin_time(Timestamp login_time) {
        this.login_time = login_time;
    }

    public Timestamp getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(Timestamp logout_time) {
        this.logout_time = logout_time;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "SessionService{" +
                "id=" + id +
                ", userId=" + userId +
                ", login_time=" + login_time +
                ", logout_time=" + logout_time +
                ", is_active=" + is_active +
                '}';
    }
}
