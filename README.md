# info-haplaner

Info-Haplaner – Aufgabenplaner mit MVC-Architektur und SQLite-Speicher.

## Build & Ausführen

**Voraussetzung:** [Apache Ant](https://ant.apache.org/) installiert.

```bash
ant clean compile   # Projekt bauen
ant run            # Starten
```

Alternativ direkt per Java (nach `ant jar`):

```bash
ant jar
java -jar dist/MVC_Lsg.jar
```

Die SQLite-Datenbank (`haplaner.db`) wird automatisch beim ersten Start im Projektverzeichnis angelegt.
