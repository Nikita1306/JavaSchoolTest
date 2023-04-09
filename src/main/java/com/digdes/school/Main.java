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
           List<Map<String,Object>> result2 = starter.execute("INSERT VALUES 'lastName'='ИванОв','id'=2, 'age'=25,'active'=false,'cost'=4.3");
           List<Map<String,Object>> result3 = starter.execute("INSERT VALUES 'lastName'='Иван', 'id'=3, 'age'=40,'active'=true");
//           List<Map<String,Object>> result4 = starter.execute("INSERT VALUES 'lastName'='dsad','id'=3, 'age'=40,'active'=true,'cost'=5.0");
           //Изменение значения которое выше записывали
           System.out.println(result1);
           System.out.println(result2);
           System.out.println(result3);
           System.out.println("UPDATE:");
           List<Map<String,Object>> result5 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1" +
                   " where id>-1");
           System.out.println("Result of UPDATE:" + result5);
//           //Получение всех данных из коллекции (т.е. в данном примере вернется 1 запись)
//           List<Map<String,Object>> result3 = starter.execute("SELECT");
          
       }catch (Exception ex){
           ex.printStackTrace();
       }
   }
}
