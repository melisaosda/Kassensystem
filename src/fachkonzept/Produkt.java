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
		if (pBezeichnung == null || pBezeichnung.trim().isEmpty() || pBezeichnung.length() > 100)
		{
			throw new IllegalArgumentException("Die Bezeichnung muss 1 bis 100 Zeichen lang sein.");
		}
		if (pNettopreis < 0)
		{
			throw new IllegalArgumentException("Der Nettopreis darf nicht negativ sein.");
		}
		if (pBasiseinheit == null || pBasiseinheit.trim().isEmpty() || pBasiseinheit.length() > 3)
		{
			throw new IllegalArgumentException("Die Basiseinheit muss 1 bis 3 Zeichen lang sein.");
		}
		if (pUmsatzsteuersatz < 0)
		{
			throw new IllegalArgumentException("Der Umsatzsteuersatz darf nicht negativ sein.");
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
