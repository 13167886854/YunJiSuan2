package org.example.Controller;

import org.example.Bean.DistrictBean;
import org.example.Bean.TaxiInDistrictBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.*;

public class TaxiNumberController {
    private double EARTH_R = 6371.137;//地球半径
    private FileReader taxiReader;
    private FileReader districtReader;
    private BufferedReader taxiBr;
    private BufferedReader districtBr;
    private String config = "ApplicationContext.xml";
    ApplicationContext context = new ClassPathXmlApplicationContext(config);
    //出租车的数量
    public String getNumber () {
        File file = (File) context.getBean("taxi_gps");
        ArrayList<String> taxiList = new ArrayList<>();//出租车的数量
        try {
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String s = null;
            int taxiCount = 0;
            while ((s = br.readLine()) != null) {
                String[] split = s.split(",");
                for (int i = 0; i < split.length; i++) {
                    int taxiNumber = Integer.parseInt(split[0]);
                    if (!taxiList.contains(split[0])) {//去重，如果集合中包含了这个数据，就继续下一步循环
                        taxiList.add(split[0]);//如果没有包含这个车辆编号，则将车辆编号添加的集合中
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "该出租车GPS数据文件包含" + taxiList.size() + "量车";
    }

    //北京城区的区
    public void District () {
        File district = (File) context.getBean("district");
        ArrayList<DistrictBean> districtBeans = new ArrayList<>();
        List<Map<String, DistrictBean>> districtMap = new ArrayList<>();
        try {
            FileReader reader = new FileReader(district);
            BufferedReader br = new BufferedReader(reader);
            int i = 1;
            String s = null;
            while ((s = br.readLine()) != null) {
                String[] split = s.split(",");
                org.example.Bean.DistrictBean districtBean = new DistrictBean(split[0],
                        (double) Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                        Double.parseDouble(split[3]));
                districtBeans.add(districtBean);
                Map<String, DistrictBean> map = new HashMap<>();
                map.put("D" + i + "区", districtBean);
                districtMap.add(map);
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i = 1;
        for (DistrictBean bean : districtBeans) {
            System.out.println("D" + i + "区: " + bean.getDistrictName() +
                    " 中心坐标为: (" + bean.getLongitude() + "," + bean.getLatitude() +
                    ") 半径为: " + bean.getRadius() + "km");
            i++;
        }

    }
    //每个城区的车辆位置点数
    public void taxiAddress () {
        List<Map<String, List<String>>> TaxiInDList = new ArrayList<>();//Map中的第一个参数是城区名字，第二参数是在这个区内车辆的集合数量构成的集合
        List<Map<String, Integer>> maps = new ArrayList<>();//Map中的第一个参数是城区名字，第二个参数是出租车的位置点数
        List<TaxiInDistrictBean> taxiInDistrictBeanList = new ArrayList<>();
        File taxi = (File) context.getBean("taxi_gps");
        File district = (File) context.getBean("district");
        try {

            districtReader = new FileReader(district);
            districtBr = new BufferedReader(districtReader);
            //将读取的数据全部存放到数组中
            String districtS = null;
            while ((districtS = districtBr.readLine()) != null) {
                taxiReader = new FileReader(taxi);
                taxiBr = new BufferedReader(taxiReader);
                List<String> taxiList = new ArrayList<>();
                //                    城区经纬度数据的下表分别是1，2
                String[] splitDistrict = districtS.split(",");
                String DistrictName = splitDistrict[0];//城区的名字
                double district_longitude = Double.parseDouble(splitDistrict[1]);//城区的经度
                double district_latitude = Double.parseDouble(splitDistrict[2]);//陈回去的纬度
                double district_Radius = Double.parseDouble(splitDistrict[3]);
                String taxiS = null;
                while ((taxiS = taxiBr.readLine()) != null) {
                    //出租车数据经纬度的下标分别是4，5
                    String[] splitTaxi = taxiS.split(",");
                    String taxiNumber = splitTaxi[0];//获取出租车的编号
                    double taxi_longitude = Double.parseDouble(splitTaxi[4]);//获取出租车的经度
                    double taxi_latitude = Double.parseDouble(splitTaxi[5]);//获取出租车的纬度
//                    double longitude = taxi_longitude - district_longitude;
//                    double latitude = taxi_latitude - district_latitude;
//                    double distance1 = Math.hypot(longitude, latitude);
//                    //知道了经纬度的差
//                    double x = (taxi_longitude - district_longitude) * Math.PI * EARTH_R *
//                            Math.cos(((taxi_latitude - district_latitude) / 2)
//                                    * Math.PI / 100) / 180;
//                    double y = (taxi_latitude - district_latitude) * Math.PI * EARTH_R / 180;
//                    double r = Math.hypot(x, y) / 1000;//x与y之间的距离
//                    double distance = 0;
//                    double rad_taxi_latitude = getRad(taxi_latitude);
//                    double rad_district_latitude = getRad(district_latitude);
//                    double dis1 = rad_taxi_latitude - rad_district_latitude;
//                    double dis2 = getRad(taxi_longitude) - getRad(district_longitude);
//                    distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dis1 / 2), 2) +
//                            Math.cos(taxi_latitude) * Math.cos(district_latitude) *
//                                    Math.pow(Math.sin(dis2 / 2), 2)));
//                    distance = distance * EARTH_R;
//                    distance = Math.round(distance * 10000d) / 10000d;
                    double mindistance = getDistance(district_longitude,district_latitude,taxi_longitude,taxi_latitude);
                    if (mindistance < district_Radius) {
                        taxiList.add(taxiNumber);
                    }

                }
                Map<String, List<String>> districtmap = new HashMap<>();
                districtmap.put(DistrictName, taxiList);
                TaxiInDList.add(districtmap);
                Map<String, Integer> map = new HashMap<>();
                map.put(DistrictName, taxiList.size());
                maps.add(map);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for (Map<String, Integer> map : maps) {
            Set<String> strings = map.keySet();
            for (String s : strings) {
                TaxiInDistrictBean districtBean = new TaxiInDistrictBean(s, map.get(s));
                taxiInDistrictBeanList.add(districtBean);
            }
        }
        for (TaxiInDistrictBean inDistrictBean : taxiInDistrictBeanList) {
            System.out.println(inDistrictBean);
        }
//        System.out.println(taxiInDistrictBeanList.toString());
//        return taxiInDistrictBeanList.toString();
    }

    //将经纬度转换成弧度
    public double getRad (double x) {
        return x * Math.PI / 180;
    }

        private static final double EARTH_RADIUS = 6378.137;
        public double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
            double Lat1 = rad(latitude1);
            double Lat2 = rad(latitude2);
            double a = Lat1 - Lat2;
            double b = rad(longitude1) - rad(longitude2);
            double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                    + Math.cos(Lat1) * Math.cos(Lat2)
                    * Math.pow(Math.sin(b / 2), 2)));
            s = s * EARTH_RADIUS;
            s = Math.round(s * 10000d) / 10000d;
            return s;
        }
        private double rad(double d) {
            return d * Math.PI / 180.0;
        }




}

