/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package YelpSwingUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

public class YelpUI extends javax.swing.JFrame {

    Connection dbConnection;
    HashMap<String, Integer> days;
    String oldQuery;

    /**
     * Creates new form YelpUI
     */
    public YelpUI() {
        oldQuery = null;
        dbConnection = DBConnect.getDBConnection();
        initComponents();
        back_button.setVisible(false);
        get_sub_categories.setVisible(false);
        days = new HashMap<String, Integer>();
        String vDay[] = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (int i = 0; i < vDay.length; i++) {
            days.put(vDay[i], i);
        }
        ChangeListener changeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent changeEvent) {
                get_sub_categories.doClick();
            }
        };
        for (Component c : CategoryPanel.getComponents()) {
            if (c.getClass().equals(javax.swing.JCheckBox.class)) {
                JCheckBox jcb = (JCheckBox) c;
                jcb.addChangeListener(changeListener);
            }
        }

    }

    private String buildQuery() {
        //User Query
        if (isUserQuery.isSelected()) {
            boolean isWhereInserted = false;
            String userQuery = "select * from users ";
            String userEnteredDate = ((JTextField) userDate.getDateEditor().getUiComponent()).getText();
            String userEnteredReviewCount = userReviewCount_value.getText();
            String userEnteredFriends = userFriends_value.getText();
            String userEnteredAvgStars = userAvgStars_value.getText();
            String userSelector = userOperator.getSelectedItem().toString();

            if (!userEnteredDate.equals("")) {
                try {
                    userQuery = userQuery + " where YELPING_SINCE > '" + userEnteredDate + "' ";
                    isWhereInserted = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!userEnteredReviewCount.equals("")) {
                String sysmbol = userReviewCount.getSelectedItem().toString();
                if (isWhereInserted) {
                    userQuery = userQuery + "  " + userSelector + " REVIEW_COUNT " + sysmbol + " " + userEnteredReviewCount + " ";
                } else {
                    userQuery = userQuery + " where " + " REVIEW_COUNT " + sysmbol + " " + userEnteredReviewCount + " ";
                    isWhereInserted = true;
                }

            }
            if (!userEnteredFriends.equals("")) {
                String sysmbol = userFriends.getSelectedItem().toString();
                if (isWhereInserted) {
                    userQuery = userQuery + "  " + userSelector + " FRIENDS_COUNT " + sysmbol + " " + userEnteredFriends + " ";
                } else {
                    userQuery = userQuery + " where " + " FRIENDS_COUNT " + sysmbol + " " + userEnteredFriends + " ";
                    isWhereInserted = true;
                }

            }
            if (!userEnteredAvgStars.equals("")) {
                String sysmbol = userAvgStars.getSelectedItem().toString();
                if (isWhereInserted) {
                    userQuery = userQuery + "  " + userSelector + " AVERAGE_STARS " + sysmbol + " " + userEnteredAvgStars + " ";
                } else {
                    userQuery = userQuery + " where " + " AVERAGE_STARS " + sysmbol + " " + userEnteredAvgStars + " ";
                    isWhereInserted = true;
                }

            }
            return userQuery;

        } else {
            //Business

            String mainQuery = "select * from business b where b.business_id in( ";
            String buisnessQuery = "select  distinct business_id from business ";
            boolean isCategoryWhereInlcued = false;
            String CheckinQuery = "Select distinct business_id from CHECK_IN";
            String reviewQuery = "Select distinct business_id from review";

            ArrayList<String> BusiCate = new ArrayList();
            for (Component c : CategoryPanel.getComponents()) {
                if (c.getClass().equals(javax.swing.JCheckBox.class)) {
                    JCheckBox check = (JCheckBox) c;
                    if (check.isSelected()) {
                        BusiCate.add(check.getText());
                    }
                }
            }

            if (BusiCate.size() > 0) {
                isCategoryWhereInlcued = true;
                buisnessQuery += " where business_id in ( select business_id from BUSINESS_CAT_SUB where CATEGORY_NAME in(";
                for (int i = 0; i < BusiCate.size(); i++) {
                    buisnessQuery = buisnessQuery + "'" + BusiCate.get(i).trim() + "'";
                    if (!(i + 1 == BusiCate.size())) {
                        buisnessQuery = buisnessQuery + " , ";
                    }
                }
                buisnessQuery = buisnessQuery + " ) ";
            }

            ArrayList<String> subCateSelected = new ArrayList();
            for (Component c : SubCategoryPanel.getComponents()) {
                if (c.getClass().equals(javax.swing.JCheckBox.class)) {
                    JCheckBox check = (JCheckBox) c;
                    if (check.isSelected()) {
                        subCateSelected.add(check.getText());
                    }
                }
            }
            if (subCateSelected.size() > 0) {
                if (!isCategoryWhereInlcued) {
                    buisnessQuery += " where business_id in ( select business_id from BUSINESS_CAT_SUB where SUB_CATEGORY_NAME in(";
                } else {
                    buisnessQuery += " AND  SUB_CATEGORY_NAME in(";
                }
                for (int i = 0; i < subCateSelected.size(); i++) {
                    buisnessQuery = buisnessQuery + "'" + subCateSelected.get(i).trim() + "'";
                    if (!(i + 1 == subCateSelected.size())) {
                        buisnessQuery = buisnessQuery + " , ";
                    }
                }
                buisnessQuery = buisnessQuery + " ) ";
            }
            if (isCategoryWhereInlcued) {
                buisnessQuery = buisnessQuery + " ) ";
            }

            //executeAppQuery(buisnessQuery);
            if (isCheckIn.isSelected()) {

                boolean isWhereInserted = false;
                String numberOfCheckin = check_count_value.getText();
                String fromHour = ck_from_hour.getText();
                String toHour = ck_to_hour.getText();

                if (!fromHour.equals("")) {
                    int day = days.get(ck_from_day.getSelectedItem().toString());
                    int hour = Integer.parseInt(fromHour);
                    CheckinQuery = CheckinQuery + " where DAYANDTIME >= " + (day + hour / 24f) + " ";
                    isWhereInserted = true;
                }
                if (!toHour.equals("")) {
                    int day = days.get(ck_to_day.getSelectedItem().toString());
                    int hour = Integer.parseInt(toHour);
                    if (isWhereInserted) {
                        CheckinQuery = CheckinQuery + " AND DAYANDTIME < " + (day + hour / 24f) + " ";
                    } else {
                        isWhereInserted = true;
                        CheckinQuery = CheckinQuery + "  where DAYANDTIME < " + (day + hour / 24f) + " ";
                    }
                }

                if (!numberOfCheckin.equals("")) {
                    String sysmbol = check_count.getSelectedItem().toString();

                    CheckinQuery = CheckinQuery + " group by business_id having sum(in_count)" + sysmbol + " " + numberOfCheckin + " ";
                }

            }

            if (isReview.isSelected()) {
                boolean isWhereInserted = false;
                String from_date = ((JTextField) review_from_date.getDateEditor().getUiComponent()).getText();
                String to_date = ((JTextField) review_to_date.getDateEditor().getUiComponent()).getText();
                String enteredStars = review_star_count.getText();
                String enteredVotes = review_vote_count.getText();

                if (!from_date.equals("")) {
                    try {
                        reviewQuery = reviewQuery + " where DATE_OF_REVIEW >= '" + from_date + "' ";
                        isWhereInserted = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!to_date.equals("")) {
                    try {
                        if (isWhereInserted) {
                            reviewQuery = reviewQuery + " AND DATE_OF_REVIEW < '" + to_date + "' ";
                        } else {
                            reviewQuery = reviewQuery + " where DATE_OF_REVIEW < '" + to_date + "' ";
                            isWhereInserted = true;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!enteredStars.equals("")) {
                    String sysmbol = review_stars.getSelectedItem().toString();
                    if (isWhereInserted) {
                        reviewQuery = reviewQuery + "  AND " + " STARS " + sysmbol + " " + enteredStars + " ";
                    } else {
                        reviewQuery = reviewQuery + " where " + " STARS " + sysmbol + " " + enteredStars + " ";
                        isWhereInserted = true;
                    }

                }

                if (!enteredVotes.equals("")) {
                    String sysmbol = review_votes.getSelectedItem().toString();
                    if (isWhereInserted) {
                        reviewQuery = reviewQuery + "  AND " + " VOTES " + sysmbol + " " + enteredStars + " ";
                    } else {
                        reviewQuery = reviewQuery + " where " + " VOTES " + sysmbol + " " + enteredStars + " ";
                        isWhereInserted = true;
                    }

                }

            }
            mainQuery = mainQuery + " " + buisnessQuery;
            if (isCheckIn.isSelected()) {
                mainQuery = mainQuery + " Intersect " + CheckinQuery;
            }
            if (isReview.isSelected()) {
                mainQuery = mainQuery + " Intersect " + reviewQuery;
            }
            mainQuery = mainQuery + " )";

            // System.out.println(mainQuery);
            return mainQuery;

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CategoryPanel = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox14 = new javax.swing.JCheckBox();
        jCheckBox15 = new javax.swing.JCheckBox();
        jCheckBox16 = new javax.swing.JCheckBox();
        jCheckBox17 = new javax.swing.JCheckBox();
        jCheckBox18 = new javax.swing.JCheckBox();
        jCheckBox19 = new javax.swing.JCheckBox();
        jCheckBox20 = new javax.swing.JCheckBox();
        jCheckBox21 = new javax.swing.JCheckBox();
        jCheckBox22 = new javax.swing.JCheckBox();
        jCheckBox23 = new javax.swing.JCheckBox();
        jCheckBox24 = new javax.swing.JCheckBox();
        jCheckBox25 = new javax.swing.JCheckBox();
        jCheckBox26 = new javax.swing.JCheckBox();
        jCheckBox27 = new javax.swing.JCheckBox();
        jCheckBox28 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        SubCategoryPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        QueryArea = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        get_sub_categories = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ResultTable = new javax.swing.JTable();
        ck_from_day = new javax.swing.JComboBox();
        ck_to_day = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        ck_from_hour = new javax.swing.JTextField();
        ck_to_hour = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        check_count_value = new javax.swing.JTextField();
        userFriends = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        userDate = new com.toedter.calendar.JDateChooser();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        check_count = new javax.swing.JComboBox();
        userReviewCount = new javax.swing.JComboBox();
        userAvgStars = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        userReviewCount_value = new javax.swing.JTextField();
        userFriends_value = new javax.swing.JTextField();
        userAvgStars_value = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        userOperator = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        review_from_date = new com.toedter.calendar.JDateChooser();
        review_to_date = new com.toedter.calendar.JDateChooser();
        review_stars = new javax.swing.JComboBox();
        review_star_count = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        review_votes = new javax.swing.JComboBox();
        review_vote_count = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        BuildQuery = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        isUserQuery = new javax.swing.JCheckBox();
        isCheckIn = new javax.swing.JCheckBox();
        isReview = new javax.swing.JCheckBox();
        back_button = new javax.swing.JButton();
        ExcuteQueryButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(java.awt.SystemColor.control);
        setForeground(java.awt.Color.lightGray);

        jLabel1.setText("Query : ");

        jLabel2.setText("Category");

        jCheckBox1.setText("Active Life");

        jCheckBox2.setText("Arts & Entertainment");

        jCheckBox3.setText("Automotive");

        jCheckBox4.setText("Car Rental");

        jCheckBox5.setText("Cafes");

        jCheckBox6.setText("Beauty & Spas");

        jCheckBox7.setText("Convenience Stores");

        jCheckBox8.setText("Dentists");

        jCheckBox9.setText("Doctors");

        jCheckBox10.setText("Drugstores");

        jCheckBox11.setText("Department Stores");

        jCheckBox12.setText("Education");

        jCheckBox13.setText("Event Planning & Services");

        jCheckBox14.setText("Flowers & Gifts");

        jCheckBox15.setText("Food");

        jCheckBox16.setText("Health & Medical");

        jCheckBox17.setText("Home Services");

        jCheckBox18.setText("Home & Garden");

        jCheckBox19.setText("Hospitals");

        jCheckBox20.setText("Hotels & Travel");

        jCheckBox21.setText("Hardware Stores");

        jCheckBox22.setText("Grocery");

        jCheckBox23.setText("Medical Centers");

        jCheckBox24.setText("Nurseries & Gardening");

        jCheckBox25.setText("Nightlife");

        jCheckBox26.setText("Restaurants");

        jCheckBox27.setText("Shopping");

        jCheckBox28.setText("Transportation");

        javax.swing.GroupLayout CategoryPanelLayout = new javax.swing.GroupLayout(CategoryPanel);
        CategoryPanel.setLayout(CategoryPanelLayout);
        CategoryPanelLayout.setHorizontalGroup(
            CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox8)
                    .addComponent(jCheckBox9)
                    .addComponent(jCheckBox10)
                    .addComponent(jCheckBox11)
                    .addComponent(jCheckBox12)
                    .addComponent(jCheckBox13)
                    .addComponent(jCheckBox14)
                    .addComponent(jCheckBox15)
                    .addComponent(jCheckBox16)
                    .addComponent(jCheckBox17)
                    .addComponent(jCheckBox18)
                    .addComponent(jCheckBox19)
                    .addComponent(jCheckBox20)
                    .addComponent(jCheckBox21)
                    .addComponent(jCheckBox22)
                    .addComponent(jCheckBox23)
                    .addComponent(jCheckBox24)
                    .addComponent(jCheckBox25)
                    .addComponent(jCheckBox26)
                    .addComponent(jCheckBox27)
                    .addComponent(jCheckBox28))
                .addContainerGap(135, Short.MAX_VALUE))
        );
        CategoryPanelLayout.setVerticalGroup(
            CategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox24)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox28)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(CategoryPanel);

        jLabel3.setText("SubCategory");

        javax.swing.GroupLayout SubCategoryPanelLayout = new javax.swing.GroupLayout(SubCategoryPanel);
        SubCategoryPanel.setLayout(SubCategoryPanelLayout);
        SubCategoryPanelLayout.setHorizontalGroup(
            SubCategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 278, Short.MAX_VALUE)
        );
        SubCategoryPanelLayout.setVerticalGroup(
            SubCategoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );

        jScrollPane2.setViewportView(SubCategoryPanel);

        QueryArea.setEditable(false);
        QueryArea.setColumns(20);
        QueryArea.setRows(5);
        jScrollPane3.setViewportView(QueryArea);

        jLabel4.setText("Users");

        jLabel5.setText("Checkin");

        jLabel6.setText("Reviews");

        get_sub_categories.setText("Get Subcategories");
        get_sub_categories.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                get_sub_categoriesActionPerformed(evt);
            }
        });

        jLabel7.setText("From");

        jLabel8.setText("To");

        ResultTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        ResultTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ResultTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(ResultTable);

        ck_from_day.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" }));

        ck_to_day.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT" }));

        jLabel9.setText("Day");

        jLabel10.setText("Hour");

        jLabel11.setText("Hour");

        jLabel12.setText("No of Checkins");

        userFriends.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", ">", "=" }));

        jLabel13.setText("Sign");

        jLabel14.setText("Value");

        jLabel15.setText("Day");

        jLabel16.setText("Member since");

        userDate.setDateFormatString("yyyy-MM");

        jLabel17.setText("Review count");

        jLabel18.setText("Number of friends");

        jLabel19.setText("Avg stars");

        check_count.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", ">", "=" }));

        userReviewCount.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", ">", "=" }));

        userAvgStars.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", ">", "=" }));

        jLabel20.setText("Value");

        jLabel21.setText("Value");

        jLabel22.setText("Value");

        jLabel23.setText("Select");

        userOperator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "AND", "OR" }));

        jLabel24.setText("From");

        jLabel25.setText("To");

        review_from_date.setDateFormatString("yyyy-MM-dd");

        review_to_date.setDateFormatString("yyyy-MM-dd");

        review_stars.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", ">", "=" }));

        jLabel26.setText("Stars");

        jLabel27.setText("value");

        review_votes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<", ">", "=" }));

        jLabel28.setText("Votes");

        jLabel29.setText("value");

        BuildQuery.setText("Build Query");
        BuildQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuildQueryActionPerformed(evt);
            }
        });

        isUserQuery.setText("Select for user");
        isUserQuery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isUserQueryActionPerformed(evt);
            }
        });

        isCheckIn.setText("Select for checkin");

        isReview.setText("select for review");

        back_button.setText("Previous Result");
        back_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                back_buttonActionPerformed(evt);
            }
        });

        ExcuteQueryButton.setText("Execute Query");
        ExcuteQueryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExcuteQueryButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(isUserQuery))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(13, 13, 13)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addGap(14, 14, 14))
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel9)
                                                            .addComponent(ck_from_day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(ck_to_day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(jLabel12))
                                                        .addGap(48, 48, 48)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel10)
                                                                .addGap(156, 156, 156)
                                                                .addComponent(review_from_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(ck_to_hour, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addGroup(layout.createSequentialGroup()
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                            .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(ck_from_hour, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGap(141, 141, 141))
                                                                            .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel26)
                                                                                .addGap(18, 18, 18)))
                                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                            .addComponent(review_to_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                            .addGroup(layout.createSequentialGroup()
                                                                                .addGap(71, 71, 71)
                                                                                .addComponent(jLabel27)))))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                                                                .addComponent(review_star_count, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                    .addComponent(review_stars, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                        .addComponent(jLabel11)
                                                                        .addGap(99, 99, 99)
                                                                        .addComponent(jLabel28)
                                                                        .addGap(18, 18, 18)
                                                                        .addComponent(review_votes, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jLabel29)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(review_vote_count, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                        .addGap(61, 61, 61)
                                                        .addComponent(jLabel13)
                                                        .addGap(47, 47, 47)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel14)
                                                            .addComponent(check_count_value, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(371, 371, 371))))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel5)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel15)
                                                        .addComponent(jLabel7)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(isCheckIn)
                                                .addGap(66, 66, 66)
                                                .addComponent(jLabel6)
                                                .addGap(18, 18, 18)
                                                .addComponent(isReview)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(back_button))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(20, 20, 20)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(51, 51, 51)
                                                .addComponent(jLabel23)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(userOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel24)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(jLabel16)
                                                            .addComponent(jLabel17)
                                                            .addComponent(jLabel18)
                                                            .addComponent(jLabel19))
                                                        .addGap(5, 5, 5)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(userDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(userFriends, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel21)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(userFriends_value, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(userReviewCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel20)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(userReviewCount_value, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                            .addGroup(layout.createSequentialGroup()
                                                                .addComponent(userAvgStars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jLabel22)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(userAvgStars_value, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                    .addComponent(jLabel25))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(140, 140, 140)
                                .addComponent(get_sub_categories)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BuildQuery)
                        .addGap(39, 39, 39)
                        .addComponent(ExcuteQueryButton)
                        .addGap(300, 300, 300)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(374, 374, 374)
                    .addComponent(check_count, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(975, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(get_sub_categories)
                                .addGap(1, 1, 1)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(17, 17, 17)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(jLabel6)
                                            .addComponent(isCheckIn)
                                            .addComponent(isReview))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel7)
                                        .addGap(13, 13, 13)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(jLabel10)
                                                .addComponent(jLabel15)
                                                .addComponent(jLabel24))
                                            .addComponent(review_from_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(ck_from_hour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel25))
                                            .addComponent(ck_from_day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(review_to_date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(back_button))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel8)
                                                .addGap(27, 27, 27)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(ck_to_day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(ck_to_hour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(16, 16, 16)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(jLabel11)
                                                    .addComponent(jLabel9))
                                                .addGap(15, 15, 15)
                                                .addComponent(jLabel12)
                                                .addGap(17, 17, 17)
                                                .addComponent(check_count_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(33, 33, 33)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(review_stars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(review_star_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel26)
                                                    .addComponent(jLabel27))
                                                .addGap(31, 31, 31)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                    .addComponent(review_votes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(review_vote_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel28)
                                                    .addComponent(jLabel29))))
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel13)
                                        .addGap(15, 15, 15)))
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(isUserQuery))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel23)
                                            .addComponent(userOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(48, 48, 48)
                                                .addComponent(jLabel1))))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addGap(27, 27, 27)
                                        .addComponent(jLabel17)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel18)
                                            .addComponent(userFriends, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel21)
                                            .addComponent(userFriends_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(22, 22, 22)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel19)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(userAvgStars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel22)
                                                .addComponent(userAvgStars_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(userDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(userReviewCount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel20)
                                            .addComponent(userReviewCount_value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BuildQuery)
                            .addComponent(ExcuteQueryButton))
                        .addContainerGap(13, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4)
                        .addGap(25, 25, 25))))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(315, 315, 315)
                    .addComponent(check_count, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(504, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void executeAppQuery(String Query, boolean isUserQurey) {
        try {
            QueryArea.setText(Query);
            Statement QueryStatemnet = dbConnection.createStatement();
            if (isUserQurey) {
                QueryStatemnet.executeUpdate("alter session set nls_date_format = 'yyyy-MM'");
            } else {
                QueryStatemnet.executeUpdate("alter session set nls_date_format = 'yyyy-MM-dd'");
            }
            ResultSet rs = QueryStatemnet.executeQuery(Query);
            ResultSetMetaData rsmd = rs.getMetaData();
            DefaultTableModel model = new DefaultTableModel();
            model.setColumnCount(rsmd.getColumnCount());
            Vector<String> cols = new Vector();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                cols.add(rsmd.getColumnName(i + 1));
            }
            model.setColumnIdentifiers(cols);
            while (rs.next()) {
                Vector<String> rows = new Vector();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    rows.add(rs.getString(rsmd.getColumnName(i + 1)));
                }
                model.addRow(rows);
            }
            ResultTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get_sub_categoriesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_get_sub_categoriesActionPerformed
        // TODO add your handling code here:
        String query = null;
        ArrayList<String> BusiCateg = new ArrayList();
        for (Component c : CategoryPanel.getComponents()) {
            if (c.getClass().equals(javax.swing.JCheckBox.class)) {
                JCheckBox jcb = (JCheckBox) c;
                if (jcb.isSelected()) {
                    BusiCateg.add(jcb.getText());
                }
            }
        }

        query = "select distinct SUB_CATEGORY_NAME from BUSINESS_CAT_SUB where CATEGORY_NAME in (";
        for (int i = 0; i < BusiCateg.size(); i++) {
            query = query + "'" + BusiCateg.get(i) + "'";
            if (!(i + 1 == BusiCateg.size())) {
                query = query + " , ";
            }

        }
        query = query + " )";
        // System.out.println(query);
        SubCategoryPanel.removeAll();
        SubCategoryPanel.repaint();
        if (BusiCateg.size() == 0) {
            return;
        }
        try {
            Statement subCatStatement = dbConnection.createStatement();
            ResultSet res = subCatStatement.executeQuery(query);
            subCatCheckBoxs = new ArrayList();
            while (res.next()) {
                JCheckBox newCheckBox = new JCheckBox();
                newCheckBox.setText(res.getString(1) + "\n");
                subCatCheckBoxs.add(newCheckBox);
            }
            SubCategoryPanel.setLayout(new GridLayout(0, 1, 10, 10));
            for (JCheckBox ch : subCatCheckBoxs) {
                SubCategoryPanel.add(ch);
                SubCategoryPanel.revalidate();
                SubCategoryPanel.repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_get_sub_categoriesActionPerformed

    private void BuildQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuildQueryActionPerformed
        // TODO add your handling code here:
        String query = buildQuery();
        QueryArea.setText(query);
    }//GEN-LAST:event_BuildQueryActionPerformed

    private void isUserQueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isUserQueryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isUserQueryActionPerformed

    private void ResultTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ResultTableMouseClicked
        // TODO add your handling code here:

        String reviewQuery = "";
        oldQuery = QueryArea.getText();
        int selectedRowID = ResultTable.getSelectedRow();
        if (isUserQuery.isSelected()) {
            String id = (String) ResultTable.getValueAt(selectedRowID, ResultTable.getColumnModel().getColumnIndex("USER_ID"));
            reviewQuery = reviewQuery + " Select * from review where USER_ID = '" + id + "'";
        } else {
            String id = (String) ResultTable.getValueAt(selectedRowID, ResultTable.getColumnModel().getColumnIndex("BUSINESS_ID"));
            reviewQuery = reviewQuery + " Select * from review where BUSINESS_ID = '" + id + "'";
        }
        back_button.setVisible(true);
        executeAppQuery(reviewQuery, false);

        QueryArea.setText(reviewQuery);

    }//GEN-LAST:event_ResultTableMouseClicked

    private void back_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_back_buttonActionPerformed
        // TODO add your handling code here:
        if (isUserQuery.isSelected()) {
            executeAppQuery(oldQuery, true);
        } else {
            executeAppQuery(oldQuery, false);
        }
        back_button.setVisible(false);
    }//GEN-LAST:event_back_buttonActionPerformed

    private void ExcuteQueryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExcuteQueryButtonActionPerformed
        // TODO add your handling code here:
        String query = buildQuery();
        if (isUserQuery.isSelected()) {
            executeAppQuery(query, true);
        } else {
            executeAppQuery(query, false);
        }
    }//GEN-LAST:event_ExcuteQueryButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(YelpUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(YelpUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(YelpUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(YelpUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new YelpUI().setVisible(true);
            }
        });
    }

    private ArrayList<JCheckBox> subCatCheckBoxs;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BuildQuery;
    private javax.swing.JPanel CategoryPanel;
    private javax.swing.JButton ExcuteQueryButton;
    private javax.swing.JTextArea QueryArea;
    private javax.swing.JTable ResultTable;
    private javax.swing.JPanel SubCategoryPanel;
    private javax.swing.JButton back_button;
    private javax.swing.JComboBox check_count;
    private javax.swing.JTextField check_count_value;
    private javax.swing.JComboBox ck_from_day;
    private javax.swing.JTextField ck_from_hour;
    private javax.swing.JComboBox ck_to_day;
    private javax.swing.JTextField ck_to_hour;
    private javax.swing.JButton get_sub_categories;
    private javax.swing.JCheckBox isCheckIn;
    private javax.swing.JCheckBox isReview;
    private javax.swing.JCheckBox isUserQuery;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox15;
    private javax.swing.JCheckBox jCheckBox16;
    private javax.swing.JCheckBox jCheckBox17;
    private javax.swing.JCheckBox jCheckBox18;
    private javax.swing.JCheckBox jCheckBox19;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox20;
    private javax.swing.JCheckBox jCheckBox21;
    private javax.swing.JCheckBox jCheckBox22;
    private javax.swing.JCheckBox jCheckBox23;
    private javax.swing.JCheckBox jCheckBox24;
    private javax.swing.JCheckBox jCheckBox25;
    private javax.swing.JCheckBox jCheckBox26;
    private javax.swing.JCheckBox jCheckBox27;
    private javax.swing.JCheckBox jCheckBox28;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private com.toedter.calendar.JDateChooser review_from_date;
    private javax.swing.JTextField review_star_count;
    private javax.swing.JComboBox review_stars;
    private com.toedter.calendar.JDateChooser review_to_date;
    private javax.swing.JTextField review_vote_count;
    private javax.swing.JComboBox review_votes;
    private javax.swing.JComboBox userAvgStars;
    private javax.swing.JTextField userAvgStars_value;
    private com.toedter.calendar.JDateChooser userDate;
    private javax.swing.JComboBox userFriends;
    private javax.swing.JTextField userFriends_value;
    private javax.swing.JComboBox userOperator;
    private javax.swing.JComboBox userReviewCount;
    private javax.swing.JTextField userReviewCount_value;
    // End of variables declaration//GEN-END:variables
}
