package AE1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

public class Order extends JFrame {
    private JComboBox<String> tipusComboBox;
    private JTextField quantitatCamp;
    private JCheckBox escriureATextCheckBox;
    private JTextField nomFitxerSortidaCamp;
    private JButton ordreBotó;
    private ExecutorService serveiExecutor;
    private BlockingQueue<String> cuaOrdres;
    private Manufacture manufacture;

    private static final String[] TETRIS_LLETRES = {"I", "O", "T", "J", "L", "S", "Z"};

    public Order(ExecutorService serveiExecutor, BlockingQueue<String> cuaOrdres) {
    	getContentPane().setBackground(new Color(102, 153, 255));
        this.serveiExecutor = serveiExecutor;
        this.cuaOrdres = cuaOrdres;

        setTitle("Demana Tetrominos");
        setSize(537, 272);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel tipusEtiqueta = new JLabel("Tipus (I, O, T, J, L, S, Z):");
        tipusEtiqueta.setBounds(10, 25, 137, 46);
        tipusComboBox = new JComboBox<>(TETRIS_LLETRES);
        tipusComboBox.setBounds(142, 25, 74, 46);
        JLabel quantitatEtiqueta = new JLabel("Quantitat:");
        quantitatEtiqueta.setBounds(10, 82, 84, 46);
        quantitatCamp = new JTextField();
        quantitatCamp.setBounds(96, 82, 120, 46);
        escriureATextCheckBox = new JCheckBox("Escriure a fitxer de text");
        escriureATextCheckBox.setBackground(new Color(102, 153, 255));
        escriureATextCheckBox.setBounds(307, 25, 159, 46);
        nomFitxerSortidaCamp = new JTextField("sortida.txt");
        nomFitxerSortidaCamp.setBounds(307, 82, 204, 46);
        ordreBotó = new JButton("Ordre");
        ordreBotó.setBackground(new Color(255, 255, 255));
        ordreBotó.setBounds(124, 176, 260, 46);

        ordreBotó.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tipus = (String) tipusComboBox.getSelectedItem();
                String quantitatStr = quantitatCamp.getText();

                if (!quantitatStr.matches("\\d+")) {
                    JOptionPane.showMessageDialog(Order.this, "Introduix una quantitat vàlida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int quantitat = Integer.parseInt(quantitatCamp.getText());
                boolean escriureAText = escriureATextCheckBox.isSelected();
                String nomFitxerSortida = nomFitxerSortidaCamp.getText();

                manufacture = new Manufacture(tipus, escriureAText, nomFitxerSortida, cuaOrdres);
                for (int i = 0; i < quantitat; i++) {
                    serveiExecutor.submit(manufacture);
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (manufacture != null) {
                	manufacture.completarFabricació();
                }
                serveiExecutor.shutdown();
            }
        });
        getContentPane().setLayout(null);

        getContentPane().add(tipusEtiqueta);
        getContentPane().add(tipusComboBox);
        getContentPane().add(quantitatEtiqueta);
        getContentPane().add(quantitatCamp);
        getContentPane().add(escriureATextCheckBox);
        getContentPane().add(nomFitxerSortidaCamp);
        getContentPane().add(ordreBotó);

        setVisible(true);
    }

    public static void main(String[] args) {
        ExecutorService serveiExecutor = Executors.newFixedThreadPool(8);
        BlockingQueue<String> cuaOrdres = new LinkedBlockingQueue<>();

        SwingUtilities.invokeLater(() -> {
            new Order(serveiExecutor, cuaOrdres);
        });
    }
}

