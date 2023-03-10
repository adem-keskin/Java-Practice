
import javax.swing.JFrame;
//import javax.swing.JLabel;

public class MerhabaSwing extends JFrame {

    private javax.swing.JLabel jLabel = null;
    private javax.swing.JTextField jTextField = null;
    private javax.swing.JButton jButton = null;

    public MerhabaSwing() {
        super();
        this.setSize(300, 200);
        this.getContentPane().setLayout(null);
        this.add(getJLabel(), null);
        this.add(getJTextField(), null);
        this.add(getJButton(), null);
        this.setTitle("MerhabaSwing");
    }

    private javax.swing.JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setBounds(34, 49, 53, 18);
            jLabel.setText("Adınız:");
        }
        return jLabel;
    }

    private javax.swing.JTextField getJTextField() {
        if (jTextField == null) {
            jTextField = new javax.swing.JTextField();
            jTextField.setBounds(96, 49, 160, 20);
        }
        return jTextField;
    }

    private javax.swing.JButton getJButton() {
        if (jButton == null) {
            jButton = new javax.swing.JButton();
            jButton.setBounds(103, 110, 71, 27);
            jButton.setText("OK");
        }
        return jButton;
    }

    public static void main(String[] args) {
        MerhabaSwing w = new MerhabaSwing();
        w.setVisible(true);
    }
}
