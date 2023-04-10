package com.digdes.school;


import java.util.List;
import java.util.Map;

public class Main {

   public static void main(String... args) {
       JavaSchoolStarter starter = new JavaSchoolStarter();

       try {
           //Вставка строки в коллекцию
           List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName'='Петров','id'=1, 'age'=30,'active'=true,'cost'=5.4");
//           System.out.println(result1.get(0).get("lastname").getClass());
           List<Map<String,Object>> result2 = starter.execute("INSERT VALUES 'lastName'='Иванов','id'=2, 'age'=25,'active'=false,'cost'=4.3");
           List<Map<String,Object>> result3 = starter.execute("INSERT VALUES 'lastName'='Федоров', 'id'=3, 'age'=40,'active'=true");
//           List<Map<String,Object>> result4 = starter.execute("INSERT VALUES 'lastName'='dsad','id'=3, 'age'=40,'active'=true,'cost'=5.0");
           //Изменение значения которое выше записывали
           System.out.println(result1);
           System.out.println(result2);
           System.out.println(result3);
           System.out.println("UPDATE:");
           List<Map<String,Object>> result5 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=null");
           System.out.println("Result of UPDATE:" + result5);
           List<Map<String,Object>> result7 = starter.execute("SELECT WHERe 'cost'=0");
           System.out.println("Result of SELECT:" + result7);
          
       }catch (Exception ex){
           ex.printStackTrace();
       }
   }
}
