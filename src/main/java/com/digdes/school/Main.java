package com.digdes.school;


import java.util.List;
import java.util.Map;

public class Main {

   public static void main(String... args) {
       JavaSchoolStarter starter = new JavaSchoolStarter();
       try {
           List<Map<String,Object>> result1 = starter.execute("insert VALuES 'lastname'='Петров','id'=1, 'age'=30,'active'=true,'cost'=5.4");
           List<Map<String,Object>> result2 = starter.execute("INSERT VALUES 'lastName'='Иванов','id'=2, 'age'=25,'active'=false,'cost'=4.3");
           List<Map<String,Object>> result3 = starter.execute("INSERT VALUES 'lastName'='Федоров', 'id'=3, 'age'=40,'active'=true");
           System.out.println(result1);
           System.out.println(result2);
           System.out.println(result3);
           List<Map<String,Object>> result5 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id' = 3");
           System.out.println(result5);
           List<Map<String,Object>> result6 = starter.execute("UPDATE VALUES 'active'=true where 'active' = false");
           System.out.println(result6);
           List<Map<String,Object>> result7 = starter.execute("SELECT where 'age'>=30 and 'lastname' ilike '%п%'");
           System.out.println(result7);
           List<Map<String,Object>> result8 = starter.execute("delete ");
           System.out.println(result8);
           List<Map<String,Object>> result9 = starter.execute("SELECT");
           System.out.println(result9);
       }catch (Exception ex){
           ex.printStackTrace();
       }
   }
}
