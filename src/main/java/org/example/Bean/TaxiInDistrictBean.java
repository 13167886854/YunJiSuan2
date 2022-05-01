package org.example.Bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 城区类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxiInDistrictBean {
    private String DistrictName;//城区名字
    private Integer TaxiCountInEachDis;//每个城区的出租车点数

    @Override
    public String toString () {
        return DistrictName+": 有车辆位置点数  "+TaxiCountInEachDis+" 个";
    }
}
