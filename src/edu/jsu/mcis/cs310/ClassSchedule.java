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
        cs.getInputFileData(CSV_FILENAME);
        List<String[]> gotcsv = cs.getCsv(cs.getInputFileData(CSV_FILENAME));
        cs.getCsvString(gotcsv);
        
        return ""; // remove this!
        
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
        List<String []> readCsvFile = cs.getCsv();
        //String CsvToString = cs.getCsvString(readCsvFile);
        ArrayList<HashMap> schedType = new ArrayList<>();
        ArrayList<HashMap> subj = new ArrayList<>();
        ArrayList<HashMap> course = new ArrayList<>();
        ArrayList<HashMap> section = new ArrayList<>();
        
        ArrayList<ArrayList> convertedToJson = new ArrayList<>();
        
        //JsonObject jsObj = new JsonObject();
        
        
        for(String[] row : readCsvFile){
            HashMap<String, String> CsvHMap = new HashMap<>();
            CsvHMap.put(row[5], row[11]);
            if(!schedType.contains(CsvHMap)){
              schedType.add(CsvHMap);  
            }           
        }
        
        for(String[] row : readCsvFile){
            HashMap<String, String> CsvHMap = new HashMap<>();
            CsvHMap.put(row[2].replaceAll("\\d","").trim(), row[1]);
            if(!subj.contains(CsvHMap)){
              subj.add(CsvHMap);  
            }           
        }
        
        for(String[] row : readCsvFile){
            ArrayList<HashMap> temp = new ArrayList<>();
            HashMap<String, String> CsvHMap = new HashMap<>();
            HashMap<String, ArrayList> fHMap = new HashMap<>();
            CsvHMap.put(CREDITS_COL_HEADER, row[6]);
            CsvHMap.put(DESCRIPTION_COL_HEADER, row[3]);
            CsvHMap.put(NUM_COL_HEADER, row[2].replaceAll("[^0-9]", ""));
            CsvHMap.put(SUBJECTID_COL_HEADER, row[2].replaceAll("\\d","").trim());
            
            temp.add(CsvHMap);
            
            fHMap.put(row[2], temp);
            if(!course.contains(fHMap)){
                course.add(fHMap);
            }
        }
        
        for(String[] row : readCsvFile){
            HashMap<String, String> CsvHMap = new HashMap<>();
            CsvHMap.put(CRN_COL_HEADER,row[0]);
            CsvHMap.put(SUBJECTID_COL_HEADER,row[2].replaceAll("\\d","").trim());   
            CsvHMap.put(NUM_COL_HEADER,row[2].replaceAll("[^0-9]", ""));   
            CsvHMap.put(SECTION_COL_HEADER,row[4]);   
            CsvHMap.put(TYPE_COL_HEADER,row[5]);   
            CsvHMap.put(START_COL_HEADER,row[7]);   
            CsvHMap.put(END_COL_HEADER, row[8]);   
            CsvHMap.put(DAYS_COL_HEADER, row[9]);   
            CsvHMap.put(WHERE_COL_HEADER, row[10]);   
            CsvHMap.put(INSTRUCTOR_COL_HEADER, row[12]);
            if(!section.contains(CsvHMap));{
                section.add(CsvHMap);
            }
        }
        
        //jsonCsv.put(CRN_COL_HEADER, readCsvFile.);
        /*for(int x = 1; x < schedType.size(); x++){
            System.out.println(schedType.get(x));
        }
        for(int x = 1; x < subj.size(); x++){
            System.out.println(subj.get(x));
        }*/
        
        /*for(int x = 1; x < course.size(); x++){
            System.out.println(course.get(x));
        }*/
        
        /*for(int x = 1; x < section.size(); x++){
            System.out.println(section.get(x));
        }*/
        convertedToJson.add(schedType);
        convertedToJson.add(subj);
        convertedToJson.add(course);
        convertedToJson.add(section);
        
        for(int x = 0; x < convertedToJson.size(); x++){
            System.out.println(convertedToJson.get(x));
        }
    }
    
}