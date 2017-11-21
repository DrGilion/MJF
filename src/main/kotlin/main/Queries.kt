package main

object Queries {
	val selectQueries = mapOf(
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
    ?t  rdfs:subClassOf mjf:food .
    ?food a ?t ;
    foaf:name ?foodName .
}
ORDER BY ?foodName """,

			"Refrigerator Contents" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
SELECT DISTINCT ?foodName
WHERE {
	mjf:fridge  mjf:hasFood ?f .
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
SELECT ?foodName (sum(?calories) as ?totalCalories)
WHERE {
	?food foaf:name "margharita" ;
	      mjf:contains ?ingredient ;
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
}"""
	)

	val askQueries = mapOf(
			"Does Margharita pizza contain basil?" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
ASK {
	?pizza foaf:name "margharita" .
	FILTER EXISTS { ?pizza mjf:contains mjf:basil } .
}""",

			"Is something in fridge ?" to """
PREFIX foaf: <http://xmlns.com/foaf/0.1/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX mjf: <http://michajonasfood.com/>
ASK {
	mjf:fridge mjf:hasFood ?x
}"""
	)
}