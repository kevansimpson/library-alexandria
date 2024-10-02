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
//@Entity
//@Table(name = "FORMATS")
public class AvailableFormats implements Serializable, Persistable {
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

//    @ManyToOne(fetch = FetchType.LAZY, optional = true)
//    @JoinColumn(name = "work_id", nullable = true)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private Work work;
//    @Column(name = "work_id")
//    private long workId;

//    @Column(name="format", nullable=true)
//    @Enumerated(EnumType.STRING)
    private WorkFormat workFormat;

//    @Column(name="shipping_cost", scale = 2)
    private BigDecimal shippingCost;

    public AvailableFormats(WorkFormat format) {
        this(format, null);
    }

    public AvailableFormats(WorkFormat format, BigDecimal shippingCost) {
        setWorkFormat(format);
        setShippingCost(shippingCost);
    }
}
