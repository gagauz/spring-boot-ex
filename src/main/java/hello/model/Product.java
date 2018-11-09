package hello.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("product")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "product_index", type = "product")
public class Product {
    @Id
    @BsonId
    private String id;
    private String name;
    private double price;
    private int qty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", qty=" + qty + "]";
    }

}
