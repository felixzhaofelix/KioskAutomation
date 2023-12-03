# KioskAutomation: 
## Une solution automatisée de système à base de règles pour l'entretien et la réparation des kiosques d'impression


Voici le code pour télécharger le projet:
```bash
git clone -b master https://github.com/felixzhaofelix/kiosk-automation.git

```
### Introduction

Ce projet est une solution automatisée de système à base de règles pour l'entretien et la réparation des kiosques d'impression.
Voici une structure globale du fonctionnement de ce projet:

Étape 1: L'application commence avec la classe Main où le programme élicite l'utilisateur pour entrer un ou plusieurs scénarios,
chaque scénario décrivant une kiosque d'impression en précisant les valeurs pour les clés suivantes:
- "name": le nom de la kiosque (type String)
- "jam": l'état de bourrage de papier dans la kiosque (type boolean: true pour bourrage, false pour pas de bourrage)
- "paper": le niveau de papier dans la kiosque (type int: 0 pour niveau haut, 1 pour niveau bas et 2 pour niveau vide)
Par la suite, un objet de type Scenario est créé avec les valeurs entrées par l'utilisateur et est ajouté à la liste de scénarios.
Chaque scénario a aussi deux autres clés aux valeurs par défaut:
- "Warning": si le niveau de papier a besoin d'un remplissage, peu importe s'il y a un bourrage ou non (type boolean: true pour besoin de remplissage, false pour pas besoin de remplissage)
- "Available": si la kiosque est disponible pour l'impression (type boolean: true pour disponible, false pour indisponible)

Étape 2: L'objet RuleManager va lire le fichier des règles et créer une liste de règles. Chaque règle est représentée par un objet JSON et une fois lue, par un objet Rule.
Chaque règle contient les clés suivantes et elles sont regroupées en deux catégories: les clés de prémisses et les clés de conclusions

Les prémisses:

- "jam": l'état de bourrage de papier dans la kiosque (type boolean: true pour bourrage, false pour pas de bourrage)
- "paper": le niveau de papier dans la kiosque (type int: 0 pour niveau haut, 1 pour niveau bas et 2 pour niveau vide)

Les conclusions:

- "tech": si un technicien est requis pour réparer la kiosque (type boolean: true pour requis, false pour pas requis)
- "rep": si un représentant est requis pour remplir la kiosque (type boolean: true pour requis, false pour pas requis)
- "warning": si le niveau de papier a besoin d'un remplissage, peu importe s'il y a un bourrage ou non (type boolean: true pour besoin de remplissage, false pour pas besoin de remplissage)
- "available": si la kiosque est disponible pour l'impression (type boolean: true pour disponible, false pour indisponible)



Étape 3: L'objet Status et l'objet Resolver vont lire la liste de scénarios et la liste de règles, trouver la règle correspondante pour chaque scénario et faire le traitement nécessaire pour chaque scénario.

Voici un exemple de correspondance entre les scénarios et les règles:

```
Scenario:
{
    "name": "Kiosk 1",
    "jam": true,
    "paper": 0
}

Rule:
{
    "jam": true,
    "paper": 0,
    "tech": true,
    "rep": false,
    "warning": false,
    "available": false
}
```

Correspondance:
```

/Scenario
|                           Les prémisses:                                    Les conclusions:
|                                 ↑                                                  ↑
|                   |------------------------| |-------------------------------------------------------------|    
|                   |                        | |                                                             |
↓                   ↓                        ↓ ↓                                                             ↓
{ "name": "Kiosk 1", "jam": true, "paper": 0, "warning": ---- , "available": ----                              } 
                    |  ↑                 ↑   |       ↑                 ↑           |                         |                                                           
                    |    les conditions      |            les états                |        les actions      |                            
                    |  ↓                 ↓   |       ↓                 ↓           |    ↓             ↓      | 
{                    "jam": true, "paper": 0, "warning": false, "available": false, "tech": true, "rep": false }
↑
|
|
|
|
\Rule:
```

```
----------------↑↓↘↙→←
```

Une fois la règle trouvée, l'objet Status va présenter la situation initiale des scénarios et les agents disponibles.

L'objet Resolver va faire le traitement nécessaire pour chaque scénario pour exécuter les actions comme dictées par les règles.
(Les détails de l'implémentation sont expliqués dans la section Resolver)


### Implémentation

#### RuleManager

La classe RuleManager est responsable de lire et écrire les règles dans le fichier JSON.
Ses principales méthodes sont les suivantes:

```java

public class RuleManager {
    /**
     * Serializes a list of Rule objects to JSON and saves it to a file.
     *
     * @param rules    List of Rule objects to be serialized.
     * @param filename Name of the file to save the JSON data.
     */
    //celle-ci convertit une liste de règles en un objet JSON et l'enregistre dans un fichier
    public static void dumpRulesToJson(List<Rule> rules, String filename) {
        JSONArray jsonArray = convertRulesToJsonArray(rules);

        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(jsonArray.toString(2));
            System.out.println("Rules serialized and saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads a JSON file and converts its content into a list of Rule objects.
     *
     * @param filename Name of the JSON file to read.
     * @return List of Rule objects parsed from the JSON file.
     */
    //celle-ci lit un fichier JSON et convertit son contenu en une liste d'objets de règle
    public static List<Rule> loadRulesFromJson(String filename) {
        List<Rule> rules = new ArrayList<>();
        List<JSONObject> jsonRules = readJsonFile(filename);

        for (JSONObject jsonRule : jsonRules) {
            rules.add(new Rule(jsonRule));
        }
        return rules;
    }
}
```
#### Status

La classe Status est responsable de présenter la situation initiale des scénarios et les agents disponibles.
Les attributs et les méthodes de cette classe sont statiques, ce qui permet de créer un seul objet de cette classe et de l'utiliser partout dans le programme.
Ses principales méthodes sont les suivantes:

```java
public class Status {
    /**
     * Displays the details of each scenario.
     *
     * @param scenarios List of Scenario objects to be displayed.
     */
    //celle-ci affiche les détails de chaque scénario que l'utilisateur a entré
    public static void showScenarios(List<Scenario> scenarios) {
        for (Scenario scenario : scenarios) {
            System.out.println("Kiosk " + (scenarios.indexOf(scenario) + 1) + " " + scenario);
        }
        System.out.println("Agent 1: Technician");
        System.out.println("Agent 2: Representative");
    }

    /**
     * Assess the statuses of all scenarios based on the provided rules.
     */
    //celle-ci évalue invoque la méthode assessStatus pour chaque scénario donné
    public static void assessStatuses() {
        for (Scenario scenario : scenarios) {
            assessStatus(scenario);
        }
        System.out.println("1 technician and 1 representative are available");
    }

    /**
     * Assess the status of a specific scenario based on the provided rules.
     *
     * @param scenario Scenario object to be assessed.
     */
    //celle-ci évalue l'état d'un scénario spécifique en fonction des règles fournies
    private static void assessStatus(Scenario scenario) {
        // this part is to determine the statuses using the rules
        for (Rule rule : rules) {
            if (rule.matches(scenario)) {
                if (!rule.isAvailable()) { // Rule 1 or 2 or 3 or 4
                    if (!rule.isTech()) { // Rule 4
                        status = ("Kiosk " + scenario.getName() + " is unavailable because out of paper");
                    }
                    status = ("Kiosk " + scenario.getName() + " is unavailable because of paper jam"); // Rule 1 or 2 or 3
                    break;
                } else if (rule.isWarning()) { // Rule 5
                    status = ("Kiosk " + scenario.getName() + " is available because no jam and low paper");
                    break;
                } else { // Rule 6
                    status = ("Kiosk " + scenario.getName() + " is available");
                    break;
                }
            }
        }
        printStatus();
    }
}
```
#### Solution

Cette classe contient une liste des étapes à exécuter pour résoudre le problème en un objet de type List<String>.
Chaque étape simule une action à exécuter pour résoudre le problème, sa fonction addStep() permet au Resolver d'ajouter une étape à la liste
et sa fonction printSolution() permet d'afficher la solution finale.
Elle contient aussi une fonction addDelay() qui ajoute un délai de 2 secondes après chaque réparation ou remplissage
pour simuler le temps nécessaire pour exécuter ces actions.
Ses principales méthodes sont les suivantes:

```java
public class Solution{

    /**
     * Adds a step to the solution.
     *
     * @param step Step to be added to the solution.
     */
    //celle-ci ajoute une étape à la solution, invoquée par la classe Resolver
    public void addStep(String step) {
        steps.add(step);
    }

    /**
     * Prints the solution.
     */
    //celle-ci affiche la solution finale
    //si l'étape est "loading", elle ajoute un délai de 2 secondes mais n'affiche pas "loading"
    public void printSolution() {
        for (String step : steps) {
            if (step.equals("loading")) {
                addDelay();//add delay but dont print "loading"
                continue;
            }
            System.out.println(step);
        }
    }

    //celle-ci ajoute un délai de 2 secondes après chaque réparation ou remplissage
    private void addDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```

#### Resolver

Voici le coeur de cette construction. La classe Resolver est responsable de faire le traitement nécessaire pour 
chaque scénario pour exécuter les actions comme dictées par les règles, les clés "tech" et "rep" donnent les actions à exécuter
et les clés "warning" et "available" donnent les états des scénarios.
Cette classe contient une méthode resolve() qui prend en paramètre un objet de type Scenario et 
retourne un objet de type Solution qui contient la liste des étapes à exécuter pour résoudre le problème.

La méthode resolve() fonctionne avec un principe de transformation des règles, c'est-à-dire chaque action à exécuter sur un scénario correspondant à une règle va le transformer en un scénario correspondant à une autre règle.

Pour ce faire, les règles envisagent d'abord toutes les possibilités de scénarios:
```
Rule 1: la kiosque a un bourrage de papier et le niveau de papier est vide
Rule 2: la kiosque a un bourrage de papier et le niveau de papier est bas
Rule 3: la kiosque a un bourrage de papier et le niveau de papier est haut
Rule 4: la kiosque n'a pas de bourrage de papier et le niveau de papier est vide
Rule 5: la kiosque n'a pas de bourrage de papier et le niveau de papier est bas
Rule 6: la kiosque n'a pas de bourrage de papier et le niveau de papier est haut
```

Chaque scénario doit passer par une boucle do-while en se correspondant avec une règle et recevoir une intervention de la part 
d'un technicien ou d'un représentant pour être réparé ou rempli.
À chaque passage dans la boucle, le scénario évolue avec l'aide du Resolver en un autre scénario nécessitant moins d'intervention.
Ce processus se répète jusqu'à ce que le scénario corresponde à la règle 6, ce qui signifie qu'il n'a plus besoin d'intervention.
Chaque étape de la transformation est aussi ajoutée à un objet Solution qui contient la liste des étapes à exécuter pour résoudre le problème.
Voici l'ordre d'évolution des scénarios (où Sn correspond au scénario n entre dans la boucle avec l'application de la règle n):`

Voici un schéma de la transformation des scénarios en état final:
```
S1 -----→ Entrée dans la boucle par correspondance avec R1 -----→ Intervention d'un technicien -----→ S4
S2 -----→ Entrée dans la boucle par correspondance avec R2 -----→ Intervention d'un technicien -----→ S5
S3 -----→ Entrée dans la boucle par correspondance avec R3 -----→ Intervention d'un technicien -----→ S6
S4 -----→ Entrée dans la boucle par correspondance avec R4 -----→ Intervention d'un représentant -----→ S6
S5 -----→ Entrée dans la boucle par correspondance avec R5 -----→ Intervention d'un représentant -----→ S6
S6 -----→ Entrée dans la boucle par correspondance avec R6 -----→ Sortie de la boucle
``` 
En même temps de réparer les kiosques (changer la valeur des clés "jam" et "paper"), le Resolver va aussi changer la valeur des clés "warning" et "available" pour chaque scénario.
Au début, la valeur de la clé "warning" est par défaut true et la valeur de la clé "available" est par défaut false.
Après chaque remplissage ou réparation, la valeur de la clé "warning" devient false et la valeur de la clé "available" devient true respectivement.
La boucle do-while se termine lorsque la valeur de la clé "available" devient true et que la valeur de la clé "warning" devient false.

En plus de la méthode resolve(), la classe Resolver contient aussi une méthode resolveAll() qui prend en paramètre une liste de scénarios et retourne une liste de solutions.
En même temps, elle invoque la méthode Collection.shuffle() pour mélanger la liste de scénarios afin de simuler le fait qu'aucune kiosque n'a de priorité sur une autre et le fait que les agents ne sont pas affectés à une kiosque spécifique.


Voici les principales méthodes de la classe Resolver:

```java
public class Resolver{
/**
 * Resolves a scenario based on the provided rules.
 *
 * @param scenario Scenario object to be resolved.
 * @return Solution object containing the steps to resolve the scenario.
 */
public Solution resolve(Scenario scenario) {
Solution solution = new Solution();
//DO WHILE

do {
    for (Rule rule : rules) {
        if (rule.matches(scenario)) {
            if (rule.isTech()) { //Rule 1 or 2 or 3
                solution.addStep("Kiosk " + scenario.getName() + " is unavailable because of paper jam");
                solution.addStep("loading");
                solution.addStep("Agent 1 Technician has removed the jam from Kiosk " + scenario.getName());
                scenario.setJam(false); //remove jam Rule 123 become Rule 456
                break;
            } else if (rule.isRep() && rule.isWarning() && !rule.isAvailable()) { //Rule 4
                solution.addStep("Kiosk " + scenario.getName() + " is unavailable because out of paper");
                solution.addStep("loading");
                solution.addStep("Agent 2 Representative has added a new roll to Kiosk " + scenario.getName());
                scenario.setPaper(0); //set paper to high(0), Rule 4 become Rule 6
                scenario.setWarning(false); //remove warning
                break;
            } else if (rule.isRep() && rule.isWarning() && rule.isAvailable()) { //Rule 5
                solution.addStep("Kiosk " + scenario.getName() + " is available because no jam and low paper");
                solution.addStep("loading");
                solution.addStep("Agent 2 Representative has added a new roll to Kiosk " + scenario.getName());
                scenario.setPaper(0); //set paper to high(0), Rule 5 become Rule 6
                scenario.setWarning(false); //remove warning
                break;
            } else { //Rule 6
                solution.addStep("Kiosk " + scenario.getName() + " is available and has high paper level");
                rule.applies(scenario); //remove warning and set kiosk to available
                break;
            }
        }
    }


} while (!scenario.isAvailable() || scenario.isWarning());

return solution;
}

/**
 * Resolves all scenarios based on the provided rules.
 *
 * @param scenarios List of Scenario objects to be resolved.
 * @return List of Solution objects containing the steps to resolve the scenarios in an random order.
 */
public List<Solution> resolveAll(List<Scenario> scenarios) {
    List<Solution> solutions = new ArrayList<>();
    Collections.shuffle(scenarios);

    for (Scenario scenario : scenarios) {
        solutions.add(resolve(scenario));
    }
    return solutions;
}
    
}

```

#### Main

La classe Main est responsable de l'interaction avec l'utilisateur et de l'exécution du programme.
Sa méthode main() contient:

- L'initialisation des variables et des objets nécessaires pour le fonctionnement du programme
- L'interaction avec l'utilisateur pour entrer le fichier des règles
- L'interaction avec l'utilisateur pour entrer un ou plusieurs scénarios
- L'affichage de la situation initiale des scénarios et des agents disponibles
- L'affichage de la solution finale pour chaque scénario

### Conclusion:

Ce projet est une solution automatisée de système à base de règles pour l'entretien et la réparation des kiosques d'impression,
il est capable de lire un fichier de règles et de les appliquer à un ou plusieurs scénarios pour trouver la solution finale,
sa construction a pour but d'extraire au maximum la logique d'expertise du code et de la confiner dans une base de règles, afin 
de rendre le système plus facile à maintenir et à étendre. Les experts dans le domaine de la maintenance des kiosques d'impression 
peuvent maintenant modifier les règles dans le fichier JSON pour adapter le système à leurs besoins.

Cependant, ce projet reste toujours très limités, il se base dans le contexte restraint où il n'y a que six possibilités de scénarios.
De plus, la classe Resolver contient encore des conditions if-else qui sont difficiles à maintenir et à étendre, ce qui veut dire que, 
même avec une nouvelle règle ajoutée, il faut toujours modifier le code de la classe Resolver pour l'adapter à la nouvelle règle.
Pour résoudre ces problèmes, il faut utiliser une structure de données plus complexe pour stocker les règles et les scénarios,
et utiliser des algorithmes plus avancés pour trouver la solution finale, par contre, nous n'avons que six possibilités à considérer pour
le moment, ce qui limite notre imagination pour trouver une solution avec une évolutivité plus grande.

