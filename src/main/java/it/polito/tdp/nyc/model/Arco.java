package it.polito.tdp.nyc.model;

public class Arco implements Comparable<Arco>{

	private String vertice1;
	private String vertice2;
	private Integer peso;
	
	public Arco(String vertice1, String vertice2, int peso) {
		super();
		this.vertice1 = vertice1;
		this.vertice2 = vertice2;
		this.peso = peso;
	}

	public String getVertice1() {
		return vertice1;
	}

	public String getVertice2() {
		return vertice2;
	}

	public Integer getPeso() {
		return peso;
	}

	@Override
	public int compareTo(Arco o) {
		
		//NB se la sottrazione di 2 numeri danno num<1 lui arrotonda a zero-->non va bene
		
		//lo vogliamo decrescente:
		return o.peso-this.peso;
	}

	@Override
	public String toString() {
		return "Arco: vertice1= " +vertice1+ ", vertice2= " +vertice2+ ", peso= " +peso+"";
	}
	
	
	
}
