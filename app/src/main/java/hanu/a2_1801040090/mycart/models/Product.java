package hanu.a2_1801040090.mycart.models;

public class Product {
    private int id;
    private String thumbnail;
    private String name;
    private int unitPrice;
    private int quantity;

    public Product(int id, String thumbnail, String name, int unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = 1;
    }

    public Product(int id, String thumbnail, String name, int unitPrice, int quantity){
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
