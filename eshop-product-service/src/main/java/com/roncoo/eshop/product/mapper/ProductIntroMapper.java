package com.roncoo.eshop.product.mapper;

import com.roncoo.eshop.product.model.ProductIntro;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductIntroMapper {

    @Insert("INSERT INTO product_intro(content,product_id) VALUES(#{content},#{productId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void add(ProductIntro productIntro);

    @Update("UPDATE product_intro SET content=#{content},product_id=#{productId} WHERE id=#{id}")
    public void update(ProductIntro productIntro);

    @Delete("DELETE FROM product_intro WHERE id=#{id}")
    public void delete(Long id);

    @Select("SELECT * FROM product_intro WHERE id=#{id}")
    public ProductIntro findById(Long id);

}
