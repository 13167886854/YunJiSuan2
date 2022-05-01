package org.example.Bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 经纬度类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiAddress {
    private double longitude;//经度
    private double latitude;//纬度
}
