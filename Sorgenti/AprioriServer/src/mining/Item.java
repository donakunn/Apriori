
package mining;

import data.Attribute;
import java.io.*;

/**
 * Classe astratta che modella un generico item (coppia attributo-valore, per esempio Outlook="Sunny").
 * Implementa l'interfaccia Serializable in modo che le istanze delle sue sottoclassi siano serializzabili 
 * in flussi di byte che le rappresentino.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public abstract class Item implements Serializable {
	private Attribute attribute;
	private Object value; 

	/**
	 * Inizializza i valori dei membri attributi con i parametri passati come argomento al costruttore.
	 * @param attribute rappresenta un attributo coinvolto nell'item.
	 * @param value rappresenta il valore assegnato all'attributo.
	 */
	protected Item(Attribute attribute, Object value){
		this.attribute=attribute;
		this.value=value;
	}

	/**
	 * Restituisce il membro attribute dell'Item corrente.
	 * @return Attributo membro dell'item.
	 */
	protected Attribute getAttribute(){
		return attribute;
	}

	/**
	 * Restituisce il membro value dell'Item corrente.
	 * @return Valore assegnato all'attributo.
	 */
	protected Object getValue(){
		return value;
	}

	/**Verifica che il membro value sia uguale (nello stato) all’argomento passato come parametro della funzione.
	 * Il metodo è astratto e sar&agrave; implementato all'interno della/e sottoclasse/i di Item.
	 * @param value Valore dell'attributo continuo
	 * @return true se la condizione di controllo è vera, false altrimenti
	 */
	public abstract boolean checkItemCondition(Object value);

	/**
	 * Restituisce una stringa nella forma &lt;attribute&gt;=&lt;value&gt; che concatena l'attribtuo e il suo valore 
	 * nella forma &lt;attribute&gt;=&lt;value&gt;.
	 * @return  Stringa contenente attributo e valore dell'Item corrente
	 */
	public String toString(){
		return "("+ attribute +"="+ value +")";
	}
}
