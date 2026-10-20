package benutzerschnittstelle;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import fachkonzept.Position;
import fachkonzept.Produkt;
import steuerung.Steuerung;

public class Benutzerschnittstelle extends JFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnlProdukt;
	private JPanel pnlProdukt2;
	private JButton btnSuchleiste;
	private JTextField txtSuchleiste;
	private JLabel lblSuchleiste;
	private JTable tblProdukt;
	private JTable tblPosition;
	private JButton btnPosition;
	private JPanel pnlPositionen;
	private JPanel pnlTransaktion;
	private JButton btnZuruecksetzen;
	private JButton btnZahlung;
	private JButton btnAbschliessen;
	private JTextField txtGezahlt;
	private JLabel lblGesamtbetrag;
	private JLabel lblGezahlt;
	private JLabel lblRueckgeld;
	private JTextField txtGesamtbetrag;
	private JTextField txtRueckgeld;
	private JScrollPane scrollPane;
	private JTable tblPositionen;
	private JButton btnStornieren;
	private JLabel lblGeldschublade;
	private Steuerung steuerung;
	private Produkt gefundenesProdukt;
	private List<Position> angezeigtePositionen = new ArrayList<>();
	private boolean aktualisiertGerade;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Benutzerschnittstelle frame = new Benutzerschnittstelle();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Benutzerschnittstelle()
	{
		setTitle("Kassensystem");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try
		{
			steuerung = new Steuerung();
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null,
					"Konfiguration konnte nicht gelesen werden (db.properties muss im Projektordner liegen):\n" + e.getMessage(),
					"Kassensystem", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		setBounds(100, 100, 1095, 685);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		pnlProdukt = new JPanel();
		pnlProdukt.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlProdukt.setBounds(10, 10, 1060, 231);
		contentPane.add(pnlProdukt);
		pnlProdukt.setLayout(null);
		
		pnlProdukt2 = new JPanel();
		pnlProdukt2.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlProdukt2.setBounds(10, 68, 1040, 153);
		pnlProdukt.add(pnlProdukt2);
		pnlProdukt2.setLayout(null);
		
		tblProdukt = new JTable();
		tblProdukt.setModel(new DefaultTableModel(
			new Object[][] {
				{"Produktnummer", "Bezeichnung", "Nettopreis", "USt."},
			},
			new String[] {
				"New column", "New column", "New column", "New column"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblProdukt.setBounds(10, 10, 686, 133);
		pnlProdukt2.add(tblProdukt);
		
		tblPosition = new JTable();
		tblPosition.setModel(new DefaultTableModel(
			new Object[][] {
				{"Menge", "Betrag"},
			},
			new String[] {
				"New column", "New column"
			}
		) {
			public boolean isCellEditable(int row, int column) {
				return row == 1 && column == 0;
			}
		});
		tblPosition.setBounds(706, 10, 324, 79);
		pnlProdukt2.add(tblPosition);
		tblPosition.getModel().addTableModelListener(e -> mengeGeaendert(e));
		
		btnPosition = new JButton("Position buchen");
		btnPosition.addActionListener(e -> positionBuchen());
		btnPosition.setBounds(706, 99, 324, 44);
		pnlProdukt2.add(btnPosition);
		
		btnSuchleiste = new JButton("Produkt suchen");
		btnSuchleiste.setBounds(715, 10, 335, 48);
		pnlProdukt.add(btnSuchleiste);
		btnSuchleiste.addActionListener(e -> sucheProdukt());
		
		txtSuchleiste = new JTextField();
		txtSuchleiste.setBounds(10, 25, 695, 33);
		pnlProdukt.add(txtSuchleiste);
		txtSuchleiste.setColumns(10);
		txtSuchleiste.addActionListener(e -> sucheProdukt());
		
		lblSuchleiste = new JLabel("Suchleiste für Produktnummer");
		lblSuchleiste.setLabelFor(txtSuchleiste);
		lblSuchleiste.setBounds(10, 10, 177, 13);
		pnlProdukt.add(lblSuchleiste);
		
		pnlPositionen = new JPanel();
		pnlPositionen.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlPositionen.setBounds(10, 251, 1060, 218);
		contentPane.add(pnlPositionen);
		pnlPositionen.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		pnlPositionen.add(scrollPane, BorderLayout.CENTER);
		
		btnStornieren = new JButton("Position stornieren");
		btnStornieren.addActionListener(e -> positionStornieren());
		scrollPane.setViewportView(btnStornieren);
		
		tblPositionen = new JTable();
		tblPositionen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tblPositionen.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Produktnummer", "Bezeichnung", "Nettopreis", "USt.", "Menge", "Betrag"
			}
		));
		tblPositionen.setDefaultEditor(Object.class, null);
		JPanel pnlTabelle = new JPanel(new BorderLayout());
		pnlTabelle.add(tblPositionen.getTableHeader(), BorderLayout.NORTH);
		pnlTabelle.add(tblPositionen, BorderLayout.CENTER);
		pnlPositionen.add(pnlTabelle, BorderLayout.NORTH);
		
		pnlTransaktion = new JPanel();
		pnlTransaktion.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlTransaktion.setBounds(10, 479, 1061, 159);
		contentPane.add(pnlTransaktion);
		pnlTransaktion.setLayout(null);
		
		btnZuruecksetzen = new JButton("Transaktion zurücksetzen");
		btnZuruecksetzen.setBounds(723, 10, 328, 38);
		pnlTransaktion.add(btnZuruecksetzen);
		btnZuruecksetzen.addActionListener(e -> transaktionZuruecksetzen());
		
		btnZahlung = new JButton("Zahlung erfassen");
		btnZahlung.setBounds(723, 58, 328, 38);
		pnlTransaktion.add(btnZahlung);
		btnZahlung.addActionListener(e -> zahlungErfassen());
		
		btnAbschliessen = new JButton("Transaktion abschließen");
		btnAbschliessen.setBounds(723, 106, 328, 43);
		pnlTransaktion.add(btnAbschliessen);
		btnAbschliessen.addActionListener(e -> transaktionAbschliessen());
		
		txtGezahlt = new JTextField();
		txtGezahlt.setBounds(186, 68, 96, 19);
		pnlTransaktion.add(txtGezahlt);
		txtGezahlt.setColumns(10);
		
		lblGesamtbetrag = new JLabel("Gesamtbetrag:");
		lblGesamtbetrag.setBounds(20, 14, 145, 31);
		pnlTransaktion.add(lblGesamtbetrag);
		
		lblGezahlt = new JLabel("Gezahlt:");
		lblGezahlt.setLabelFor(txtGezahlt);
		lblGezahlt.setBounds(20, 65, 111, 25);
		pnlTransaktion.add(lblGezahlt);
		
		lblRueckgeld = new JLabel("Rückgeld:");
		lblRueckgeld.setBounds(20, 115, 111, 25);
		pnlTransaktion.add(lblRueckgeld);
		
		txtGesamtbetrag = new JTextField();
		lblGesamtbetrag.setLabelFor(txtGesamtbetrag);
		txtGesamtbetrag.setEditable(false);
		txtGesamtbetrag.setColumns(10);
		txtGesamtbetrag.setBounds(186, 20, 96, 19);
		pnlTransaktion.add(txtGesamtbetrag);
		
		txtRueckgeld = new JTextField();
		lblRueckgeld.setLabelFor(txtRueckgeld);
		txtRueckgeld.setEditable(false);
		txtRueckgeld.setColumns(10);
		txtRueckgeld.setBounds(186, 118, 96, 19);
		pnlTransaktion.add(txtRueckgeld);

		lblGeldschublade = new JLabel();
		lblGeldschublade.setBounds(320, 20, 380, 25);
		pnlTransaktion.add(lblGeldschublade);

		aktualisiere();
	}

	private void sucheProdukt()
	{
		String ean = txtSuchleiste.getText().trim();
		if (ean.isEmpty())
		{
			return;
		}
		try
		{
			Produkt produkt = steuerung.rufeProduktdaten(ean);
			if (produkt == null)
			{
				hinweis("Kein Produkt mit der Produktnummer " + ean + " gefunden.");
				return;
			}
			gefundenesProdukt = produkt;
			zeigeGefundenesProdukt();
		}
		catch (SQLException ex)
		{
			hinweis("Datenbankfehler: " + ex.getMessage());
		}
	}

	private void zeigeGefundenesProdukt()
	{
		aktualisiertGerade = true;
		DefaultTableModel produktModell = (DefaultTableModel) tblProdukt.getModel();
		produktModell.setRowCount(1);
		produktModell.addRow(new Object[] { gefundenesProdukt.getEAN(), gefundenesProdukt.getBezeichnung(),
				euro(gefundenesProdukt.getNettoPreis()) + " pro " + gefundenesProdukt.getBasiseinheit(),
				gefundenesProdukt.getUmsatzsteuersatz() + "%" });
		DefaultTableModel positionModell = (DefaultTableModel) tblPosition.getModel();
		positionModell.setRowCount(1);
		positionModell.addRow(new Object[] { "1", euro(berechneBetrag(gefundenesProdukt, 1)) });
		aktualisiertGerade = false;
	}

	// Menge in der Tabelle wurde geaendert: pruefen und Betrag neu berechnen
	private void mengeGeaendert(TableModelEvent e)
	{
		if (aktualisiertGerade || gefundenesProdukt == null || e.getFirstRow() != 1 || e.getColumn() != 0)
		{
			return;
		}
		DefaultTableModel modell = (DefaultTableModel) tblPosition.getModel();
		int menge;
		try
		{
			menge = Integer.parseInt(String.valueOf(modell.getValueAt(1, 0)).trim());
		}
		catch (NumberFormatException ex)
		{
			menge = 1;
		}
		if (menge < 1)
		{
			menge = 1;
		}
		aktualisiertGerade = true;
		modell.setValueAt(String.valueOf(menge), 1, 0);
		modell.setValueAt(euro(berechneBetrag(gefundenesProdukt, menge)), 1, 1);
		aktualisiertGerade = false;
	}

	private double berechneBetrag(Produkt produkt, int menge)
	{
		Position rechner = new Position();
		rechner.erfassePosition(produkt, menge);
		return rechner.getBrutto();
	}

	private void positionBuchen()
	{
		if (gefundenesProdukt == null)
		{
			hinweis("Bitte zuerst ein Produkt suchen.");
			return;
		}
		if (tblPosition.isEditing())
		{
			tblPosition.getCellEditor().stopCellEditing();
		}
		try
		{
			int menge = Integer.parseInt(String.valueOf(tblPosition.getModel().getValueAt(1, 0)).trim());
			steuerung.leseBarcode(gefundenesProdukt.getEAN(), menge);
		}
		catch (SQLException ex)
		{
			hinweis("Datenbankfehler: " + ex.getMessage());
			return;
		}
		catch (IllegalArgumentException | IllegalStateException ex)
		{
			hinweis(ex.getMessage());
			return;
		}
		loescheSuchergebnis();
		aktualisiere();
	}

	private void positionStornieren()
	{
		int zeile = tblPositionen.getSelectedRow();
		if (zeile < 0 || zeile >= angezeigtePositionen.size())
		{
			hinweis("Bitte zuerst eine Position in der Tabelle auswählen.");
			return;
		}
		steuerung.storniere(angezeigtePositionen.get(zeile));
		aktualisiere();
	}

	private void zahlungErfassen()
	{
		String eingabe = txtGezahlt.getText().trim();
		if (eingabe.isEmpty())
		{
			hinweis("Bitte den gezahlten Betrag eingeben.");
			return;
		}
		try
		{
			steuerung.bekommeZahlung(Double.parseDouble(eingabe.replace(',', '.')));
		}
		catch (NumberFormatException ex)
		{
			hinweis("Bitte einen gültigen Betrag eingeben, z. B. 42,00");
		}
		catch (IllegalArgumentException | IllegalStateException ex)
		{
			hinweis(ex.getMessage());
		}
		aktualisiere();
	}

	private void transaktionAbschliessen()
	{
		try
		{
			Path bon = steuerung.schliesseTransaktionAb();
			txtGezahlt.setText("");
			aktualisiere();
			JOptionPane.showMessageDialog(this, "Transaktion abgeschlossen.\nBon gespeichert: " + bon, "Kassensystem",
					JOptionPane.INFORMATION_MESSAGE);
		}
		catch (IllegalStateException ex)
		{
			hinweis(ex.getMessage());
		}
		catch (IOException ex)
		{
			hinweis("Bon konnte nicht gespeichert werden: " + ex.getMessage());
		}
	}

	private void transaktionZuruecksetzen()
	{
		int antwort = JOptionPane.showConfirmDialog(this, "Transaktion wirklich zurücksetzen?", "Kassensystem",
				JOptionPane.YES_NO_OPTION);
		if (antwort == JOptionPane.YES_OPTION)
		{
			steuerung.zuruecksetzen();
			txtGezahlt.setText("");
			loescheSuchergebnis();
			aktualisiere();
		}
	}

	private void loescheSuchergebnis()
	{
		aktualisiertGerade = true;
		gefundenesProdukt = null;
		((DefaultTableModel) tblProdukt.getModel()).setRowCount(1);
		((DefaultTableModel) tblPosition.getModel()).setRowCount(1);
		txtSuchleiste.setText("");
		aktualisiertGerade = false;
	}

	private void aktualisiere()
	{
		DefaultTableModel modell = (DefaultTableModel) tblPositionen.getModel();
		modell.setRowCount(0);
		angezeigtePositionen = steuerung.getPositionen();
		for (Position position : angezeigtePositionen)
		{
			Produkt produkt = position.getProdukt();
			modell.addRow(new Object[] { produkt.getEAN(), produkt.getBezeichnung(), euro(produkt.getNettoPreis()),
					produkt.getUmsatzsteuersatz() + "%", position.getMenge(), euro(position.getBrutto()) });
		}
		txtGesamtbetrag.setText(euro(steuerung.getGesamtbetrag()));
		txtRueckgeld.setText(steuerung.getGezahlt() > 0 ? euro(steuerung.getRueckgeld()) : "");
		lblGeldschublade.setText("Geldschublade: " + (steuerung.istGeldschubladeOffen() ? "offen" : "geschlossen")
				+ (steuerung.istGeldschubladeSimulation() ? " [Simulation]" : ""));
	}

	private void hinweis(String text)
	{
		JOptionPane.showMessageDialog(this, text, "Kassensystem", JOptionPane.WARNING_MESSAGE);
	}

	private static String euro(double betrag)
	{
		return String.format(Locale.GERMANY, "%.2f €", betrag);
	}
}
