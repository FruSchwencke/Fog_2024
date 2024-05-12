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

    public int getMaterialId() {
        return materialId;
    }

    public int getUnitId() {
        return unitId;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Material(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Material(int materialId, String name, String description, double price, int unitId, int width, int length, int height) {
        this.materialId = materialId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.unitId = unitId;
        this.width = width;
        this.length = length;
        this.height = height;
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
