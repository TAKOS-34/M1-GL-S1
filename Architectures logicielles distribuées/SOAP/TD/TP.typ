= WSDL

1. 5 grand types
    - `types` : Types des données, type simple ou objet
    - `message` : Encapsule les données pour une requête / réponse, définit les paramètres et le type de sortie
    - `portType` : L’API, chaque opération associé à entrée sortie erreur
    - `binding` : Lie le portType à un protocole de communication (ici SOAP HTTP)
    - `service` : Définie les endpoints, associe le binding à une URL
2. On définie un `simpleType` avec une énumartion de type toléré et on force un paramètre d’entrée obligatoire avec `type`
3. Structure de réponse modifié avec `type=tns:Date` et on définie le type Date dans `types`

= SOAP

1. 2 grandes partie
    - Entete HTTP
    - Requete et Reponse