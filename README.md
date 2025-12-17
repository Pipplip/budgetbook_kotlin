## domain

Keine Abhängigkeit nach außen. <br>
Enthält Entitäten und Value Objects

## application

Koordiniert Abläufe, enthält keine Fachlogik.

## infrastructure

Hier lebt alles Technische

## ui

Benutzerschnittstellen

## Abhängigkeitsregeln

ui → application → domain
↑
infrastructure


Domain kennt keine Infrastruktur. 
Infrastruktur kennt Domain

