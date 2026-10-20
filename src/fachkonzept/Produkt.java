package fachkonzept;

public class Produkt
{
	private String EAN;
	private String bezeichnung;
	private double nettopreis;
	private String basiseinheit;
	private int umsatzsteuersatz;

	public Produkt(String pEAN, String pBezeichnung, double pNettopreis, String pBasiseinheit, int pUmsatzsteuersatz)
	{
		if (pEAN == null || !pEAN.matches("\\d{13}"))
		{
			throw new IllegalArgumentException("Die EAN muss aus genau 13 Ziffern bestehen.");
		}
		EAN = pEAN;
		bezeichnung = pBezeichnung;
		nettopreis = pNettopreis;
		basiseinheit = pBasiseinheit;
		umsatzsteuersatz = pUmsatzsteuersatz;
	}

	public String getEAN()
	{
		return EAN;
	}

	public String getBezeichnung()
	{
		return bezeichnung;
	}

	public double getNettoPreis()
	{
		return nettopreis;
	}

	public String getBasiseinheit()
	{
		return basiseinheit;
	}

	public int getUmsatzsteuersatz()
	{
		return umsatzsteuersatz;
	}

	@Override
	public String toString()
	{
		return EAN + " | " + bezeichnung + " | " + nettopreis + " EUR netto / " + basiseinheit + " | "
				+ umsatzsteuersatz + "% MwSt";
	}
}
