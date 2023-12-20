package com.distrupify.requests;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SupplierEditRequest {
    public String name;

    public String address;

    public String contactNumber;
}
