package app.entities;

import java.util.Objects;

public class Material {
    private int materialId;
    private String name;
    private String description;
    private double price;
    private int unitId;
    private int width;
    private int length;
    private int height;

    private int quantity;

    public Material(int materialId, String name, String description, double price, int unitId, int width, int length, int height, int quantity) {
        this.materialId = materialId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.unitId = unitId;
        this.width = width;
        this.length = length;
        this.height = height;
        this.quantity = quantity;
    }

    public Material() {
    }
    public Material( String name, String description, double price, int unitId, int width, int length, int height) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.unitId = unitId;
        this.width = width;
        this.length = length;
        this.height = height;
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

    public Material(int materialId, String name, String description, double price) {
        this.materialId = materialId;
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
                ", quantity=" + quantity +
                '}';
    }


    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return materialId == material.materialId && Double.compare(price, material.price) == 0 && unitId == material.unitId && width == material.width && length == material.length && height == material.height && quantity == material.quantity && Objects.equals(name, material.name) && Objects.equals(description, material.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, name, description, price, unitId, width, length, height, quantity);
    }
}