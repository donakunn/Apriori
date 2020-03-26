package client;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import org.jfree.ui.RefineryUtilities;
import estensioni.Grafici;

/**
 * Crea un'interfaccia grafica lato client contenente comandi utili per l'estrazione di un insieme 
 * di entit&agrave; del tipo &lt;pattern frequente - { insieme di regole d'associazione confidenti}&gt;, 
 * salvataggio di tale insieme in un file pdf memorizzatto sul server, creazione di un grafico
 * a curva di precisione che riporta la distribuzione di frequenza dei pattern e delle regole
 * d'associazione rispetto al supporto, nel primo caso, e alla confidenza, nel secondo.
 *              
 *@author Gruppo Map formato da Donato Lucente, Antonio Ricchetti, Gianluca Colaianni              
 */
public class Apriori extends java.awt.Frame{

	/**
	 * Rappresenta il minimo supporto, inserito dall'utente e in quanto numero reale.
	 */
	private float minSup;

	/**
	 * Rappresenta la minima confidenza, inserita dall'utente e in quanto numero reale.
	 */
	private float minConf;

	/**
	 * Rappresenta il nome della tabella del database di cui si estraggono lo schema e l'insieme di tuple che contiene.
	 */
	private String tableName;

	/**
	 * Finestra principale, contenente l'interfaccia grafica utente lato client; lo stile del frame sfrutta lo stile
	 * delle finestre grafiche tipico del sistema operativo su cui l'utente opera.
	 */
	private JFrame frame;

	/**
	 * Riquadro contenente sotto-riquadri per la selezione della sorgente dati e l'immissione di parametri di input.
	 * Tale riquadro &egrave; contenuto nel primo pannello principale, panel1.
	 */
	private TitledBorder bordoPanel1;

	/**
	 * Sotto-riquadro di bordoPanel1, contenente campi di selezione della sorgente dati
	 */
	private TitledBorder borderPanel2;

	/**
	 * Sotto-riquadro di bordoPanel1 che delimita uno spazio contenente i campi di immissione dei parametri utente, quali nome della tabella o del file, 
	 * minimo supporto e minima confidenza.
	 */
	private TitledBorder borderPanello3;

	/**
	 * Riquadro delimitante un'area testuale in cui mostrare i risultati dell'estrazione di pattern frequenti e relative regole d'associazione
	 * da un database o da un file memorizzato nel filsystem del server. 
	 */
	private TitledBorder borderPanel5;

	/**
	 * Pannello principale nel quale inserire le componenti grafiche dell'interfaccia utente; in particolare un riquadro 
	 * interno a tale pannello, etichettato con la stringa "Apriori", delinea un'area all'interno della quale saranno poste 
	 * le sezioni riguardanti l'immissione dei parametri utente.
	 */
	private JPanel panel1;

	/**
	 * Sotto-pannello del pannello principale panel1 contenente un riquadro che delinea un'area dedicata alla selezione della sorgente dati 
	 * ( database relazionale o tabella ).
	 */
	private JPanel panel2;

	/**
	 * Sotto-pannello del pannello principale panel1 contenente i campi testuali per l'immissione di parametri di input. quali il nome 
	 * della tabella dati, il minimo supporto e la minima confidenza; ad ogni campo testuale &egrave; affiancata una targhetta opportuna che ne 
	 * riporta il nome.
	 */
	private JPanel panel3;

	/**
	 * Secondo pannello principale, contenente le componenti grafiche per l'attivazione del algoritmo di estrazione dei pattern frequenti
	 * e relative regole d'associazione confidenti. 
	 */
	private JPanel panel4;

	/**
	 * Terzo pannello principale, contenente un'area di testo atta a mostrare i pattern frequenti e le relative regole d'associazione 
	 * in formato testuale. Un'altra area si testo, sottostante la precedente, mostra i messaggi di sistema relativi ad eventuali 
	 * eccezioni al run-time.
	 */
	private JPanel panel5;

	/**
	 * Quarto pannello principale contenente le componenti grafiche per il salvataggio
	 * del resultset  di pattern frequenti e regole d'associazione confidenti estratto,
	 * su un file in formato pdf, e per la visualizzazione del contenuto di un file 
	 * pdf contenente suddetto set.
	 */
	private JPanel panel6;

	/**
	 * Componente non visibile, che permette di raggruppare le componenti JRadioButton
	 * in modo che solo uno di essi possa essere selezionato.
	 */
	private ButtonGroup bottonGroup;

	/**
	 * Componente JRadioButton per la selezione della sorgente dati ( database o file, a seconda
	 * di come viene impostato ).
	 */
	private JRadioButton radioUno;

	/**
	 * Componente JRadioButton per la selezione della sorgente dati ( database o file, a seconda
	 * di come viene impostato ).
	 */
	private JRadioButton radioDue;

	/**
	 * Etichetta associata all'area di testo contenuta nella sezione dei parametri di input,
	 * in cui immettere il nome della tabella sorgente.
	 */
	private JLabel labelData;

	/**
	 * Etichetta associata all'area di testo contenuta nella sezione dei paramtri di input,
	 * in cui immettere il valore di minimo supporto.
	 */
	private JLabel lblMinSup;

	/**
	 * Etichetta associata all'area di testo contenuta nella sezione dei paramtri di input,
	 * in cui immettere il valore di minima confidenza.
	 */
	private JLabel lblMinConf;

	/**
	 * Area di testo contenuta nella sezione dei paramtri di input, in cui immettere 
	 * il nome della tabella sorgente.
	 */
	private JTextField txtData;

	/**
	 * Area di testo contenuta nella sezione dei paramtri di input, in cui immettere 
	 * il valore di minimo supporto.
	 */
	private JTextField Minsup;

	/**
	 * Area di testo contenuta nella sezione dei paramtri di input, in cui immettere 
	 * il valore di minima confidenza.
	 */
	private JTextField Minconf;

	/**
	 * Area di testo contenente messaggi emessi dal sistema, a seguito dell'occorrere
	 * di eccezioni al run-time o dello stato di successo dell'estrazione dei pattern
	 * frequenti e regole d'associazione confidenti.
	 */
	private JTextArea msgArea;

	/**
	 * Area di testo contenente i risultati dell'estrazione di pattern frequenti e regole d'associazione.
	 * confidenti.
	 */
	private JTextArea rulesArea;

	/**
	 *Area scrollabile, consente di rendere scrollabile ( per esempio ) il contenuto di un'area testuale.
	 */
	private JScrollPane scrollBar;

	/**
	 * Bottone grafico per l'attivaizone dell'algoritmo di estrazione di pattern frequenti
	 * e regole d'associazione confidenti; un apposito listener associato al bottone stesso
	 * si occupa dell'attivazione suddetta, il tutto in modo nascosto all'utente.
	 */
	private JButton btApriori;

	/**
	 * Bottone grafico per il salvataggio del set di pattern frequenti e regole d'associazione confidenti
	 * in un file pdf; un apposito listener associato al bottone si occupa della suddetta visualizzazione, 
	 * in modo nascosto all'utente.
	 */
	private JButton SavePdf;

	/**
	 * Bottone grafico per la generazione di un istogramma.
	 */
	private JButton grafico;

	/**
	 * Flusso di output da client a server, per l'invio dei dati utente, immessi nell'interfaccia grafica 
	 * utente.
	 * 
	 */
	private ObjectOutputStream out;

	/**
	 * Flusso di input dal server al client, per la ricezione dei risultati dell'estrazione
	 * di pattern frequenti e regole d'associazione confidenti.
	 */
	private ObjectInputStream in;

	/**
	 * Socket lato client per la connessione verso il server e la ricezione di informazioni
	 * dal server.
	 */
	private Socket socket;

	/**
	 * Flag per l'abilitazione alla generazione di un grafico.
	 *  */
	private boolean continueToGraphFlag = false;

	/**
	 * Flag per l'abilitazione del salvataggio dei dati in un file in formato pdf.
	 */
	private boolean continueToPdfFlag = false;

	/**
	 * Costruttore; inizializza l'interfaccia utente lato client.
	 */
	public Apriori() {
		inizializza();			  
	}

	/**
	 * Si occupa dell'inizializzazione dell'interfaccia utente lato client e dell'associazione dei listener per la risposta alle azioni
	 * dell'utente.
	 * Possono occorrere seguenti eccezioni:
	 * 
	 * 1) Se l'indirizzo relativo al server a cui ci si vuol connettere non pu&ograve; essere determinato, 
	 *    un'eccezione di tipo UnknownHostException viene catturata e gestita emettendo un apposito 
	 *    messaggio d'errore;
	 *    
	 * 2) Se occorrono errori nel tentativo di connessione con il server e nello scambio di informazioni con lo stesso, 
	 *    un'eccezione di tipo IOException viene catturata e gestita emettendo un apposito messaggio di errore;
	 *    
	 */
	private void inizializza() {

		InetAddress addr;
		try {
			addr = InetAddress.getByName("127.0.0.1");
			socket = new Socket (addr,8080);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(null, "Impossibile connettersi al server, indirizzo IP non determinabile","ERROR", JOptionPane.ERROR_MESSAGE);

			System.exit(0);

		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Impossibile connettersi con il server","ERROR", JOptionPane.ERROR_MESSAGE);

			System.exit(0);
		}

		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch (IOException e1) {
			JOptionPane.showMessageDialog(frame, "Scambio messaggi con il server fallito ","ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}

		frame = new JFrame("Apriori");
		frame.setResizable(false);
		frame.setBounds(100, 100, 1031, 758);
		frame.getContentPane().setLayout(null);
		Image immagine = Toolkit.getDefaultToolkit().getImage("logo.jpg");
		frame.setIconImage(immagine);
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


		/**
		 * Definizione di un windowsListener attraverso un'istanza di una classe anonima, a sua volta sottoclasse 
		 * di java.awt.event.WindowAdapter, che ridefinisce il metodo windowClosing(WindowWvwnt e); il Listener
		 * &egrave; poi associato al tasto di chiusura del frame principale.
		 */
		WindowListener exitListener = new WindowAdapter() {

			/**
			 * Si occupa di definire la risposta alla selezione del tasto di chiusura del frame; tale risposta 
			 * consiste nella creazione di una finestra di dialogo che chiede all'utente la conferma della chiusura 
			 * dell'applicazione. A seconda ddella selezione dell'utente, l'applicazione sar&agrave; chiusa, altrimenti
			 * non succeder&agrave; nulla e si ritorner&agrave; alla situazione precedente. Un apposito flag indica la chiusura
			 * dell'applicazione stessa.
			 * Se occorrono errori durante la chiusura dell'applicazione, dunque della connessione attraverso cui avviene
			 *  il passaggio d'informazioni tra client e server, un'eccezione di tipo IOException &egrave; catturata e gestita 
			 *  emettendo un'apposita finestra di errore con riportata una descrizione dell'eccezione occorsa.
			 */
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(frame, "Sei sicuro di voler uscire?","Exit Confirmation", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, null, null);

				if (confirm == 0) {
					try {

						out.writeObject(-1);
						out.close();
						socket.close();
						in.close();
					} 
					catch (IOException e1) {
						JOptionPane.showMessageDialog(frame, "Errore durante la chiusura della connessione","ERROR", JOptionPane.ERROR_MESSAGE);

					}
					finally {
						frame.dispose();
						System.exit(0);
					}
				}
			}
		};

		frame.addWindowListener(exitListener);

		bordoPanel1 = new TitledBorder("Apriori");		
		bordoPanel1.setTitlePosition(TitledBorder.TOP);

		borderPanel2 = new TitledBorder("Select Data Source");
		borderPanel2.setTitlePosition(TitledBorder.TOP);

		borderPanello3= new TitledBorder("Input Parameters");
		borderPanello3.setTitlePosition(TitledBorder.TOP);

		borderPanel5= new TitledBorder("Patterns and Rules");
		borderPanel5.setTitlePosition(TitledBorder.TOP);

		panel1 = new JPanel();
		panel1.setBorder(bordoPanel1);		
		panel1.setBounds(12, 26, 1001, 211);
		panel1.setLayout(null);
		frame.getContentPane().add(panel1);

		panel2 = new JPanel();
		panel2.setBounds(139, 37, 264, 121);
		panel2.setBorder(borderPanel2);
		FlowLayout flowLayout = (FlowLayout) panel2.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panel1.add(panel2);

		panel3 = new JPanel();
		panel3.setBounds(445, 54, 521, 85);
		panel3.setBorder(borderPanello3);
		panel3.setLayout(null);
		panel1.add(panel3);

		panel4 = new JPanel();
		panel4.setBorder(null);
		panel4.setBounds(12, 236, 1001, 43);
		frame.getContentPane().add(panel4);

		panel5 = new JPanel();
		panel5.setBounds(12, 275, 1001, 395);
		panel5.setBorder(borderPanel5);
		panel5.setLayout(null);
		frame.getContentPane().add(panel5);

		panel6 = new JPanel();
		panel6.setBorder(null);
		panel6.setBounds(12, 668, 1001, 42);
		frame.getContentPane().add(panel6);


		bottonGroup= new ButtonGroup();
		radioUno = new JRadioButton("Learning Rules from File");
		radioDue = new JRadioButton("Learning Rules From Database");
		bottonGroup.add(radioUno);
		bottonGroup.add(radioDue);
		radioDue.setSelected(true);			
		panel2.add(radioDue);
		panel2.add(radioUno);

		labelData = new JLabel("Data");
		labelData.setBounds(12, 33, 35, 16);
		panel3.add(labelData);

		lblMinSup = new JLabel("min sup");
		lblMinSup.setBounds(171, 33, 56, 16);
		panel3.add(lblMinSup);

		lblMinConf = new JLabel("min conf");
		lblMinConf.setBounds(322, 33, 56, 16);
		panel3.add(lblMinConf);

		txtData = new JTextField();
		txtData.setBounds(57, 30, 88, 22);
		panel3.add(txtData);

		Minsup = new JTextField();
		Minsup.setBounds(234, 30, 56, 22);
		panel3.add(Minsup);		

		Minconf = new JTextField();
		Minconf.setBounds(390, 30, 63, 22);
		panel3.add(Minconf);

		msgArea = new JTextArea();
		msgArea.setEditable(false);
		msgArea.setBounds(12, 333, 980, 51);	
		panel5.add(msgArea);

		rulesArea = new JTextArea();
		rulesArea.setEditable(false);
		rulesArea.setBounds(12, 13, 980, 320);
		Border cornice = BorderFactory.createLineBorder(Color.GRAY);
		rulesArea.setBorder(cornice);
		panel5.add(rulesArea);

		scrollBar = new JScrollPane(rulesArea);
		scrollBar.setBounds(12, 13, 980, 320);
		scrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panel5.add(scrollBar);
		
		/**
		 * Definizione di un Listener associato al bottone grafico "APRIORI", attraverso un'istanza di una classe anonima, 
		 * a sua volta sottoclasse della classe astratta javax.swing.AbstractAction; la classe anonima ridefinisce il metodo 
		 * actionPerformed(ActionEvent e) ereditato dall'interfaccia ActionListener.
		 */
		btApriori = new JButton(new AbstractAction("APRIORI") {

			/**
			 * Messaggio di sistema.
			 */
			private String systemMex;

			/**
			 * Definisce di un corpo per il metodo actionPerformed(ActionEvent) dell'interfaccia ActionListener,
			 * al fine di controllare la risposta alla selezione del bottone "APRIORI" rispetto alla selezione di uno tra i due
			 * bottoni della sezione grafica "Select Data Source" del pannello principale "Apriori", determinando così l'ambito 
			 * da cui estrarre le informazioni ( da database o da file ) sui pattern frequenti e le relative regole d'associazione 
			 * confidenti. Il metodo controlla anzitutto quale dei due bottoni della sezione "Select Data Source" &egrave; selezionato, 
			 * dunque invia al server l'opportuno comando; se la sorgente dati &egrave; un databse relazionale, le informazioni sul nome 
			 * della tabella del database da cui estrarre le informazioni richieste, minimo supporto, minima confidenza sono inviate 
			 * al server. Ricevute il set di dati richiesto dal server, rappresenta il contenuto di quest'ultimo in un'area di testo 
			 * apposita. 
			 * 
			 *Possono occorrere le seguenti eccezioni al run-time:
			 *
			 * - Se occorrono errori durante la chiusura dell'applicazione e degli stream che regolano il passaggio
			 *  d'informazioni tra client e server, un'eccezione di tipo IOException &egrave; catturata e gestita emettendo 
			 *  un'apposita finestra di errore con riportata una descrizione dell'eccezione occorsa. 
			 * 
			 * - Se occorrono errori nella formattazione numerica associata ad un dato numerico inserito dall'utente, 
			 *   un'eccezione di tipo NumberFormatException &egrave; catturata e gestita emettendo un'apposita finestra
			 *   di errore con riportata una descrizione dell'eccezione occorsa.
			 * 
			 * - Se il database dal quale estrarre le informazioni non esiste, l'utente &egrave; avvisato mediante un messaggio emesso
			 *   sull'interfaccia grafica, al di sotto dell'area di testo riportante i risultati.
			 * 
			 * - Se il caricamento delle informazioni richieste dal client avviene da file e questo &egrave; assente o vuoto, un messaggio 
			 *   apposito avvisa l'utente di tali eventualit&agrave;.
			 * 
			 * - Se occorrono errori durante lo scambio di informazioni con il server, un'eccezione di tipo IOException &egrave; catturata
			 *   e gestita emettendo un apposito messaggio.
			 */
			public void actionPerformed(ActionEvent e) {

				if (radioDue.isSelected()) {

					tableName = txtData.getText();				

					if(tableName.isEmpty()) {
						JOptionPane.showMessageDialog(frame, "Inserire nome tabella","WARNING", JOptionPane.WARNING_MESSAGE);

						return;
					}
					try {
						minSup = Float.parseFloat(Minsup.getText());
					}
					catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame, "Inserire valore Supporto compreso tra 0 e 1","WARNING", JOptionPane.WARNING_MESSAGE);
						return;
					}

					if (minSup < 0 || minSup >1) {
						JOptionPane.showMessageDialog(frame, "Inserire valore Supporto compreso tra 0 e 1","WARNING", JOptionPane.WARNING_MESSAGE);
						return;
					}
					try {
						minConf = Float.parseFloat(Minconf.getText());
					} 
					catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(frame, "Inserire valore Confidenza compreso tra 0 e 1","WARNING", JOptionPane.WARNING_MESSAGE);
						return;
					}

					if (minConf < 0 || minConf >1) {
						JOptionPane.showMessageDialog(frame, "Inserire valore Confidenza compreso tra 0 e 1","WARNING", JOptionPane.WARNING_MESSAGE);
						return;
					}

					try {
						out.writeObject(1);
						out.writeObject(tableName);
						out.writeObject(minSup);
						out.writeObject(minConf);

						String miningOutput= (String) in.readObject();
						if (miningOutput.isEmpty()) {
							rulesArea.setText(null);
							msgArea.setText("\nIl Database non esiste o non vi e' nessuna regola da mostrare");
							return;
						}
						rulesArea.setText(miningOutput);

						out.writeObject(2);
						out.writeObject("Rules.dat");

						systemMex = (String) in.readObject();
						msgArea.setText(systemMex);
						continueToGraphFlag = true;

					}
					catch (IOException | ClassNotFoundException e1 ) {
						JOptionPane.showMessageDialog(frame, "Scambio messaggi con il server fallito","ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
				else {	
					try {
						boolean setTextFlag= true;
						out.writeObject(3);
						out.writeObject("Rules.dat");

						String loadedFile= (String)in.readObject();		
						if (loadedFile.isEmpty()) {	
							setTextFlag= false;
							JOptionPane.showMessageDialog(frame, "File vuoto o inesistente","ERROR", JOptionPane.ERROR_MESSAGE);
						}
						if(setTextFlag){
						rulesArea.setText(loadedFile);
						msgArea.setText("\nPattern e regole caricate CON SUCCESSO");
						}
					}
					catch (IOException | ClassNotFoundException e1) {
						JOptionPane.showMessageDialog(frame, "Scambio messaggi con il server fallito","ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});

		panel4.add(btApriori);

		/**
		 * Definizione di un Listener associato al bottone grafico "Salva Pdf", attraverso un'istanza di una classe anonima, 
		 * a sua volta sottoclasse della classe astratta javax.swing.AbstractAction; la classe anonima ridefinisce il metodo 
		 * actionPerformed(ActionEvent e) ereditato dall'interfaccia ActionListener.
		 */
		SavePdf= new JButton(new AbstractAction("Salva Pdf"){

			/**
			 * Definisce di un corpo per il metodo actionPerformed(ActionEvent) dell'interfaccia ActionListener,
			 * al fine di controllare la risposta alla selezione del bottone "Salva Pdf" e attivare, lato server, 
			 * il processo di salvataggio dell'archivio di coppie &lt;pattern frequenti - {insieme di regole d'associazione 
			 * confidenti}&gt;; inoltre, il metodo invia al server il comando apposito e, in forma serializzata, il grafico 
			 * che rappresenta la distribuzione delle frequenze assolute dei pattern frequenti rispetto al supporto 
			 * e di tutte le regole confidenti rispetto alla confidenza; infine invia le informazioni sulla tabella 
			 * da cui le entit&agrave; precedenti sono state estratte, il supporto e confidenza minimi.
			 */
			public void actionPerformed(ActionEvent e) {
				if (continueToPdfFlag == true) {

					try {
						JFileChooser fileChooser = new JFileChooser();
						int n = fileChooser.showSaveDialog(Apriori.this);
						if (n == JFileChooser.APPROVE_OPTION) {
							File f = fileChooser.getSelectedFile();
							String path= f.getCanonicalPath();

							out.writeObject(4);

							out.writeObject(path + ".pdf");
							out.writeObject(minSup);
							out.writeObject(minConf);
							out.writeObject(tableName);

							Image image = (BufferedImage)ImageIO.read(new File("image.png"));
							ImageIcon imgtoSend = new ImageIcon("image.png");
							out.writeObject(imgtoSend);
							out.reset();
						}
					} 
					catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "Impossibile Salvare. Creare ResultSet e grafico","ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});

		panel6.add(SavePdf);

		/**
		 * Definizione di un Listener associato al bottone grafico "Curva di precisione", attraverso un'istanza di una classe anonima, 
		 * a sua volta sottoclasse della classe astratta javax.swing.AbstractAction; la classe anonima ridefinisce il metodo 
		 * actionPerformed(ActionEvent e) ereditato dall'interfaccia ActionListener.
		 */
		grafico = new JButton(new AbstractAction("Curva di precisione"){

			/**
			 * Definisce di un corpo per il metodo actionPerformed(ActionEvent) dell'interfaccia ActionListener,
			 * al fine di controllare la risposta alla selezione del bottone "Curva di Precisionea" e attivare il processo 
			 * generazione del grafico a curva di precisione riportante la distribuzione frequenze assolute dei pattern frequenti
			 * e delle regole d'associazione confidenti, rispetto al supporto nel primo caso, alla confidenza nel secondo. 
			 * Le informazoni di base per la generazione del grafico sono inviate dal server al client via connessione.
			 */
			public void actionPerformed(ActionEvent e) {

				if (continueToGraphFlag == true) {
					int suppCount[] = new int [10];
					int confCount[] = new int [10];

					try {
						out.writeObject(5);
						suppCount=(int[]) in.readObject();
						confCount= (int[]) in.readObject();
					} 
					catch (IOException e1) {
						System.out.println("");
						e1.printStackTrace();
					} 
					catch (ClassNotFoundException e1) {
						System.out.println("");
						e1.printStackTrace();
					}

					final String titolo = "Distribuzione Supporto e Confidenza";
					final Grafici CdP = new Grafici(titolo,suppCount,confCount);
					CdP.pack();
					RefineryUtilities.centerFrameOnScreen(CdP);
					CdP.setVisible(true);
					continueToPdfFlag = true;
				}
				else {
					JOptionPane.showMessageDialog(frame, "Impossibile Creare grafico. Creare ResultSet dal DB","ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});	
		panel6.add(grafico);
	}

	/**
	 * Si occupa di porre gli eventi in coda mediate un apposito dispatcher di eventi asincrono; il comportamento del dispatcher dipende
	 * dalla piattaforma su cui l'applicazione viene eseguita; in particolare, quando chiamato, il metodo run() &egrave; posto nella coda
	 * degli eventi, in attesa di essere eseguito.
	 * Il metodo pu catturare un'eccezione generica di tipo Exception, e la gestisce emettendo un apposito messaggio d'errore. Tale 
	 * eccezione pu&ograve; verificarsi al run-time a causa dell'impossibilit&agrave; di creare e visualizzare l'interfaccia grafica utente. 
	 * Una finestra di dialogo mostra il messaggio suddetto.
	 * @param args argomenti inseriti dalla linea di comando della console.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Apriori window = new Apriori();
					window.frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Impossibile creare la finestra","ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}