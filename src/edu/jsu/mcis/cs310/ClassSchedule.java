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
        ClassSchedule cs = new ClassSchedule();
        JsonObject gotJson = new JsonObject();
        JsonObject deserialize = new JsonObject();
        gotJson = cs.getJson();
        deserialize = cs.getJson(gotJson.toJson());
        
        ArrayList<String> csvlist = new ArrayList();
        
        System.out.println(deserialize);
        
    }  
}