# 🚀 Guida all'uso della Repository

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

**Una volta finito di lavorare**, mettiti nella cartella "**/01_Progetto_esame**" e aggiungi la cartella del gruppo:
```bash
git add Gruppo_IS_31
```
⚠️Se hai effettuato modifiche che **non riguardano Visual Paradigm**, dovete aggiungere il (o i) file che hai modificato:
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
