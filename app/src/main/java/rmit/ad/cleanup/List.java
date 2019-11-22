package rmit.ad.cleanup;

public class List {
    private String name;
    private String birth;
    private String gender;
    private String phone;

    public List(String name, String birth, String gender, String phone) {
        this.name = name;
        this.birth = birth;
        this.gender = gender;
        this.phone = phone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }
    public void setBirth(String birth) {
        this.birth = birth;
    }

    public  String getGender(){
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}
