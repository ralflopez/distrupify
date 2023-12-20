package com.distrupify.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierCreateRequest {
    public String name;

    public String address;

    public String contactNumber;
}
