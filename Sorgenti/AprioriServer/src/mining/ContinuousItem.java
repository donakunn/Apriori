
package mining;

import data.ContinuousAttribute;

/**
 * Modella la coppia &lt;Attributo continuo - Intervallo di valori&gt;.
 *
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class ContinuousItem extends Item {

	/**
	 * @param attribute attributo continuo istanza della classe ContinuousAttribute
	 * @param value intervallo di valori reali istanza della classe Interval
	 */
	ContinuousItem(ContinuousAttribute attribute, Interval value) {
		super(attribute, value);		
	}

	/**
	 * Verifica che il membro value sia uguale (nello stato) all’argomento passato come parametro della funzione.
	 * Definisce un corpo per il metodo astratto checkItemCondition della superclasse.
	 * @param value valore Valore dell'attributo corrente
	 * @return true se il valore è incluso nell'intervallo specificato, false altrimenti
	 */
	public boolean checkItemCondition(Object value){
		return ( (Interval) getValue()).checkValueInclusion((float) value);
	}

	/**
	 * Restituisce una stringa rappresentate il valore dell'attributo continuo e l'intervallo di valori
	 * reali in cui &egrave; incluso.
	 * Ridefinisce il metodo toString() della superclasse Object.
	 * @return Stringa rappresentate il valore dll'attributo continuo e l'intervallo di valori
	 */
	public String toString(){
		String value="";		
		return value += this.getAttribute() + " in " + (Interval)getValue();
	}
}
