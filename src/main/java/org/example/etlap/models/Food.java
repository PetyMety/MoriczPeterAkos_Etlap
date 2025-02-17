package org.example.etlap.models;

public class Food {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Integer price;

    public Food(Integer id, String name, String description, String category, Integer price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Food{" +
                "id=" + id +
                ", nev='" + name + '\'' +
                ", leiras='" + description + '\'' +
                ", kategoria=" + category +
                ", ar='" + price + '\'' +
                '}';
    }

}
