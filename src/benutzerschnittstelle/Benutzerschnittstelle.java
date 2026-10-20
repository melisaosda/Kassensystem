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
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblPosition.setBounds(706, 10, 324, 79);
		pnlProdukt2.add(tblPosition);
		
		btnPosition = new JButton("Position buchen");
		btnPosition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPosition.setBounds(706, 99, 324, 44);
		pnlProdukt2.add(btnPosition);
		
		btnSuchleiste = new JButton("Produkt suchen");
		btnSuchleiste.setBounds(715, 10, 335, 48);
		pnlProdukt.add(btnSuchleiste);
		
		txtSuchleiste = new JTextField();
		txtSuchleiste.setBounds(10, 25, 695, 33);
		pnlProdukt.add(txtSuchleiste);
		txtSuchleiste.setColumns(10);
		
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
		btnStornieren.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
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
		pnlPositionen.add(tblPositionen, BorderLayout.NORTH);
		
		pnlTransaktion = new JPanel();
		pnlTransaktion.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnlTransaktion.setBounds(10, 479, 1061, 159);
		contentPane.add(pnlTransaktion);
		pnlTransaktion.setLayout(null);
		
		btnZuruecksetzen = new JButton("Transaktion zurücksetzen");
		btnZuruecksetzen.setBounds(723, 10, 328, 38);
		pnlTransaktion.add(btnZuruecksetzen);
		
		btnZahlung = new JButton("Zahlung erfassen");
		btnZahlung.setBounds(723, 58, 328, 38);
		pnlTransaktion.add(btnZahlung);
		
		btnAbschliessen = new JButton("Transaktion abschließen");
		btnAbschliessen.setBounds(723, 106, 328, 43);
		pnlTransaktion.add(btnAbschliessen);
		
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

	}
}
