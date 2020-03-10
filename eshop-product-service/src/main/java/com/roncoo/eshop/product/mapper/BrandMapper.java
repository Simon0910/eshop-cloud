package com.roncoo.eshop.product.mapper;

import com.roncoo.eshop.product.model.Brand;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BrandMapper {

    @Insert("INSERT INTO brand(name,description) VALUES(#{name},#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void add(Brand brand);

    @Update("UPDATE brand SET name=#{name},description=#{description} WHERE id=#{id}")
    void update(Brand brand);

    @Delete("DELETE FROM brand WHERE id=#{id}")
    void delete(Long id);

    @Select("SELECT * FROM brand WHERE id=#{id}")
    Brand findById(Long id);

    @Select("SELECT * FROM brand WHERE id in (${ids})")
    List<Brand> findByIds(@Param("ids") String ids);
}
