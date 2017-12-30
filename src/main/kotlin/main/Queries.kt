package main

object Queries {
	val queries = mapOf(
			"All Food Types" to """
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT DISTINCT ?x
WHERE {
    ?x  rdfs:subClassOf mjf:food .
}""",

			"All Food" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT DISTINCT ?foodName
WHERE {
    {
        ?t rdfs:subClassOf mjf:food .
        ?food a ?t ;
              foaf:name ?foodName .
    } UNION {
        ?food a mjf:food ;
             foaf:name ?foodName
    }
}
ORDER BY ?foodName """,

			"Refrigerator Contents" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT ?fridgeName ?foodName
WHERE {
    ?fridge a mjf:fridge ;
        foaf:name ?fridgeName ;
        mjf:hasFood ?f .
    ?f foaf:name ?foodName .
}""",

			"Food with Calories" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT DISTINCT ?foodName ?calories
WHERE {
    ?t  rdfs:subClassOf mjf:food .
    ?food a ?t ;
          foaf:name ?foodName .
    OPTIONAL { ?food mjf:caloriesPer100g ?calories }
}
ORDER BY DESC (?calories)""",

			"Calories of Margharita Pizza" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT ?foodName (sum(?calories * ?quantity / 100.0) as ?totalCalories)
WHERE {
    ?food foaf:name "margharita" ;
          mjf:contains [ mjf:foodItem ?ingredient ; mjf:quantity ?quantity ] ;
          foaf:name ?foodName .
    ?ingredient mjf:caloriesPer100g ?calories .
}
GROUP BY ?foodName""",

			"Food Without Calories Information" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT DISTINCT ?foodName
WHERE {
    ?foodType rdfs:subClassOf mjf:food .
    ?food a ?foodType ;
          foaf:name ?foodName .
    FILTER NOT EXISTS { ?food mjf:caloriesPer100g ?calories }
}""",

			"Recipes with Ingredients" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT ?dishName ?foodName ?quantity ?UnitOfMeasurement
WHERE {
    ?dish a mjf:dish ;
          foaf:name ?dishName ;
          mjf:contains [ 
              mjf:quantity ?quantity ;
              mjf:UOM ?UnitOfMeasurement ;
              mjf:foodItem [ foaf:name ?foodName ]
          ] .
}""",

			"Does Margharita pizza contain basil?" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
ASK {
    ?pizza foaf:name "margharita" .
    FILTER EXISTS { ?pizza mjf:contains mjf:basil } .
}""",

			"Is something in Michaels fridge ?" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
ASK {
    mjf:MichasFridge mjf:hasFood ?x
}""",

			"Is bitter a taste?" to """
PREFIX mjf: <http://michajonasfood.com/>
ASK {
    mjf:bitter a mjf:taste .
}""",

			"Is mayonaise an instrument?" to """
PREFIX dbr: <http://dbpedia.org/resource/>
PREFIX mjf: <http://michajonasfood.com/>
ASK {
    dbr:Mayonnaise a dbr:Musical_instrument .
}""",

			"How does a dish taste ?" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
CONSTRUCT { ?dish mjf:couldTaste ?taste . }
WHERE {
    ?dish mjf:contains [ mjf:foodItem ?food ] .
    OPTIONAL { ?food mjf:tastes ?taste }
} ORDER BY ?dish""",

			"All Fridges From OWL" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>

SELECT ?f
WHERE {
	?x rdfs:subClassOf mjf:Fridges .
	?f a ?x .
}
""",

			"All unhealthy foods inferred from Jena" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT ?food ?calories
WHERE {
    ?x a mjf:unhealthyFood ;
       foaf:name ?food ;
       mjf:caloriesPer100g ?calories .
}""",

			"Foods which are Vegetable and Meat" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT ?food
WHERE {
    ?food a mjf:meat ;
       a mjf:vegetable .
}""",

			"Find Sandwiches" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT ?food
WHERE {
    ?food a mjf:sandwich .
}"""

	)
}
