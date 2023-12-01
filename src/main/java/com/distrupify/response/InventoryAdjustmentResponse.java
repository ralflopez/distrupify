package com.distrupify.response;

import com.distrupify.dto.InventoryAdjustmentDTO;
import lombok.AllArgsConstructor;

import java.util.List;

// TODO: convert response into records
@AllArgsConstructor
public class InventoryAdjustmentResponse {
    public List<InventoryAdjustmentDTO> inventoryAdjustments;
    public int pageCount;
}
