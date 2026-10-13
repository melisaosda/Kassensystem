package fachkonzept;

public class Produkt
{
	private String EAN;
	private String bezeichnung;
	private double nettopreis;
	private String basiseinheit;
	private int umsatzsteuersatz;
	
	public Produkt(String pEAN)
	{
		EAN = pEAN;
	}

	public Produkt(String pEAN, String pBezeichnung, double pNettopreis, String pBasiseinheit, int pUmsatzsteuersatz)
	{
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
}
