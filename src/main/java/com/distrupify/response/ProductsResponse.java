package com.distrupify.response;

import com.distrupify.dto.ProductDTO;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ProductsResponse {
    public List<ProductDTO> products;
    public int pageCount;
}
