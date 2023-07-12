package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private List<String> boroughs;
	private List<NTA> nta;
	private Graph<NTA, DefaultWeightedEdge> grafo;
	
	
	public Model() {
		
		
	}
	
	public void creaGrafo(String borough) {
		
		NYCDao dao = new NYCDao();
		this.nta = dao.getNTAbyBorough(borough);
		
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//creo vertici
		Graphs.addAllVertices(this.grafo, this.nta);
		
		//creo arco
		for(NTA n1 : this.nta) {
			for(NTA n2 : this.nta) {
				//per crearmi l'arco i due vertici devono essere diversi
				if(n1.equals(n2)==false) {
					Set<String> unione = new HashSet<String>(n1.getSSID());
					//aggiungo al Set di SSID(n1) il Set di SSID(n2)
					unione.addAll(n2.getSSID());				
					//ho preso i vertici NTA n1 e n2 con arco unione.size()
					Graphs.addEdge(this.grafo, n1, n2, unione.size());
				}
			}	
		}
		
		System.out.println("Vertici: "+this.grafo.vertexSet().size()+"\n"+"Archi: "+this.grafo.edgeSet().size());
		
	}
	
	public List<Arco> analisiArchi() {
		
		//calcolo media degli archi
		double media =0;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			media = media +this.grafo.getEdgeWeight(e);
		}
		media = media/this.grafo.edgeSet().size();
		
		//devo prendere archi con valore MAGGIORE della media
		List<Arco> result = new ArrayList<Arco>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			//archi solo maggiori della media
			if(this.grafo.getEdgeWeight(e)>media) {
				//Arco(vertice1, vertice2, pesoArco)
				Arco arco = new Arco(this.grafo.getEdgeSource(e).getNTACode(), this.grafo.getEdgeTarget(e).getNTACode(), (int)(this.grafo.getEdgeWeight(e)));
				result.add(arco);
			}
		}
		//ordinati per peso decrescente
		Collections.sort(result);
		return result;
	}
	
	public List<String> getBoroughs() {
		
		if(this.boroughs==null) {
			NYCDao dao = new NYCDao();
			this.boroughs = dao.getHotspotBorough();
		}
		return this.boroughs;
	}
	
	public Map<NTA, Integer> simula(double probabilità, int durata) {
		Simulatore sim = new Simulatore(this.grafo, probabilità, durata);
		sim.inizializzazione();
		sim.run();
		return sim.getNumeroConnessioni();
	}
	
}
