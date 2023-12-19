package com.distrupify.response;

import com.distrupify.dto.SalesDTO;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class SalesResponse {
    public List<SalesDTO> sales;
    public int pageCount;
}
