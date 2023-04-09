package com.digdes.school;

import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Pattern;

public class JavaSchoolStarter {
    //Column names
    private static final String columnId = "'id'";
    private static final String columnLastName = "'lastName'";
    private static final String columnAge = "'age'";
    private static final String columnCost = "'cost'";
    private static final String columnActive = "'active'";
    //Possible operations
    private static final String insert = "insert";
    private static final String update = "update";
    private static final String delete = "delete";
    private static final String select = "select";
    private List<Map<String, Object>> data = new ArrayList<>();

    //Дефолтный конструктор
    public JavaSchoolStarter() {

    }

    //На вход запрос, на выход результат выполнения запроса
    public List<Map<String, Object>> execute(String request) throws Exception {
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

    private List<Map<String, Object>> insertOperation(String request) {
        List<Map<String, Object>> inputRow = new ArrayList<>();
        System.out.println(request);
        Map<String, Object> row = new HashMap<>();
        String[] dataSplitted = request.split(",");
        getUpdatedRows(row, dataSplitted);
        inputRow.add(row);
        data.add(row);
        return inputRow;
    }

    private List<Map<String, Object>> updateOperation(String request) throws ScriptException {
        List<Map<String, Object>> inputRow = new ArrayList<>();
        Map<String, Object> row = new HashMap<>();

        if (!request.toLowerCase().contains("where")) {
            String[] updateRows = request.split(",");
            Set<Integer> resultSet = new HashSet<>();
            getUpdatedRows(row, updateRows);
            for (int i =0; i < data.size(); i++) {
                for (Map.Entry<String,Object> entry : row.entrySet()) {
                        data.get(i).put(entry.getKey(), entry.getValue());
                }
            }
            return data;
        }
        String[] updateRows = request.substring(0, request.toLowerCase().indexOf("where")).split(",");
        Set<Integer> resultSet = new HashSet<>();
        getUpdatedRows(row, updateRows);
        String condition = request.substring(request.toLowerCase().indexOf("where") + 6);
        String[] test = new String[0];
        if (!condition.toLowerCase().contains(" or ") && !condition.toLowerCase().contains(" and ")) {
            System.out.println("FIRST");
            String operation = "";
            String[] newValues = new String[2];
            basicConditionHandler(resultSet, operation, newValues, condition);
            System.out.println(resultSet);
        }
        else
        if (condition.toLowerCase().contains(" or ")) {
            test = condition.split(" (?i)or ");
        } else {
//            test = condition.split(" (?i)and ");
            System.out.println("ELSE");
            String operation = "";
            String[] newValues = new String[2];
            String[] andCond = condition.split(" (?i)and ");
            basicConditionHandler(resultSet, operation, newValues, andCond[0]);
            for (int i = 1; i < andCond.length; i++) {
                System.out.println("AND");
                andHandler(resultSet, operation, newValues, andCond[i]);

            }
        }

        for (String cond : test) {
            String operation = "";
            System.out.println("TEST");
            System.out.println(cond);
//            operation.replace("'", "");
            String[] newValues = new String[2];
            String[] andCond = cond.split(" (?i)and ");
            if (andCond.length > 1) {
                Set<Integer> andSet = new HashSet<>();
                basicConditionHandler(andSet, operation, newValues, andCond[0]);
                for (int i = 1; i < andCond.length; i++) {
                    andHandler(andSet, operation, newValues, andCond[i]);
                    System.out.println(andSet);
                }
                resultSet.addAll(andSet);
            } else {
                basicConditionHandler(resultSet, operation, newValues, cond);
            }


        }
        System.out.println("ROW: " + row);
        List<Map<String, Object>> updateData = new ArrayList<>();
        System.out.println("UPDATING");
        for (Integer integer : resultSet) {
            for (Map.Entry<String,Object> entry : row.entrySet()) {
                System.out.println(entry);
                if (entry.getValue() != null)
                    data.get(integer).put(entry.getKey(), entry.getValue());
                else data.get(integer).remove(entry.getKey());

            }
            updateData.add(new HashMap<>(data.get(integer)));
        }
        System.out.println(resultSet);
        return updateData;
    }

    private void basicConditionHandler(Set<Integer> resultSet, String operation, String[] newValues, String andFromMultiple) {
        String[] splittedAnd = getOperation(andFromMultiple, operation, newValues);
        splittedAnd[1] = splittedAnd[1].equalsIgnoreCase("lastname") ? "lastName" : splittedAnd[1];
        System.out.println(splittedAnd[0]);
        System.out.println(splittedAnd[1]);
        System.out.println(splittedAnd[2]);
        for (int i = 0; i < data.size(); i++) {
            switch (splittedAnd[0]) {
                case ">=" -> {
                    System.out.println(">=");
                    if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]),
                            ">=", splittedAnd[2]))
                        resultSet.add(i);
                }
                case "<=" -> {
                    if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]),
                            "<=", splittedAnd[2]))
                        resultSet.add(i);
                }
                case "=" -> {
                    if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]),
                            "=", splittedAnd[2])) {
                        resultSet.add(i);
                    }
                }
                case "!=" -> {
                    if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]),
                            "!=", splittedAnd[2]))
                        resultSet.add(i);
                }
                case "<" -> {
                    if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]),
                            "<", splittedAnd[2]))
                        resultSet.add(i);
                }
                case ">" -> {
                    if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]),
                            ">", splittedAnd[2]))
                        resultSet.add(i);
                }
                case "like" -> {
                    if (!splittedAnd[2].contains("%")) {
                        if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]),
                                "=", splittedAnd[2]))
                            resultSet.add(i);
                        continue;
                    }
                    if (splittedAnd[2].indexOf("%") == 0 && splittedAnd[2].substring(1).contains("%")) {
                        if (data.get(i).get(splittedAnd[1]).toString()
                                .contains(splittedAnd[2].replace("%", ""))) {
                            resultSet.add(i);
                        }
                    } else if (splittedAnd[2].indexOf("%") == 0) {
                        if (data.get(i).get(splittedAnd[1]).toString().endsWith(splittedAnd[2].replace("%", "")))
                            resultSet.add(i);
                    } else {
                        if (data.get(i).get(splittedAnd[1]).toString().startsWith(splittedAnd[2].replace("%", "")))
                            resultSet.add(i);
                    }
                }
                case "ilike" -> {
                    if (!splittedAnd[2].contains("%")) {
                        if (compareValues(splittedAnd[1], data.get(i).get(splittedAnd[1]).toString().toLowerCase(),
                                "=", splittedAnd[2].toLowerCase()))
                            resultSet.add(i);
                        continue;
                    }
                    if (splittedAnd[2].indexOf("%") == 0 && splittedAnd[2].substring(1).contains("%")) {
                        if (data.get(i).get(splittedAnd[1]).toString().toLowerCase()
                                .contains(splittedAnd[2].toLowerCase().replace("%", ""))) {
                            resultSet.add(i);
                        }
                    } else if (splittedAnd[2].indexOf("%") == 0) {
                        if (data.get(i).get(splittedAnd[1]).toString().toLowerCase().endsWith(splittedAnd[2].toLowerCase().replace("%", "")))
                            resultSet.add(i);
                    } else {
                        if (data.get(i).get(splittedAnd[1]).toString().toLowerCase().startsWith(splittedAnd[2].toLowerCase().replace("%", "")))
                            resultSet.add(i);
                    }
                }
            }

        }
    }

    private void andHandler(Set<Integer> resultSet, String operation, String[] newValues, String andFromMultiple) {
        String[] splittedAnd = getOperation(andFromMultiple, operation, newValues);
        System.out.println(splittedAnd[0]);
        System.out.println(splittedAnd[1]);
        System.out.println(splittedAnd[2]);
        Set<Integer> addSetCopy = new HashSet<>(resultSet);
        for (Integer index : addSetCopy) {
            switch (splittedAnd[0]) {
                case ">=" -> {
                    if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]),
                            ">=", splittedAnd[2]))
                        resultSet.remove(index);
                }
                case "<=" -> {
                    if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]),
                            "<=", splittedAnd[2]))
                        resultSet.remove(index);
                }
                case "=" -> {
                    if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]),
                            "=", splittedAnd[2])) {
                        resultSet.remove(index);
                    }
                }
                case "!=" -> {
                    if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]),
                            "!=", splittedAnd[2]))
                        resultSet.remove(index);
                }
                case "<" -> {
                    if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]),
                            "<", splittedAnd[2]))
                        resultSet.remove(index);
                }
                case ">" -> {
                    if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]),
                            ">", splittedAnd[2]))
                        resultSet.remove(index);
                }
                case "like" -> {
                    if (!splittedAnd[2].contains("%")) {
                        if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]),
                                "=", splittedAnd[2]))
                            resultSet.remove(index);
                        continue;
                    }
                    if (splittedAnd[2].indexOf("%") == 0 && splittedAnd[2].substring(1).contains("%")) {
                        if (!data.get(index).get(splittedAnd[1]).toString()
                                .contains(splittedAnd[2].replace("%", ""))) {
                            resultSet.remove(index);
                        }
                    } else if (splittedAnd[2].indexOf("%") == 0) {
                        if (!data.get(index).get(splittedAnd[1]).toString().endsWith(splittedAnd[2].replace("%", "")))
                            resultSet.remove(index);
                    } else {
                        if (!data.get(index).get(splittedAnd[1]).toString().startsWith(splittedAnd[2].replace("%", "")))
                            resultSet.remove(index);
                    }
                }
                case "ilike" -> {
                    if (!splittedAnd[2].contains("%")) {
                        if (!compareValues(splittedAnd[1], data.get(index).get(splittedAnd[1]).toString().toLowerCase(),
                                "=", splittedAnd[2].toLowerCase()))
                            resultSet.remove(index);
                        continue;
                    }
                    if (splittedAnd[2].indexOf("%") == 0 && splittedAnd[2].substring(1).contains("%")) {
                        if (!data.get(index).get(splittedAnd[1]).toString().toLowerCase()
                                .contains(splittedAnd[2].toLowerCase().replace("%", ""))) {
                            resultSet.remove(index);
                        }
                    } else if (splittedAnd[2].indexOf("%") == 0) {
                        if (!data.get(index).get(splittedAnd[1]).toString().toLowerCase().endsWith(splittedAnd[2].toLowerCase().replace("%", "")))
                            resultSet.remove(index);
                    } else {
                        if (!data.get(index).get(splittedAnd[1]).toString().toLowerCase().startsWith(splittedAnd[2].toLowerCase().replace("%", "")))
                            resultSet.remove(index);
                    }
                }
            }

        }
    }

    private boolean compareValues (String column ,Object value1, String condition, String value2) {
        column = column.toLowerCase();
        System.out.println(value1);
        switch (condition) {
            case ">=" -> {
                switch (column) {
                    case "id", "age" -> {
                        return (Long) value1 >= Long.parseLong(value2.trim());
                    }
                    case "cost" -> {
                        return (Double) value1 >= Double.parseDouble(value2);
                    }
                }
                break;
            }
            case "<=" -> {
                switch (column) {
                    case "id", "age" -> {
                        return (Long) value1 <= Long.parseLong(value2.trim());
                    }
                    case "cost" -> {
                        return (Double) value1 <= Double.parseDouble(value2);
                    }
                }
                break;
            }
            case "=" -> {
                switch (column) {
                    case "id", "age" -> {
                        return (Long) value1 == Long.parseLong(value2);
                    }
                    case "cost" -> {
                        return (Double) value1 == Double.parseDouble(value2);
                    }
                    case "active" -> {
                        return (Boolean) value1 == Boolean.valueOf(value2);
                    }
                    case "lastname" -> {
                        System.out.println("Compare last " + value1 + " " + value2);
                        return value1.equals(value2);
                    }
                }
                break;
            }
            case "!=" -> {
                switch (column) {
                    case "id", "age" -> {
                        return (Long) value1 != Long.parseLong(value2);
                    }
                    case "cost" -> {
                        return (Double) value1 != Double.parseDouble(value2);
                    }
                    case "active" -> {
                        return (Boolean) value1 != Boolean.valueOf(value2);
                    }
                    case "lastname" -> {
                        System.out.println(value1 + " " + value2);
                        return !value1.equals(value2);
                    }
                }
                break;
            }
            case "<" -> {
                switch (column) {
                    case "id", "age" -> {
                        return (Long) value1 < Long.parseLong(value2.trim());
                    }
                    case "cost" -> {
                        return (Double) value1 < Double.parseDouble(value2);
                    }
                }
                break;
            }
            case ">" -> {
                switch (column) {
                    case "id", "age" -> {
                        return (Long) value1 > Long.parseLong(value2.trim());
                    }
                    case "cost" -> {
                        return (Double) value1 > Double.parseDouble(value2);
                    }
                }
                break;
            }
        }
        return true;
    }
    private String[] getOperation(String condition, String operation, String[] newValues) {
        if (condition.contains(">=")) {
            operation = ">=";
            newValues = condition.replace("'", "").split(">=");
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }
        if (condition.contains("<=")) {
            operation = "<=";
            newValues = condition.replace("'", "").split("<=");
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }
        if (condition.contains("=")) {
            operation = "=";
            newValues = condition.replace("'", "").split("=");
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }
        if (condition.contains("!=")) {
            operation = "!=";
            newValues = condition.replace("'", "").split("!=");
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }
        if (condition.contains(">")) {
            operation = ">";
            newValues = condition.replace("'", "").split(">");
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }
        if (condition.contains("<")) {
            operation = "<";
            newValues = condition.replace("'", "").split("<");
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }
        if (condition.contains("ilike")) {
            operation = "ilike";
            newValues = condition.replace("'", "").split("ilike");
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }
        if (condition.contains("like")) {
            operation = "like";
            newValues = condition.replace("'", "").split("like");
            System.out.println("ARRAYS" + Arrays.toString(newValues));
            return new String[]{operation, newValues[0].trim(), newValues[1].trim()};
        }

        return new String[]{operation, newValues[0].trim(), newValues[1].trim()};

    }

    private void getUpdatedRows(Map<String, Object> row, String[] updateRows) {
        for (String i : updateRows) {
            i = i.trim();
            String[] pair = i.split("=");
            switch (pair[0].trim().toLowerCase()) {
                case "'lastname'" -> {
                    if (pair[1].trim().equalsIgnoreCase("null")) {
                        row.put(columnLastName.replace("'", ""), "");
                    } else {
                        row.put(columnLastName.replace("'", ""), pair[1].trim().replace("'", ""));
                    }
                    break;
                }
                case columnId -> {
                    if (pair[1].trim().equalsIgnoreCase("null")) {
                        row.put(columnId.replace("'", ""), null);
                    } else
                        row.put(columnId.replace("'", ""), Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnAge -> {
                    if (pair[1].trim().equalsIgnoreCase("null")) {
                        row.put(columnAge.replace("'", ""), null);
                    } else
                        row.put(columnAge.replace("'", ""), Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnCost -> {
                    if (pair[1].trim().equalsIgnoreCase("null")) {
                        row.put(columnCost.replace("'", ""), null);
                    } else
                        row.put(columnCost.replace("'", ""), Double.valueOf(pair[1].trim()));
                    break;
                }
                case columnActive -> {
                    if (pair[1].trim().equalsIgnoreCase("null")) {
                        row.put(columnActive.replace("'", ""), null);
                    } else
                        row.put(columnActive.replace("'", ""), Boolean.valueOf(pair[1].trim()));
                    break;
                }
            }
        }
    }
}
