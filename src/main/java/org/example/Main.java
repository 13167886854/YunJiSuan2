package org.example;

import org.example.Controller.TaxiNumberController;


public class Main{
    public static void main( String[] args ) {
        //调用方法
        TaxiNumberController controller = new TaxiNumberController();
        //出租车的数量
        System.out.println("taxi_gps文件的出租车数量");
        String Taxinumber = controller.getNumber();
        System.out.println(Taxinumber);
        System.out.println("-------------------------------------");
        System.out.println();

        System.out.println("北京市各个区的信息");
        //每个城区的信息
        controller.District();
        System.out.println("--------------------------------------");
        System.out.println();

        System.out.println("每个区的车辆位置点数");
        //每个城区的车辆位置数量
        controller.taxiAddress();

    }

}
