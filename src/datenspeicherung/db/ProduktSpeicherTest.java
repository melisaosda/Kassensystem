package datenspeicherung.db;

import fachkonzept.Produkt;

public class ProduktSpeicherTest
{
	public static void main(String[] args)
	{
		try
		{
			ProduktSpeicher speicher = new ProduktSpeicher();
			for (Produkt p : speicher.alleLaden())
			{
				System.out.println(p.getEAN() + " | " + p.getBezeichnung() + " | " + p.getNettoPreis() + " | "
						+ p.getBasiseinheit() + " | " + p.getUmsatzsteuersatz() + "%");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
