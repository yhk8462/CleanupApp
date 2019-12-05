package rmit.ad.cleanup;

public class mContact {
    private String id;
    private String contact;

    public mContact(String id,String contact) {
        this.id = id;
        this.contact = contact;

    }
    public mContact(){

    }
    public String getId() {
        return id;
    }
    public void setId(String name) {
        this.id = name;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String phone) {
        this.contact = phone;
    }
}
