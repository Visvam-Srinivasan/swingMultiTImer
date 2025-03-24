import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TimerDash extends JFrame {

    class TimerBox extends JPanel {

        private Color timerBoxBgColor = Color.DARK_GRAY;
        private Color timerBoxFgColor = Color.WHITE;

        private JPanel timerName;
        private JButton closeTimer;
        private JLabel timerNameLabel;
        private JTextField timerNameInp;

        private JPanel timerVal;
        private JLabel timerValLabel;
        private JTextField timerValInp;

        private JLabel timerLabel;

        private JButton stop;
        private JButton reset;
        private JButton start;
        private JButton noteTime;
        private JPanel control;

        private Timer timer;
        private int mins = 00, sec = 00;

        private String minsInpStr;
        private String timerNameInpStr;
        boolean isStarted = false;

        public TimerBox() {
            //Panel format
            setLayout(new GridLayout(4, 1, 10, 10));
            setPreferredSize(new Dimension(150, 150));
            setBackground(timerBoxBgColor);
            setBorder(new EmptyBorder(10, 10, 10, 10));

            closeTimer = new JButton("X");
            closeTimer.addActionListener(e -> closeTimeBox(this));
            closeTimer.setPreferredSize(new Dimension(10, 10));
            add(closeTimer);

            //Name input
            timerName = new JPanel(new GridLayout(2, 2, 5, 5));
            timerName.setBackground(timerBoxBgColor);
            timerNameLabel = new JLabel("Name: ", SwingConstants.CENTER);
            timerNameLabel.setForeground(timerBoxFgColor);
            timerNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            timerNameInp = new JTextField();
            timerNameInp.setFont(new Font("Arial", Font.PLAIN, 20));
            timerName.add(timerNameLabel);
            timerName.add(timerNameInp);
            
            //Time input
            timerVal = new JPanel(new GridLayout(1, 2));
            timerVal.setBackground(timerBoxBgColor);
            timerValLabel = new JLabel("Time (mins): ", SwingConstants.CENTER);
            timerValLabel.setForeground(timerBoxFgColor);
            timerValLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            timerValInp = new JTextField();
            timerValInp.setFont(new Font("Arial", Font.PLAIN, 20));
            timerName.add(timerValLabel);
            timerName.add(timerValInp);
            add(timerName);
            
            //Timer output
            timerLabel = new JLabel(formatTime(mins, sec), SwingConstants.CENTER);
            timerLabel.setFont(new Font("Arial", Font.BOLD, 35));
            timerLabel.setForeground(Color.WHITE);
            add(timerLabel);
    
            //Timer Controls
            control = new JPanel(new GridLayout(1, 4, 10, 0));
            control.setBackground(timerBoxBgColor);
            start = new JButton("START");
            start.setBackground(Color.GREEN);
            start.addActionListener(e -> startTimer());
            stop = new JButton("PAUSE");
            stop.setBackground(Color.ORANGE);
            stop.addActionListener(e -> stopTimer());
            reset = new JButton("RESET");
            reset.setBackground(Color.RED);
            reset.addActionListener(e -> resetTimer());
            noteTime = new JButton("NOTE TIME");
            noteTime.setBackground(Color.CYAN);
            noteTime.addActionListener(e -> noteTimer());
            control.add(start);
            control.add(stop);
            control.add(reset);
            control.add(noteTime);

            add(control);
        }

        public void startTimer(){
            minsInpStr = timerValInp.getText();
            timerNameInpStr = timerNameInp.getText();
            if(minsInpStr.isEmpty() || minsInpStr.equals("Enter time!")) {
                timerValInp.setText("Enter time!");
                return;
            }
            if(timerNameInpStr.isEmpty() || timerNameInpStr.equals("Enter name!")) {
                timerNameInp.setText("Enter name!");
                return;
            }
            if (!isStarted) {
                mins = Integer.parseInt(minsInpStr);
                isStarted = true;
            }

            if (timer == null || !timer.isRunning()) {
                timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateTimer();
                    }
                });
                timer.start();
            }
        }

        private void stopTimer() {
            if(timer != null) {
                timer.stop();
            }
        }

        private void resetTimer() {
            stopTimer();
            mins = 0;
            sec = 0;
            isStarted = false;
            timerLabel.setText(formatTime(mins, sec));
        }

        private void updateTimer() {
            if(sec == 00 && mins == 00) {
                stopTimer();
                mins = 0;
                sec = 0;
                timerLabel.setText(formatTime(mins, sec));
                JOptionPane.showMessageDialog(null, timerNameInpStr + " Timer Over!", "Timer Complete", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (sec == 00) {
                sec = 59;
                mins--;
                timerLabel.setText((formatTime(mins, sec)));
                return;
            }
            sec--;
            timerLabel.setText((formatTime(mins, sec)));
        }

        private void noteTimer() {

        }

        private String formatTime(int min, int sec) {
            return String.format("%02d:%02d", min, sec);
        }
    }

    private JPanel timerPanel;
    private JButton addButton;
    private ArrayList<TimerBox> timerBoxList;
    public JLayeredPane layeredPane;
    public JPanel addButtonPanel;

    public TimerDash() {
        //Timer Dash format
        setTitle("Timer Dashboard");
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        timerBoxList = new ArrayList<>();

        //Time box background panel
        timerPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        timerPanel.setBackground(Color.LIGHT_GRAY);
        timerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(timerPanel, BorderLayout.CENTER);

        //Add timer button
        addButtonPanel = new JPanel(new BorderLayout());
        addButtonPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        addButton = new JButton("Add Timer");
        addButton.setPreferredSize(new Dimension(120, 40));
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTimer();
            }
        });
        addButtonPanel.add(addButton, BorderLayout.SOUTH);
        add(addButtonPanel, BorderLayout.SOUTH);
    }

    private void addTimer() {
        TimerBox box = new TimerBox();
        timerBoxList.add(box);
        timerPanel.add(box);
        timerPanel.revalidate();
        timerPanel.repaint();
    }

    public void closeTimeBox(TimerBox t) {
        timerPanel.remove(t);
        timerBoxList.remove(t);
        timerPanel.revalidate();
        timerPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TimerDash frame = new TimerDash();
            frame.setVisible(true);
        });
    }
}
