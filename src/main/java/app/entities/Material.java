package app.entities;

public class Material {

    private int materialId;
    private String name;
    private String description;
    private double price;
    private int unitId;
    private int width;
    private int length;
    private int height;

    public Material(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Material{" +
                "materialId=" + materialId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", unitId=" + unitId +
                ", width=" + width +
                ", length=" + length +
                ", height=" + height +
                '}';
    }
}
