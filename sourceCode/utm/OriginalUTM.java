package utm;

import java.io.*;
import java.util.ArrayList;

import static utm.MoveClassical.LEFT;
import static utm.MoveClassical.RIGHT;
import static utm.MoveLRTM.RESET;

/**
 * The derived Class
 */
public class OriginalUTM {
    private UniversalTuringMachine utm;

    private  boolean isClassical=false;
    private  boolean isLRTM=false;
    private boolean isBBTM=false;

    /**
     * Constructor of OriginalUTM
     * @param utm A empty utm
     */
    public OriginalUTM(UniversalTuringMachine utm){
        this.utm=utm;
    }

    /**
     * display the window
     */
    public void displayWindow(){
        utm.display();
    }

    /**
     * Extract the file in certain path and then get rule in file, write rules into turing machine
     * @param tmDescriptionFilePath A String describes the path of file
     * @return A Turing machine contains settled rules
     */
    public TuringMachine createTMFromFile(String tmDescriptionFilePath){
        /**
         extract the file
         */
        TuringMachine tm = new TuringMachine(Config.MAX_CELLS,"q0","qa","qr");
        ArrayList<String> ruleset ;
        ruleset =extractFile(tmDescriptionFilePath);

        for(String rule: ruleset) {
            tm.addRule(splitRule(rule).get(0),splitRule(rule).get(1).charAt(0),
                    splitRule(rule).get(2),splitRule(rule).get(3).charAt(0),
                    extractDirection(splitRule(rule).get(4)));;
        }// get each state and sybmol and and store in an arrayList
        return tm;
    }

    /**
     * find the rule in file,and then return ruleset
     * @param tmDescriptionFilePath A String describes path of file
     * @return A arraylist contains rules
     */
    public  ArrayList<String> extractFile(String tmDescriptionFilePath){

        ArrayList<String> ruleset=new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tmDescriptionFilePath))); // read data in buffer
            String s = reader.readLine();
            String str = "";
            while (s != null)
            {
                s = reader.readLine();
                // determine the variant in tmDescriptionFile
                // get the kind of utm, judge if it is Classical or LRTM or BBTM
                if(s.contains("variant=CLASSICAL")) isClassical=true;
                if(s.contains("variant=LEFT_RESET")) isLRTM=true;
                if (s.contains("variant=BUSY_BEAVER")) isBBTM=true;

                // get the ruleset
                //if(s.contains("#rules")){
                if(s.contains("rules=")&&!s.contains("#")){
                    str = s.split("=")[1];// get the message of rule
                    ruleset.add(str.split("<>")[0]);
                    while(str.contains("<>")) {
                        ruleset.add(str.split("<>")[1]);
                        str=str.substring(str.indexOf(">")+1);// get each rule
                    }
                    break;
                }
            }
            reader.close();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ruleset;
    }

    /**
     * split the ruleSet into separate rule.
     * @param ruleset A string of a set of rules
     * @return An ArrayList of string,each element is a rule
     */
    public static ArrayList<String> splitRule(String ruleset){
        ArrayList<String> rule = new ArrayList<String>();
        for (int i=0;i<5;i++)   rule.add(ruleset.split(",")[i]);
        return rule;
    }

    /**
     * extract the moveClassical in rule, alter the String into Move
     * @param move: the string "RIGHT", "LEFT" or "RESET"
     * @return A Move is corresponding to String
     */
    public Move extractDirection(String move){
        if(move.equals("RIGHT")) return RIGHT;
        else if(move.equals("LEFT"))   return LEFT;
        else if(move.equals("RESET"))  return RESET;
        else
            System.out.println("ERROR on translate direction!");
        return LEFT;
    }

    /**
     * Execute the TM
     * @param tm A turing machine contains rules
     * @param input A string input from user
     * Must be overrided in sub-class
     */
    public void executeTM(TuringMachine tm, String input){}

    /**
     * Alter the user input "animation" and "non-animation" into boolean
     * @param isAnimation the user input of "animation" or "non-animation"
     * @return the flag IsAnimation
     */
    public boolean getIsAnimation(String isAnimation){
        if(isAnimation.equals("--animation")) return true;
        return false;
    }

    /**
     * Get the flag if the variant in rule file is Classical
     * @return the flag isClassical
     */
    public boolean getIsClassical() {   return isClassical; }

    /**
     * Get the flag if the variant in rule file is busy_beaver
     * @return the flag IsBBTM
     */
    public boolean getIsBBTM() {    return isBBTM;  }

    /**
     * Get the flag if the variant in rule file is leftrest
     * @return the flag IsLRTM
     */
    public boolean getIsLRTM() {    return isLRTM;  }
}
