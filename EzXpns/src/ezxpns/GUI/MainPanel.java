package ezxpns.GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/** @see http://stackoverflow.com/questions/7059278 */
class MainPanel extends JPanel {

    private static final String title = "Registration Panel";
    private JFrame frame = new JFrame(title);
    private JPanel registration = new JPanel();

    public MainPanel() {
        this.setLayout(new BorderLayout());
        registration.setBorder(BorderFactory.createTitledBorder(title));
        registration.add(PanelType.USER_AGREEMENT.panel);
        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Button Panel"));
        add(registration, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void display() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MainPanel().display();
            }
        });
    }

    private class ButtonPanel extends JPanel {

        public ButtonPanel() {
            for (final PanelType panel : PanelType.values()) {
                final JButton button = panel.button;
                this.add(button);
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        registration.removeAll();
                        registration.add(panel.create());
                        frame.pack();
                        frame.setLocationRelativeTo(null);
                    }
                });
            }
        }
    }

    private enum PanelType {

        USER_AGREEMENT("User Agreement", 2),
        USER_INFO("User Information", 4),
        ENROLLMENT("Enrollment Form", 6);
        private String name;
        private int count;
        private JButton button;
        private JPanel panel;

        private PanelType(String name, int count) {
            this.name = name;
            this.count= count;
            this.button = new JButton(name);
            this.panel = create();
        }

        private JPanel create() {
            this.panel = new JPanel(new GridLayout(0, 1));
            this.panel.add(new JLabel(name));
            this.panel.add(new JLabel(" "));
            for (int i = 0; i < count; i++) {
                this.panel.add(new JLabel("Label " + String.valueOf(i + 1)));
                            }
            return panel;
        }
    }
}