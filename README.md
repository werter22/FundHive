# FundHive
FundHive ist eine FinTeech-Plattform, die Startups mit interessierten Investoren verbindet. Investoren können sich über die Startups informieren und direkt investieren. Die exklusive KI der Plattform hilft sowohl Investoren als auch Unternehmern, das Investitionsrisiko und die Erfolgsaussichten besser einzuschätzen.

# Inhaltsverzeichnis
- [Einleitung](#einleitung)
    - [Explore-Board](#explore-board)
    - [Create-Board](#create-board)
    - [Evaluate-Board](#evaluate-board)
    - [Diskussion Feedback Pitch](#diskussion-feedback-pitch)
- [Anforderungen](#anforderungen)
    - [Use-Case Diagramm](#use-case-diagramm)
    - [Use-Case Beschreibung](#use-case-beschreibung)
    - [Fachliches Datenmodell](#fachliches-datenmodell)
    - [Erläuterungen zum Datenmodell](#erläuterungen-zum-datenmodell)
    - [Zustandsdiagramm](#zustandsdiagramm)
    - [UI-Mockup](#ui-mockup)
- [Implementation](#implementation)
    - [Frontend](#frontend)
    - [KI-Funktionen](#ki-funktionen)
- [Fazit](#fazit)
    - [Stand der Implementation](#stand-der-implementation)
    
# Einleitung

## Explore-Board
### TRENDS & TECHNOLOGIE
Durch die zunehmende Digitalisierung und den erleichterten Zugang zu Know-how hat sich in der Schweiz in den letzten 15 Jahren ein wachsender Markt für Crowdfunding entwickelt. Wie in der folgenden Abbildung ersichtlich, gab es in den letzten zehn Jahren einen Boom an Crowdfunding-Plattformen. Dieser Boom hat sich jedoch mittlerweile abgeschwächt und die Gesamtanzahl der Plattformen ist seit 2020 weitgehend stabil geblieben.

![Diagram](doc/abb_1.png)

Besonders relevant für FundHive ist die Entwicklung des Volumens im Bereich des Crowdinvestings. Diese Branche ist noch relativ jung und hat sich seit 2017 bei einem durchschnittlichen Volumen von rund 150 Millionen Franken stabilisiert.

![Diagram](doc/abb_2.png)

FundHive sieht in der fortschreitenden Entwicklung von prädiktiven Modellen die Chance, sich in diesen Markt zu integrieren. Die stetig verbesserten Datenmodelle und das wachsende Volumen an Datensätzen ermöglichen es, eine Vielzahl von Branchen zu innovieren und diese werden Kapital benötigen.

### POTENTIELLE PARTNER & WETTBEWERB
Grösste Wettbewerber im der Crowdfunding Branche: Conda, OOMNIUM 

Als mögliche Partner könnten Crowdhouse (gegründet 2015) und Foxstone(2017) unsere Plattform unterstützen da sie den Grossteil des Volumens in Crowdfunding einnehmen jedoch beträgt die Mindestinvestitionssummen in der Regel mehrere zehntausend Franken. Daher besteht die Möglichkeit das FundHive das finanzieren attraktiver macht für weniger Kapitaleinsatz.

### FAKTEN
Viele der Abbildungen und Informationen zum Markt und zur Branche wurden aus der jährlichen Crowdfunding Studie des Instituts für Finanzdienstleistungen Zug IFZ der Hochschule Luzern. Das Dokument ist im Ordner /doc beireitgestellt.

### POTENZIALFELDER
Demokratisierung der Startup-Investitionen:
Der aktuelle Crowdinvesting-Markt in der Schweiz ist geprägt von hohen Mindestinvestitionen, die oft mehrere zehntausend Franken betragen. Dies stellt eine Hürde für Kleinanleger dar, die sich an vielversprechenden Startups beteiligen möchten. FundHive könnte hier eine Lücke schließen, indem es eine zugänglichere Investmentlösung mit geringeren Einstiegshürden anbietet.

Transparenz und Glaubwürdigkeit im Startup-Finanzierungsmarkt:
Ein wiederkehrendes Thema im Bereich Startup-Finanzierung ist das fehlende Vertrauen zwischen Investoren und Startups. Viele Investoren haben Schwierigkeiten, die Qualität und Seriosität eines Startups objektiv einzuschätzen. Durch KI-gestützte Due-Diligence-Prozesse und ein Bewertungssystem für Startups und Investoren könnte FundHive das Vertrauen erhöhen und so den Markt effizienter gestalten.

Wachsendes Volumen an Startup-Daten für prädiktive Modelle:
Die zunehmende Digitalisierung ermöglicht es, große Mengen an Daten über Startups, Finanzierungsrunden und Markttrends zu sammeln. Die Nutzung von KI- und Datenanalysen zur Vorhersage von Investmentchancen wird in der Finanzbranche immer relevanter. FundHive könnte sich als führende Plattform für datengetriebenes Investieren positionieren und Investoren eine analytisch fundierte Entscheidungsbasis bieten.

Integration neuer Finanzierungsmodelle:
Neben klassischen Crowdinvesting-Methoden entstehen neue Modelle wie Revenue-Based Financing oder tokenisierte Investments (z. B. über Blockchain). FundHive könnte langfristig solche Modelle integrieren, um eine flexible und innovative Finanzierungsplattform für Startups und Investoren zu bieten.

### USER
FundHive richtet sich an Startups auf der Suche nach Finanzierung und Investoren, die nach innovativen Investmentmöglichkeiten suchen. Startups benötigen Zugang zu Kapital und vertrauenswürdigen Geldgebern, während Investoren Wert auf transparente Bewertungen und effiziente Entscheidungsprozesse legen.

### BEDÜRFNISSE
Startups brauchen eine einfache Möglichkeit, Investoren zu finden, eine stärkere Glaubwürdigkeit und eine schnellere Kapitalbeschaffung.

Investoren verlangen verlässliche Bewertungen, geringere Einstiegshürden und effizientere Due-Diligence-Prozesse.

### ERKENNTNISSE
 - Investoren benötigen transparente Bewertungsmechanismen, um fundierte Entscheidungen zu treffen.
 - FundHive soll wie LinkedIn für Venture Capital funktionieren.
 - Startups haben Schwierigkeiten, geeignete Investoren zu finden und Vertrauen aufzubauen.
 - Startups müssen aktiv Investoren überzeugen.

### TOUCHPOINTS
 - Plattform & Mobile-App
 - Netzwerk-Events & Online-Pitches
 - LinkedIn & Finanzportale
 - Partnerschaften mit Inkubatoren
 - E-Mail & Push-Benachrichtigungen

### WIE KÖNNEN WIR?
Wie können wir Investoren den Zugang zu glaubwürdigen Startups erleichtern und Startups helfen, vertrauenswürdige Investoren effizienter zu finden?

## Create-Board
### IDEEN-BESCHREIBUNG
FundHive ist eine Plattform, die Investoren mit verlässlichen Daten unterstützt und Startups eine glaubwürdige Präsentation ermöglicht. Durch KI-gestützte Analysen vereinfachen wir Investitionsentscheidungen und reduzieren den Aufwand für komplexe Bewertungen.

Anwendungsfall: Ein Startup stellt sich auf FundHive mit einer KI-gestützten Unternehmensbewertung vor. Investoren sehen nicht nur Finanzkennzahlen, sondern auch automatisierte Risikoanalysen und Zusammenhänge, die durch Algorithmen erkannt wurden. Dadurch können sie fundierte Investmententscheidungen treffen, ohne selbst tief in Finanzmodelle einsteigen zu müssen.

### ADRESSIERTE NUTZER
Startups (Kapitalnehmende):
 - Frühphasen-Startups (Seed bis Series A), die Kapital für Wachstum benötigen.
 - Junge Unternehmen mit innovativen Geschäftsmodellen, die sich abseits klassischer VC-Finanzierungen bewegen.

Investoren (Kapitalgebende):
 - Angel-Investoren & Business Angels → Erfahrene Einzelpersonen, die in junge Startups investieren und oft auch Mentoring bieten.
 - VC-Fonds → Professionelle Investoren, die in Startups mit erstem Markterfolg investieren, um schnelles Wachstum zu fördern.

### ADRESSIERTE BEDÜRFNISSE
Startups:
 - Zugang zu passenden Investoren, die über das nötige Kapital und Branchenwissen verfügen.
 - Glaubwürdige Präsentation der eigenen Finanz- und Geschäftsdaten, um das Vertrauen von Investoren zu gewinnen.
 - Vereinfachung des Investment-Prozesses, um weniger Zeit für Investorensuche und Due-Diligence-Verfahren aufwenden zu müssen.

Investoren:
 - Zugriff auf strukturierte, verständliche und KI-gestützte Bewertungen, um Startups effizient einschätzen zu können.
 - Reduzierung des eigenen Rechercheaufwands durch vorbereitete Finanzkennzahlen und Risikoanalysen.
 - Erhöhte Sicherheit durch datenbasierte Startup-Bewertungen, um fundierte Investmententscheidungen zu treffen.

### PROBLEME
Startups:
 - Schwierigkeiten, relevante Investoren zu finden, die zu ihrem Geschäftsmodell passen.
 - Fehlende Mittel oder Kenntnisse, um Finanz- und Unternehmensdaten verständlich aufzubereiten.
 - Der Finanzierungsprozess ist aufwändig, langwierig und erfordert viel manuelle Arbeit.

Investoren:
 - Fehlende Transparenz über die Qualität und das Potenzial eines Startups.
 - Hoher Zeitaufwand für die individuelle Due-Diligence und Bewertung von Startups.
 - Fehlende strukturierte Plattform, um Investments effizient zu vergleichen und Entscheidungen schneller zu treffen.

### IDEENPOTENZIAL
Mehrwert: Mückenstich vs. Hai-Attacke

🔵🔵🔵⚪️⚪️⚪️⚪️⚪️⚪️⚪️

Übertragbarkeit: Robinson Crusoe vs. die Welt

🔵🔵🔵🔵⚪️⚪️⚪️⚪️⚪️⚪️

Machbarkeit: Hammer vs. Raumschiff

🔵🔵🔵🔵🔵🔵🔵🔵⚪️⚪️

### DAS WOW
„Die erste Plattform, die Startup-Investments mit KI-gestützten Bewertungen revolutioniert – Investoren erhalten datenbasierte Einblicke, die sonst nur Großbanken vorbehalten sind.“

### HIGH-LEVEL-KONZEPT
„Das Bloomberg-Terminal für Startup-Investments – einfach, transparent und datengetrieben.“
„Startup-Investing neu gedacht – datengetrieben, intuitiv und transparent.“

### WERTVERSPRECHEN
FundHive verbindet Startups und Investoren durch datenbasierte, transparente Analysen. Unsere KI-gestützte Plattform reduziert den Due-Diligence-Aufwand und schafft Vertrauen durch objektive Bewertungen – für effizientere, schnellere und fundierte Investmententscheidungen.

## Evaluate-Board
### KANÄLE
> Beschreibe die Vertriebs- und Marketingkanäle, über welche die NutzerInnen erreicht werden sollen. Beispiel: TikTok, E-Mail, Flyer etc.

### UNFAIRER VORTEIL
> Notiere Faktoren der Lösung, die nur schwer oder gar nicht kopierbar sind. Diese Faktoren machen es schwierig, ein Konkurrenzprodukt deiner Lösung zu lancieren. 

### KPI
Anzahl Startups, Anzahl Investoren

> Trage hier Messgrössen ein, mit denen sich der Erfolg deiner Lösung messen lässt. Beispiele: Anzahl Verkäufe, Anzahl Kunden, Anzahl Transaktionen, Umsatz...

### EINNAHMEQUELLEN
> Beschreibe, wie mit deiner Lösung Geld verdient werden soll. Wo und durch wen werden Einnahmen generiert? Hinweis: die Einnahmen müssen nicht unbedingt von den NutzerInnen stammen. Es kann auch eine Trägerschaft wie z.B. ein Verein mit Mitgliederbeiträgen, Spenden oder ähnlichem gewählt werden.

## Diskussion Feedback Pitch
> Diskussion des Feedbacks aus dem Pitch (bezogen auf Projektinhalt)

# Anforderungen
## Use-Case Diagramm
> Hier das Diagramm einbinden

## Use-Case Beschreibung
![Datenmodell](doc/UC_diagram.drawio.svg)
> Hier die Use-Case Beschreibung einfügen so wie du das in RE gelernt hast. 

## Fachliches Datenmodell 
![Datenmodell](doc/ER_diagram.drawio.svg)

## Erläuterungen zum Datenmodell 
> Beschreibe die Entitäten, deren Attribute sowie die Beziehungen zwischen den Entitäten.

## Zustandsdiagramm
> Hier das Zustandsdiagramm einbinden für diejenige Entität(en), welche mehrere Zustände durchläuft mit Events, Effects und Guards.

## UI-Mockup 
> Mockup oder Skizze des UIs

# Implementation
## Frontend
> Beschreibung des Frontends mit Screenshots der fertigen Applikation. Alle Teile des GUIs, die bewertet werden sollen, müssen abgebildet sein.

## KI-Funktionen
> Aufgaben und Funktionen des eingebundenen KI-Modells.

# Fazit

## Stand der Implementation
> Stand der Implementation, nächste Schritte (mit Referenz auf den Backlog).
