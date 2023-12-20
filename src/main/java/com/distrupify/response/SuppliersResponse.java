package com.distrupify.response;

import com.distrupify.dto.SupplierDTO;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SuppliersResponse {
    public List<SupplierDTO> suppliers;
    public int pageCount;
}
