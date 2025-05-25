![Workflow](https://github.com/werter22/FundHive/actions/workflows/ci.yml/badge.svg)
![Coverage](https://github.com/werter22/FundHive/blob/main/.github/badges/jacoco.svg)
![Branches](https://github.com/werter22/FundHive/blob/main/.github/badges/branches.svg)

# FundHive
FundHive ist eine FinTech-Plattform, die Startups mit interessierten Investoren verbindet. Investoren können sich über die Startups informieren und direkt investieren. Die exklusive KI der Plattform hilft sowohl Investoren als auch Unternehmern, das Investitionsrisiko und die Erfolgsaussichten besser einzuschätzen.

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

![Diagram Funding Plattformen](doc/refrences/abb_1.png)

Besonders relevant für FundHive ist die Entwicklung des Volumens im Bereich des Crowdinvestings. Diese Branche ist noch relativ jung und hat sich seit 2017 bei einem durchschnittlichen Volumen von rund 150 Millionen Franken stabilisiert.

![Diagram finazierte Kampagnen](doc/refrences/abb_2.png)

FundHive sieht in der fortschreitenden Entwicklung von prädiktiven Modellen die Chance, sich in diesen Markt zu integrieren. Die stetig verbesserten Datenmodelle und das wachsende Volumen an Datensätzen ermöglichen es, eine Vielzahl von Branchen zu innovieren und diese werden Kapital benötigen.

### POTENTIELLE PARTNER & WETTBEWERB
Grösste Wettbewerber im der Crowdfunding Branche: Conda, OOMNIUM 

Als mögliche Partner kämen Crowdhouse (gegründet 2015) und Foxstone (gegründet 2017) in Frage, da sie den Grossteil des Crowdfunding-Volumens abdecken. Die Mindestinvestitionssummen betragen dort jedoch in der Regel mehrere zehntausend Franken. Daher besteht die Möglichkeit, dass FundHive das Finanzieren durch geringeren Kapitaleinsatz attraktiver gestaltet.

### FAKTEN
Viele der Abbildungen und Informationen zum Markt und zur Branche wurden aus der jährlichen Crowdfunding Studie des Instituts für Finanzdienstleistungen Zug IFZ der Hochschule Luzern. Das Dokument ist im Ordner /doc beireitgestellt.

### POTENZIALFELDER
Demokratisierung der Startup-Investitionen:
Der aktuelle Crowdinvesting-Markt in der Schweiz ist geprägt von hohen Mindestinvestitionen, die oft mehrere zehntausend Franken betragen. Dies stellt eine Hürde für Kleinanleger dar, die sich an vielversprechenden Startups beteiligen möchten. FundHive könnte hier eine Lücke schliessen, indem es eine zugänglichere Investmentlösung mit geringeren Einstiegshürden anbietet.

Transparenz und Glaubwürdigkeit im Startup-Finanzierungsmarkt:
Ein wiederkehrendes Thema im Bereich Startup-Finanzierung ist das fehlende Vertrauen zwischen Investoren und Startups. Viele Investoren haben Schwierigkeiten, die Qualität und Seriosität eines Startups objektiv einzuschätzen. Durch KI-gestützte Due-Diligence-Prozesse und ein Bewertungssystem für Startups und Investoren könnte FundHive das Vertrauen erhöhen und so den Markt effizienter gestalten.

Wachsendes Volumen an Startup-Daten für prädiktive Modelle:
Die zunehmende Digitalisierung ermöglicht es, grosse Mengen an Daten über Startups, Finanzierungsrunden und Markttrends zu sammeln. Die Nutzung von KI- und Datenanalysen zur Vorhersage von Investmentchancen wird in der Finanzbranche immer relevanter. FundHive könnte sich als führende Plattform für datengetriebenes Investieren positionieren und Investoren eine analytisch fundierte Entscheidungsbasis bieten.

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
„Die erste Plattform, die Startup-Investments mit KI-gestützten Bewertungen revolutioniert – Investoren erhalten datenbasierte Einblicke, die sonst nur Grossbanken vorbehalten sind.“

### HIGH-LEVEL-KONZEPT
„Das Bloomberg-Terminal für Startup-Investments – einfach, transparent und datengetrieben.“
„Startup-Investing neu gedacht – datengetrieben, intuitiv und transparent.“

### WERTVERSPRECHEN
FundHive verbindet Startups und Investoren durch datenbasierte, transparente Analysen. Unsere KI-gestützte Plattform reduziert den Due-Diligence-Aufwand und schafft Vertrauen durch objektive Bewertungen – für effizientere, schnellere und fundierte Investmententscheidungen.

## Evaluate-Board
### KANÄLE
- **Digital:** LinkedIn, Twitter/X, Google Ads, Finanzportale  
- **Offline:** Startup- & Investoren-Events, Kooperationen mit Inkubatoren  
- **Direkt:** E-Mail-Marketing, Referral-Programme  

### UNFAIRER VORTEIL
- **KI-gestützte Due-Diligence** für automatisierte Startup-Bewertungen  
- **KI-gestützte Unterstützung** für Selbstdarstellung der Starups
- **Exklusive Datenbank** mit validierten Startups & Investoren  
- **Niedrige Einstiegshürden** im Vergleich zu klassischen VC-Investments  

### KPI
- Anzahl **registrierter Startups & Investoren**  
- Anzahl **erfolgreicher Investments**  
- **Zeit bis zur Finanzierung** reduzieren  

### EINNAHMEQUELLEN
- **Erfolgsgebühr** auf abgeschlossene Investments  
- **Premium-Modelle** für erweiterte Analysefunktionen  

## Diskussion Feedback Pitch
| **Thema**                         | **Antwort / Massnahme** |
|----------------------------------|--------------------------|
| **Vertrauen & Sicherheit**       | Startups durchlaufen eine **Verifizierungsphase**, z. B. Upload von Handelsregisterauszug, Website, Pitch Deck. Zusätzlich führen wir ein **KI-gestütztes Scoring-Modell** ein, das kontinuierlich neue Daten (Aktualität, Plausibilität) berücksichtigt. Scam-Versuche können durch Flagging durch andere Nutzer gemeldet werden. |
| **Datenherkunft & Qualität**     | Alle Analyse-Daten stammen direkt von den Startups, werden aber mit öffentlichen Quellen (z. B. Handelsregister, LinkedIn, Firmenverzeichnissen) abgeglichen. Eine **manuelle Stichprobenkontrolle** ist bei verdächtigen Fällen vorgesehen. |
| **KI-Funktion & Bias-Vermeidung**| Die KI dient nicht der Entscheidung, sondern der **Unterstützung**: Sie analysiert Trends, Risiken und Auffälligkeiten und zeigt diese transparent an. Investoren können dann eigenständig entscheiden. Wir planen zudem eine Möglichkeit zur **Erklärung von Scoring-Ergebnissen** („Why this score?“). |
| **Mehrwert gegenüber Konkurrenz**| Im Gegensatz zu Kickstarter & Co liegt der Fokus auf **echten Beteiligungen** und **datengesteuertem Matching**. FundHive ist **kein Spendenportal**, sondern eine Plattform für Kapitalbeteiligungen mit Fokus auf professionelle und zukünfige Investoren. |
| **Provision & Geschäftsmodell**  | Die **5–10 % Erfolgsprovision** liegt im Branchenschnitt. Weitere Einnahmen sind durch **Premium-Zugänge**  und **analytische Zusatzfunktionen** geplant. |
| **Geldfluss & Transaktionen**    | FundHive agiert als **Vermittlungsplattform** – die eigentliche Zahlungsabwicklung erfolgt über einen **verifizierten Drittanbieter**. So wird sichergestellt, dass das Geld erst nach Vertragsschluss weitergeleitet wird. |


# Anforderungen
## Use-Case Diagramm
![Use-Case Diagramm](doc/UC_diagram.drawio.svg)

## Use-Case Beschreibung
Use-Cases sind in alphabetisher Reihenfolge aufgelistet.


**Finanzierungsrunde erfassen**

- **Actors**: Entrepreneur  
- **Eintrittsbedingungen**: Das zugehörige Startup existiert bereits und ist aktiv. Der Entreprenuer ist Besitzer des Starups. 
- **Ereignissequenz**:  
  1. Entrepreneur wählt ein Startup aus.  
  2. Öffnet das Formular „Finanzierungsrunde erfassen“.  
  3. Gibt relevante Informationen ein (z. B. Rundenname, Zielbetrag, Zeitraum).  
  4. Speichert die Finanzierungsrunde.  
- **Austrittsbedingung**: Die Finanzierungsrunde ist dem Startup zugeordnet und in der Datenbank gespeichert.  
- **Daten**: `round_name`, `goal_amount`, `start_date`, `end_date`  

**Finanzierungsrundenansicht**

- **Actors**: Admin  
- **Eintrittsbedingungen**: Der Admin ist eingeloggt.  
- **Ereignissequenz**:  
  1. Admin öffnet die Ansicht „Finanzierungsrunden“.  
  2. Das System zeigt eine Liste aller bestehenden Finanzierungsrunden mit Details an.   

**Finanzierungsrundenverwaltung**

- **Actors**: Entrepreneur  
- **Eintrittsbedingungen**: Der Entrepreneur ist Besitzer des dazugehörigen Startups und hat mindestens eine Finanzierungsrunde erstellt.  
- **Ereignissequenz**:  
  1. Entrepreneur navigiert zu seinem Startupdetailansicht.  
  2. Er erhält eine Liste seiner Runden und je nach Status der Runden hat er Optionen die nur er ausführen kann.   

**In offene Finanzierungsrunden investieren**

- **Actors**: Investor  
- **Eintrittsbedingungen**: Der Investor ist eingeloggt und es existieren offene Finanzierungsrunden.  
- **Ereignissequenz**:  
  1. Investor sucht oder filtert nach Startups.  
  2. In der Startupdetailansicht sieht er die Runden die dieses Startup verwaltet.  
  3. Der Investor wählt eine Runde die den Button "Invest" hat.
  4. Gibt Investitionsbetrag ein und bestätigt die Transaktion.  
- **Austrittsbedingung**: Die Investition ist abgeschlossen und im Portfolio sowie bei der Finanzierungsrunde verbucht.  
- **Ausnahmen**: Ungültiger Betrag - melden erfolgt, technischer Fehler bei Transaktion - Fehlermeldung.  
- **Daten**: `amount`  

**Investmentportfolio ansehen**

- **Actors**: Investor  
- **Eintrittsbedingungen**: Der Investor ist eingeloggt und hat optional mindestens eine Transaktion durchgeführt.  
- **Ereignissequenz**:  
  1. Investor navigiert zur Portfolio-Ansicht.  
  2. Das System zeigt alle getätigten Investitionen samt Beträgen, Runden und Datum.  
- **Ausnahmen**: Noch keine Transactionen - In der Tabelle: "Keine Transaktionen vorhanden".

**KI-Assistent für Beschreibung**

- **Actors**: Entrepreneur  
- **Eintrittsbedingungen**: Der Entrepreneur befindet sich auf der Detailseite seines Startups.  
- **Ereignissequenz**:  
  1. Entrepreneur öffnet den KI-Assistenten unter dem Editor für Beschreibung.  
  2. Der Assistent generiert automatisch eine Vorschlagsbeschreibung basierend auf vorhandenen Startupdaten.  
  3. Der Entrepreneur kann die generierte Beschreibung anpassen oder text teile kopieren.  
- **Austrittsbedingung**: Die neue oder überarbeitete Beschreibung wird gespeichert.  
- **Ausnahmen**: Die KI liefert keinen sinnvollen Vorschlag oder ein technischer Fehler tritt auf. 
- **Besondere Anforderungen**: Antwortzeit der KI soll rasch sein.  

**KI-Assistent für Startupsuche**

- **Actors**: Investor, Entrepreneur, Admin  
- **Eintrittsbedingungen**: Der Nutzer ist eingeloggt.  
- **Ereignissequenz**:  
  1. Der Nutzer aktiviert den KI-Assistenten zur Unterstützung der Startup-Suche.  
  2. Die KI analysiert Nutzerinteressen und vorhandene Daten.  
  3. Es werden individualisierte Startup-Vorschläge angezeigt.  
- **Austrittsbedingung**: Eine Liste relevanter Startups wird präsentiert.  
- **Ausnahmen**: Keine passenden Vorschläge gefunden - KI bringt das zum Audruck und schlägt Alternativen vor.  
- **Besondere Anforderungen**: Personalisierung und Reaktionszeit der KI.  

**Startupdetails ansehen**

- **Actors**: Investor  
- **Eintrittsbedingungen**: Der Investor findet ein Starup das ihn interessiert.  
- **Ereignissequenz**:  
  1. Investor klickt auf ein Startup aus der Ergebnisliste.  
  2. Das System zeigt eine Detailansicht mit Beschreibung, Branche, Finanzierungsstatus,AI-Rating und die vorhandenen Finanzierungsrunden.  
- **Austrittsbedingung**: Die Detailseite des gewählten Startups wird angezeigt.  
- **Ausnahmen**: Das Startup ist nicht mehr verfügbar oder konnte nicht geladen werden - Fehlermeldung.  

**Startupdetails bearbeiten**

- **Actors**: Entrepreneur  
- **Eintrittsbedingungen**: Ein Startup wurde bereits vom Entrepreneur erstellt und er ist der Besitzer.  
- **Ereignissequenz**:  
  1. Entrepreneur navigiert zur Deatailansicht seines Startups.  
  2. Änderungen an Feldern wie Beschreibung, Branche, Zielbetrag oder Bewertung werden vorgenommen.  
  3. Änderungen werden gespeichert.  
- **Austrittsbedingung**: Die aktualisierten Informationen sind in der Datenbank gespeichert und auf der Webseite sichtbar.  
- **Ausnahmen**: Ungültige Eingaben oder Speichervorgang schlägt fehl - Fehlermeldung.  
- **Daten**: `name`, `description`, `industry`, `valuation`, `funding_status`

**Startup erstellen**

- **Actors**: Entrepreneur  
- **Eintrittsbedingungen**: Der Entrepreneur ist eingeloggt.  
- **Ereignissequenz**:  
  1. Entrepreneur geht zum Formular „Startup erstellen“ im Account-Page.  
  2. Gibt grundlegende Informationen ein (z. B. Name, Branche, Beschreibung).  
  3. Speichert das Startup.  
- **Austrittsbedingung**: Das neue Startup ist in der Datenbank gespeichert und dem Entrepreneur zugeordnet.  
- **Ausnahmen**: Pflichtfelder nicht ausgefüllt, technische Fehler beim Speichern - Fehlermeldung.  
- **Daten**: `name`, `description`, `industry`, `valuation`, `funding_status`

**Startups suchen**

- **Actors**: Investor, Entrepreneur, Admin,    
- **Ereignissequenz**:  
  1. Nutzer sind auf der Startups-Page.  
  2. Gibt Filterkriterien ein (z. B. Branche, Bewertung, Finanzierungsstatus).  
  4. Das System zeigt passende Startups an.  
- **Austrittsbedingung**: Eine Liste gefundener Startups wird angezeigt.  

**Transaktionenansicht**

- **Actors**: Admin  
- **Eintrittsbedingungen**: Der Admin ist eingeloggt.  
- **Ereignissequenz**:  
  1. Admin öffnet die Transaktionenansicht.  
  2. Das System lädt alle Transaktionen inkl. zugehöriger Investoren, Beträge und Zeitpunkte.  
  3. Admin filtert die Transaktionen.  

## Fachliches Datenmodell 
![ER_Diagram](doc/ER_diagram.drawio.svg)

## Erläuterungen zum Datenmodell 

**Entitäten und Attribute**

| **Entität**         | **Attribute**                                                                                                   | **Attribut-Beschreibung**                                                                                                                                                           |
|---------------------|------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Entrepreneur**     | `name`, `email`                                                                                                 | Der Name und die E-Mail-Adresse des Entrepreneurs, die zur Identifikation und Kontaktaufnahme dienen.                                                                                 |
| **Startup**          | `name`, `description`, `AI_rating`, `industry`, `funding_status`, `valuation`, `amount_raised`, `goal_amount` | Der Name des Startups, eine Beschreibung seines Geschäftsmodells, die durch KI vergebene Bewertung, die Branche, der aktuelle Finanzierungsstatus, die Bewertung des Unternehmens, der bereits eingesammelte Betrag und das Finanzierungsziel. |
| **Investment round** | `round_name`, `start_date`, `end_date`                                                                         | Bezeichnung der Finanzierungsrunde (z. B. Seed, Series A) sowie deren Start- und Enddatum.                                                                                            |
| **Investor**         | `name`, `email`, `AI_rating`                                                                                    | Der Name und die E-Mail-Adresse des Investors sowie eine durch KI vergebene Bewertung seiner Investitionshistorie oder Vertrauenswürdigkeit.                                         |
| **Transaction**      | `amount`, `date`                                                                                                | Die Höhe des investierten Betrags und das Datum, an dem die Transaktion durchgeführt wurde.                                                                                           |

**Beziehungen**
| **Beziehung** | **Beteiligte Entitäten**              | **Kardinalität**                            | **Beschreibung**                                                                 |
|---------------|----------------------------------------|---------------------------------------------|----------------------------------------------------------------------------------|
| `creates`     | Entrepreneur → Startup                 | 1 Entrepreneur erstellt N Startups          | Ein Entrepreneur kann mehrere Startups gründen, aber jedes Startup hat nur einen Gründer. |
| `launches`    | Startup → Investment round             | 1 Startup startet N Runden                  | Ein Startup kann mehrere Finanzierungsrunden durchführen.                        |
| `has`         | Investment round → Transaction         | 1 Runde → N Transaktionen                   | Eine Finanzierungsrunde umfasst viele Transaktionen.                             |
| `does`        | Investor → Transaction                 | 1 Investor tätigt N Transaktionen           | Ein Investor kann mehrfach investieren. Jede Transaktion gehört zu einem Investor. |
## Zustandsdiagramm
**Status der Finanzierungsrunden:**

![State_Diagram](doc/State_diagram.drawio.svg)

## UI-Mockup 

![Login_Page](doc/mockUI/login.drawio.svg)
![Signup_Page](doc/mockUI/signup.drawio.svg)


**Admin Pages:**

![Admin_InvestmentRounds_View_Page](doc/mockUI/admin_IR_page.drawio.svg)
![Admin_TransactionView_Page](doc/mockUI/admin_transaction_view.drawio.svg)
![Admin_Acc_Page](doc/mockUI/admin_acc.drawio.svg)

**Entrepreneur Pages:**

![Entrepreneur_OwnStartup_Page](doc/mockUI/entrepreneur_own_startup.drawio.svg)
![Entrepreneur_Acc_Page](doc/mockUI/entrepreneur_acc.drawio.svg)

**Investor Pages:**

![Investor_Startup_Details_Page](doc/mockUI/investor_indiv_startup_page.drawio.svg)
![Investor_Portfolio_Page](doc/mockUI/investor_portfolio.drawio.svg)
![Investor_Acc_Page](doc/mockUI/investor_acc.drawio.svg)

**Für alle Benutzer:**

![Startups_Page](doc/mockUI/startup_page_AI_chat.drawio.svg)

# Implementation
## Frontend

Die "Landing-Page" lädt Nutzer ein, sich als Entrepreneur oder Investor zu registrieren.
![1 Root Page](doc/screenshots/1_root_page.PNG)

Damit auch unregistrierte Nutzer einen Vorgeschmack auf die Plattform bekommen, ist die Startup-Seite auch ohne Login einsehbar. Allerdings sind die individuellen Startup-Seiten und der AI-Assistent zur Suche nicht nutzbar.
![2 Startup Page No Login](doc/screenshots/2_startup_page_no_login.PNG)

Schlichtes "Log in" Formular mit Logo.
![3 Login](doc/screenshots/3_login.PNG)

Bei der Anmeldung kann der Nutzer angeben, ob er ein Startup finanzieren lassen oder in ein Startup investieren möchte.
Diese Auswahl weist dem Account automatisch die entsprechende Rolle zu: „Entrepreneur“ bei „get my startup funded“ und „Investor“ bei „invest in a startup“.
![4 Signup](doc/screenshots/4_signup.PNG)

Nach erfolgreicher Anmeldung oder dem Einloggen wird der Nutzer begrüsst.
Da Startups im Mittelpunkt der Anwendung stehen, führt ein direkter Link zur Startup-Seite.
![5 Landing Page](doc/screenshots/5_landing_page.PNG)

Der praktische Startup-AI-Assistent steht nun allen Benutzerrollen zur Verfügung.
Der Chat kann bei Bedarf eingeblendet und bei Nichtgebrauch wieder ausgeblendet werden.
![6 Startup Page Ai Chat](doc/screenshots/6_startup_page_AI_chat.PNG)

Der Assistent lässt sich mit natürlichen Fragen bedienen.
Im gezeigten Beispiel wird nach Startups mit Umweltbewusstsein gesucht. Die KI erkennt die Anfrage korrekt, identifiziert passende Startups und gibt gezielte Empfehlungen.
![7 Startup Page Ai Chat Used](doc/screenshots/7_startup_page_AI_chat_used.PNG)

Der Admin kann unter „Investment Rounds“ alle Runden einsehen, die jemals auf der Plattform erstellt wurden. Mithilfe der Filteroptionen lassen sich beispielsweise alle offenen Runden dieses Monats anzeigen.
Zusätzlich kann im erweiterten Filterbereich auch das Enddatum gezielt definiert werden.
Wichtig dabei: Bei gleichzeitiger Auswahl von Start- und Enddatum sollte beachtet werden, dass Start- und Enddatum standardmässig 90 Tage auseinanderliegen.
![8 Admin All Rounds](doc/screenshots/8_admin_all_rounds.PNG)

Diese Seite listet alle Investitionen tabellarisch auf. Admins können gezielt nach Transaktionen filtern – z.B. anhand eines Datumsbereichs. Dies ermöglicht eine präzise Nachverfolgung und Analyse sämtlicher Investitionsaktivitäten auf der Plattform.
![9 Admin All Transactions](doc/screenshots/9_admin_all_transactions.PNG)

Die Account-Seite für Admins und Investoren zeigt ihre persönlichen Daten an. In zukünftigen Erweiterungen soll es den Nutzern möglich sein, ihre Account-Angaben zu bearbeiten und Zugriff auf weitere benutzerspezifische Funktionen zu erhalten.
![10 Admin Acc](doc/screenshots/10_admin_acc.PNG)

Diese Seite bietet Investoren eine Übersicht über ihre bisherigen Investitionen.
![11 Investor Portfolio](doc/screenshots/11_investor_portfolio.PNG)

Auf der individuellen Startup-Seite erhält der Investor eine Übersicht über die Anzahl der Investmentrunden sowie das bisher insgesamt eingesammelte Kapital. Zusätzlich wird hier die vollständige Beschreibung des Startups angezeigt.
![12 Investor Startup View](doc/screenshots/12_investor_startup_view.PNG)

Scrollt der Investor zum unteren Ende der Seite, sieht er eine Tabelle mit allen Investmentrunden des Startups.
Zu beachten ist: Investitionen sind nur in offenen Runden möglich, in diesem Beispiel ist derzeit keine Runde offen.
![13 Investor Startup View](doc/screenshots/13_investor_startup_view.PNG)

Hier sieht man, dass dem Investor ein „Invest-Button" angezeigt wird, da diese Runde offen ist.
![14 Investor Startup View Open Round](doc/screenshots/14_investor_startup_view_open_round.PNG)

Der Investor kann hier den gewünschten Investitionsbetrag eingeben und durch Klick auf „Confirm“ die Investition abschliessen oder mit „Cancel“ abbrechen. Nach der Bestätigung wird der Betrag unter „Amount Raised“ automatisch aktualisiert, ebenso der „Total Raised“-Wert im oberen Bereich der Seite.
![15 Investor Startup View Invest](doc/screenshots/15_investor_startup_view_invest.PNG)

Als Entrepreneur hat der Benutzer zusätzlich zu seinen Profilangaben Zugriff auf ein Formular zur Erstellung eines neuen Startups.
![16 Entrepreneur Acc Create Startup](doc/screenshots/16_entrepreneur_acc_create_startup.PNG)

Zusätzlich kann er als Besitzer eines Startups auf der individuellen Startupdeatilseite die Angaben bearbeiten.
Dafür steht ihm ein einfacher Editor zur Verfügung, der auch das Hochladen von Bildern erlaubt.
![17 Entrepreneur Startup Editor](doc/screenshots/17_entrepreneur_startup_editor.PNG)

Des Weiteren kann der Gründer den AI-Assistenten nutzen, um die Beschreibung besonders ansprechend zu gestalten und Feedback oder Verbesserungsvorschläge zu erhalten.
![18 Entrepreneur Startup Ai Assist](doc/screenshots/18_entrepreneur_startup_AI_assist.PNG)

Hier ist auch ersichtlich, dass der Investor die Möglichkeit hat, eine Investitionsrunde mit dem Status „Upcoming“ manuell zu öffnen oder zu stornieren.
![19 Entrepreneur Startup](doc/screenshots/19_entrepreneur_startup.PNG)

Zudem kann er über den Button „+ Create New Funding Round“ eine neue Runde erstellen.
![20 Entrepreneur Startup New Round](doc/screenshots/20_entrepreneur_startup_new_round.PNG)

## KI-Funktionen

| **Anwendung** | **Aufgaben & Funktionen** |
|---------------|----------------------------------------|
| **Startup-Bewertung (AI Rating)** | Das System analysiert auf Basis von Name, Branche, Bewertung und Beschreibung eines Startups dessen Erfolgspotenzial. Eine Large Language Model (LLM)-basierte Bewertung liefert einen numerischen Score (0.0–5.0), der regelmässig aktualisiert und beim Erstellen eiens Startups automatisch generiert wird. **Ziel:** Orientierungshilfe für Investoren und einheitliche Qualitätsindikatoren. |
| **Pitch-Optimierung (Beschreibung verbessern)** | Die KI überarbeitet die vorhandene Startup-Beschreibung im HTML-Format sprachlich und stilistisch, ohne neue Informationen hinzuzufügen. Dabei achtet das Modell auf professionelle, überzeugende Formulierungen und gute Struktur. **Ergebnis:** Eine ansprechende und investorentaugliche Präsentation des Startups – semantisch korrektes HTML inklusive. |
| **Investorensuche / Empfehlung (Startup-Matching)** | Ein dialogbasierter KI-Assistent versteht die Anfrage eines Investors (z. B. branchenspezifische Interessen oder Bewertungskriterien) und schlägt passende Startups aus der Datenbank vor. Die Entscheidung basiert auf Startupdaten, semantischem Verständnis und optional auch Tool-gestütztem Filtern (z. B. Branchen, Fundingstatus, Bewertungsspanne). **Ziel:** Relevante, präzise Empfehlungen in natürlicher Sprache. |


# Fazit

## Stand der Implementation
**Stand der Implementation**

Die Kernfunktionen der Plattform sind umgesetzt und funktionsfähig. Benutzer können sich registrieren, ihre Rolle wählen (Investor oder Entrepreneur) und erhalten entsprechend angepasste Ansichten. Das System ist stabil, klar strukturiert und bereit für die nächsten Ausbauschritte.

**Nächste Schritte**

Der Prototyp bietet bereits eine solide Grundlage, doch es stehen noch einige spannende Verbesserungen an:

- **“Meine Startups” für Gründer**  
  Gründer sollen bald direkt in ihrem Account alle von ihnen angelegten Startups auf einen Blick sehen. So ersparen sie sich den Umweg über die öffentliche Übersicht und können mit einem Klick in die Detailansicht wechseln.

- **Investoren-Übersicht im Admin-Bereich**  
  Für Administratoren wird es einen neuen Bereich geben, in dem alle registrierten Investoren samt Kontaktdaten und KI-Rating übersichtlich gelistet sind. Das erleichtert Monitoring und Compliance und hilft bei der Pflege unserer Investorendatenbank.

- **Profilbearbeitung**  
  Der „Edit Profile“-Button auf der Account-Seite wird noch zum Leben erweckt: Nutzer können künftig ihren Namen, Spitznamen und ihre E-Mail-Adresse direkt im Frontend anpassen und Änderungen sofort sehen.

- **Status der Finanzierungsrunden direkt in der Übersicht**  
  Investoren müssen nicht mehr erst in jede Einzelansicht springen, um zu prüfen, ob eine Runde offen oder bereits geschlossen ist. Ein Badge auf der Startup-Karte zeigt künftig den aktuellen Status und das Datum an.

- **“Meine Investoren” für Gründer**  
  Ähnlich zur Startup-Übersicht entsteht ein Bereich, in dem Gründer sehen, wer bereits in ihr Projekt investiert hat. Kontaktinformationen und Profillinks ermöglichen den direkten Austausch – ganz ohne Einblicke in vertrauliche Beträge.

- **“Follow” & Benachrichtigungen für Investoren**  
  Investoren können zukünftig Startups “folgen” und erhalten automatisch Benachrichtigungen, wenn neue Runden starten, schliessen oder wichtige Meilensteine erreicht werden. Ein kleines Glöckchen-Icon im Header zeigt dabei die ungelesenen Updates an.

Mit diesen Features machen wir unsere Plattform noch nutzerfreundlicher und schaffen klare, schnelle Wege zu den wichtigsten Informationen – sowohl für Gründer als auch Investoren.
