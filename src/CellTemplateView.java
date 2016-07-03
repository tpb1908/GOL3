import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;


/**
 * Created by Theo on 13/09/2015.
 */
public class CellTemplateView extends JFrame {

    private WindowManager parentManager;
    private Object[] cellTemplate;
    private Object[] lastCellTemplate;
    private MigLayout g;
    private JButton OK;
    private JButton Cancel;
    private JSlider[] sliders;
    private JCheckBox[] checkboxes;
    private boolean firstRun = true;

    private JCheckBox allRandom;
    private JLabel allRandomLabel;
    private boolean allRandomBool = false;

    private JCheckBox isBlock;
    private JLabel isBlockLabel;
    private boolean isBlockBool = false;

    private JCheckBox isAlive;
    private JLabel isAliveLabel;
    private int isAliveInt = 1;

    private JLabel drawSizeLabel;
    private JSlider drawSizeSlider;
    private int drawSizeInt;

    private int lowerBirthBound = 2;

    private int upperBirthBound = 4;

    private int lowerSurvivalBound = 2;

    private int upperSurvivalBound = 4;

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








    private JScrollPane scrollPane;
    private JPanel scrollPanel;
    private MigLayout scrollLayout;

    public CellTemplateView(WindowManager parentManager){
        this.parentManager = parentManager;
        cellTemplate = new Object[100];

        lastCellTemplate = cellTemplate;
        sliders = new JSlider[100];
        checkboxes = new JCheckBox[100];
        g = new MigLayout("wrap 3");
        setLayout(g);
        scrollLayout = new MigLayout("wrap 3");
        scrollPanel = new JPanel(scrollLayout);
        scrollPane = new JScrollPane(scrollPanel);
        OK = new JButton();
        OK.setText("Ok");
        Cancel = new JButton();
        Cancel.setText("Cancel");

        allRandom = new JCheckBox();
        allRandomLabel = new JLabel("Randomise all traits");
        add(allRandomLabel);
        add(allRandom, "wrap 15");

        drawSizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 50, 3);
        Hashtable<Integer, JLabel> map = new Hashtable<Integer, JLabel>();
        map.put(1, new JLabel("1"));
        map.put(10, new JLabel("10"));
        map.put(20, new JLabel("20"));
        map.put(30, new JLabel("30"));
        map.put(40, new JLabel("40"));
        map.put(50, new JLabel("50"));
        drawSizeSlider.setLabelTable(map) ;
        drawSizeSlider.setPaintTicks(true);
        drawSizeSlider.setMinorTickSpacing(2);
        drawSizeSlider.setPaintLabels(true);
        drawSizeLabel = new JLabel("Draw size");
        //TODO- Add the rest of the objects here

        positionUI();
        addListeners();
        this.setMinimumSize(new Dimension(530, 700));
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    public void positionUI() {
        generateCellTemplate();
        setLastCellTemplate();
        add(drawSizeLabel, "wrap 15");
        add(drawSizeSlider, "wrap 15");

        scrollPanel.setMinimumSize(new Dimension(500, 450));
        scrollPane.setMinimumSize(new Dimension(500, 450));
        scrollPanel.setMaximumSize(new Dimension(500, 450));
        scrollPane.setMaximumSize(new Dimension(500, 450));
        scrollPanel.setLayout(scrollLayout);
        scrollPanel.setVisible(true);
        scrollPane.setVisible(true);
        scrollPane.add(scrollPanel);
        scrollPane.setViewportView(scrollPanel);
        add(scrollPane, "span, wrap 15");
        sliders[0] = drawSizeSlider;

        addVariableChanger("Cold resistance", 10, -50, 0, 0, 8, 9);
        addVariableChanger("Heat resistance", 10, 0, 50, 0, 10, 11); //Heat resistance, 0 to 50C
        addVariableChanger("Humidity resistance", 10, 0, 100, 10, 12, 13); //Humidity of 0-100%
        addVariableChanger("Acid resistance", 1, 7, 14, 7, 14, 15); //PH of 7-14
        addVariableChanger("Alkali resistance", 1, 0, 7, 7, 16, 17); //PH of 7-0
        addVariableChanger("Atmospheric pressure", 100, 0, 1000, 0, 18, 19); //bars
        addVariableChanger("Radiation resistance", 5,0, 30, 3, 20, 21); //Gy doesn't just kill, increases chance of mutation
        //addVariableChanger("Decay", 1, 1, 8, 3, 24, 25); //Increases the chance of disease
        addVariableChanger("Oxygen level", 10, 0, 100, 22, 22, 23); //Parts per million, can cause to split faster
        //Possibly add photosynthesis and food supplies per cell
        //addVariableChanger("Static electricity", 1, 1, 8,3, 28, 29); //define later
        addVariableChanger("Toxicity", 1, 0, 8, 0, 24, 25); //https://en.wikipedia.org/wiki/Cytotoxicity
        //Ground suitability for cell division


        add(OK);
        add(Cancel);

        this.setVisible(true);
    }

    public void addVariableChanger(String title, int majorTickSpacing,  int low, int high, int defaultValue, final int valueToChange, final int randomBooleanToChange) {
        JLabel label = new JLabel(title);

        JCheckBox UsedCheckBox = new JCheckBox();

        final JSlider  slider = new JSlider(low, high, defaultValue);
        slider.setMajorTickSpacing(majorTickSpacing);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        sliders[valueToChange] = slider;

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                switch (valueToChange) {

                    case 8:
                        lowTempResistance = slider.getValue();
                        break;
                    case 10:
                        highTempResistance = slider.getValue();
                        break;
                    case 12:
                        humidityResistance  = slider.getValue();
                        break;
                    case 14:
                        acidResistance = slider.getValue();
                        break;
                    case 16:
                        alkaliResistance = slider.getValue();
                        break;
                    case 18:
                        atmosphericPressureResistance = slider.getValue();
                        break;
                    case 20:
                        radiationResistance = slider.getValue();
                        break;
                    case 22:
                        minOxygenLevel = slider.getValue();
                        break;
                    case 24:
                        toxicityResistance = slider.getValue();
                        break;


                }

            }
        });


        UsedCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                boolean checkBox = true;
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    checkBox = false;
                }

                switch (randomBooleanToChange) {
                    case 9:
                        lowTempResistanceUsed = checkBox;
                        break;
                    case 11:
                        highTempResistanceUsed = checkBox;
                        break;
                    case 13:
                        humidityResistanceUsed = checkBox;
                        System.out.println("CellTemplateView humidity value = " + checkBox);
                        break;
                    case 15:
                        acidResistanceUsed = checkBox;
                        break;
                    case 17:
                        alkaliResistanceUsed = checkBox;
                        break;
                    case 19:
                        atmosphericPressureResistanceUsed = checkBox;
                        break;
                    case 21:
                        radiationResistanceUsed = checkBox;
                        break;
                    case 23:
                        minOxygenLevelUsed = checkBox;
                        break;
                    case 25:
                        toxicityResistanceUsed = checkBox;
                        break;
                }
            }
        });
        scrollPanel.add(label, "wrap 15");
        scrollPanel.add(UsedCheckBox);
        scrollPanel.add(slider, "span 2, wrap 15");

    }

    public void addListeners(){
        drawSizeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                drawSizeInt = drawSizeSlider.getValue();
            }
        });
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(firstRun) {
                   generateCellTemplate();
                    firstRun = false;
                    parentManager.setCellTemplate(cellTemplate);
                    parentManager.setCellViewSetUpDone();
                    if(parentManager.getEnvironmentVarViewSetUpDone()) {
                        parentManager.initGrid();
                    }
                    setVisible(false);
                } else {
                    generateCellTemplate();
                    parentManager.setCellTemplate(cellTemplate);
                    parentManager.setTextArea("Changed cell template values");
                    setVisible(false);
                }
            }
        });
        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreLastTemplate();
                parentManager.setTextArea("Cancelled changing cell template values");
                setVisible(false);
            }
        });

        allRandom.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    scrollPane.setVisible(false);
                    setMinimumSize(new Dimension(500, 300));
                    setSize(500, 300);
                    scrollPane.setMinimumSize(new Dimension(1, 1));
                    scrollPane.setSize(1,1);
                } else {
                    scrollPane.setVisible(true);
                    setMinimumSize(new Dimension(530, 1000));
                    scrollPane.setMinimumSize(new Dimension(500, 700));
                    scrollPane.setSize(500, 700);
                }
            }
        });
    }

    public void setLastCellTemplate() {
        lastCellTemplate = cellTemplate;
    }

    public void restoreLastTemplate(){
        cellTemplate = lastCellTemplate;
        for (int i = 0; i < sliders.length; i += 2) {
            if(sliders[i] != null && cellTemplate[i] != null && i != 2) {
                sliders[i].setValue((Integer) cellTemplate[i]);
            } else {
                System.out.println("Null slider");
            }
        }
    }

    public void generateCellTemplate() {

        cellTemplate[0] = drawSizeInt;
        cellTemplate[1] = allRandom;
        cellTemplate[2] = isBlockBool;
        cellTemplate[3] = isAliveInt;
        cellTemplate[4] = lowerBirthBound;
        cellTemplate[5] = upperBirthBound;
        cellTemplate[6] = lowerSurvivalBound;
        cellTemplate[7] = upperSurvivalBound;

        cellTemplate[8] = lowTempResistance;
        cellTemplate[9] = lowTempResistanceUsed;
        cellTemplate[10] = highTempResistance;
        cellTemplate[11] = highTempResistanceUsed;
        cellTemplate[12] = humidityResistance;
        cellTemplate[13] = humidityResistanceUsed;
        cellTemplate[14] = acidResistance;
        cellTemplate[15] = acidResistanceUsed;
        cellTemplate[16] = alkaliResistance;
        cellTemplate[17] = alkaliResistanceUsed;
        cellTemplate[18] = atmosphericPressureResistance;
        cellTemplate[19] = atmosphericPressureResistanceUsed;
        cellTemplate[20] = radiationResistance;
        cellTemplate[21] = radiationResistanceUsed;
        cellTemplate[22] = minOxygenLevel;
        cellTemplate[23] = minOxygenLevelUsed;
        cellTemplate[24] = toxicityResistance;
        cellTemplate[25] = toxicityResistanceUsed;

        parentManager.setCellTemplate(cellTemplate);

        //layout of cell template-
        /*
        0- Draw size
        1- Should each cell generate its own traits randomly
        2-isBlock
        3-isAlive
        4-lowerBirthBound
        5-upperBirthBound
        6-lowerSurvivalBound
        7-upperSurvivalBound
        8-generationsSurvived
        9- generationsBetweenSplit
        9+- The rest of the things
        */

    }

    public Object[] getValues() {
        return new Object[drawSizeInt];
    }
}
