import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Graphical user interface that allows filtering of online course data and displaying of useful averages
 */
public class CourseDataFilteringGui extends JFrame {

    /** width of the dialog box */
    private final static int DIALOG_WIDTH = 510;
    /** height of the dialog box */
    private final static int DIALOG_HEIGHT = 490;

    /** width of filter combo boxes */
    private final static int FILTER_COMBO_WIDTH = 175;
    /** height of filter combo boxes and labels */
    private final static int FILTER_WIDGET_HEIGHT = 30;
    /** starting x position for the first filtering widget */
    private final static int FILTER_X_START = 25;
    /** offset to add to the x position for subsequent buttons in horizontal row */
    private final static int FILTER_X_BUTTON_OFFSET = 100;
    /** starting y position for the first filtering widget */
    private final static int FILTER_Y_START = 25;
    /** offset to add to the y position for subsequent filtering widgets */
    private final static int FILTER_Y_OFFSET = 25;
    /** additional offset to add to the y position between groups of filtering widgets, e.g., after label/combo */
    private final static int FILTER_Y_ADDITIONAL_GROUP_OFFSET = 20;

    /** starting x position for the first stats widget */
    private final static int STATS_X_START = 250;
    /** starting y position for the first stats widget */
    private final static int STATS_Y_START = 50;
    /** offset to add to the y position for subsequent stats widgets */
    private final static int STATS_Y_OFFSET = 40;
    /** height of stats widgets */
    private final static int STATS_WIDGET_HEIGHT = 30;
    /** width of stats labels */
    private final static int STATS_LABEL_WIDTH = 300;

    /** current Y position for filtering widgets; update after each widget is added */
    private int filterYCurrent = FILTER_Y_START;
    /** current Y position for stats widgets; update after each widget is added */
    private int statsYCurrent = STATS_Y_START;

    /**
     * Constructor; displays and shows the dialog representing the application
     * @param courseDb  the associated online course database from which to draw data; must not be null
     */
    public CourseDataFilteringGui(OnlineCourseDatabaseInterface courseDb) {
        throwIfNull(courseDb, "courseDb");

        setTitle("Online Course Data Filtering and Stats GUI");
        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        getContentPane().add(mainPanel);

        JComboBox<String> filterExperienceLevelCombo = addFilteringCombo("Experience Level:", mainPanel, courseDb.getExperienceLevelOptions());
        JComboBox<String> filterCourseTypeCombo = addFilteringCombo("Course Type:", mainPanel, courseDb.getCourseTypeOptions());
        JComboBox<String> filterPlatformCombo = addFilteringCombo("Platform:", mainPanel, courseDb.getPlatformOptions());
        JComboBox<String> filterCompletionStatusCombo = addFilteringCombo("Completion Status:", mainPanel, courseDb.getCompletionStatusOptions());
        JComboBox<String> filterDropoutReasonCombo = addFilteringCombo("Dropout Reason:", mainPanel, courseDb.getDropoutReasonOptions());

        filterYCurrent += FILTER_Y_OFFSET;  // extra space before buttons

        //TODO: remove filter button?  If we react to combo changes, we don't need it, with clever listener work
        JButton filterButton = new JButton("Filter");
        filterButton.setBounds(FILTER_X_START, filterYCurrent, 75, FILTER_WIDGET_HEIGHT);
        mainPanel.add(filterButton);

        JButton resetButton = new JButton("Reset");
        resetButton.setBounds(FILTER_X_START + FILTER_X_BUTTON_OFFSET, filterYCurrent, 75, FILTER_WIDGET_HEIGHT);
        mainPanel.add(resetButton);

        JLabel statsRecordCount = addStatsLabel(mainPanel);
        JLabel statsHoursLabelAvg = addStatsLabel(mainPanel);
        JLabel statsCourseDurationAvg = addStatsLabel(mainPanel);
        JLabel statsCompletionPctAvg = addStatsLabel(mainPanel);;
        JLabel statsSatisfactionAvg = addStatsLabel(mainPanel);

        //TODO: Create single listener for all dropdowns?
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // grab combo indexes (subtracting one for client-based indexing), update stat labels
                CourseStats stats = courseDb.calcFilteredAverages(
                        (byte) (filterExperienceLevelCombo.getSelectedIndex() - 1),
                        (byte) (filterCourseTypeCombo.getSelectedIndex() - 1),
                        (byte) (filterPlatformCombo.getSelectedIndex() - 1),
                        (byte) (filterCompletionStatusCombo.getSelectedIndex() - 1),
                        (byte) (filterDropoutReasonCombo.getSelectedIndex() - 1));
                statsRecordCount.setText(String.format("Filtered record count:  %d", stats.recordCount()));
                statsHoursLabelAvg.setText(String.format("Average hours per week: %.2f", stats.avgHrsPerWeek()));
                statsCourseDurationAvg.setText(String.format("Average course duration: %.2f", stats.avgCourseDuration()));
                statsCompletionPctAvg.setText(String.format("Average completion percentage: %.2f", stats.avgCompletionPercent()));
                statsSatisfactionAvg.setText(String.format("Average satisfaction rating: %.2f", stats.avgSatisfactionScore()));
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component comp : mainPanel.getComponents()) {
                    if (comp instanceof JComboBox) {
                        ((JComboBox<?>) comp).setSelectedIndex(0);
                    }
                }
                // click the Filter button so non-filtered results are displayed
                filterButton.doClick();
            }
        });

        filterExperienceLevelCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {filterButton.doClick();}});

        filterCourseTypeCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {filterButton.doClick();}});

        filterPlatformCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {filterButton.doClick();}});

        filterCompletionStatusCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {filterButton.doClick();}});

        filterDropoutReasonCombo.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {filterButton.doClick();}});

        filterButton.doClick();
        setVisible(true);
    }

    /**
     * Creates and adds to the specified panel, a label for the stats portion of the dialog
     * @param panel     panel to which to add the newly created label; must not be null
     * @return          newly created label
     */
    private JLabel addStatsLabel(JPanel panel) {
        throwIfNull(panel, "panel");
        JLabel label = new JLabel();
        label.setBounds(STATS_X_START, statsYCurrent, STATS_LABEL_WIDTH, STATS_WIDGET_HEIGHT);
        statsYCurrent += STATS_Y_OFFSET;
        panel.add(label);
        return label;
    }

    /**
     * Creates and adds a label and associated combo box to the filtering portion of the dialog
     * @param labelText     text for the newly created label
     * @param panel         panel to add the newly created label and combo box to
     * @param optionsArray  options to add to the combo box; will also add a "no filter" option at the start
     * @return              newly created combo box
     */
    private JComboBox<String> addFilteringCombo(String labelText, JPanel panel, String[] optionsArray) {
        throwIfNull(labelText, "labelText");
        throwIfNull(panel, "panel");
        throwIfNull(optionsArray, "optionsArray");

        JLabel label = new JLabel(labelText);
        label.setBounds(FILTER_X_START, filterYCurrent, FILTER_COMBO_WIDTH, FILTER_WIDGET_HEIGHT);
        filterYCurrent += FILTER_Y_OFFSET;
        panel.add(label);

        JComboBox<String> combo = new JComboBox<>(optionsArray);
        combo.insertItemAt("(no filter)", 0);
        combo.setSelectedIndex(0);
        combo.setBounds(FILTER_X_START, filterYCurrent, FILTER_COMBO_WIDTH, FILTER_WIDGET_HEIGHT);
        filterYCurrent += FILTER_Y_OFFSET + FILTER_Y_ADDITIONAL_GROUP_OFFSET;
        panel.add(combo);
        return combo;
    }

    /**
     * Throws an exception if the specified object reference is null
     * @param obj       object reference to check
     * @param context   variable name, used to construct an exception message
     */
    private static void throwIfNull(Object obj, String context) {
        if (obj == null) {
            throw new IllegalArgumentException(context + " must not be null");
        }
    }

}
