# Aufbau des Projekts

Clean Architecture / Hexagonal Architecture

Abhängigkeiten zeigen immer nach innen, d.h. 

UI --> Service --> Domain <-- Infrastructure

Kurz:
- domain.model kennt nichts
- domain.repository kennt nur domain
- domain.exception kennt nur domain
- service kennt nur domain (+ repository-Interfaces)
- infratructure kennt nur domain (+ repository-Interfaces)
- ui kennt nur Service
- Main.kt kennt alles

## Service
"Use cases" oder Application Layer genannt
- Orchestriert die Fachlogik
- Implementiert konkrete Anwendungsfälle
- Verbindet Domain mit Aussenwelt
- darf Domain nutzen
- darf Repository Interfaces aufrufen
- darf NICHT UI und Datenbankdetails
- Ein Service beschreibt ein Use-Case

## domain

Keine Abhängigkeit nach außen. <br>
Enthält Entitäten und Value Objects und fachliche Kernlogik.

#### domain.model / Entities
Reine Kotlin Klassen oder data classes (z.B. data class User)

Regeln für Model:
1) Keine Frameworks
2) Keine Datenbank
3) Keine CLI / UI
4) Nur Fachlogik
5) Fachliche Regeln gehören hierher, nicht in Services

#### domain.exception
Exceptions werden später von Service oder UI behandelt

#### domain.repository
Interfaces für den Zugriff auf Daten.

Sie definieren was gebraucht wird, nicht wie.

Wichtig:
- Nur Interfaces
- Keine Implementierung
- keine Datenbankkenntnis

## infrastructure

Hier lebt alles "Technische"

Bsp: Datenbankzugriffe, Dateien, HTTP etc.
Frameworks dürfen verwendet werden.

Implementiert repository Interfaces.

Domain kennt diese Schicht nicht.

## ui

Benutzerschnittstellen

Rufen Services auf und fangen Exceptions ab

Außerdem wird die Fachlogik in Benutzerinteraktion umgewandelt.

## DTOs
Sind Transport-Daten zwischen Schichten und Systeme

Sie gehören nicht in domain, eher an die Grenzen der Anwendung.

Konkret in:
- ui.cli.dto
-- für z.B. Request/Response DTOs, Benutzereingaben etc.
- persistence.dto

Service sollte keine DTOs kennen. Services sollten mit Domain-Objekten oder primitiven Werten arbeiten.