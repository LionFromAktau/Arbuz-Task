package com.example.arbuztask.data

class RandomGenerator {
    private val set = HashSet<String>()

    fun getRandomPrice(): Int = (100 .. 1000).random()

    fun getRandomDrinkName(): String{
        val drinkNames = listOf(
            "Tonic Water",
            "Beer",
            "Wine",
            "Champagne",
            "Whiskey",
            "Vodka",
            "Rum",
            "Gin",
            "Tequila",
            "Brandy",
            "Cognac",
            "Margarita",
            "Martini",
            "Mojito",
            "Pina Colada",
            "Bloody Mary",
            "Manhattan",
            "Old Fashioned",
            "Mint Julep",
            "Sangria"
        )
        var drink = drinkNames.random()
        while(set.contains(drink)){
            drink = drinkNames.random()
        }
        return drink
    }

    fun getRandomFoodName(): String{
        val foodNames = listOf(
            "Pizza",
            "Burger",
            "Sushi",
            "Pasta",
            "Salad",
            "Tacos",
            "Steak",
            "Sandwich",
            "Soup",
            "Cake",
            "Fried Chicken",
            "Fish and Chips",
            "Ramen",
            "Dumplings",
            "Curry",
            "Burrito",
            "Donuts",
            "Pancakes"
        )
        var food = foodNames.random()
        while(set.contains(food)){
            food = foodNames.random()
        }
        return food
    }


}