package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ClassSchedule {
    
    private final String CSV_FILENAME = "jsu_sp24_v1.csv";
    private final String JSON_FILENAME = "jsu_sp24_v1.json";
    
    private final String CRN_COL_HEADER = "crn";
    private final String SUBJECT_COL_HEADER = "subject";
    private final String NUM_COL_HEADER = "num";
    private final String DESCRIPTION_COL_HEADER = "description";
    private final String SECTION_COL_HEADER = "section";
    private final String TYPE_COL_HEADER = "type";
    private final String CREDITS_COL_HEADER = "credits";
    private final String START_COL_HEADER = "start";
    private final String END_COL_HEADER = "end";
    private final String DAYS_COL_HEADER = "days";
    private final String WHERE_COL_HEADER = "where";
    private final String SCHEDULE_COL_HEADER = "schedule";
    private final String INSTRUCTOR_COL_HEADER = "instructor";
    private final String SUBJECTID_COL_HEADER = "subjectid";
    
    public String convertCsvToJsonString(List<String[]> csv) {
        ClassSchedule cs = new ClassSchedule();
        List<String []> readCsvFile = cs.getCsv();
        
        JsonObject convertedToJson = new JsonObject();
        
        JsonObject sched = new JsonObject();
        JsonObject subj = new JsonObject();
        JsonObject course = new JsonObject();
        JsonArray section = new JsonArray();

        for(String[] row : readCsvFile){
            String key = row[5]; 
            String value = row[11];
            if(!sched.containsKey(key) && !key.equals("type")){
              sched.put(key,value);  
            }           
        }
        
        for(String[] row : readCsvFile){
            String key = row[2].replaceAll("\\d","").trim();
            String value = row[1];
            if(!subj.containsKey(key) && !key.equals("num")){
              subj.put(key, value);  
            }           
        }
        

        for(String[] row : readCsvFile){
            String classNum = row[2];
            String credit = CREDITS_COL_HEADER;
            String credValue = row[6];
            String desc = DESCRIPTION_COL_HEADER;
            String descValue = row[3];
            String numKey = NUM_COL_HEADER;
            String numValue = row[2].replaceAll("[^0-9]", "");
            String subjId = SUBJECTID_COL_HEADER;
            String subjIdValue = row[2].replaceAll("\\d","").trim();
            
            JsonObject temp = new JsonObject();
            temp.put(subjId, subjIdValue);
            temp.put(numKey, numValue);
            temp.put(desc, descValue);
            temp.put(credit, credValue);
            
            course.put(classNum, temp);
            
        }
        
        for(String[] row : readCsvFile){
            String crn = CRN_COL_HEADER; String crnVal = row[0];
            String subjId = SUBJECTID_COL_HEADER; String subjIdVal = row[2].replaceAll("\\d","").trim();   
            String numKey = NUM_COL_HEADER; String numVal = row[2].replaceAll("[^0-9]", "");   
            String sect = SECTION_COL_HEADER; String sectVal = row[4];   
            String type = TYPE_COL_HEADER; String typeVal = row[5];   
            String srt = START_COL_HEADER; String srtVal = row[7];   
            String ed = END_COL_HEADER; String edVal = row[8];   
            String days = DAYS_COL_HEADER; String daysVal = row[9];   
            String where = WHERE_COL_HEADER; String whereVal = row[10];   
            String inst = INSTRUCTOR_COL_HEADER; String instVal = row[12];
            
            JsonObject temp = new JsonObject(); 
            temp.put(crn, crnVal);
            temp.put(subjId, subjIdVal);
            temp.put(numKey, numVal);
            temp.put(sect, sectVal);
            temp.put(type, typeVal);
            temp.put(srt, srtVal);
            temp.put(ed, edVal);
            temp.put(days, daysVal);
            temp.put(where, whereVal);
            temp.put(inst, instVal);
            if(!temp.containsValue("instructor")){
                section.add(temp);
            }
        }
        

        convertedToJson.put("scheduletype", sched);
        convertedToJson.put("subject", subj);
        convertedToJson.put("course", course);
        convertedToJson.put("section", section);
        
        return convertedToJson.toJson(); // remove this!
        
    }
    
    public String convertJsonToCsvString(JsonObject json) {
        ClassSchedule cs = new ClassSchedule();
        JsonObject gotJson = new JsonObject();
        gotJson = cs.getJson();
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\\', "\n");        
        String[] csvData = {CRN_COL_HEADER, SUBJECT_COL_HEADER, NUM_COL_HEADER, DESCRIPTION_COL_HEADER,
        SECTION_COL_HEADER, TYPE_COL_HEADER, CREDITS_COL_HEADER, START_COL_HEADER, END_COL_HEADER,
        DAYS_COL_HEADER, WHERE_COL_HEADER, SCHEDULE_COL_HEADER, INSTRUCTOR_COL_HEADER};
        
        csvWriter.writeNext(csvData);
        
        JsonObject scheduleType = (JsonObject) gotJson.get("scheduletype");
        JsonObject subject = (JsonObject) gotJson.get("subject");
        JsonObject course = (JsonObject) gotJson.get("course");
        JsonArray sectionArray = (JsonArray) gotJson.get("section");
        
        ArrayList<String[]> schedTypes = new ArrayList<>();
        ArrayList<String[]> subjTypes = new ArrayList<>();
        ArrayList<String[]> courses = new ArrayList<>();
        ArrayList<String[]> classNums = new ArrayList<>();
        ArrayList<String[]> sections = new ArrayList<>();

        //System.out.println(gotJson);
        
        for(Object key : scheduleType.keySet()){
            String value = scheduleType.get(key).toString();
            String[] pair = new String[2];
            pair[0] = key.toString();
            pair[1] = value;
            schedTypes.add(pair);
        }
        
        for(Object key : subject.keySet()){
            String value = subject.get(key).toString();
            String[] pair = new String[2];
            pair[0] = key.toString();
            pair[1] = value;
            subjTypes.add(pair);
        }
        
        /*for(Object key : course.keySet()){
            Object value = course.get(key);
            JsonObject nestedJson = (JsonObject) value;
            for(Object nestedKey : nestedJson.keySet()){
                Object nestedValue = nestedJson.get(nestedKey);
                JsonObject nestedededJson = (JsonObject) nestedValue;
                for(Object nestededKey : nestedededJson.keySet()){
                    String nestedestValue = (String) nestedededJson.get(nestededKey).toString();
                    String[] keyValuePair = new String[2];
                    keyValuePair[0] = nestededKey.toString();
                    keyValuePair[1] = nestedestValue;
                    courses.add(keyValuePair);
                }
            }
        }*/
        
        for (Object sectionObj : sectionArray) {
            JsonObject section = (JsonObject) sectionObj;
            String[] pair = new String[9];
            pair[0] = section.get("crn").toString();
            pair[1] = (String) section.get("subjectid");
            pair[2] = (String) section.get("num").toString();
            pair[3] = (String) section.get("section");
            pair[4] = (String) section.get("type");
            pair[5] = (String) section.get("start");
            pair[6] = (String) section.get("end");
            pair[7] = (String) section.get("days");
            pair[8] = (String) section.get("where");
            sections.add(pair);
        }
        
        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> typeSpelled = new ArrayList<>();

        for(String[] pair : schedTypes){
            type.add(pair[0]);
            typeSpelled.add(pair[1]);
        }
        for(String[] pair : subjTypes){
            //System.out.println("Key: " + pair[0] + ", Value: " + pair[1]);
        }
        /*for(String[] pair : courses){
            System.out.println("Key: " + pair[0] + ", Value: " + pair[1]);
        }*/
        /*for(String[] pair : sections){
            System.out.println("Key: " + pair[0] + ", Value: " + pair[1]);
        }*/
        int size = 100;
        for(int x = 0; x<size; x++){
            System.out.println(type.get(x));
        }
        
        String csvString = writer.toString();
        
        //System.out.println(csvString);
        
        
        return ""; // remove this!
        
    }
    
    public JsonObject getJson() {
        
        JsonObject json = getJson(getInputFileData(JSON_FILENAME));
        return json;
        
    }
    
    public JsonObject getJson(String input) {
        
        JsonObject json = null;
        
        try {
            json = (JsonObject)Jsoner.deserialize(input);
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return json;
        
    }
    
    public List<String[]> getCsv() {
        
        List<String[]> csv = getCsv(getInputFileData(CSV_FILENAME));
        return csv;
        
    }
    
    public List<String[]> getCsv(String input) {
        
        List<String[]> csv = null;
        
        try {
            
            CSVReader reader = new CSVReaderBuilder(new StringReader(input)).withCSVParser(new CSVParserBuilder().withSeparator('\t').build()).build();
            csv = reader.readAll();
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return csv;
        
    }
    
    public String getCsvString(List<String[]> csv) {
        
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n");
        
        csvWriter.writeAll(csv);
        
        return writer.toString();
        
    }
    
    private String getInputFileData(String filename) {
        
        StringBuilder buffer = new StringBuilder();
        String line;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
        
            BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + filename)));

            while((line = reader.readLine()) != null) {
                buffer.append(line).append('\n');
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return buffer.toString();
        
    }
    
    public void test(){
        
}