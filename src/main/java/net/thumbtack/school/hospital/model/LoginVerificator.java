package net.thumbtack.school.hospital.model;

// REVU не нужен такой класс, не усложняйте
public class LoginVerificator {

    private int id;
    private UserType userType;
    private String password;

    public LoginVerificator(int id, UserType userType, String password) {
        this.id = id;
        this.userType = userType;
        this.password = password;
    }

    public LoginVerificator(int id, UserType userType) {
        this.id = id;
        this.userType = userType;
    }

    public LoginVerificator() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
