
/* Modify.java, created by Gregor Marston
*Assessment Part 2 – due 13th Jan 2020 */

package cm2100.diary;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author gregormarston
 */
public class Modify extends javax.swing.JFrame {
    
    Calendar cal = Calendar.getInstance();
    Main main;
    Output output;
    boolean x = false;
    boolean t = false;
    boolean r = false;
    int year;
    int untilYear;
    
    /**
     * Creates new form Modify
     */
    public Modify(Main m) {
        main = m;
        main.setEnabled(false);
        year = main.year;
        initComponents();
        hideEndTime();
        hideUntil();
        yearField.setText(String.valueOf(cal.get(Calendar.YEAR)));
        monthBox.setSelectedIndex(cal.get(Calendar.MONTH));
        dayBox.setSelectedIndex(cal.get(Calendar.DAY_OF_MONTH)-1);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                main.setEnabled(true);
                dispose();
            }
        });
    }
    
    public Modify(Output o, int i) {
        main = o.main;
        output = o;
        output.setEnabled(false);
        Appointment a = o.appointments.get(i);
        Date date = a.getDate();
        year = date.getYear();
        initComponents();
        hideEndTime();
        hideUntil();
        descriptionField.setText(a.getDescription());
        yearField.setText(String.valueOf(year));
        monthBox.setSelectedIndex(date.getMonth()-1);
        dayBox.setSelectedIndex(date.getDay()-1);
        if (a instanceof TimedAppointment) {
            startHourBox.setSelectedIndex(((TimedAppointment) a).getStartTime().getHour()+1);
            startMinBox.setSelectedIndex(((TimedAppointment) a).getStartTime().getMinute()+1);
            endHourBox.setSelectedIndex(((TimedAppointment) a).getEndTime().getHour());
            endMinBox.setSelectedIndex(((TimedAppointment) a).getEndTime().getMinute());
        }
        if (a instanceof RepeatAppointment) {
            repeatBox.setSelectedIndex(((RepeatAppointment) a).getRepeatType().ordinal()+1);
            untilYear = ((RepeatAppointment) a).getEndDate().getYear();
            untilYearField.setText(String.valueOf(untilYear));
            untilMonthBox.setSelectedIndex(((RepeatAppointment) a).getEndDate().getMonth()-1);
            untilDayBox.setSelectedIndex(((RepeatAppointment) a).getEndDate().getDay()-1);
        }
        x = true;
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                output.setEnabled(true);
                dispose();
            }
        });
    }
    
    private void hideEndTime() {
        endLabel.setVisible(false);
        endHourBox.setVisible(false);
        colonLabel2.setVisible(false);
        endMinBox.setVisible(false);
    }
    
    private void hideUntil() {
        untilLabel.setVisible(false);
        untilDayBox.setVisible(false);
        untilMonthBox.setVisible(false);
        untilYearField.setVisible(false);
    }
    
    private void showEndTime() {
        if (startHourBox.getSelectedIndex() != 0 && startMinBox.getSelectedIndex() != 0) {
            if (startHourBox.getSelectedIndex() == 24) {
                endHourBox.setSelectedIndex(23);
                endMinBox.setSelectedIndex(59);
            } else {
                endHourBox.setSelectedIndex(startHourBox.getSelectedIndex());
                endMinBox.setSelectedItem(startMinBox.getSelectedItem());
            }
            endLabel.setVisible(true);
            endHourBox.setVisible(true);
            colonLabel2.setVisible(true);
            endMinBox.setVisible(true);
            t = true;
        } else {
            hideEndTime();
            t = false;
        }
    }
    
    private void showUntil(int i) {
        if (i != 0) {
            untilYear = year;
            untilYearField.setText(String.valueOf(year));
            initDayBox(untilDayBox, Date.daysInMonth(year, monthBox.getSelectedIndex()+1));
            untilDayBox.setSelectedIndex(dayBox.getSelectedIndex());
            untilMonthBox.setSelectedIndex(monthBox.getSelectedIndex());
            switch (i) {
                case 1:
                    incDay();
                    break;
                case 2:
                    incWeek();
                    break;
                case 3:
                    incYear();
                    break;
            }
            untilLabel.setVisible(true);
            untilDayBox.setVisible(true);
            untilMonthBox.setVisible(true);
            untilYearField.setVisible(true);
            r = true;
        } else {
            hideUntil();
            r = false;
        }
    }
    
    private void incDay() {
        if (dayBox.getSelectedIndex()+1 == dayBox.getItemCount()) {
            untilDayBox.setSelectedIndex(0);
            incMonth();
        } else {
            untilDayBox.setSelectedIndex(dayBox.getSelectedIndex()+1);
        }
    }
    
    private void incWeek() {
        if (dayBox.getSelectedIndex()+8 > dayBox.getItemCount()) {
            untilDayBox.setSelectedIndex((dayBox.getSelectedIndex()+7)-dayBox.getItemCount());
            incMonth();
        } else {
            untilDayBox.setSelectedIndex(dayBox.getSelectedIndex()+7);
        }
    }
    
    private void incMonth() {
        if (monthBox.getSelectedIndex() == 11) {
            untilMonthBox.setSelectedIndex(0);
            incYear();
        } else {
            untilMonthBox.setSelectedIndex(monthBox.getSelectedIndex()+1);
        }
    }
    
    private void incYear() {
        if (year < 9999) {
            untilYearField.setText(String.valueOf(year+1));
            getUntilYear();
        } else {
            untilMonthBox.setSelectedIndex(11);
            untilDayBox.setSelectedIndex(30);
        }
    }
    
    private void getYear() {
        int y = main.validateYear(yearField.getText());
        if (y > 0) {
            year = y;
            initDayBox(dayBox, Date.daysInMonth(y, monthBox.getSelectedIndex()+1));
        }
    }
    
    private void getUntilYear() {
        int y = main.validateYear(untilYearField.getText());
        if (y > 0) {
            untilYear = y;
            initDayBox(untilDayBox, Date.daysInMonth(y, untilMonthBox.getSelectedIndex()+1));
        }
    }
    
    private void initDayBox(JComboBox j, int d) {
        int s = j.getSelectedIndex();
        String days[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
        DefaultComboBoxModel model = new DefaultComboBoxModel(days);
        j.setModel(model);
        if ((s+1) > d) {
            j.setSelectedIndex(d-1);
        } else {
            j.setSelectedIndex(s);
        }
        while (d < j.getItemCount()) {
            j.removeItemAt(j.getItemCount()-1);
        }
    }
    
    private void add(Appointment a) {
        if (x) {
            if (JOptionPane.showConfirmDialog(null, "Modify appointment?") == 0) {
                output.modify(a);
                dispose();
                output.setEnabled(true);
            }
        } else {
        main.diary.add(a);
        main.diary.sortByDateTime();
        main.setDays();
        dispose();
        main.setEnabled(true);
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

        descriptionField = new javax.swing.JTextField();
        startHourBox = new javax.swing.JComboBox<>();
        endHourBox = new javax.swing.JComboBox<>();
        startMinBox = new javax.swing.JComboBox<>();
        endMinBox = new javax.swing.JComboBox<>();
        confirmButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        colonLabel2 = new javax.swing.JLabel();
        startLabel = new javax.swing.JLabel();
        untilLabel = new javax.swing.JLabel();
        endLabel = new javax.swing.JLabel();
        untilDayBox = new javax.swing.JComboBox<>();
        untilMonthBox = new javax.swing.JComboBox<>();
        untilYearField = new javax.swing.JTextField();
        colonLabel1 = new javax.swing.JLabel();
        repeatLabel = new javax.swing.JLabel();
        repeatBox = new javax.swing.JComboBox<>();
        dayBox = new javax.swing.JComboBox<>();
        monthBox = new javax.swing.JComboBox<>();
        yearField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        descriptionField.setText("Description");
        descriptionField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descriptionFieldFocusGained(evt);
            }
        });

        startHourBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        startHourBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startHourBoxActionPerformed(evt);
            }
        });

        endHourBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));

        startMinBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "", "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        startMinBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMinBoxActionPerformed(evt);
            }
        });

        endMinBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));

        confirmButton.setText("Confirm");
        confirmButton.setPreferredSize(new java.awt.Dimension(115, 29));
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.setPreferredSize(new java.awt.Dimension(115, 29));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        colonLabel2.setText(":");

        startLabel.setText("Start Time");

        untilLabel.setText("Until");

        endLabel.setText("End Time");

        untilDayBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        untilMonthBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
        untilMonthBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                untilMonthBoxActionPerformed(evt);
            }
        });

        untilYearField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        untilYearField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                untilYearFieldKeyReleased(evt);
            }
        });

        colonLabel1.setText(":");

        repeatLabel.setText("Repeat");

        repeatBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Never", "Daily", "Weekly", "Yearly" }));
        repeatBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repeatBoxActionPerformed(evt);
            }
        });

        dayBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        monthBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" }));
        monthBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthBoxActionPerformed(evt);
            }
        });

        yearField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        yearField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                yearFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionField)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startHourBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colonLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startMinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(endLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endHourBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(colonLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endMinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(repeatLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(repeatBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(untilLabel))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dayBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monthBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(yearField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(untilDayBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(untilMonthBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(untilYearField, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(descriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yearField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dayBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(monthBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startLabel)
                    .addComponent(startHourBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colonLabel1)
                    .addComponent(startMinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(endHourBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colonLabel2)
                    .addComponent(endMinBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(endLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(repeatLabel)
                    .addComponent(repeatBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(untilLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(untilYearField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(untilDayBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(untilMonthBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void descriptionFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descriptionFieldFocusGained
        descriptionField.selectAll();
    }//GEN-LAST:event_descriptionFieldFocusGained

    private void startHourBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startHourBoxActionPerformed
        showEndTime();
    }//GEN-LAST:event_startHourBoxActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        String d = descriptionField.getText();
        Date sd = new Date(year, monthBox.getSelectedIndex()+1, dayBox.getSelectedIndex()+1);
        Date ed = new Date(untilYear, untilMonthBox.getSelectedIndex()+1, untilDayBox.getSelectedIndex()+1);
        Time st = new Time(startHourBox.getSelectedIndex()-1, startMinBox.getSelectedIndex()-1);
        Time et = new Time(endHourBox.getSelectedIndex(), endMinBox.getSelectedIndex());;
        RepeatType rt = RepeatType.DAILY;
        switch (repeatBox.getSelectedIndex()) {
                            case 1:
                                rt = RepeatType.DAILY;
                                break;
                            case 2:
                                rt = RepeatType.WEEKLY;
                                break;
                            case 3:
                                rt = RepeatType.YEARLY;
                                break;
                        }
        if (d.isEmpty() || d.isBlank()) {
            JOptionPane.showMessageDialog(null, "Please enter a description.");
            descriptionField.requestFocus();
        } else {
            if (main.validateYear(yearField.getText())<0) {
                JOptionPane.showMessageDialog(null, "Please enter a year between 1 and 9999.");
                yearField.requestFocus();
            } else {
                if ((st.getHour() < 0 && st.getMinute() >= 0) || (st.getHour() >= 0 && st.getMinute() < 0)) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid start time.");
                    if (st.getHour() < 0) {
                        startHourBox.requestFocus();
                    } else {
                        startMinBox.requestFocus();
                    }
                } else {
                    if (t && st.compareTo(et) >= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a valid end time.");
                        if (st.getHour() > et.getHour()) {
                            endHourBox.requestFocus();
                        } else {
                            endMinBox.requestFocus();
                        }
                    } else {
                        if (r && sd.compareTo(ed) >= 0) {
                            JOptionPane.showMessageDialog(null, "Please enter a valid end date.");
                            if (year > untilYear) {
                                untilYearField.requestFocus();
                            } else if (monthBox.getSelectedIndex() > untilMonthBox.getSelectedIndex()) {
                                untilMonthBox.requestFocus();
                            } else {
                                untilDayBox.requestFocus();
                            }
                        } else {
                            if (t && r) {
                            add(new TimedRepeatAppointment(d, sd, ed, st, et, rt));
                            } else if (t) {
                                add(new TimedAppointment(d, sd, st, et));
                            } else if (r) {
                                add(new UntimedRepeatAppointment(d, sd, ed, rt));
                            } else {
                                add(new UntimedAppointment(d, sd));
                            }
                        } 
                    }
                }
            }
        }
    }//GEN-LAST:event_confirmButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        dispose();
        if (x) {
            output.setEnabled(true);
        } else {
            main.setEnabled(true);
        }
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void startMinBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMinBoxActionPerformed
        showEndTime();
    }//GEN-LAST:event_startMinBoxActionPerformed

    private void repeatBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repeatBoxActionPerformed
        showUntil(repeatBox.getSelectedIndex());
    }//GEN-LAST:event_repeatBoxActionPerformed

    private void monthBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthBoxActionPerformed
        getYear();
    }//GEN-LAST:event_monthBoxActionPerformed

    private void untilMonthBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_untilMonthBoxActionPerformed
        initDayBox(untilDayBox, Date.daysInMonth(untilYear, untilMonthBox.getSelectedIndex()+1));
    }//GEN-LAST:event_untilMonthBoxActionPerformed

    private void yearFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_yearFieldKeyReleased
        getYear();
    }//GEN-LAST:event_yearFieldKeyReleased

    private void untilYearFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_untilYearFieldKeyReleased
        getUntilYear();
    }//GEN-LAST:event_untilYearFieldKeyReleased
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel colonLabel1;
    private javax.swing.JLabel colonLabel2;
    private javax.swing.JButton confirmButton;
    private javax.swing.JComboBox<String> dayBox;
    private javax.swing.JTextField descriptionField;
    private javax.swing.JComboBox<String> endHourBox;
    private javax.swing.JLabel endLabel;
    private javax.swing.JComboBox<String> endMinBox;
    private javax.swing.JComboBox<String> monthBox;
    private javax.swing.JComboBox<String> repeatBox;
    private javax.swing.JLabel repeatLabel;
    private javax.swing.JComboBox<String> startHourBox;
    private javax.swing.JLabel startLabel;
    private javax.swing.JComboBox<String> startMinBox;
    private javax.swing.JComboBox<String> untilDayBox;
    private javax.swing.JLabel untilLabel;
    private javax.swing.JComboBox<String> untilMonthBox;
    private javax.swing.JTextField untilYearField;
    private javax.swing.JTextField yearField;
    // End of variables declaration//GEN-END:variables
}