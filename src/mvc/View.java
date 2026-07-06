package mvc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;

public class View extends JFrame implements Beobachter {
    private static final Color HINTERGRUND = new Color(16, 16, 16);
    private static final Color TEXT = new Color(228, 228, 228);
    private static final Color RAND = new Color(210, 210, 210);
    private static final Font HANDSCHRIFT_GROSS = new Font("Comic Sans MS", Font.BOLD, 32);
    private static final Font HANDSCHRIFT_NORMAL = new Font("Comic Sans MS", Font.BOLD, 24);

    private final Controller controller;
    private final JTextField titelFeld;
    private final JTextField datumFeld;
    private final JComboBox<Fach> fachAuswahl;
    private final JPanel aufgabenListe;
    private final SimpleDateFormat datumFormat;

    public View(Controller c) {
        controller = c;
        datumFormat = new SimpleDateFormat("d.M.yy");
        datumFormat.setLenient(false);

        titelFeld = eingabeFeld("Titel hinzufügen", 520);
        datumFeld = eingabeFeld("Datum", 170);
        fachAuswahl = new JComboBox<Fach>();
        aufgabenListe = new JPanel(new GridBagLayout());

        Wert.geben().registrieren(this);
        initComponents();
        datenGeaendert();
    }

    public void datenGeaendert() {
        fachAuswahl.setModel(new DefaultComboBoxModel<Fach>(Wert.geben().fächerZurückgeben()));
        zeichneAufgaben();
    }

    private void initComponents() {
        setTitle("Hausaufgabenplaner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(HINTERGRUND);

        JPanel rahmen = new JPanel(new BorderLayout(0, 26));
        rahmen.setBackground(HINTERGRUND);
        rahmen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(54, 66, 54, 66),
                linienRand(2, 34)));

        JPanel innen = new JPanel(new BorderLayout(0, 36));
        innen.setBackground(HINTERGRUND);
        innen.setBorder(BorderFactory.createEmptyBorder(42, 64, 54, 64));

        JLabel ueberschrift = new JLabel("Hausaufgabenplaner");
        ueberschrift.setForeground(TEXT);
        ueberschrift.setFont(HANDSCHRIFT_GROSS);

        JPanel oben = new JPanel(new GridBagLayout());
        oben.setBackground(HINTERGRUND);
        oben.add(ueberschrift, position(0, 0, 5, 1, 1.0, 0, GridBagConstraints.WEST));
        oben.add(titelFeld, position(0, 1, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL));
        oben.add(datumFeld, position(1, 1, 1, 1, 0, 0, GridBagConstraints.NONE));
        oben.add(fachAuswahl, position(2, 1, 1, 1, 0, 0, GridBagConstraints.NONE));
        JButton aufgabeKnopf = knopf("+", 82, 56);
        oben.add(aufgabeKnopf, position(4, 1, 1, 1, 0, 0, GridBagConstraints.NONE));

        JButton fachKnopf = knopf("+", 190, 56);
        oben.add(fachKnopf, position(2, 2, 1, 1, 0, 0, GridBagConstraints.NONE));

        aufgabeKnopf.addActionListener(e -> aufgabeHinzufuegen());
        fachKnopf.addActionListener(e -> fachHinzufuegen());

        gestalteComboBox();
        aufgabenListe.setBackground(HINTERGRUND);

        innen.add(oben, BorderLayout.NORTH);
        innen.add(aufgabenListe, BorderLayout.CENTER);
        rahmen.add(innen, BorderLayout.CENTER);
        add(rahmen, BorderLayout.CENTER);
        setMinimumSize(new Dimension(980, 620));
        setSize(1180, 690);
        setLocationRelativeTo(null);
    }

    private JTextField eingabeFeld(String text, int breite) {
        JTextField feld = new JTextField(text);
        feld.setPreferredSize(new Dimension(breite, 56));
        feld.setForeground(TEXT);
        feld.setCaretColor(TEXT);
        feld.setBackground(HINTERGRUND);
        feld.setFont(HANDSCHRIFT_NORMAL);
        feld.setBorder(BorderFactory.createCompoundBorder(linienRand(2, 13), BorderFactory.createEmptyBorder(0, 18, 0, 18)));
        return feld;
    }

    private JButton knopf(String text, int breite, int hoehe) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(breite, hoehe));
        button.setForeground(TEXT);
        button.setBackground(HINTERGRUND);
        button.setFont(HANDSCHRIFT_NORMAL);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(linienRand(2, 13));
        return button;
    }

    private void gestalteComboBox() {
        fachAuswahl.setPreferredSize(new Dimension(190, 56));
        fachAuswahl.setForeground(TEXT);
        fachAuswahl.setBackground(HINTERGRUND);
        fachAuswahl.setFont(HANDSCHRIFT_NORMAL);
        fachAuswahl.setBorder(linienRand(2, 13));
    }

    private void zeichneAufgaben() {
        aufgabenListe.removeAll();
        Aufgabe[] aufgaben = Wert.geben().aufgabenZurückgeben();
        for (int i = 0; i < aufgaben.length; i++) {
            Aufgabe aufgabe = aufgaben[i];
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(aufgabe.istErledigt());
            checkBox.setBackground(HINTERGRUND);
            checkBox.setBorder(linienRand(2, 5));
            checkBox.addActionListener(e -> controller.statusÄndernButtonGedrueckt(aufgabe.gibId()));

            JLabel titel = new JLabel(aufgabe.gibFach().gibKürzel() + ": " + aufgabe.gibTitel());
            titel.setForeground(TEXT);
            titel.setFont(HANDSCHRIFT_NORMAL);

            JLabel datum = new JLabel(datumFormat.format(aufgabe.gibAblaufdatum()), SwingConstants.RIGHT);
            datum.setForeground(TEXT);
            datum.setFont(HANDSCHRIFT_NORMAL);

            GridBagConstraints cbPosition = position(0, i, 1, 1, 0, 0, GridBagConstraints.NONE);
            cbPosition.insets = new Insets(0, 0, 18, 26);
            aufgabenListe.add(checkBox, cbPosition);

            GridBagConstraints titelPosition = position(1, i, 1, 1, 1.0, 0, GridBagConstraints.HORIZONTAL);
            titelPosition.insets = new Insets(0, 0, 18, 22);
            aufgabenListe.add(titel, titelPosition);

            GridBagConstraints datumPosition = position(2, i, 1, 1, 0, 0, GridBagConstraints.NONE);
            datumPosition.insets = new Insets(0, 0, 18, 0);
            aufgabenListe.add(datum, datumPosition);
        }
        aufgabenListe.add(new JPanel(new FlowLayout()), position(0, aufgaben.length, 3, 1, 1.0, 1.0, GridBagConstraints.BOTH));
        aufgabenListe.revalidate();
        aufgabenListe.repaint();
    }

    private void aufgabeHinzufuegen() {
        Fach fach = (Fach) fachAuswahl.getSelectedItem();
        if (fach == null) {
            return;
        }
        try {
            Date datum = datumFormat.parse(datumFeld.getText().trim());
            controller.aufgabeHinzufügen(fach.gibId(), titelFeld.getText(), datum);
            titelFeld.setText("");
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Bitte ein Datum wie 3.7.26 eingeben.");
        }
    }

    private void fachHinzufuegen() {
        String kürzel = JOptionPane.showInputDialog(this, "Fach eingeben");
        if (kürzel != null) {
            controller.fachHinzufügen(kürzel, kürzel);
        }
    }

    private Border linienRand(int staerke, int rundung) {
        return BorderFactory.createLineBorder(RAND, staerke, true);
    }

    private GridBagConstraints position(int x, int y, int breite, int hoehe, double gewichtX, double gewichtY, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = breite;
        c.gridheight = hoehe;
        c.weightx = gewichtX;
        c.weighty = gewichtY;
        c.fill = fill;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(0, 0, 10, 14);
        return c;
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ZaehlerController();
            }
        });
    }
}
