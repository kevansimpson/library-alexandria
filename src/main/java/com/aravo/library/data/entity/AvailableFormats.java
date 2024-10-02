package com.aravo.library.data.entity;

import com.aravo.library.data.Persistable;
import com.aravo.library.data.WorkFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvailableFormats implements Serializable, Persistable {
    private long id;
    private long workId;
    private WorkFormat workFormat;
    private BigDecimal shippingCost;

    public AvailableFormats(WorkFormat format) {
        this(format, null);
    }

    public AvailableFormats(WorkFormat format, BigDecimal shippingCost) {
        setWorkFormat(format);
        setShippingCost(shippingCost);
    }
}
