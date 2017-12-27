/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_ir;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jdk.nashorn.internal.parser.TokenStream;
import java.rmi.server.UID;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import static sun.rmi.transport.TransportConstants.Version;
import org.apache.lucene.analysis.snowball.*;
import org.tartarus.snowball.ext.PorterStemmer;


//13-4073. 


/**
 *
 * @author manal
 */
/*public class Java_IR {

    /**
     * @param args the command line arguments
     */
    /*public static void main(String[] args) {
        // TODO code application logic here
    }
    
}*/
public class Part1_Tokenizer{
//Interface Not created.
   
    
    /**
     *
     * @param args
     * @throws IOException
     */
    public static void getOffsets(List<String> TermID, List<Integer> offsets) throws IOException //This function stores offsets 
            //and retrieves correct line from the offsets arraylist too.
    {
//        List<Integer> AllOffsets=new ArrayList<Integer>();
//        path=FileSystems.getDefault().getPath(".",filename);
//        br=Files.newBufferedReader(path_doc_title_index_path, Charset.defaultCharset());
//        int offset=0; //offset of first line.       
//        String strline=br.readline();
//        offset+=strline.length()+1; //offset of second line
        File readFile = new File("C:/Users/manal/Desktop/Fall2016/IR/Java_IR/term_index.txt");
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader( new FileReader(readFile) );
        }
        catch (IOException ioe)
        {
            System.err.println("Error: " + ioe.getMessage());     
        }
        String strline;
        int index=0;
        RandomAccessFile raf = new RandomAccessFile(readFile, "rw");
        //offsets.add(0,0);
        //System.out.println(offsets.get(index));
        raf.seek(0); //First line retrieved from offset 0
        System.out.println(raf.readLine());
        //'Offsets' Arraylist contains second line on 0th index i.e the first index. That's cuz saving the offset of the first line isnt needed.  
        while((strline=reader.readLine())!=null){
            
            String TermFromArr=getNthWord(TermID.get(index),1); //one term from termID file.
            //System.out.println(TermFromArr);
            String TermFromText=getNthWord(strline,1); //one term from termID file.
            //System.out.println("ArrTerm: "+TermFromArr+ " TextTerm: "+TermFromText);
            if(TermFromArr.equalsIgnoreCase(TermFromText)){
                //System.out.println(strline);
                Integer num_bytes = strline.getBytes().length+System.getProperty("line.separator").length();
                //System.out.println(num_bytes);
                offsets.add( index==0 ? num_bytes : offsets.get(index-1)+num_bytes );
                //index++;
//                offsets.add(strline.length() + System.getProperty("line.separator").length());
                //System.out.println("offset:"+num_bytes);
                raf.seek(offsets.get(index));
                //System.out.println(raf.readLine());
                index++;
            }
            
        }
        
       
//        //for(int index1=0;index1!=offsets.size()-1;index1++){
//            raf.seek(offsets.get(index1));
//            System.out.println(raf.readLine());
            
        //}
        
//        raf.seek(offsets.get(0));
//            System.out.println(raf.readLine());
//        raf.seek(offsets.get(1));
//            System.out.println(raf.readLine());
//        raf.seek(offsets.get(2));
//            System.out.println(raf.readLine());
//        for(Integer m: offsets)
//        {
//            System.out.println(m);
//        }
//        

    }              

    public static void populateDocIndex(List<String> DocIndex) {
          
            try(BufferedReader buf = new BufferedReader(new FileReader("C:/Users/manal/Desktop/Fall2016/IR/Java_IR/PostSorting.txt")))
            {
            
            String lineJustFetched;
            int i=0;
            while((lineJustFetched=buf.readLine())!=null){
              DocIndex.add(i,lineJustFetched);
              i++;
                
            }
            } catch(IOException e){
               e.printStackTrace();   
            }

//            for(String each : DocIndex){
//                System.out.println(each);
    }
    public static void populateTermID(List<String> TermID) {
        
        try(BufferedReader buf = new BufferedReader(new FileReader("C:/Users/manal/Desktop/Fall2016/IR/Java_IR/termids.txt")))
            {

            String lineJustFetched;
            int i=0;
            while((lineJustFetched=buf.readLine())!=null){
//              if(i==0)
//                 lineJustFetched=buf.readLine();
              TermID.add(i,lineJustFetched);
              //int temp=AllOffsets.get(i)+ lineJustFetched.getBytes(Charset.defaultCharset()).length +1;
              //AllOffsets.add(i,AllOffsets.get(i)+ lineJustFetched.getBytes(Charset.defaultCharset()).length +1);
              
              i++;
            }
            } catch(IOException e){
               e.printStackTrace();   
            }

//            for(int t=0;t<AllOffsets.size();t++) 
//            System.out.println(AllOffsets.get(t)); 

    }
    public static String getNthWord(String fullString, int nth)
    {
       String[] temp = fullString.split("\t");
       if(nth-1 < temp.length)
          return temp[nth - 1];
       return null;
    }
    public static void makeTermInfoFile(List<String> TermID, List<Integer> Docs, List<Integer> TermOccurrences,List<Integer> offsets) throws IOException{
        
        File tempFile = new File("term_info.txt");
        BufferedWriter w = new BufferedWriter(new FileWriter(tempFile,true));
        w.write(getNthWord(TermID.get(0),1)+'\t'+ 0 +'\t'+ Docs.get(0) +'\t'+ TermOccurrences.get(0));
        w.newLine();
        for(int i=1;i<offsets.size();i++){
            
            w.write(getNthWord(TermID.get(i),1)+'\t'+ offsets.get(i) +'\t'+ Docs.get(i) +'\t'+ TermOccurrences.get(i));
            w.newLine();
        }
        w.close();
    }
    public static void Part2_InvertedIndex() throws IOException{
        
        List<String> TermID=new ArrayList<String>();
        List<String> DocIndex=new ArrayList<String>();
        List<Integer> AllDocsForATerm=new ArrayList<Integer>();
        List<Integer> AllOccurrencesOfATerm=new ArrayList<Integer>();
        
        populateTermID(TermID);
        populateDocIndex(DocIndex);
        String temp=null;
        int baseOffset=0;
        String difference="";
        String toBePrinted=getNthWord(TermID.get(0),1);
        int count=0;
        //int SIZE=4;
        int SIZE=TermID.size();
        int countForDocs;
        int countForTermOccurrences;
        File tempFile = new File("term_index.txt");/////////////////////////////////////////////////////////////
        BufferedWriter w = new BufferedWriter(new FileWriter(tempFile,true));
        
//        System.out.println("TERMID: "+ TermID.get(0));
//        System.out.println("DOCID: "+ DocIndex.get(0));

        for(int i=0;i<SIZE;i++)
        {
           // toBePrinted=getNthWord(TermID.get(i),1);
            //String arr[] = TermID.get(i).split(" ", 2);
//          //String T_ID = arr[0];   //TermID.
            String T_ID=getNthWord(TermID.get(i),1); //one term from termID file.
            toBePrinted=T_ID;
          
            countForDocs=0;
            countForTermOccurrences=0;
            for(int j=0;j<DocIndex.size();j++)
            {
                String TD_ID=getNthWord(DocIndex.get(j),2); //Compared with all terms in DocIndex.txt file.
                
                if(T_ID.equals(TD_ID)==true) //T_ID is the termID from TermID.text. //TD_ID is the termID from DocIndex.txt
                {
                    
                    if(count==0) //this will be true when the first Doc ID needs to be printed in full.
                    {
                        baseOffset=Integer.valueOf(getNthWord(DocIndex.get(j),1));
                        toBePrinted= T_ID+"\t" + String.valueOf(baseOffset) + ":" + getNthWord(DocIndex.get(j),3);
                        countForTermOccurrences++;
                        //AllDocsForATerm.add(i,AllDocsForATerm.get(i)+1);

                    }else if(count==1) //this will be true after the first Doc ID is printed in full.
                    {
                        //difference = String.valueOf(Integer.valueOf(getNthWord(DocIndex.get(j-1),1))- baseOffset);
                        baseOffset = Integer.valueOf(difference);
                        baseOffset=Integer.valueOf(getNthWord(DocIndex.get(j),1));
                        toBePrinted= toBePrinted+"\t" + baseOffset + ":" + getNthWord(DocIndex.get(j),3);
                        //countForDocs++;
                        countForTermOccurrences++;
                        
                    }
                    //countForTermOccurrences++;
                    countForDocs++;
                    for(int k=4;k <= DocIndex.get(j).split("\t+").length;k++) //This forloop is just to contruct the string for Inverted Index.
                    {

                          difference = String.valueOf(Integer.valueOf(getNthWord(DocIndex.get(j),1))- baseOffset);
                          temp=getNthWord(DocIndex.get(j),k);
                          toBePrinted= toBePrinted+"\t" + difference + ":" +temp;
                          countForTermOccurrences++;
                        
                        
                    }
                    count=1;
                    
                    
                }
                
                   
            
            }
            AllDocsForATerm.add(i,countForDocs);
            AllOccurrencesOfATerm.add(i,countForTermOccurrences);

            //TO DO:
            
            //COUNT ALL TABS IN EACH ROW OF TERM_INDEX DOC FOR NUMBER OF OCCURRENCES OF EACH TERM IN ALL DOCS.
            //offset += strline.getBytes(Charset.defaultCharset()).length + 1; //STORE OFFSET OF EACH LINE WHILE TRAVERSING.
            w.write(toBePrinted);
            w.newLine();
            //System.out.println(toBePrinted);
        }
        
//        for(int t=0;t<AllDocsForATerm.size();t++) 
//            System.out.println(AllDocsForATerm.get(t)); 
//        for(int t=0;t<AllOccurrencesOfATerm.size();t++) 
//            System.out.println(AllOccurrencesOfATerm.get(t));   
//              List<Integer> offsets=new ArrayList<Integer>(); //offset of first line.       
        List<Integer> offsets=new ArrayList<Integer>(); //offset of first line.       
  
        w.close();
        getOffsets(TermID,offsets);
        makeTermInfoFile(TermID,offsets,AllDocsForATerm,AllOccurrencesOfATerm);
        
    }
    
    public static void SortFileByDocID(BufferedReader reader) throws Exception {
       
//        Map<String, String> map=new TreeMap<String, String>();
//        String line="";
//        while((line=reader.readLine())!=null){
//        	map.put(getField(line),line);
//        }
//        reader.close();
//        FileWriter writer = new FileWriter("SortedByDocID.txt");
//        for(String val : map.values()){
//        	writer.write(val);	
//        	writer.write('\n');
//        }
//        writer.close();
        Map<String, String> map=new TreeMap<String, String>();
        String line="";
        while((line=reader.readLine())!=null){
                map.put(getField(line),line);
        }
        reader.close();
        BufferedWriter writer = new BufferedWriter(new FileWriter("PostSorting.txt"));
        for(String val : map.values()){
                writer.write(val);      
                writer.newLine();
        }
        writer.close();

    }

    private static String getField(String line) {
    	return line.split(" ")[0];//extract value you want to sort on
    }
    public HashMap ProcessOneFile(File currentFile,String Stopwords[],long UniqueDocID, HashMap prevHashMap) throws IOException{
        
        Document htmlFile = null;

        try {

           //htmlFile = Jsoup.parse(new File("C:/Users/manal/Desktop/Fall2016/IR/corpus/corpus/clueweb12-0000tw-35-20780"), "UTF-8");
           htmlFile = Jsoup.parse(currentFile,"UTF-8");

        } 
        catch (IOException e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

        } // right

        String content = htmlFile.body().text();
        
//        File file1 = new File("AfterParsing.txt");
//        File file2 = new File("AfterTokenizing.txt");
//        //if file doesnt exists, then create it
//	    if (!file1.exists()) {
//		file1.createNewFile();
//	    }
//            if( !file2.exists())
//            {
//                file2.createNewFile();
//            }
//            
//	FileWriter fw1 = new FileWriter(file1.getAbsoluteFile());
//	BufferedWriter bw1 = new BufferedWriter(fw1);
//	bw1.write(content);
//	bw1.close();
        
        //Just Checking!
        //System.out.println("Done With Parsing. Lets Tokenize!");
        
        String[] Tokens = content.split("[\\p{Punct}\\s]+");
        //String[] Tokens = Text.split("\\P{L}+"); //A similar regular expression to the one above.
        //String[] Tokens = content.split("\\w+ (\\.?\\w+)*"); //The regex given in assignment statement doesn't work correctly.
        
        //Tokens to lowercase.
        for(int i=0;i<Tokens.length;i++)
            Tokens[i]=Tokens[i].toLowerCase();
        
        
        
        
        
        //Applying Stopwording.
        //File tempFile = new File("afterStopWording.txt");
//        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        List<String> currentTokenList = new ArrayList(Arrays.asList(Tokens));
        String s;
        for (int i=0;i!=Stopwords.length-1;i++) {
            for(int j=0;j!=Tokens.length-1;j++)
            {
               if (Stopwords[i].equals(Tokens[j])) {
                   s=Tokens[j];
                   currentTokenList.remove(s);
               } 
            }
            
        } 
        String[] RareTokens = currentTokenList.toArray(new String[0]);


        //Stemming!
        
       
        //tempFile = new File("afterStemming.txt");
        String StemmedTokens[]= currentTokenList.toArray(new String[0]);;
        //BufferedWriter w = new BufferedWriter(new FileWriter(tempFile));
        int k=0;
        for(String word: RareTokens)
        {
            PorterStemmer obj = new PorterStemmer();
            obj.setCurrent(word);
            obj.stem();
            StemmedTokens[k]=obj.getCurrent(); //StemmedTokens will have the stemmed words now.
            //w.write(obj.getCurrent());
            //w.newLine();
            k++;
            //System.out.println(obj.getCurrent());
            
        }
        
        /*termids.txt – A file mapping a token found during tokenization to a unique integer, its TERMID. 
        Each line should be formatted with a TERMID and token separated by a tab, 
        as follows: 567\tasparagus */
        //LIMIT = 1000L;
        
    
        currentTokenList=Arrays.asList(StemmedTokens);

       
        File tempFile = new File("termids.txt");
       
        BufferedWriter w = new BufferedWriter(new FileWriter(tempFile,true));
        
        long UniqueTermID=0;
        //w.write("Note: A three digit Term-ID only allows the mapping of 900 unique tokens. To preserve the uniqueness of the termID, a 6 digit ID is used instead.");
        //w.newLine();
        //w.newLine();
        for(String n: currentTokenList)
        {
            //Object obj = prevHashMap.get(n);
            //if (obj == null)
            if(prevHashMap.containsKey(n)==false)//if Hashmap doesnt already contain n
            {
                Random generator = new Random();
                //UniqueTermID = generator.nextInt(899) + 100;
                UniqueTermID = generator.nextInt(899999) + 100000;
                String AppendedDocTermID = Long.toString(UniqueTermID);
                prevHashMap.put(n,AppendedDocTermID);
                //System.out.println(n);
                w.write(/*UniqueDocID+":"+*/AppendedDocTermID+ "\t" + n);
                //w.write(UniqueDocID+":"+ AppendedDocTermID+ "\t" + n);
                //w.write(/*UniqueDocID+":"+*/AppendedDocTermID+ "\t" + n); //!!!!!The format for termId Document is modified 
                //since 3 digit unique ID's cant include all the unique terms in all documents!!!!! 
                //So to avoid repetition of ID's, DocID is appended with it.
                w.newLine();
            } 
            //else
            else if(prevHashMap.containsKey(n)==true)//if hashmap contains n already.
            {
                
                //System.out.println(String.valueOf(n + "=" +prevHashMap.get(n)));
                //String AppendedDocTermID = Long.toString(UniqueDocID) + ":" + String.valueOf(prevHashMap.get(n));
                String AppendedDocTermID = String.valueOf(prevHashMap.get(n));
                //System.out.println("ID for "+n + prevHashMap.get(n)); 
                //The printed values will show all those terms that are already present in the hashmap.
                prevHashMap.put(n,AppendedDocTermID);
                
            }
        }
          
       
        w.close();
        
        
        /*doc_index.txt – A forward index containing the position of each term in each file.
        One line of this file contains all the positions of a single token in a single document. 
        Each line should contain a DOCID, a TERMID, and a list of all the positions of that term 
        in that document (the first term has position 1, the second has position 2, etc.). 
        The DOCID, TERMID, and positions should be separated by tabs, as follows: 1234\t567\t1\t3\t12\t42 
        */ 
        
        tempFile = new File("doc_index.txt");
        w = new BufferedWriter(new FileWriter(tempFile,true));
        HashMap h=new HashMap();                      //This hashmap is for DocId's.   
        String[] AllTokens = currentTokenList.toArray(new String[0]); 
        //The array 'AllTokens' contains all the terms in a single document 
        //after getting rid of the special characters in between.
        int p=0;
        for(String n: AllTokens)
        {  
            if(h.containsKey(n))
            {
               String i=h.get(n).toString();
               String allPostings=i+ "\t" +Integer.valueOf(p);
               h.put(n,allPostings);
            }
            else
              h.put(n, p);
            p++;
        }
        
        Iterator it = prevHashMap.entrySet().iterator(); //Simply iterating to check if each term is mapped to all its correct positions. 
       
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            String A_term=pair.getKey().toString();
            String B_termID=pair.getValue().toString();
            
            for (Object o: h.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                
                if(entry.getKey().equals(A_term))
                {
                    w.write(UniqueDocID+ "\t"+ B_termID+ "\t" + entry.getValue());
                    w.newLine();
                    
                }
                
                
            }
        }
        
        w.close();
        
        return prevHashMap;
    }
    public static void main(String args[]) throws IOException, Exception {


      
        BufferedReader in = new BufferedReader(new FileReader("C:/Users/manal/Desktop/Fall2016/IR/stoplist.txt"));
        String str;
        List<String> list = new ArrayList<String>();
        while((str = in.readLine()) != null){
            list.add(str);
        }
        
        //Storing stoplist in an array of strings.
        String[] Stopwords = list.toArray(new String[0]);
        
      
        
        File folder = new File("C:/Users/manal/Desktop/Fall2016/IR/corpus/corpus"); //Need to take this as INPUT!
        File[] listOfFiles = folder.listFiles();
        long UniqueDocID = 0;
        HashMap prevHashMap = new HashMap();//PrevHashMap contains hash of terms from one document in each iteration.
        File tempFile = new File("docids.txt");
        BufferedWriter w = new BufferedWriter(new FileWriter(tempFile,true));
        Part1_Tokenizer handler=new Part1_Tokenizer();
        
        //int size= listOfFiles.length; //--------------------> Comment out to run over all of the directory files.
        int size=5; //Testing for a limited number of documents during development.
        
        for (int i = 0; i < size ; i++) {
            
            Random generator = new Random();
            UniqueDocID = generator.nextInt(8999) + 1000;
            
            if (listOfFiles[i].isFile()) {
            //System.out.println(UniqueDocID + "\\" + listOfFiles[i].getName());
              prevHashMap = handler.ProcessOneFile(listOfFiles[i],Stopwords,UniqueDocID,prevHashMap);
              w.write(UniqueDocID + "\t" + listOfFiles[i].getName());
              w.newLine();

            } 
            else if (listOfFiles[i].isDirectory()) {
                //System.out.println("Directory " + listOfFiles[i].getName());
                w.write("Directory " + listOfFiles[i].getName());
                w.newLine();

            }
        }
        w.close();

        BufferedReader reader = new BufferedReader(new FileReader("C:/Users/manal/Desktop/Fall2016/IR/Java_IR/doc_index.txt"));
        Part1_Tokenizer.SortFileByDocID(reader);
        
        Part2_InvertedIndex();
        
     

    }

}
