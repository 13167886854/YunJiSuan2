package org.example.Bean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictBean {
    private String DistrictName;//城区名字
    private double longitude;//经度
    private double latitude;//纬度
    private double Radius;//城区半径
    @Override
    public String toString () {
        return "城区名称为: "+DistrictName+
                " 中心坐标为: ("+longitude+","+latitude+")  " +
                ""+" 半径为: "+Radius+"km\n";
    }

}
