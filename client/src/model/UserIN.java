package model;

public class UserIN {
    private Integer id;
    private String name;

    public UserIN() {
    }

    public UserIN(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Object[] toObject() {
        return new Object[]{this.name};
    }
}