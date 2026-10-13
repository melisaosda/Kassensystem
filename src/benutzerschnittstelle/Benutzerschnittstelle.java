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

public class Benutzerschnittstelle extends JFrame
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel pnlProdukt;
	private JPanel pnlProdukt2;
	private JButton btnSuchleiste;
	private JTextField txtSuchleiste;
	private JLabel lblSuchleiste;
	private JPanel produktBild;
	private JTable tblProdukt;
	private JTable tblPosition;
	private JButton btnPosition;

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
		setBounds(100, 100, 1094, 516);
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
		
		produktBild = new JPanel();
		produktBild.setBorder(new LineBorder(new Color(128, 128, 128)));
		produktBild.setBounds(10, 10, 153, 133);
		pnlProdukt2.add(produktBild);
		
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
		tblProdukt.setBounds(173, 10, 523, 133);
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

	}
}
