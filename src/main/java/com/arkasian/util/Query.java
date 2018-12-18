package com.arkasian.util;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Query {
    private String databaseType;
    private QueryType queryType;
    private QueryContent queryContent;
    private QueryFormatter queryFormatter;

    public Query(QueryContent queryContent, String databaseName, QueryType queryType){
        this.databaseType = databaseName;
        this.queryContent = queryContent;
        this.queryType = queryType;
        this.queryFormatter = new QueryFormatter();
    }

    @Override
    public String toString(){
        return getFormattedQuery();
    }

    String getDatabaseName(){
        return this.databaseType;
    }

    private String getFormattedQuery(){
        switch(queryType){
            case SELECT:
                return this.queryFormatter.formatSelectQuery();
            case UPDATE:
                return this.queryFormatter.formatUpdateQuery();
            case INSERT:
                return this.queryFormatter.formatInsertQuery();
            case DELETE:
                return this.queryFormatter.formatDeleteQuery();
            default:
                return "";
        }
    }

    private class QueryFormatter{
        String formatUpdateQuery(){
            return String.format("update %s set %s", databaseType, queryContent.updateQueryContent());
        }

        String formatSelectQuery(){
            boolean isConditional = queryContent.isConditional();
            if(!isConditional) {
                return String.format("select %s from %s", queryContent.getParamPart(), databaseType);
            }else{
                return String.format("select %s from %s %s", queryContent.getParamPart() ,databaseType ,queryContent.getConditionalPart());
            }
        }

        String formatInsertQuery(){
            return String.format("insert into %s(%s) values(%s)", databaseType, queryContent.getParamPart(), queryContent.insertQueryContent());
        }

        String formatDeleteQuery(){
            return String.format("delete from %s %s", databaseType, queryContent.getConditionalPart());
        }
    }

    public static class QueryContent{
        private ArrayList<String> paramNames;
        private HashMap<String,Pair<String, Boolean>> values;
        private boolean isConditional;

        public QueryContent(ArrayList<String> paramNames, HashMap<String, Pair<String, Boolean>> values, boolean isConditional){
            this.paramNames = paramNames;
            this.values = values;
            this.isConditional = isConditional;
        }

        String updateQueryContent(){
            StringBuilder builder = new StringBuilder();
            produceParamString(builder);
            String conditionString = appendCondition(isConditional);
            builder.append(conditionString);
            return builder.toString();
        }

        private String appendCondition(boolean isConditional) {
            if(isConditional){
                StringBuilder conditionalString = new StringBuilder();
                conditionalString.append(" where ");
                values.forEach((param, predicate) -> {
                    String condition = predicate.getValue() != null ? predicate.getValue() ? " AND " : " OR " : "";
                    StringBuilder tmp = new StringBuilder();
                    format(tmp, predicate.getKey());
                    conditionalString.append(param).append("=").append(tmp.toString()).append(condition);
                });
                conditionalString.delete(conditionalString.toString().endsWith("AND") ? conditionalString.length() - 5 : conditionalString.length() - 4, conditionalString.length());
                return conditionalString.toString();
            }
            return "";
        }


        String insertQueryContent(){
            StringBuilder out = new StringBuilder();
            //produceParamString(out);
            produceValuesString(out);
            return out.toString();
        }

        private void produceValuesString(StringBuilder out) {
            values.forEach((key,value) ->{
                format(out, key);
            });
            out.delete(out.length() - 1, out.length());
            out.reverse();
        }

        private void produceParamString(StringBuilder out) {
            paramNames.forEach(param -> out.append(param).append(","));
            out.delete(out.length() - 1, out.length());
        }

        String getConditionalPart(){
            return appendCondition(true);
        }

        String getParamPart(){
            StringBuilder out = new StringBuilder();
            produceParamString(out);
            return out.toString();
        }

        private void format(StringBuilder out, String value){
            try {
                Float.parseFloat(value);
                out.append(value).append(",");
            }catch(NumberFormatException ex){
                out.append("'").append(value).append("'").append(",");
            }
            out.delete(out.length() - 1, out.length());
        }

        boolean isConditional(){
            return this.isConditional;
        }
    }
}
