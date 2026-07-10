/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;

public class View extends JFrame implements Beobachter {
    private final Controller controller;
    private final javax.swing.JTextField titelFeld;
    private final JSpinner datumPicker;
    private final JComboBox<Fach> fachAuswahl;
    private final JPanel aufgabenListe;
    private final SimpleDateFormat datumFormat;

    public View(Controller c) {
        controller = c;
        datumFormat = new SimpleDateFormat("d.M.yy");
        datumFormat.setLenient(false);

        titelFeld = new javax.swing.JTextField(24);
        datumPicker = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        datumPicker.setEditor(new JSpinner.DateEditor(datumPicker, "dd.MM.yy"));
        fachAuswahl = new JComboBox<Fach>();
        aufgabenListe = new JPanel();
        aufgabenListe.setLayout(new BoxLayout(aufgabenListe, BoxLayout.Y_AXIS));
        aufgabenListe.setBorder(BorderFactory.createEmptyBorder());

        Wert.geben().registrieren(this);
        initComponents();
        datenGeaendert();
    }

    @Override
    public void datenGeaendert() {
        fachAuswahl.setModel(new DefaultComboBoxModel<Fach>(Wert.geben().fächerZurückgeben()));
        zeichneAufgaben();
    }

    private void initComponents() {
        setTitle("Hausaufgabenplaner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.add(new JLabel("Titel"));
        form.add(titelFeld);
        form.add(new JLabel("Datum"));
        form.add(datumPicker);
        form.add(new JLabel("Fach"));
        form.add(fachAuswahl);

        javax.swing.JButton aufgabeKnopf = new javax.swing.JButton("Aufgabe hinzufügen");
        aufgabeKnopf.addActionListener(e -> aufgabeHinzufuegen());
        form.add(aufgabeKnopf);

        javax.swing.JButton fachKnopf = new javax.swing.JButton("Fach hinzufügen");
        fachKnopf.addActionListener(e -> fachHinzufuegen());
        form.add(fachKnopf);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(aufgabenListe), BorderLayout.CENTER);

        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void zeichneAufgaben() {
        aufgabenListe.removeAll();
        Aufgabe[] aufgaben = Wert.geben().aufgabenZurückgeben();
        for (Aufgabe aufgabe : aufgaben) {
            JPanel zeile = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            zeile.setBorder(BorderFactory.createEmptyBorder());
            zeile.setAlignmentX(LEFT_ALIGNMENT);
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(aufgabe.istErledigt());
            checkBox.addActionListener(e -> controller.statusÄndernButtonGedrueckt(aufgabe.gibId()));
            zeile.add(checkBox);
            zeile.add(new JLabel(aufgabe.gibFach().gibKürzel() + ": " + aufgabe.gibTitel()));
            zeile.add(new JLabel(datumFormat.format(aufgabe.gibAblaufdatum())));
            zeile.setMaximumSize(new Dimension(Integer.MAX_VALUE, zeile.getPreferredSize().height));
            aufgabenListe.add(zeile);
        }
        aufgabenListe.revalidate();
        aufgabenListe.repaint();
    }

    private void aufgabeHinzufuegen() {
        Fach fach = (Fach) fachAuswahl.getSelectedItem();
        if (fach == null) {
            return;
        }
        Date datum = (Date) datumPicker.getValue();
        controller.aufgabeHinzufügen(fach.gibId(), titelFeld.getText(), datum);
        titelFeld.setText("");
    }

    private void fachHinzufuegen() {
        String eingabe = javax.swing.JOptionPane.showInputDialog(this, "Fachname und Kürzel");
        if (eingabe != null && eingabe.trim().length() > 0) {
            controller.fachHinzufügen(eingabe.trim(), eingabe.trim());
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ZaehlerController();
            }
        });
    }
}
