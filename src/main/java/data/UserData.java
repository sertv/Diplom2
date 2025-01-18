package data;

public class UserData {

    private String email;
    private String password;
    private String name;
    public static String userEmail = "tut2@yandex.ru";
    public static String userName = "tutu1";
    public static String userPassword = "12345user";
    public static String userIncorrectPassword = "12345";
    public static String userIncorrectEmail = "12345";
    public static String changedEmail = "tut3@yandex.ru";
    public static String changedUserName = "tutut";
    public static String changedUserPassword = "12345user12";

    public UserData(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public UserData (String email, String password){
        this.email = email;
        this.password = password;
    }
    public static String getEmail() {
        return userEmail;
    }

    public void setEmail(String email) {
        this.email = userEmail;
    }

    public static String getPassword() {
        return userPassword;
    }
    public void setPassword(String password) {
        this.password = userPassword;
    }

    public static String getName() {
        return userName;
    }

    public void setName(String name) {
        this.name = userName;
    }

}