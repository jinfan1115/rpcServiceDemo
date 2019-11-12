package com.fyang.service;

import com.fyang.bean.Product;

public class ProductService implements IProductService{


    @Override
    public Product queryById(int id) {
        Product product = new Product();
        product.setId(100);
        product.setName("测试商品");
        product.setPrice(10.2);

        return product;
    }
}
