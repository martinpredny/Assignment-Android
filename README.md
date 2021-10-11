# ANDROID APP ASSIGNMENT #

This assignment just helps us to evaluate level of your skills. You don't have to solve everything perfectly to be hired.

### What is this assignment NOT about? ###

* It's not about deep knowledge of some specific technologies 
* It's not about showing us that you know how to glue dozens of libraries together
* It's not about creating super-duper, modular, multi-layer architecture
* It's not about dependency injection

### What is this assignment about? ###

* It's about solving few simple problems that often occurs in real apps
* It's about using simple solutions without overengineering
* It's about understanding of android lifecycles (activity, process, viewmodel, recycler...)
* It's about asynchronous loading of data and representing them in reasonable way on UI

### Handover ###

* Create a private fork of this repository
* Assign admin rights to jozef.knazko@goodrequest.com
* Create develop branch
* Do all 3 task in order after each other in 3 separated commits
* Share repository to jozef.knazko@goodrequest.com and veronika.stefanatna@goodrequest.com

### Project ###

You will work with prepared project. It's very small and you should have no problem to read and understand it all.
We picked technologies recommended by google to make this assignment accessible to most developers.

* Kotlin
* ViewBinding
* LiveData
* ViewModel
* Coroutines (only most basic usage)

Application is loading a list of Pokemons. You will be fixing some issues and adding new features.

There are only 4 files:

* api.kt - implementation of 2 api request. You probably won't have to touch this code.
* other 3 files - contains Activity, ViewModel and RecyclerView.Adapter

## Task 1: ##

Your colleague wrote these 4 files but then mysteriously disappeared and you inherited his code. At first, you have to check if everything works as expected and possibly fix it. Expected behavior:

* If we don't have any data yet and we are loading them there should be just a progressbar in the center of the screen
* If loading fails there should be an error message and retry button
* Successfully loaded data are shown in RecyclerView
* If we already have data, user can pull-to-refresh to refresh them. While refresh is running user can still see current data and refresh indicator
* If refresh fails, user can still see current data but except of that some error message should be shown in snackbar

App should correctly handle configuration changes (e.g. screen rotation) and process death.

* current state (loading, refreshing, error, ...) should be correctly preserved after configuration change
* RecyclerView scroll position should be preserved after configuration change
* there should no unnecessary http requests

Obviously, app shouldn't crash, leak memory, get stuck and shouldn't show some nonsensical UI state.
Find and fix all the issues in the code. Try to make just minimal changes to existing code (try not to rewrite everything from scratch). But please, don't add any other libraries for this task.

## Task 2: ##

We are loading only 20 pokemon names in one request. Let's make this app more useful. We want to show more info about every pokemon (image, weight and name of his special move).
This data can be retrieved with already implemented function Api.getPokemonDetail(). It returns details of a single pokemon, so we have to call it for every pokemon in our list.

* in every call of PokemonViewModel.load() load list of Pokemons but also load detail for every one of them
* show image, weight and move for every pokemon in recycler view
* if some getPokemonDetail() call fails show just placeholder image in this item


## Task 3: ##

20 Pokemons is not enough! Let's add some paging. When user scrolls to the bottom of the list we need to automatically load next data. For example: we have initial 20 items, user scrolls to the bottom and we load next 20. Now we have 40 items in recycler and so on.
Don't use Paging library from google or any other library. This task is not specifically about paging. It should just demonstrate how you can handle a little more complicated state machine with combination of crazy android lifecycles. You will probably have to make bigger changes to the project. Focus on correctness, readability, and simplicity.

* all requirements from Task 1 and 2 should still work the same
* for every new page you also have to load 20 new Pokemon details
* if you already have let's say 2 pages (40 details) and now user does pull-to-refresh then loading and failure behavior is same as in Task 1. But if refresh succeeds then drop all previous data and keep only these 20 new ones
* while loading of page is running add new item at the end of recycler. This item will show only progressbar.
* when loading of page fails show new item at the end of recycler. This item will contain retry button that tries to load this failed page one more time

When everything is done our UI should handle 3 different loading states and 3 different error states (first page / any other page / refresh)
