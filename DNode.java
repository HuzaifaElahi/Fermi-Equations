package code;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class DNode<E> {
  String element;
  String name;
  DNode<E> next;
  DNode<E> prev;    

  static Scanner input = new Scanner(System.in);
  
  static ArrayList<ArrayList<Integer>> twoD = new ArrayList<ArrayList<Integer>>();

  public static void main(String[] args) throws IOException {
    System.out.println("Welcome to order of magnitude pro!");
    LinkedList<DNode> a = new LinkedList<DNode>();

    //reads in data
    readIn(a);

    //set bounds
//    setBounds(a);

  //  add vocab
    addVocab(a);

    multiplier(a, true, 1);
    
    //reads out data
    readOut(a);

    /*
    //add powers to the list
    addPowers(a);

    //check powers by for looping through
    systemCheck(a);
     */

  }

  static int questionnaire(LinkedList<DNode> nodeList, int mid, int left, int right, String object){
    //if min
    if((right < left)){
      return left;
    }
    
    //if bigger than min less than min+1
    if((left > mid)){
      return left;
    }
    
    //ask user for relative size comparison with middle of nodelist
    System.out.println("Is "+ object + " bigger than or smaller than: " + nodeList.get(mid).name  + "?");
    String answer = "";
    answer = input.nextLine();
    
    //if bigger than middle of nodelist, recursively check upper half
    if(answer.equals("bigger")){
      return questionnaire(nodeList, ((mid+1) + right)/2, mid+1, right, object);

    }

    //if middle
    else if((left==right)){
      return mid;
    }
    
    //if smaller than middle of nodelist, recursively check lower half
    else{
      return questionnaire(nodeList, ((left) + mid-1)/2, left, mid-1, object);
    }

  }

  //input powers value in element field of nodelist based on user input
  static void addPowers(LinkedList<DNode> nodeList){
    for(int i = 0 ; i < nodeList.size() ; i++){
      System.out.println("What's the value of " + nodeList.get(i).name + "?");
      nodeList.get(i).element = input.nextLine();
    }
  }

  //take three bounds values from user input
  static void setBounds(LinkedList<DNode> a){
    //Set lower bound
    DNode memberLow = new DNode();
    System.out.println("What's your lower bound?");
    String nameLow = input.nextLine();
    memberLow.name = nameLow;
    a.add(0, memberLow);

    //Set mid bound
    DNode memberMid = new DNode();
    System.out.println("What's your mid bound?");
    String nameMid = input.nextLine();
    memberMid.name = nameMid;
    a.add(1, memberMid);

    //Set upper bound
    DNode memberUpper = new DNode();
    System.out.println("What's your upper bound?");
    String nameUpper = input.nextLine();
    memberUpper.name = nameUpper;
    a.add(2, memberUpper);
  }

  //add to vocabulary list
  static void addVocab(LinkedList<DNode> a) throws FileNotFoundException{
    //boolean for if new vocabulary
    boolean addVocabBool = true;
    
    //Add vocabulary
    while(true){
      addVocabBool = true;
      
      //ask user for new vocabulary
      System.out.println("Add your vocabulary: ");
      String object = input.nextLine();
      
      //if user says done adding vocabulary, exit function
      if(object.equals("done")){
        break;
      }
      
      //check if added vocabulary already exists, don't add if so
      for(int i = 0 ; i < a.size() ; i++){
        if(object.equals(a.get(i).name)){
          addVocabBool = false;
        }
      }
      
      //case where vocabulary exists and user is informed 
      if (addVocabBool == false){
        System.out.println("Already in the list!");
        continue;
      }
      
      //if new vocabulary, add to vocabulary after asking user for comparative size
      else{
        DNode newNode= new DNode();
        newNode.name = object;
        int insert = questionnaire(a, (a.size()-1)/2, 0, a.size()-1, object);
        a.add(insert, newNode);
      }

      //Print list on each run
      for(int j = 0 ; j < a.size() ; j++){
        System.out.println(a.get(j).name);
      }
    }
  }

  //print all element names with corresponding elements in DNode linked list to check if correct
  static void systemCheck(LinkedList<DNode> a){
    for(int k=0 ; k < a.size() ; k++){
      System.out.println(a.get(k).name + " is of magnitude " + a.get(k).element);
    }
  }

  //take values for how many objects inside object larger than it from a specified number of users
  static void multiplier(LinkedList<DNode> a, boolean demo, int column) {
    String object, container;
    int containerIndex = 0;
    int objectIndex = 0;
    int twoDColumn = 0;
    int twoDrow = 0;
    ArrayList<Integer> valueArray = new ArrayList<Integer>();

    //user inputs their preferred calculation from object to container
    if (demo == false){
      System.out.println("What is your object?");
      object = input.nextLine();
      System.out.println("What is your container?");
      container = input.nextLine();
      System.out.println("Your Question: How many "+ object + "'s fit inside a "+container+"?");
      containerIndex = 0;
      objectIndex = 0;
      for (int i = 0; i < a.size(); i++){
        DNode tempNode1 = a.get(i);
        if (object.equals(tempNode1.name)) {
          objectIndex = i;
        }
        for (int j = i; j < a.size(); j++){
          DNode tempNode2 = a.get(j);
          if (container.equals(tempNode2.name)) {
            containerIndex = j;
          }
        }
      }
    }
    
    //user is forced to give values from smallest in list to largest
    else{
      object = a.get(0).name;
      container = a.get(a.size()-1).name;
    }

    //loop through for numerous inputs from users
    while(column > 0){
      valueArray = new ArrayList<Integer>();
      containerIndex = a.size()-1;
      objectIndex = 0;
      BigInteger orderOfMagnitude = BigInteger.valueOf(1);
      
      //loop through ordered list and ask from smallest to largest how many objects in larger object
      while(objectIndex < containerIndex){
        System.out.println("how many "+ a.get(objectIndex).name + "s fit inside a " + a.get(objectIndex+1).name + "?");
        long temp = input.nextLong();
        int value = (int) (Math.round(Math.log10(temp)));
        valueArray.add(value);
        long pow = (long) (Math.pow(10, value));
        BigInteger powValue = BigInteger.valueOf(pow);
        orderOfMagnitude = (orderOfMagnitude.multiply(powValue));
        objectIndex++;
        twoDColumn++;

      }
      
      //add to twoD array
      twoD.add(twoDrow, valueArray);
      System.out.println(orderOfMagnitude + " " + object + "'s fit inside of a " + container);
      column--;
      twoDrow++;
    }
    
    //print results
    for(int p = 0 ; p < twoDrow; p++){
      System.out.println("");
      for(int k =0 ; k < objectIndex ; k++){
        System.out.print(twoD.get(p).get(k) + " ");
      }
    }
  }

  //read in a database
  static void readIn(LinkedList<DNode> a) throws IOException{
    final String SAMPLE_CSV_FILE_PATH = "C:\\Users\\Huzaifa\\Documents\\Hackathon\\inputText.txt\\";

    try (
        Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
        CSVReader csvReader = new CSVReader(reader);
        ) {
      //Reading Records One by One in a String array
      String[] nextRecord;
      int index= 0;
      while ((nextRecord = csvReader.readNext()) != null) {
        DNode d = new DNode();
        //   d.element = nextRecord[1];
        d.name = nextRecord[0];
        a.add(d);

        System.out.println("Name : " + nextRecord[0]);
        index++;

      }
    }
  }

  //export out a database
  static void readOut(LinkedList<DNode> a) throws IOException{

    String csv = "C:\\Users\\Huzaifa\\Documents\\Hackathon\\outputText.txt\\";
    CSVWriter writer = new CSVWriter(new FileWriter(csv),CSVWriter.NO_QUOTE_CHARACTER, 
        CSVWriter.NO_ESCAPE_CHARACTER, 
        System.getProperty("line.separator"));
    String[] data = new String[twoD.get(0).size()+1];
    for(int twoDSize = 0 ; twoDSize < twoD.size() ; twoDSize++){
      for(int nameSize = 0; nameSize < twoD.get(twoDSize).size()-1 ; nameSize++){
        data[nameSize]= (twoD.get(twoDSize).get(nameSize) +"");
      }
      data[twoD.get(0).size()] =",";

      writer.writeNext(data);

    }

    writer.close();
  }
}



