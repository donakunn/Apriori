
package mining;

import java.util.*;
import java.io.Serializable;

/**
 * Modella un itemset o pattern, frequente. Implementa l'interfaccia Serializable in modo che le sue 
 * istanze siano serializzabili in flussi di byte che le rappresentino.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class FrequentPattern implements Serializable{

	/**
	 * Array che contiene riferimenti a oggetti istanza della classe Item che definiscono il pattern.
	 */
	private List<Item> fp= new LinkedList<Item>();

	/**
	 * Valore di supporto calcolato per il pattern fp.
	 */
	private float support;

	/**
	 * Costruttore di un pattern che alloca fp come array di dimensione 0.
	 */
	FrequentPattern(){
		this.fp= new LinkedList<Item>();  
	}

	/**
	 * Aggiunge un nuovo Item al pattern estendendo la dimensione di fp di 1 e si inserendolo in ultima posizione.
	 * @param item item da aggiungere al pattern attuale
	 */
	protected void addItem(Item item){
		fp.add(item);
	}

	/**
	 * Restituisce l'item in posizione index di fp.
	 * @param index posizione dell'item richiesto
	 * @return Item in posizione index nel pattern corrente
	 */
	Item getItem(int index){
		return fp.get(index);
	}

	/**
	 * Restituisce il membro support del pattern corrente.
	 * @return Supporto del pattern corrente
	 */
	public float getSupport(){
		return support;		 
	}

	/**
	 * Restituisce la lunghezza del pattern corrente.
	 * @return Lunghezza del pattern corrente
	 */
	int getPatternLength(){
		return fp.size();
	}

	/**
	 * Assegna al membro support il parametro in input.
	 * @param Supporto del pattern corrente
	 */
	void setSupport(float support){
		this.support=support;
	}

	/**
	 * Scandisce il pattern corrente al fine di concatenare in una 
	 * stringa la rappresentazione degli item; alla fine si concatena il 
	 * supporto.
	 * Ridefinisce il metodo toString() della superclasse Object.
	 *@return Stringa rappresentante il pattern corrente
	 */
	public String toString(){

		Iterator<Item> b = this.iterator();
		String value="";

		while(b.hasNext()){
			value+= b.next() ;
			if(b.hasNext())
				value += " AND ";
		}
		if(fp.size()>0){
			value+="["+support+"]";
		}
		return value;
	}

	/**
	 * Iteratore della lista tipizzata ad Item.
	 * @return Iteratore che itera sui pattern contenuti nella lista 
	 *         corrente
	 */
	public Iterator<Item> iterator(){
		return fp.iterator();
	}
}
