= Définitions

-  Un système distribué est un système formé de composants logiciels localisés sur des ordinateurs en réseau qui communiquent et coordonnent leurs actions uniquement par l’envoi de messages, le tout en vue de répondre à un besoin donné

-  Une architecture est un ensemble de composants logiciels qui interagissent. Chaque composant offre un service

- Avantage : 
    - Homogénéité : (compatibilité des données)
    - Manipulation réseau

= Architecture d’un intergiciel

#figure(
    image("images/f1.png")
)

1. Proxy : Intermédiaire qui transfert la requête sans que le client ai à coder en dur et connaisse les protocoles de communications
2. Wrapper : Rajoute / enlève des informations dans la requete, soit pour etre conforme aux normes soit pour rajouter des informations (ex : date)
3. Factory : Créer dynamiquement des instances multiples d’une classe d’objets, c’est une interface génériques pour la créations d’objets
4. Intercepteur : Controle la requête, peut la rediriger, la stoper ou ajouter de nouvelles fonctions

= Remote Procdure Call | RPC

#figure(
    image("images/f2.png", width: 50%)
)

= Java RMI

RMI fournis :

- Un générateur de stub
- Service de noms (Object Registry) : transmission d’objet par copie ou par référence

Implémentation d’interface Distante :

```java
// Code client / serveur
public interface Hello extends Remote {
	String sayHello() throws RemoteException;
}

// Code serveur
public class Server implements Hello {
	public Server() {}
	public String sayHello() {
		return "Hello, world!";
	}
}
```

Code serveur d’objets distants :

```java
// 1ère étape : Le main() doit créer l'objet

// 2ème étape : exporter l'objet distant
Server obj = new Server();
Hello stub = (Hello)UnicastRemoteObject.exportObject(obj, 0) // Permet de recevoir les appels distants

// 3ème étape : enrigistrer l'objet dans le registe Java RMI
Registry registry = LocateRegistry.getRegistry();
registry.bind("Hello", stub);
```

Code client :

```java
public class Client {
	private Client() {}
	public static void main(String[] args) {
		String host = (args.length < 1) ? null : args[0];
		try {
			Registry registry = LocateRegistry.getRegistry(host);
			Hello stub = (Hello)registry.lookup("Hello");
			String response = stub.sayHello();
			System.out.println("Response: " + response);
		} catch (Exception e) {
			System.err.println("Exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
```

= SOAP

Services web : soit permettre la communication et l'échange de données entre applications et systèmes hétérogènes dans des environnements distribués en utilisant HTTP

Synchrone : Attente d’une réponse direct, bloquant

Asynchrone : Peut continuer d’executer du code après l’appel, non bloquant

3 types d’appels :

=== Call

```xml
<!-- call -->
POST /StockQuote HTTP/1.1
Host: www.stockquoteserver.com
Content-Type: text/xml
Content-Length: nnnn
SOAPMethodName: Some-Namespace-URI#GetLastTradePrice

<SOAP:Envelope xmlns:SOAP="urn:schemas-xmlsoap-
org:soap.v1”>
	<SOAP:Body>
		<m:GetLastTradePrice
		xmlns:m="Some-Namespace-URI”>
			<symbol>DIS</symbol>
			</m:GetLastTradePrice>
	</SOAP:Body>
</SOAP:Envelope>
```

=== Response

```xml
<!-- response -->
HTTP/1.1 200 OK
Content-Type: text/xml
Content-Length: nnnn

<SOAP:Envelope xmlns:SOAP="urn:schemas-xmlsoap-
org:soap.v1”>
	<SOAP:Body>
	<m:GetLastTradePriceResponse
	xmlns:m="Some-Namespace-URI”>
		<return>34.5</return>
		</m:GetLastTradePriceResponse>
	</SOAP:Body>
</SOAP:Envelope>
```

=== Fault

```xml
<!-- fault -->
	<SOAP:Envelope xmlns:SOAP="urn:schemas-xmlsoap-org:soap.v1>
	<SOAP:Body>
		<SOAP:Fault>
			<faultcode>200</faultcode>
			<faultstring> SOAP Must Understand Error
			</faultstring>
			<runcode>1</runcode>
		</SOAP:Fault>
	<SOAP:Body>
</SOAP:Envelope>
```

Encodage en type simple (string, int) mais aussi en type XML schema (<age>45</age>) mais hérité de type simple

#figure(
    image("images/f3.png")
)

= WSDL

Fichier XML descriptif d’un service SOAP

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<definitions name="FooSample"
    targetNamespace="http://tempuri.org/wsdl/"
    xmlns:wsdlns="http://tempuri.org/wsdl/"
    xmlns:typens="http://tempuri.org/xsd"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
    xmlns:stk="http://schemas.microsoft.com/soap-toolkit/wsdl-extension"
    xmlns="http://schemas.xmlsoap.org/wsdl/">

    <!-- ================== TYPES ================== -->
    <types>
        <!-- Définition des types XML utilisés par le service -->
        <schema targetNamespace="http://tempuri.org/xsd"
                xmlns="http://www.w3.org/2001/XMLSchema"
                xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/"
                xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                elementFormDefault="qualified">
        <!-- Pas de type personnalisé défini ici, juste un espace réservé -->
        </schema>
    </types>

    <!-- ================== MESSAGES ================== -->
    <!-- Message d'entrée pour l'opération "foo" -->
    <message name="Simple.foo">
        <part name="arg" type="xsd:int"/> <!-- Paramètre : un entier -->
    </message>

    <!-- Message de sortie pour l'opération "foo" -->
    <message name="Simple.fooResponse">
        <part name="result" type="xsd:int"/> <!-- Résultat : un entier -->
    </message>
    
    <!-- ================== PORT TYPE ================== -->
        <!-- Définition abstraite de l’interface (opérations disponibles) -->
        <portType name="SimplePortType">
        <operation name="foo" parameterOrder="arg">
            <input message="wsdlns:Simple.foo"/>
            <output message="wsdlns:Simple.fooResponse"/>
        </operation>
        </portType>

    <!-- ================== BINDING ================== -->
    <!-- Spécifie comment le service est lié à SOAP -->
    <binding name="SimpleBinding" type="wsdlns:SimplePortType">
        <!-- Extension spécifique Microsoft -->
        <stk:binding preferredEncoding="UTF-8" />
        
        <!-- Binding SOAP : style RPC, transport HTTP -->
        <soap:binding style="rpc"
                    transport="http://schemas.xmlsoap.org/soap/http"/>
        
        <!-- Définition de l’opération "foo" -->
        <operation name="foo">
        <!-- Action SOAP associée -->
        <soap:operation soapAction="http://tempuri.org/action/Simple.foo"/>
        
        <!-- Message d'entrée -->
        <input>
            <soap:body use="encoded"
                    namespace="http://tempuri.org/message/"
                    encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" />
        </input>
        
        <!-- Message de sortie -->
        <output>
            <soap:body use="encoded"
                    namespace="http://tempuri.org/message/"
                    encodingStyle="http://schemas.xmlsoap.org/soap/encoding/" />
        </output>
        </operation>
    </binding>

    <!-- ================== SERVICE ================== -->
    <!-- Définition du service exposé -->
    <service name="FOOSAMPLEService">
        <port name="SimplePort" binding="wsdlns:SimpleBinding">
        <!-- Adresse réseau du service -->
        <soap:address location="http://carlos:8080/FooSample/FooSample.asp"/>
        </port>
    </service>
</definitions>
```

= REST

Utilise le HTTP

Différent format de données possible : text, json, xml

API : contrat d’utilisation

Plusieurs méthodes : GET, POST, PUT, DELETE

=== Requête

```
POST /users HTTP/1.1
Host: api.exemple.com
Content-Type: application/json
Authorization: Bearer 123456789abcdef

{
    "username": "test",
    "email": "test@example.com",
}
```

Ressource accessible dans les url via variable ou paramètres :

```
Variables :
	/users/getNumbersOfLikes/{userId}
	/users/getNumbersOfLikes/1

Paramètres :
	/users/listMessages/{userId}?limit=10&year=2025
```

=== Message de retours

```json
200 OK                    # La requête a réussi
201 Created               # La ressource a été créée avec succès
204 No Content            # La requête a réussi mais il n’y a pas de contenu à renvoyer

301 Moved Permanently     # La ressource a été déplacée définitivement à une nouvelle URL
302 Found                 # La ressource a été trouvée à une autre URL (redirection temporaire)
304 Not Modified          # La ressource n’a pas changé depuis la dernière requête (cache)

400 Bad Request           # La requête est mal formée ou invalide
401 Unauthorized          # Authentification requise ou échouée
403 Forbidden             # Accès refusé, même avec authentification
404 Not Found             # La ressource demandée n’existe pas

500 Internal Server Error # Erreur côté serveur inattendue
```

=== HATEOAS

Documentation de l’API en faisant une requête API :

```json
{
    "id": 42,
    "title": "Apprendre REST",
    "author": "Test"
}

// Response :
{
    "id": 42,
    "title": "Apprendre REST",
    "author": "Test",
    "links": {
    "self": { "href": "/books/42" },
    "update": { "href": "/books/42", "method": "PUT" },
    "delete": { "href": "/books/42", "method": "DELETE" },
    "author": { "href": "/authors/takos" }
    }
}

```