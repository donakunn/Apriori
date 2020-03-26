
package mining;

import java.lang.Comparable;
import java.io.Serializable;

/**
 * Modella una regola di associazione con confidenza maggiore o al pi&ugrave; uguale a quella specificata dall'utente.
 * Implementa le interfacce Serializable, in modo da rendere le istanze di tale classe serializzabili in flussi di byte
 * che le rappresentino, e l'interfaccia generica Comparable(T), ( con T posto a Float, in tal caso), in modo che gli oggetti 
 * contenuti all'interno dei contenitori sue istanze siano ordinate secondo l'ordinamento naturale specificato attraverso 
 * l'implementazione del metodo Comprarable.compareTo().
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class AssociationRule implements Comparable<AssociationRule>, Serializable {	

	/**
	 * Modella l'antecedente della regola di associazione, inizialmente vuoto
	 */
	private Item antecedent[]=new Item[0];

	/**
	 * Modella il conseguente della regola di associazione, inizialmente vuoto
	 */
	private Item consequent[]=new Item[0];

	/**
	 * Valore del supporto della regola d'associazione.
	 */
	private float support;

	/**
	 * Valore della confidenza della regola.
	 */
	private float confidence;

	/**
	 * Costruttore di una regola d'associazione; inizializza il membro support con l'argomento passatogli in input
	 * @param support Supporto del pattern frequente da cui la regola &egrave; derivato 
	 */
	AssociationRule(float support){
		this.support=support;
	}

	/**
	 * Restituisce il valore del supporto della regola d'associazione corrente.
	 * @return Supporto della corrente regola d'associazione
	 */
	protected float getSupport(){
		return support;
	}

	/**
	 * Restituisce il valore della confidenza della regola d'associazione corrente.
	 * @return Confidenza della corrente regola d'associazione 
	 */
	public float getConfidence(){
		return confidence;
	}

	/**
	 * Restituisce la lunghezza dell'antecedente della corrente regola d'associazione
	 * @return Lunghezza dell'antecedente della regola d'associazione
	 */
	int getAntecedentLength(){
		return antecedent.length;
	}

	/**
	 * Restituisce la lunghezza del conseguente della corrente regola d'associazione.
	 * @return Lunghezza del conseguente della regola d'associazione
	 */
	int getConsequentLength(){
		return consequent.length;
	}

	/**
	 * Estende di una unit&agrave; la dimensione dell'array antecedent e aggiunge l'argomento della procedura nell'ultima
	 * posizione di tale array
	 * @param item Item da aggiungere all'antecedente della regola d'associazione corrente
	 */
	void addAntecedentItem(Item item){
		int length =antecedent.length;
		Item temp []=new Item[length+1];
		System.arraycopy(antecedent, 0, temp, 0, length);
		temp [length]=item;
		antecedent=temp;
	}

	/**
	 * Estende di una unit&agrave; la dimensione dell'array consequent e aggiunge l'argomento della procedura nell'ultima
	 * posizione di tale array
	 * @param item Item da aggiungere al conseguente della regola d'associaizone corrente
	 */
	void addConsequentItem(Item item){
		int length =consequent.length;
		Item temp []=new Item[length+1];
		System.arraycopy(consequent, 0, temp, 0, length);
		temp [length]=item;
		consequent=temp;
	}

	/**
	 * Restituisce  l'elemento dell'antecedente nella posizione indicata da index
	 * @param index indice dell'array antecedent
	 * @return Elemento dell'antecedente nella posizione indicata da index
	 */
	Item getAntecedentItem(int index){
		return antecedent[index];
	}

	/**
	 * Restituisce l'elemento del conseguente nella posizione indicata da index
	 * @param index indice dell'array consequent
	 * @return Elemento del conseguente nella posizione indicata da index
	 */
	Item getConsequentItem(int index){
		return consequent[index];
	}

	/**
	 * Assegna al membro confidence il paramentro della	procedura
	 * @param confidence confidenza della regola d'associazione corrente
	 */
	protected void setConfidence(float confidence){
		this.confidence=confidence;
	}

	/**
	 * Crea e restituisce la stringa che rappresenta la regola d'associazione corrente con relativi supporto e confidenza.
	 * Ridefinisce il metodo toString() della superclasse Object.
	 * @return Stringa che rappresenta la regola d'associazione
	 */
	public String toString(){

		String value="";
		for(int i=0; i<antecedent.length;i++){
			value+= antecedent[i];
			if(i!=antecedent.length-1)
				value+=" AND ";
		}

		value+="==>";
		for(int j=0; j<consequent.length;j++){
			value+= consequent[j];	
			if(j!=consequent.length-1)
				value+=" AND ";
		}
		return value += " ["+ support + ", "+ confidence +"]"+"\n";					
	}

	/**
	 * Ridefinisce il metodo compareTo() dell'interfaccia Comparable, in modo da stabilire un'ordinamento
	 * naturale tra le regole d'associazione; in particolare, suddette regole sono ordinate in modo ascendente
	 * rispetto al valore della confidenza a ciascuna di esse associate.
	 */
	public int compareTo(AssociationRule ar) {
		return (this.confidence < ar.getConfidence() ? -1 : (this.confidence == ar.getConfidence()) ? 0 : 1 );	
	}
}











