package utm;

import static utm.MoveLRTM.RESET;
import static utm.MoveClassical.RIGHT;
import static utm.MoveClassical.LEFT;


/**
 * A sub-class of OriginalUTM
 *
 */
public class LRTM extends OriginalUTM{

    private UniversalTuringMachine utm;
    private boolean isAnimation;

    /**
     * Constructor of LRTM
     * @param utm A utm with variant "Left_reset"
     * @param isAnimation A boolean variable represents the state of "animation"
     */
    public LRTM(UniversalTuringMachine utm,String isAnimation){
        super(utm);
        this.utm=utm;
        this.isAnimation=super.getIsAnimation(isAnimation);
        //if(this.isAnimation) this.displayWindow();
    }

    /**
     * exectue the Left_reset turing machine
     * @param tm A turing machine contains rules
     * @param input A string input from user
     */
    @Override
    public void executeTM(TuringMachine tm, String input){
        utm.loadTuringMachine(tm);
        utm.loadInput(input);
        int index = 0;
        loop:{
            while (index <= input.length()) {
                for (String[] rule : tm.getRules()) {// traverse the ruleset i  order to select the appropriate rule

                    //judge the rule through current state and value
                    if (tm.getHead().getCurrentState().equals(rule[0]))//locate the correct rule by state firstly
                    {
                        index = tm.getHead().getCurrentCell();//get current index
                        if (tm.getTape().get(index) == rule[1].charAt(0)) {// Then locate the correct rule by input
                            //judge the next state,is accepted or rejected or qx
                            if (rule[2].equals(tm.getAcceptState())) {
                                utm.writeOnCurrentCell(rule[3].charAt(0));
                                utm.updateHeadState(rule[2]);
                                utm.displayHaltState(HaltState.ACCEPTED);
                                break loop; //if the new symbol is qa, jump over the loop
                            } else if (rule[2].equals(tm.getRejectState())) {
                                utm.updateHeadState(rule[2]);
                                utm.displayHaltState(HaltState.REJECTED);
                                break loop; //if the new symbol is qr, jump over the loop
                            } else {
                                utm.writeOnCurrentCell(rule[3].charAt(0));//set new state
                                if (extractDirection(rule[4]).equals(RESET)) tm.getHead().reset();
                                else utm.moveHead(extractDirection(rule[4]), isAnimation);
                                utm.updateHeadState(rule[2]);//set new symbol
                                break;//if the next state is qx, change the state and move head
                            }
                        }
                    }
                }
            }
        }
    }



    public static void main(String[] args){

        if(args.length !=3){

            System.out.println("Usage: java -jar pratical-37849956.jar \"desc_abspath\" "+"\"input\" l--animation/--nonanimation");
            System.exit(0);
        }
        UniversalTuringMachine utm =new UniversalTuringMachine();
        LRTM extendUtm = new LRTM(utm, args[2]);
        if(args[2].equals("--animation")) extendUtm.displayWindow();
        TuringMachine tm = extendUtm.createTMFromFile(args[0]);
        extendUtm.executeTM(tm, args[1]);

    }


}
