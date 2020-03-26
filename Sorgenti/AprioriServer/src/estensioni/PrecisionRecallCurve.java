
package estensioni;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import mining.AssociationRule;
import mining.AssociationRuleArchive;
import mining.FrequentPattern;
import mining.NoPatternException;

/**
 * Modella un meccanismo di rilevazione delle frequenze assolute dei pattern frequenti rispetto al valore di supporto; 
 * la stessa operazione &egrave; eseguita per le regole d'associazione, rispetto però alla confidenza di queste ultime.
 *                 
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni
 */
public class PrecisionRecallCurve {

	/**
	 * Contenitore di coppie del tipo &lt;pattern frequenti - {insieme di regole d'associazione confidenti}&gt;
	 */
	private HashMap<FrequentPattern, TreeSet<AssociationRule>> archive;

	/**
	 * Contenitore di regole d'associazione confidenti.
	 */
	private AssociationRuleArchive arch ;

	/**
	 * Costruttore; si occupa di inizializzare i membri archive e arch di una nuova istanza di PrecisionRecallCurve
	 * @param archive contenitore di coppie &lt;pattern frequenti - {insieme di regole d'associazione confidenti}&gt;
	 * @param arch contenitore di regole d'associazione
	 */
	public PrecisionRecallCurve(HashMap<FrequentPattern, TreeSet<AssociationRule>> archive, AssociationRuleArchive arch){
		this.archive=archive;
		this.arch = arch;
	}

	/**
	 * Si occupa di contare il numero di occorrenze di pattern frequenti il cui supporto ricade
	 * in un determinato intervallo di valori ( per esempio, tra 0 e 0.1;  tra 0.1 e 0.2;... etc.)
	 * @return Vettore contenente le frequenze assolute relative a ciascun intervallo di valori del supporto
	 */
	public int[] suppCount() {

		Iterator <Map.Entry<FrequentPattern, TreeSet<AssociationRule>>> it = (archive).entrySet().iterator();
		double supp;
		int suppCounter[]= new int[10];

		while (it.hasNext()) {
			Map.Entry<FrequentPattern, TreeSet<AssociationRule>> entry = it.next();
			supp = (entry.getKey()).getSupport();
			if (supp >=0 && supp < 0.1) {
				suppCounter[0] ++;
			}
			else if (supp >=0.1 && supp < 0.2) {
				suppCounter[1] ++;
			}
			else if (supp >=0.2 && supp < 0.3) {
				suppCounter[2] ++;
			}
			else if (supp >=0.3 && supp < 0.4) {
				suppCounter[3] ++;
			}
			else if (supp >=0.4 && supp < 0.5) {
				suppCounter[4] ++;
			}
			else if (supp >=0.5 && supp < 0.6) {
				suppCounter[5] ++;
			}
			else if (supp >=0.6 && supp < 0.7) {
				suppCounter[6] ++;
			}
			else if (supp >=0.7 && supp < 0.8) {
				suppCounter[7] ++;
			}
			else if (supp >=0.8 && supp < 0.9) {
				suppCounter[8] ++;
			}
			else  {
				suppCounter[9] ++;
			}
		}
		return suppCounter;
	}

	/**
	 * Si occupa di contare il numero di occorrenze di regole d'associazione confidenti la cui confidenza
	 * ricade in un determinato intervallo di valori ( per esempio, tra 0 e 0.1; tra 0.1 e 0.2;... etc.)
	 * @return Vettore contenente le frequenze assolute relative a ciscun intervallo di valori della confidenza
	 * @throws NoPatternException nessuna regola confidente è generata da un pattern frequente
	 */
	public int[] confCount() throws NoPatternException{

		Iterator <Map.Entry<FrequentPattern, TreeSet<AssociationRule>>> it = (archive).entrySet().iterator();
		double conf;
		TreeSet<AssociationRule> rules = new TreeSet<AssociationRule>();
		int confCounter[]= new int[10];

		while (it.hasNext()) {
			Map.Entry<FrequentPattern, TreeSet<AssociationRule>> entry = it.next();
			rules = arch.getRules(entry.getKey());
			Iterator<AssociationRule> it2 = rules.iterator();
			while (it2.hasNext()) {
				conf = it2.next().getConfidence();

				if (conf >=0 && conf < 0.1) {
					confCounter[0] ++;
				}
				else if (conf >=0.1 && conf < 0.2) {
					confCounter[1] ++;
				}
				else if (conf >=0.2 && conf < 0.3) {
					confCounter[2] ++;
				}
				else if (conf >=0.3 && conf < 0.4) {
					confCounter[3] ++;
				}
				else if (conf >=0.4 && conf < 0.5) {
					confCounter[4] ++;
				}
				else if (conf >=0.5 && conf < 0.6) {
					confCounter[5] ++;
				}
				else if (conf >=0.6 && conf < 0.7) {
					confCounter[6] ++;
				}
				else if (conf >=0.7 && conf < 0.8) {
					confCounter[7] ++;
				}
				else if (conf >=0.8 && conf < 0.9) {
					confCounter[8] ++;
				}
				else  {
					confCounter[9] ++;
				}
			}
		}
		return confCounter;
	}
}