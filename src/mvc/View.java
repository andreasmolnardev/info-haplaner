/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
    private final JComboBox<String> sortAuswahl;
    private final JPanel aufgabenListe;
    private final SimpleDateFormat datumFormat;

    public View(Controller c) {
        controller = c;
        datumFormat = new SimpleDateFormat("d.M.yy");
        datumFormat.setLenient(false);

        sortAuswahl = new JComboBox<String>(new String[]{"Datum", "Name"});
        sortAuswahl.addActionListener(e -> zeichneAufgaben());
        aufgabenListe = new JPanel();
        aufgabenListe.setLayout(new BoxLayout(aufgabenListe, BoxLayout.Y_AXIS));
        aufgabenListe.setBorder(BorderFactory.createEmptyBorder());

        Wert.geben().registrieren(this);
        initComponents();
        datenGeaendert();
    }

    @Override
    public void datenGeaendert() {
        zeichneAufgaben();
    }

    private void initComponents() {
        setTitle("Hausaufgabenplaner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(12, 12));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        javax.swing.JButton aufgabeKnopf = new javax.swing.JButton("Aufgabe hinzufügen");
        aufgabeKnopf.addActionListener(e -> aufgabeDialogOeffnen());
        form.add(aufgabeKnopf);

        javax.swing.JButton fachKnopf = new javax.swing.JButton("Fach hinzufügen");
        fachKnopf.addActionListener(e -> fachHinzufuegen());
        form.add(fachKnopf);

        form.add(new JLabel("Sortieren"));
        form.add(sortAuswahl);

        add(form, BorderLayout.NORTH);
        add(new JScrollPane(aufgabenListe), BorderLayout.CENTER);

        setSize(900, 600);
        setLocationRelativeTo(null);
    }

    private void zeichneAufgaben() {
        aufgabenListe.removeAll();
        Aufgabe[] aufgaben;
        if ("Name".equals(sortAuswahl.getSelectedItem())) {
            aufgaben = controller.aufgabenNachNameSortiertZurückgeben();
        } else {
            aufgaben = controller.aufgabenNachDatumSortiertZurückgeben();
        }
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

    private void aufgabeDialogOeffnen() {
        JDialog dialog = new JDialog(this, "Aufgabe hinzufügen", true);
        dialog.setLayout(new GridBagLayout());

        javax.swing.JTextField titelFeld = new javax.swing.JTextField(24);
        JSpinner datumPicker = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        datumPicker.setEditor(new JSpinner.DateEditor(datumPicker, "dd.MM.yy"));
        JComboBox<Fach> fachAuswahl = new JComboBox<Fach>(Wert.geben().fächerZurückgeben());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(6, 6, 6, 6);
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        dialog.add(new JLabel("Titel"), constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(titelFeld, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.NONE;
        dialog.add(new JLabel("Fach"), constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(fachAuswahl, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.NONE;
        dialog.add(new JLabel("Datum fällig"), constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(datumPicker, constraints);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        javax.swing.JButton abbrechenKnopf = new javax.swing.JButton("Abbrechen");
        abbrechenKnopf.addActionListener(e -> dialog.dispose());
        javax.swing.JButton hinzufuegenKnopf = new javax.swing.JButton("Hinzufügen");
        hinzufuegenKnopf.addActionListener(e -> {
            Fach fach = (Fach) fachAuswahl.getSelectedItem();
            if (fach != null && titelFeld.getText().trim().length() > 0) {
                controller.aufgabeHinzufügen(fach.gibId(), titelFeld.getText(), (Date) datumPicker.getValue());
                dialog.dispose();
            }
        });
        buttons.add(abbrechenKnopf);
        buttons.add(hinzufuegenKnopf);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        dialog.add(buttons, constraints);

        dialog.getRootPane().setDefaultButton(hinzufuegenKnopf);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
                new AufgabenController();
            }
        });
    }
}
