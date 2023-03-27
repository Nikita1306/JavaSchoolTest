package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {
    private List<Map<String, Object>> data = new ArrayList<>();
    //Column names
    private static final String columnId = "'id'";
    private static final String columnLastName = "'lastname'";
    private static final String columnAge = "'age'";
    private static final String columnCost = "'cost'";
    private static final String columnActive = "'active'";

    //Possible operations
    private static final String insert = "insert";
    private static final String update = "update";
    private static final String delete = "delete";
    private static final String select = "select";
    //Дефолтный конструктор
    public JavaSchoolStarter(){

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String,Object>> execute(String request) throws Exception {
        //Здесь начало исполнения вашего кода
        String operation = request.substring(0, request.indexOf(" "));
        switch (operation.toLowerCase()) {
            case insert: {
                return insertOperation(request.substring(request.toLowerCase().indexOf("values") + 6));
            }
            case update: {
                return updateOperation(request.substring(request.toLowerCase().indexOf("values") + 6));
            }
            case delete: {
                break;
            }
            case select: {
                break;
            }

        }
          return new ArrayList<>();
    }

    private List<Map<String,Object>> insertOperation(String request) {
        List<Map<String, Object>> inputRow = new ArrayList<>();
        System.out.println(request);
        Map<String,Object> row = new HashMap<>();
        String[] dataSplitted = request.split(",");
        for (String i : dataSplitted) {
            i = i.trim();
            String[] pair = i.split("=");
            //TODO: Поменять вставку ключей с констант на обычные (не 'lastname', а lastName и также в других)
            switch(pair[0].trim().toLowerCase()) {
                case columnLastName: {
                    row.put(columnLastName.replace("'", ""), pair[1].trim().replace("'", ""));
                    break;
                }
                case columnId: {
                    row.put(columnId.replace("'", ""),Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnAge: {
                    row.put(columnAge.replace("'", ""),Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnCost: {
                    row.put(columnCost.replace("'", ""),Double.valueOf(pair[1].trim()));
                    break;
                }
                case columnActive: {
                    row.put(columnActive.replace("'", ""),Boolean.valueOf(pair[1].trim()));
                    break;
                }
            }
        }
        inputRow.add(row);
        data.add(row);
        return inputRow;
    }

    private List<Map<String,Object>> updateOperation(String request) {
        List<Map<String, Object>> inputRow = new ArrayList<>();
        System.out.println(request);
        String condition = request.substring(request.toLowerCase().indexOf("where") + 6);
        System.out.println(condition);
        Map<String,Object> row = new HashMap<>();
        String[] updateRows = request.substring(0, request.toLowerCase().indexOf("where")).split(",");
        for (String i : updateRows) {
            i = i.trim();
            String[] pair = i.split("=");
            System.out.println(i);
            //TODO: Поменять вставку ключей с констант на обычные (не 'lastname', а lastName и также в других)
            switch(pair[0].trim().toLowerCase()) {
                case columnLastName: {
                    row.put(columnLastName.replace("'", ""), pair[1].trim().replace("'", ""));
                    break;
                }
                case columnId: {
                    row.put(columnId.replace("'", ""),Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnAge: {
                    row.put(columnAge.replace("'", ""),Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnCost: {
                    row.put(columnCost.replace("'", ""),Double.valueOf(pair[1].trim()));
                    break;
                }
                case columnActive: {
                    row.put(columnActive.replace("'", ""),Boolean.valueOf(pair[1].trim()));
                    break;
                }
            }
        }
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> tempValue = data.get(i);
            if (row.containsKey("active") && row.get("active") != data.get(i).get("active")) {
                tempValue.put("active", row.get("active"));
            }
            if (row.containsKey("cost") && row.get("cost") != data.get(i).get("cost")) {
                tempValue.put("cost", row.get("cost"));
            }
            if (row.containsKey("id") && row.get("id") != data.get(i).get("id")) {
                tempValue.put("id", row.get("id"));
            }
            if (row.containsKey("age") && row.get("age") != data.get(i).get("age")) {
                tempValue.put("age", row.get("age"));
            }
            if (row.containsKey("lastname") && row.get("lastname") != data.get(i).get("lastname")) {
                tempValue.put("lastname", row.get("lastname"));
            }
            data.set(i, tempValue);
        }
//        inputRow.add(row);
//        return inputRow;
        return data;
    }
}
