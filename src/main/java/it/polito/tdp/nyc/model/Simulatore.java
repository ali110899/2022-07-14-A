package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.nyc.model.Evento.EventType;

public class Simulatore {

	/*
	 * 1.Nuova condivisione file
	 * 2.Ri-condivisione file(durata = durata/2)
	 * 3.Termine condivisione di file
	 */
	
	/*
	 * EVENTO (Nuoca condivisione o ri-condivisione)
	 * -Tempo T
	 * -NTA
	 * -Durata d condivisione
	 */
	
	/*
	 * EVENTO (Termina la condivisione)
	 * -Tempo T
	 * -
	 */
	
	//Parametri input
	private double probabilità;
	private int durata;
	
	//Stato del sistema
	private Graph<NTA, DefaultWeightedEdge> grafo;
	//questa mappa mi servirà per capire se un nodo sta ancora condividendo
	Map<NTA, Integer> numeroCondivisioni;
	private List<NTA> vertici;
	
	//Output-mappa di tutte le condivisioni(share-stop) fatte
	Map<NTA, Integer> numeroTotaleCondivisioni;
	
	//Coda degli eventi
	private PriorityQueue<Evento> queue;

	public Simulatore(Graph<NTA, DefaultWeightedEdge> grafo, double probabilità, int durata) {
		this.probabilità = probabilità;
		this.durata = durata;
		this.grafo = grafo;
	}
	
	public void inizializzazione() {
		
		numeroCondivisioni = new HashMap<NTA, Integer>();
		numeroTotaleCondivisioni = new HashMap<NTA, Integer>();
		
		//le popolo di zeri-->inizializzo(nessuno condivide
		for(NTA nta : this.grafo.vertexSet()) {
			this.numeroCondivisioni.put(nta, 0);
			this.numeroTotaleCondivisioni.put(nta, 0);
		}
		
		this.vertici = new ArrayList<NTA>(this.grafo.vertexSet());
		this.queue = new PriorityQueue<Evento>();
		
		//creo eventi iniziali
		for(int t=0; t<100; t++) {
			if(Math.random()<=this.probabilità) {
				//di tutti miei vertici, ne ottengo uno a caso(ovviamente l'indice nella lista)
				int n = (int) (Math.random()*this.vertici.size());
				//creao il primo evento da nta randomico
				Evento e = new Evento(EventType.SHARE, t, this.vertici.get(n), this.durata);
				this.queue.add(e);
			}
		}
	
	}
	
	public void run() {
		while(this.queue.isEmpty()==false) {
			//estraggo un evento 
			Evento e = this.queue.poll();
			//check se non ho superato il 100-esimo giorno
			if(e.getTime()>=100) {
				break;
			}
			
			int time = e.getTime();
			int duration = e.getDurata();
			NTA nta = e.getNta();
			
			System.out.println(e.getType()+" "+time+" "+nta.getNTACode()+" "+duration);
			
			switch(e.getType()) {
			case SHARE:
				//stai condividendo ad un altro nta
				this.numeroCondivisioni.put(nta, this.numeroCondivisioni.get(nta)+1);
				this.numeroTotaleCondivisioni.put(nta, this.numeroTotaleCondivisioni.get(nta)+1);
				//al tempo "" smetterò di condividere
				Evento evento = new Evento(EventType.STOP, time+duration, nta, 0);
				this.queue.add(evento);
				
				//RICONDIVISIONE
				//trovare tra i miei adiacenti, nta che non sta condividendo con peso magg
				if(duration/2>0) {
					NTA nuovo = trovaNTA(nta);
					if(nuovo!=null) {
						Evento evento2 = new Evento(EventType.SHARE, time+1, nuovo, duration/2);
						this.queue.add(evento2);
					}
				}
				break;
				
			case STOP:
				//quel nta non sta più condividendo
				//aggiorno stato sistema(decremento le condivisioni di nta)
				this.numeroCondivisioni.put(nta, this.numeroCondivisioni.get(nta)-1);
				break;
			}
		}
	}

	private NTA trovaNTA(NTA nta) {
		
		int max = 0;
		NTA best = null;
		
		//ciclo sugli archi uscenti(non orientato è uguale per quelli entranti)
		for(DefaultWeightedEdge d : this.grafo.outgoingEdgesOf(nta)) {
			NTA vicino = Graphs.getOppositeVertex(this.grafo, d, nta);
			int peso = (int)(this.grafo.getEdgeWeight(d));
			//check se peso arco e maggiore e se il vcino non sta condividento
			if(peso>max && this.numeroCondivisioni.get(vicino)==0) {
				max = peso;
				best = vicino;
			}
		}
		return best;
	}
	
	public Map<NTA, Integer> getNumeroConnessioni() {
		return numeroTotaleCondivisioni;
	}
	
}
