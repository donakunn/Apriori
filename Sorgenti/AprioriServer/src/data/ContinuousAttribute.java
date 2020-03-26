
package data;

import java.util.Iterator;

/**
 * Modella un attributo continuo (numerico) rappresentandone il dominio [min,max]. La classe 
 * implementa l'interfaccia generica Iterable&lt;T&gt; in modo tale che le sue istanze possano 
 * essere target dell'istruzione iterativa foreach().
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class ContinuousAttribute extends Attribute implements Iterable<Float>{

	/**
	 * Estremo superiore dell'intervallo di valori di un'istanza di ContinuousAttribute.
	 */
	private float max;

	/**
	 * Estremo inferiore dell'intervallo di valori di un'istanza di ContinuousAttribute.
	 */
	private float min;

	/**
	 * Costruttore; invoca il costruttore della classe genitore e avvalora i membri name, index, min e max.
	 * @param name nome simbolico dell'attributo
	 * @param index identificativo numerico dell'attributo
	 * @param min estremo inferiore dell'intervallo
	 * @param max estremo superiore dell'intervallo
	 */
	ContinuousAttribute(String name, int index, float min, float max){
		super(name,index);
		this.min=min;
		this.max=max;
	}

	/**
	 * Restituisce l'estremo inferiore dell'intervallo in cui ricade il valore relativo alla corrente istanza di ContinuousAttribute.
	 * @return Estremo inferiore dell'intervallo suddetto
	 */
	protected float getMin(){
		return min;
	}

	/**
	 * Restituisce l'estremo superiore dell'intervallo in cui ricade il valore relativo alla corrente istanza di ContinuousAttribute.
	 * @return Estremo superiore dell'intervallo 
	 */
	protected float getMax(){
		return max;
	}

	/**
	 * Definisce un iteratore specifico per un contenitore di oggetti istanze della classe ContinuousAttribute.
	 * Definisce un corpo per il metodo astratto Iterator&lt;T&gt; iterator() dell'interfaccia generica Iterator&lt;T&gt; con parametro di tipo float
	 * @return Iteratore di un contenitore di numeri reali
	 */
	public Iterator<Float> iterator() {
		return new ContinuousAttributeIterator(getMin(), getMax(), 5);
	}
}
