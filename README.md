# 🚀 Guida all'uso della Repository per il progetto

Ecco i comandi da usare in ordine ogni volta che lavorate al progetto.

---

## 🛠 Come lavorare (Passo dopo passo)

Seguite questi 4 step ogni singola volta che aprite il progetto:

### 1. Prendi le ultime modifiche

**Prima di iniziare a operare**, mettiti nella cartella **/01_Progetto_esame** e aggiorna il tuo PC con quello che hanno fatto gli altri:
```bash
git pull
```

### 2. Aggiungi la cartella

**Una volta finito di lavorare**, mettiti nella cartella **/01_Progetto_esame** e aggiungi la cartella del gruppo:
```bash
git add Gruppo_IS_31
```
⚠️Se hai effettuato modifiche che **non riguardano Visual Paradigm**, devi aggiungere il (o i) file che hai modificato:
```bash
git add file1 file2 filen
```

### 3. Fai il commit

**Una volta aggiunta la cartella**, dici cosa hai modificato o aggiunto:
```bash
git commit -m "[update]"
```

### 4. Carica

**Una volta fatto il commit**, esegui il seguente comando:
```bash
git push
```

### 5. Guida all'uso del software

Per eseguire correttamente il programma, è necessario essere connessi alla rete del progetto e avviare l'applicativo tramite Maven. Segui attentamente questi passaggi:

**🌐 A. Connessione alla VPN**
Prima di avviare il codice, devi collegarti alla VPN utilizzando WireGuard:
1. Assicurati di avere **WireGuard** installato sul tuo computer.
2. Apri WireGuard e importa il file di configurazione denominato `config_prof.wg`.
3. Clicca su **Attiva** (o "Connect") per stabilire la connessione alla VPN.

**📂 B. Posizionati nella cartella corretta**
Una volta connesso alla VPN, apri il terminale e spostati all'interno della cartella contenente il progetto Java:
```bash
cd 01_Progetto_esame/JavaProject
```

**🚀 C. Esegui il programma**
Ora che sei nella cartella giusta e connesso alla VPN, puoi compilare ed eseguire il programma con un solo comando Maven:
```bash
mvn -q compile exec:java
```