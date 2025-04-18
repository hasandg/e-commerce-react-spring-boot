package com.hasandag.ecommerce.shared.dto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private String id;
    private String userId;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private boolean isDefault;
    private String addressName; // e.g., "Home", "Work", etc.
} 