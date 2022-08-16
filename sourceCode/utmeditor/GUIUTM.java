package utmeditor;

import utm.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The driver class of utmeditor
 */
public class GUIUTM implements UTMController{

    private UniversalTuringMachine utm;
    private String isAnimation;
    private OriginalUTM extendUTM;
    private TuringMachine tm;

    /**
     * create a new UniversalTuringMachine with GUI
     * get the Turing Machine from Universal Turing Machine
     * create an Original UTM with the new utm
     * @param isAnimation the flag represents the state of "animation" or "noanimation"
     */
    public GUIUTM(String isAnimation){
        utm =new UniversalTuringMachine();
        utm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        tm = utm.getTuringMachine();
        extendUTM = new OriginalUTM(utm);
        this.isAnimation=isAnimation;
    }

    /**
     * Load the turing machine by override the UTMController interface
     * @param filePath A string representing the absolute path to the TM
     */
    @Override
    public void loadTuringMachineFrom(String filePath) {
        tm = extendUTM.createTMFromFile(filePath);
        utm.loadTuringMachine(tm);
        // distinct the different type of UTM by variants
        if(extendUTM.getIsBBTM())  extendUTM = new BBTM(utm,isAnimation);
        else if(extendUTM.getIsLRTM())  extendUTM = new LRTM(utm,isAnimation);
        else    extendUTM = new ClassicalTM(utm,isAnimation);
    }

    /**
     * give the input of UTM
     * @param input A string representing the input for the loaded TM.
     */
    @Override
    public void runUTM(String input) {

        extendUTM.executeTM(tm,input);

    }

    public OriginalUTM getExtendUTM() {
        return extendUTM;
    }

    public static void main(String[] args){
        // default arguments
        if(args.length==0) {
            GUIUTM graphicUTM = new GUIUTM("--animation");
            UTMEditor editor = new UTMEditor();
            editor.setUTMController(graphicUTM);
            graphicUTM.getExtendUTM().displayWindow();
        }
        // three arguments of user's input represents desc_abspath, input, and --animation or --noanimation
        else if(args.length==3){
            GUIUTM extendUTM = new GUIUTM(args[2]);
            extendUTM.loadTuringMachineFrom(args[0]);
            if(args[2].equals("--animation")) extendUTM.getExtendUTM().displayWindow();
            extendUTM.runUTM(args[1]);
        }

        else{
            System.out.println("Usage: java -jar pratical-37849956.jar \"desc_abspath\" "+"\"input\" l--animation/--nonanimation");
            System.exit(0);
        }


    }
}
