package com.distrupify.resources.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class SupplierEditRequest {
    @NotNull
    public String name;

    @NotNull
    public String address;

    @NotNull
    public String contactNumber;
}
