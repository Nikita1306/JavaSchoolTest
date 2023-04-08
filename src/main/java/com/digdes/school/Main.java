package com.digdes.school;


import java.util.List;
import java.util.Map;

public class Main {

   public static void main(String... args) {
       JavaSchoolStarter starter = new JavaSchoolStarter();

       try {
           //Вставка строки в коллекцию
           List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'lastName'='Федоров','id'=5, 'age'=40,'active'=false,'cost'=6.5");
//           System.out.println(result1.get(0).get("lastname").getClass());
           List<Map<String,Object>> result2 = starter.execute("INSERT VALUES 'lastName'='ЦЫганюк','id'=3, 'age'=40,'active'=false,'cost'=69");
           List<Map<String,Object>> result3 = starter.execute("INSERT VALUES 'lastName'='фаывпфва','id'=5, 'age'=40,'active'=true,'cost'=5.0");
           List<Map<String,Object>> result4 = starter.execute("INSERT VALUES 'lastName'='dsad','id'=10, 'age'=40,'active'=true,'cost'=5.0");
           //Изменение значения которое выше записывали
           System.out.println(result2);
           System.out.println(result3);
           System.out.println("UPDATE:");
           List<Map<String,Object>> result5 = starter.execute("UPDATE VALUES 'active'=true,'id'=1  where 'id'>=5  or 'age'=40 and 'active'=false");
//           System.out.println("Result of UPDATE:" + result4);
//           //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
//           List<Map<String,Object>> result3 = starter.execute("SELECT");
          
       }catch (Exception ex){
           ex.printStackTrace();
       }
   }
}
