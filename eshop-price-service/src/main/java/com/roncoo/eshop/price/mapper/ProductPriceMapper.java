package com.roncoo.eshop.price.mapper;

import com.roncoo.eshop.price.model.ProductPrice;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductPriceMapper {

    @Insert("INSERT INTO product_price(price,product_id) VALUES(#{price},#{productId})")
    void add(ProductPrice productPrice);

    @Update("UPDATE product_price SET value=#{price},product_id=#{productId} WHERE id=#{id}")
    void update(ProductPrice productPrice);

    @Delete("DELETE FROM product_price WHERE id=#{id}")
    void delete(Long id);

    @Select("SELECT * FROM product_price WHERE id=#{id}")
    ProductPrice findById(Long id);

    @Select("SELECT * FROM product_price WHERE product_id=#{productId}")
    ProductPrice findByProductId(Long productId);

}
