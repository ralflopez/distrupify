package com.distrupify.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierCreateRequest {
    @NotNull
    public String name;

    @NotNull
    public String address;

    @NotNull
    public String contactNumber;
}
