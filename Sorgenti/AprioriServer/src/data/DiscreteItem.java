
package data;

import mining.Item;

/**
 * Modella la coppia  &lt;Attributo  discreto  -  valore  discreto&gt; (es.: "Outlook = Sunny").
 * 
 * @author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class DiscreteItem extends Item {

	/**
	 * Costruttore di un Item; Invoca il costruttore della classe madre per istanziare e avvalorare le sue istanze.
	 * @param attribute attributo discreto
	 * @param value valore discreto associato al relativo attributo discreto
	 */
	public DiscreteItem(DiscreteAttribute attribute, String value) {//
		super(attribute, value);
	}

	/**
	 * Verifica che il membro value sia uguale (nello stato) all’argomento passato come parametro della funzione.
	 * Definisce un corpo per il metodo astratto checkItemCondition() della superclasse Item.
	 * @param value valore dell'attributo discreto
	 * @return true se value &egrave; uguale al valore dell'attributo discreto dell'item corrente, false altrimenti
	 */
	public boolean checkItemCondition(Object value) {
		return getValue().equals(value);
	}
}
