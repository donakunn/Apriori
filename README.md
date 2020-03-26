GUIDA UTENTE

INDICE
1.	Installazione e avvio del Server
2.	Avvio del Client
3.	Servizi offerti dal Server
4.	Servizi offerti dal client
5.	Estensioni
5.1 Lato Server
5.2 Lato Client
      6. Casi di test

Prerequisiti per il corretto funzionamento del programma.
Affinché il programma possa essere eseguito correttamente è necessario installare l’ultima versione di Java Runtime Environment (32 o 64 bit) disponibile al seguente indirizzo:
https://www.java.com/it/download/
Inoltre occorre installare MySQL Server disponibile all’indirizzo:
https://dev.mysql.com/downloads/mysql/

Guida
L’installazione del programma prevede dapprima il popolamento di un database MySQL; ciò può essere fatto utilizzando lo script SQL “ScriptSQL.sql” fornito nella cartella principale APRIORI.

1.	Installazione e avvio del Server
Dopo aver avviato lo script SQL e popolato il database, bisogna avviare il server eseguendo il file “ServerMonitor.bat”. Apparirà la seguente schermata:
	 
In alternativa si può eseguire direttamente il file “AprioriServer.jar” mediante linea di  comando, accedendo al prompt e immettendo il comando appposito, come illustrato nella figura: così facendo il server sarà attivo in background; per vedere se è stato correttamente avviato, andare in gestione attività (ctrl + alt + canc) e visualizzare il processo “Java(TM) Platform SE binary” tra i programmi in esecuzione, così come mostrato di seguito:
 
N.B. Affinché il server funzioni correttamente è necessaria una cartella di nome “ServerLog” nella stessa directory del suo eseguibile. Se non è presente è necessario crearla.
2.	Avvio Client
Per avviare il client non è necessaria alcuna installazione; si può eseguire direttamente il file “AprioriClient.jar”, e in caso di successo comparirà la seguente schermata:
 
N.B. Bisogna eseguire sempre prima il Server e poi il Client per non visualizzare errori di apertura, come nell’immagine qui sotto riportata.
Es:  

3.	Servizi offerti dal server

Una volta avviato, il server è in esecuzione in background sulla macchina locale, in attesa di un’eventuale richiesta di connessione da parte di un client.
All’occorrere di suddetta richiesta, il server, salvo situazioni eccezionali ( trattati successivamente nei casi di test, paragrafo 6 ), accetterà la richiesta, consentendo al client di connettersi e inviare il nome della tabella del database e i parametri di minimo supporto e minima confidenza sulla base dei quali estrarre i pattern frequenti e le regole di associazione confidenti.
Una volta estratte le informazioni suddette, il server le invierà al client attraverso la connessione fra essi stabilita.

Un ulteriore servizio offerto dal server è il salvataggio, su un file pdf, dei pattern frequenti e regole d’associazione confidenti estratte, e relativo grafico a curva di precisione. 

Infine, il server, subito dopo l’estrazione dei pattern e delle regole d’associazione dal database, crea automaticamente un file con estensione .dat in cui salva tali informazioni; il file è poi collocato nella directory del server stesso. Su richiesta del client, il server legge il contenuto del file suddetto e lo restituisce al client stesso attraverso la connessione.


4.	Servizi offerti dal client

Una volta avviata l’applicazione client, se il server è stato avviato correttamente, e la connessione con quest’ultimo correttamente stabilita, verrà mostrata la seguente interfaccia:

 

A questo punto è possibile scegliere se leggere i dati da un database o da file. Nel primo caso sarà obbligatorio compilare i campi testuali  “Data”, “min sup” e “min conf” con rispettivamente il nome del database a cui accedere, il supporto minimo e confidenza minima da inviare al server; nel paragrafo 6 sono riportati casi di test in cui occorrono eccezioni in caso di omissione di almeno uno di questi dati.
Nel caso in cui si scelga la seconda opzione, ossia la lettura da file, verrà mostrato il risultato dell’ultima estrazione di pattern e regole associate, salvato dal server nel file “Rules.dat” e presente nella directory principale “APRIORI”.
In entrambi i casi dopo aver effettuato una scelta occorre cliccare sul tasto “APRIORI” per avviare l’esecuzione del comando selezionato.

È possibile inoltre, creare un grafico a “Curva di precisione” a partire dal set di pattern e regole ottenuto e mostrarlo in una nuova finestra, cliccando sul tasto “Curva di precisione” come mostrato nella seguente immagine. 


Es:
 

Dopo aver creato il grafico è possibile, cliccando su “Salva pdf”,  salvare il result set ed il relativo grafico su un file .pdf in una directory definita dall’utente, selezionabile nella seguente schermata:

 


5.	Estensioni
5.1 LATO SERVER
Il codice relativo al lato server è stato esteso con le seguenti funzionalità:
1.	Registro eventi lato Server: è stata introdotta la creazione e gestione di un file di log per la registrazione degli eventi che sussistono nell’interazione client - server. Ogni occorrenza di ciascun evento è registrato su un file di log apposito in formato .txt, salvato nella cartella ServerLog; per ogni evento, inoltre, sono riportati data espressa in aa/mm/gg, e tempo, espresso in hh:mm:ss e una breve descrizione dell’evento verificatosi. Esempi di eventi registrati sul file di log possono essere i seguenti:
a.	Server started; ( ossia, l’avvio del server è avvenuto con successo )
b.	In attesa di connessione... ( il server attende una richiesta di connessione da parte di un client )
c.	Nuovo Client connesso ( il client è connesso alla socket ad esso dedicata )
d.	Connessione accettata all'indirizzo /127.0.0.1 su numero di porta 8080 (la richiesta di connessione da parte di un client è stata accettata all’indirizzo IP e numero di porta relativi al processo server )
e.	Errore durante connessione al Db ( tentativo di connessione al database fallito )
f.	Pattern e regole salvate CON SUCCESSO ( il processo di estrazione di pattern frequenti e relative regole d’associazione confidenti, rispetto al minimo supporto e confidenza specificati dall’utente, è avvenuto con successo )
g.	Errore durante il caricamento di tabella con nome “playtennisl”( il nome della tabella non corrisponde ad alcun nome di tabella esistente all’interno del database )
h.	Client disconnesso ( il client non necessita più dei servizi del server, dunque si disconnette )
2.	Salva pdf: è stata introdotta la funzione salva pdf, la quale permette di salvare un file pdf contenente la lista dei pattern frequenti e relative regole d’associazione confidenti, seguite da un grafico a curva di precisione illustrante la loro distribuzione nei diversi intervalli di supporto ( per i pattern frequenti ) e confidenza ( per le regole d’associazione confidenti ) in una specifica directory selezionata dall’utente.


5.2 LATO CLIENT
Il codice relativo al lato client è stato esteso con le seguenti funzionalità:
1.	Grafico: è stato introdotto un nuovo bottone, che permette di mostrare un grafico a curva di precisione relativo alla distribuzione dei pattern frequenti rispetto al supporto e delle regole d’associazione confidenti rispetto alla confidenza.
Si noti che le due curve di distribuzione coinvolgono:
a.	 l’una, tutti i pattern frequenti;
b.	 l’altra, tutte le regole d’associazione confidenti, indipendentemente dai pattern da cui queste ultime sono generate.
La distinzione tra le due curve è data da opportune colorazioni e targhette che ne specificano il ruolo.


6. Casi di Test
1.Nel caso in cui, all’avvio del client, la connessione al server fallisca verrà mostrata la seguente finestra di errore:
 

2.Selezionata l’opzione “Learning Rules From Database”, inserito un nome di tabella esistente nel campo Data, un valore compreso tra 0 ed 1 nei campi “min sup” e “min conf”, cliccando successivamente sul bottone “APRIORI” il client invia i dati al server, il quale li elabora e restituisce il risultato del processo di estrazione di pattern e regole d’associazione; quest’ultimo viene visualizzato nel campo “Pattern and Rules”. A questo punto il client invia un comando al server affinché quest’ultimo salvi il risultato estratto in un file con nome predefinito “Rules.dat”. Il server tenta il salvataggio del file ed invia al client uno dei due seguenti messaggi: “Pattern e regole salvate CON SUCCESSO” oppure “Errore durante il salvataggio”, rispettivamente se il salvataggio va a buon fine o no. Il messaggio è mostrato nel campo dei messaggi di sistema.
 
3.Cliccando su “APRIORI” con selezionato il campo “Learning Rules From Database” e campi “Data”, “min sup” e “min conf” vuoti verrà mostrata una finestra di avviso in cui viene chiesto all’utente di inserire il nome di una tabella.

4.Cliccando su “APRIORI” con selezionata l’opzione “Learning Rules From Database” , avvalorando “Data” con una qualsiasi stringa di caratteri , “min sup” e “min conf” vuoti, verrà mostrata una finestra di avviso dove viene chiesto all’utente di inserire nel campo “min sup” un valore compreso tra 0 e 1.

5.Cliccando su “APRIORI” con selezionato il campo “Learning Rules From Database” ,“Data” avvalorato con una qualsiasi stringa di caratteri , “min sup” con valore compreso tra 0 e 1 e “min conf” con valore strettamente maggiore di 1, verrà mostrata una finestra di avviso dove viene chiesto all’utente di inserire nel campo “min conf” un valore compreso tra 0 e 1.

6.Nel caso in cui, con i valori passati dal client al server il risultato dell’estrazione di pattern e regole sia vuoto, verrà mostrato nel campo dei messaggi di sistema il messaggio “La tabella xxxx non esiste o non vi è nessuna regola da mostrare” (dove xxxx è il nome della tabella inserito dall’utente), e l’area testuale dedicata ai pattern e regole risulterà vuota.

7.Nel caso in cui, venga selezionata l’opzione “Learning Rules from File” e si clicchi su “APRIORI” il client invierà al server il comando per l’acquisizione dei pattern frequenti e relative regole di associazione confidenti da file. Il server passerà al client il risultato e quest’ultimo lo mostrerà nel campo “Pattern and Rules”.  Nel campo dei messaggi di sistema verrà visualizzato il messaggio: “Pattern e regole caricate da file CON SUCCESSO”.

 8.Nel caso in cui, si tenti di leggere da file ma quest’ultimo è vuoto o inesistente nel campo dei messaggi di sistema verrà visualizzato il messaggio: ”Errore durante il caricamento del file contenente pattern e regole; file vuoto o inesistente”.
 
9.Nel caso in cui il server smetta di funzionare o lo scambio di messaggi tra client e server fallisca, verrà visualizzata una finestra di errore, come di seguito mostrato:
 
10.Nel caso in cui, si clicchi su “curva di precisione” prima di aver estratto un set di Pattern frequenti e regole di associazione confidenti dal database, sarà mostrato il seguente messaggio di errore:
 
11. Nel caso in cui, si clicchi su “Salva Pdf” prima di aver estratto un set di Pattern frequenti e regole di associazione confidenti dal database, e prima di aver creato relativo grafico a curva di precisione, verrà mostrato il seguente messaggio di errore:
 
12.Nel caso in cui, dopo aver creato un set di Pattern frequenti e regole di associazione dal database si clicchi su “curva di precisione”, sarà aperta una nuova finestra contenente il grafico curva di precisione relativo al set di pattern e regole estratto. 
  
13. Nel caso in cui, dopo aver estratto un set di pattern frequenti e regole di associazione confidenti da database ed aver creato relativo grafico, si clicchi su “Salva Pdf”, sarà aperta una nuova finestra dove è possibile scegliere una directory dove salvare il file .pdf contenente set e grafico suddetti.
 
14.Nel caso in cui si cerchi di chiudere il programma comparirà una finestra di conferma.
 

Cliccando “Si” il client chiude la connessione con il server, e viene arrestato. In caso di problemi durante la chiusura verrà mostrata una finestra di errore come la seguente:
 
