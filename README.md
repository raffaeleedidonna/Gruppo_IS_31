# 🚀 Guida all'uso del software

---

Per eseguire correttamente il programma, è necessario essere connessi alla rete del progetto e avviare l'applicativo tramite Maven. Segui attentamente questi passaggi:

**🌐 A. Connessione alla VPN**
Prima di avviare il codice, devi collegarti alla VPN utilizzando WireGuard:
1. Assicurati di avere **WireGuard** installato sul tuo computer.
2. Apri WireGuard e importa il file di configurazione denominato `wg_config.conf`.
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
