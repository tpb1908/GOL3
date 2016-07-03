import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * Created by Theo on 05/09/2015.
 */
public class Cell implements Serializable {

    public float isAlive;
    private boolean killed = false;
    public boolean isBlock;
    private float generationsSurvived;
    private float lowerSurvivalBound;
    private float upperSurvivalBound;
    private float birthLowerBound;
    private float birthUpperBound;
	private float colourCode;
    private float foodLevel;
    private Random random;
    private float generationsBetweenSplit;
    private Object[] cellTemplate;
    private Object[] environmentTemplate;


    private int lowTempResistance;
    private boolean lowTempResistanceUsed = true;

    private int highTempResistance;
    private boolean highTempResistanceUsed = true;

    private int humidityResistance;
    private boolean humidityResistanceUsed = true;

    private int acidResistance;
    private boolean acidResistanceUsed = true;

    private int alkaliResistance;
    private boolean alkaliResistanceUsed = true;

    private int atmosphericPressureResistance;
    private boolean atmosphericPressureResistanceUsed = true;

    private int radiationResistance;
    private boolean radiationResistanceUsed = true;

    private double minOxygenLevel;
    private boolean minOxygenLevelUsed = true;

    private int toxicityResistance;
    private boolean toxicityResistanceUsed = true;

    private int environmentToxicity;
    private int environmentTemperature;
    private int environmentHumidity;
    private int environmentPH;
    private int environmentPressure;
    private int environmentOxygenLevel;
    public int environmentRadiationLevel;


    public Cell() {
        isAlive = 0f;
        generationsSurvived = 0;
        generationsBetweenSplit = 1f;
        lowerSurvivalBound = 2;
        upperSurvivalBound = 3f;
        birthUpperBound = 5;
        birthLowerBound = 1;
        random = new Random();
        colourCode = 0;
        foodLevel = 0;



    }

    public void update(List<Cell> surroundingCells) {
        if(!isBlock && isAlive > 0) {
            foodLevel -= 0.5;
            int sum = 0;
            killed = false;
            //TODO- Implement the evolution here
            //There should be some other variables. No idea what
            for (Cell cell : surroundingCells) {
                //Iterating through the surrounding cells
                sum += cell.getIsAlive();
            }
            if((int) environmentTemplate[4] > (int) cellTemplate[20] && (boolean) cellTemplate[21]) {
                //System.out.println("Killed due to radiation");
                kill();
            }else if((int)environmentTemplate[1] > (int) cellTemplate[12] && (boolean) cellTemplate[13]) {
                //System.out.println("Killed due to humidity");
                kill();
            }
            if(foodLevel < 0) {
                kill();
            }
//            }else if(environmentOxygenLevel < minOxygenLevel && minOxygenLevelUsed) {
//                System.out.println("Killed due to oxygen level");
//                kill();
//            } else if(environmentPH > acidResistance && acidResistanceUsed) {
//                System.out.println("Killed due to acid: Acid resistance of " + acidResistance + " and PH of " + environmentPH);
//                kill();
//            }  else if(environmentPH < alkaliResistance && alkaliResistanceUsed) {
//                System.out.println("Killed due to alkali");
//                kill();
//            } else if(environmentPressure > atmosphericPressureResistance && atmosphericPressureResistanceUsed) {
//                System.out.println("Killed due to pressure");
//                kill();
//            } else if(environmentToxicity > toxicityResistance && toxicityResistanceUsed) {
//                System.out.println("Killed due to toxicity");
//                kill();
//            }

            else if (sum < lowerSurvivalBound || sum > upperSurvivalBound /*  Placeholder- If sum is between survival upper and lower bounds */) {
                //System.out.println("Killing myself with sum of " + sum);
                kill();
            }



            if (sum < lowerSurvivalBound || sum > upperSurvivalBound /*  Placeholder- If sum is between survival upper and lower bounds */) {
                //System.out.println("Killing myself with sum of " + sum);
                kill();
            } else if (sum > birthLowerBound && sum < birthUpperBound /*  Placeholder- If sum is between the birth upper and lower bounds */) {
                //System.out.println("Creating a new cell");
                if(generationsSurvived%generationsBetweenSplit == 0 && !killed) {
                    int i = 0;
                    int rnd = random.nextInt(surroundingCells.size() - 1);
                    while (surroundingCells.get(rnd).isAlive != 0 && !surroundingCells.get(rnd).isBlock && !killed) {
                        rnd = random.nextInt(surroundingCells.size() - 1);
                        if (i++ > 7) {
                            break;
                        }
                        surroundingCells.get(rnd).split(this);
                    }
                }
            }
        } else {
            kill();
        }
        //The cell does nothing
        if (isAlive != 0) {
            generationsSurvived++;
        }
    }

    public void split(Cell cell) {
        lowerSurvivalBound = cell.getLowerSurvivalBound();
        upperSurvivalBound = cell.getUpperSurvivalBound();
        birthLowerBound = cell.getBirthLowerBound();
        birthUpperBound = cell.getBirthUpperBound();
        isAlive = 1;
        generationsSurvived = 0;
        applyTemplate(cell.getCellTemplate());

    }

    public void kill() {
        killed = true;
        isAlive = 0;
        generationsSurvived = 0;
        //NO need to sort the rest, they are reset on split()
    }

    public void macroEvolve() {}

    public int generateColourCode() {
        if(isBlock) {
            return 689127;
        } else if(isAlive > 0) {
            return 16711680;
        } else {
            return 16777215;
        }
    }

    public void applyTemplate(Object[] newValues){
        cellTemplate = newValues;
        //isBlock = (boolean) newValues[2];
        if(!isBlock) {
            isAlive = (int) newValues[3];
            birthLowerBound = (int) newValues[4];
            birthUpperBound = (int) newValues[5];
            lowerSurvivalBound = (int) newValues[6];
            upperSurvivalBound = (int) newValues[7];
            lowTempResistance = (int) newValues[8];
            lowTempResistanceUsed = (boolean) newValues[9];
            highTempResistance = (int) newValues[10];
            highTempResistanceUsed = (boolean) newValues[11];
            humidityResistance = (int) newValues[12];
            humidityResistanceUsed = (boolean) newValues[13];
            acidResistance = (int) newValues[14];
            acidResistanceUsed = (boolean) newValues[15];
            alkaliResistance = (int) newValues[16];
            alkaliResistanceUsed = (boolean) newValues[17];
            atmosphericPressureResistance = (int) newValues[18];
            atmosphericPressureResistanceUsed = (boolean) newValues[19];
            radiationResistance = (int) newValues[20];
            radiationResistanceUsed = (boolean) newValues[21];
            minOxygenLevel = (double) newValues[22];
            minOxygenLevelUsed = (boolean) newValues[23];
            toxicityResistance = (int) newValues[24];
            toxicityResistanceUsed = (boolean) newValues[25];
        }
    }

    public void applyEnvironmentTemplate(Object[] o) {
        environmentTemplate = o;
        environmentTemperature = (int) o[0];
        environmentHumidity = (int) o[1];
        environmentPH = (int) o[2];
        environmentPressure = (int) o[3];
        environmentRadiationLevel = (int) o[4];
        environmentOxygenLevel = (int) o[5];
        environmentToxicity = (int) o[6];
        foodLevel += (int) o[7];
    }

    //Most likely an un-needed method
	public void setColourCode(int colourCode){
		this.colourCode = colourCode;
	}

    //Getters
    public float getIsAlive() {
        return isAlive;
    }

    public Object[] getCellTemplate() {
        return cellTemplate;
    }




    public float getLowerSurvivalBound() {
        return lowerSurvivalBound;
    }

    public float getUpperSurvivalBound() {
        return upperSurvivalBound;
    }

    public float getBirthLowerBound() {
        return birthLowerBound;
    }

    public float getBirthUpperBound() {
        return birthUpperBound;
    }

    public float getGenerationsSurvived(){
        return generationsSurvived;
    }

    public void increaseFood(int increase) {
        foodLevel += increase;
    }
}
