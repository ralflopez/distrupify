package com.distrupify.auth.dtos;

import com.distrupify.resources.dto.OrganizationDTO;
import lombok.Builder;

@Builder
public class UserDTO {
    public long id;
    public String email;
    public String name;
    public OrganizationDTO organization;
}
