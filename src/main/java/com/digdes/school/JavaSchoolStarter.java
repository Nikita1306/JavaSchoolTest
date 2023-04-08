package com.digdes.school;

import javax.script.ScriptException;
import java.util.*;
import java.util.stream.Collectors;

public class JavaSchoolStarter {
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
        String condition = request.substring(request.toLowerCase().indexOf("where") + 6);
        Map<String, Object> row = new HashMap<>();
        String[] updateRows = request.substring(0, request.toLowerCase().indexOf("where")).split(",");
        Set<Table> resultSet = new HashSet<>();
        getUpdatedRows(row, updateRows);

        String[] test;
        if (condition.toLowerCase().contains(" or ")) {
            test = condition.toLowerCase().split(" (?i)or ");
        } else {
            test = condition.toLowerCase().split(" (?i)or ");
            String operation = "";
            String[] newValues = new String[2];
            String[] andCond = condition.toLowerCase().split(" (?i)and ");
//            System.out.println("AND");
//            Set<Table> andSet = new HashSet<>();
            test(resultSet, operation, newValues, andCond[0]);

            for (int i = 1; i < andCond.length; i++) {
                andHandler(resultSet, operation, newValues, andCond[i]);

            }
        }
//        System.out.println(Arrays.toString(test));

        for (String cond : test) {
            String operation = "";
            String[] newValues = new String[2];
            String[] andCond = cond.toLowerCase().split(" (?i)and ");
            if (cond.toLowerCase().split(" (?i)and ").length > 1) {
                System.out.println("AND");
                Set<Table> andSet = new HashSet<>();
                test(andSet, operation, newValues, andCond[0]);

                for (int i = 1; i < andCond.length; i++) {
                    andHandler(andSet, operation, newValues, andCond[i]);

                }
                resultSet.addAll(andSet);
                System.out.println(andSet);
            } else {
                System.out.println("ELSE " + cond);
                test(resultSet, operation, newValues, cond);
            }


        }

        System.out.println(resultSet);
//        System.out.println(resultSet.remove(new Table(5L, "Федоров", 6.5, 40L, false)));
//        System.out.println(resultSet);
        return data;
    }

    private void test(Set<Table> resultSet, String operation, String[] newValues, String andFromMultiple) {
        String[] splittedAnd = getOperation(andFromMultiple, operation, newValues);
        System.out.println(splittedAnd[0]);
        System.out.println(splittedAnd[1]);
        System.out.println(splittedAnd[2]);
        for (Map<String, Object> datum : data) {
            switch (splittedAnd[0]) {
                case ">=" -> {
                    if (compareValues(splittedAnd[1], datum.get(splittedAnd[1]),
                            ">=", splittedAnd[2]))
                        resultSet.add(new Table((Long) datum.get("id"), (String) datum.get("lastname")
                                , (Double) datum.get("cost"), (Long) datum.get("age"), (Boolean) datum.get("active")));
                }
                case "<=" -> {
                }
                case "=" -> {
                    if (compareValues(splittedAnd[1], datum.get(splittedAnd[1]),
                            "=", splittedAnd[2])) {
                        resultSet.add(new Table((Long) datum.get("id"), (String) datum.get("lastname")
                                , (Double) datum.get("cost"), (Long) datum.get("age"), (Boolean) datum.get("active")));
                    }
                }
                case "!=" -> {
                }
                case "<" -> {
                }
                case ">" -> {
                }
            }

        }
    }

    private void andHandler(Set<Table> resultSet, String operation, String[] newValues, String andFromMultiple) {
        String[] splittedAnd = getOperation(andFromMultiple, operation, newValues);
        System.out.println(splittedAnd[0]);
        System.out.println(splittedAnd[1]);
        System.out.println(splittedAnd[2]);
        Set<Table> addSetCopy = new HashSet<>(resultSet);
        for (Table t : addSetCopy) {
            switch (splittedAnd[0]) {
                case ">=" -> {
                    if (!compareValues(splittedAnd[1], t.getValueByName(splittedAnd[1]),
                            ">=", splittedAnd[2]))
                        resultSet.remove(t);
                }
                case "<=" -> {
                }
                case "=" -> {
                    if (!compareValues(splittedAnd[1], t.getValueByName(splittedAnd[1]),
                            "=", splittedAnd[2])) {
                        resultSet.remove(t);
                    }
                }
                case "!=" -> {
                }
                case "<" -> {
                }
                case ">" -> {
                }
            }

        }
    }

    private boolean compareValues (String column ,Object value1, String condition, String value2) {
        Long ageValue, idValue, ageCompare, idCompare;
        String lastName = "", lastNameCompare = "";
        Boolean active, activeCompare;
        Double cost, costCompare;

        switch (condition) {
            case ">=": {
                switch (column) {
                    case "id", "age" -> {
                        return (Long)value1 >= Long.parseLong(value2.trim());
                    }
                    case "cost" -> {
                        return objectToDouble(value1) >= objectToDouble(value2);
                    }
                }
                break;
            }
            case "<=": {
                switch (column) {
                    case "id", "age" -> {
                        return objectToLong(value1) <= objectToLong(value2);
                    }
                    case "cost" -> {
                        return objectToDouble(value1) <= objectToDouble(value2);
                    }

                }
                break;
            }
            case "=": {
                switch (column) {
                    case "id", "age" -> {
                        System.out.println("TRUE ");
                        return (Long)value1 == Long.parseLong(value2);
                    }
                    case "cost" -> {
                        return objectToDouble(value1).equals(objectToDouble(value2));
                    }
                    case "active" -> {
                        return value1 == Boolean.valueOf(value2);
                    }
                }
                break;
            }
            case "!=": {
                break;
            }
            case "<": {
                break;
            }
            case ">": {
                break;
            }
        }
        return true;
    }

    private Long objectToLong(Object object) {
        return Long.valueOf((String) object);
    }
    private String objectToString(Object object) {
        return (String) object;
    }
    private Double objectToDouble(Object object) {
        return Double.valueOf((String) object);
    }
    private Boolean objectToBoolean(Object object) {
        return Boolean.valueOf((String)object);
    }
    private String[] getOperation(String condition, String operation, String[] newValues) {
        if (condition.contains(">=")) {
            operation = ">=";
            newValues = condition.replace("'", "").split(">=");
            return new String[]{operation, newValues[0], newValues[1].trim()};
        }
        if (condition.contains("<=")) {
            operation = "<=";
            newValues = condition.replace("'", "").split("<=");
            return new String[]{operation, newValues[0], newValues[1].trim()};
        }
        if (condition.contains("=")) {
            operation = "=";
            newValues = condition.replace("'", "").split("=");
            return new String[]{operation, newValues[0], newValues[1].trim()};
        }
        if (condition.contains("!=")) {
            operation = "!=";
            newValues = condition.replace("'", "").split("!=");
            return new String[]{operation, newValues[0], newValues[1].trim()};
        }
        if (condition.contains(">")) {
            operation = ">";
            newValues = condition.replace("'", "").split(">");
            return new String[]{operation, newValues[0], newValues[1].trim()};
        }
        if (condition.contains("<")) {
            operation = "<";
            newValues = condition.replace("'", "").split("<");
            return new String[]{operation, newValues[0], newValues[1].trim()};
        }
        return new String[]{operation, newValues[0], newValues[1].trim()};

    }

    private void getUpdatedRows(Map<String, Object> row, String[] updateRows) {
        for (String i : updateRows) {
            i = i.trim();
            String[] pair = i.split("=");
            switch (pair[0].trim().toLowerCase()) {
                case columnLastName: {
                    row.put(columnLastName.replace("'", ""), pair[1].trim().replace("'", ""));
                    break;
                }
                case columnId: {
                    row.put(columnId.replace("'", ""), Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnAge: {
                    row.put(columnAge.replace("'", ""), Long.valueOf(pair[1].trim()));
                    break;
                }
                case columnCost: {
                    row.put(columnCost.replace("'", ""), Double.valueOf(pair[1].trim()));
                    break;
                }
                case columnActive: {
                    row.put(columnActive.replace("'", ""), Boolean.valueOf(pair[1].trim()));
                    break;
                }
            }
        }
    }

    private class Table {
        private Long id;
        private String lastName;
        private Double cost;
        private Long age;
        private Boolean active;

        public Table(Long id, String lastName, Double cost, Long age, Boolean active) {
            this.id = id;
            this.lastName = lastName;
            this.cost = cost;
            this.age = age;
            this.active = active;
        }

        private Object getValueByName(String name) {
            switch (name) {
                case "lastName" -> {
                    return this.lastName;
                }
                case "id" -> {
                    return this.id;
                }
                case "cost" -> {
                    return this.cost;
                }
                case "age" -> {
                    return this.age;
                }
                case "active" -> {
                    return this.active;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "id=" + id +
                    ", lastName='" + lastName + '\'' +
                    ", cost=" + cost +
                    ", age=" + age +
                    ", active=" + active +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Table table = (Table) o;
            return Objects.equals(id, table.id) && Objects.equals(lastName, table.lastName) && Objects.equals(cost, table.cost) && Objects.equals(age, table.age) && Objects.equals(active, table.active);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, lastName, cost, age, active);
        }
    }

}
