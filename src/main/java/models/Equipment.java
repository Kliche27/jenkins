package models;

public class Equipment {
    private int id;
    private String name;
    private String type;
    private String status;
    private int quantity;
    private String description;
    public Equipment() {

    }

    public Equipment(String name, String type, String status, int quantity, String description) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.quantity = quantity;
        this.description = description;
    }

    public Equipment(int id, String name, String type, String status, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.status = status;
        this.quantity = quantity;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Equipment "+ id + " " + name + " " + type + " " + status + " " + quantity + " " + description;
    }
}
