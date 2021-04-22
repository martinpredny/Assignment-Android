# ANDROID APP ASSIGNMENT #

This assignment should just helps us to evaluate level of your skills. You don't have to solve everything perfectly to be hired.

### What is this assignment NOT about? ###

* It's not about deep knowledge of some specific technologies 
* It's not about showing us that you know how to glue dozens of libraries together
* It's not about creating super-duper, modular, multi-layer architecture
* It's not about dependency injection

### What is this assignment about? ###

* It's about solving few simple problems that often occurs in real apps
* It's about using simple solutions without many libraries and overengineering
* It's about understanding android lifecycles (activity, process, viewmodel, recycler...)
* It's about asynchronous loading of data and showing reasonable state on UI

### Handover ###

* Create a private fork of this repository
* Assign admin rights to jozef.knazko@goodrequest.com
* Create develop branch
* Do all 3 task in order after each other in 3 separated commits
* Set jozef.knazko@goodrequest.com as reviewer

### Project ###

You will work with prepared project. It's very small, around 150 lines of code and you should have no problem to read and understand it all.
We picked technologies recommended by google to make this assignment accessible to most developers.

* Kotlin
* ViewBinding
* LiveData
* ViewModel
* Coroutines (only most basic usage)

Application loads and shows a list of cat images. You will be fixing some issues and adding new features.

There are only 4 files:

* common.kt - these are some general utils and you probably won't have to touch this code. There are already prepared 2 http requests and helper function to creating ViewModel with SavedStateHandle.
* other 3 files - contains Activity, ViewModel and RecyclerView.Adapter

## Task 1: ##

Your colleague wrote these 4 files but then mysteriously disappeared and you have to finish it. You have to check if everything works as expected and possibly fix it. There is no catch, everything is standard, user-friendly behavior.

* If we don't have any data yet and we are loading them there should be just a progressbar in the center of the screen
* If loading fails there should be error message and retry button
* Loaded data are shown in RecyclerView
* If we already have data, user can pull-to-refresh to refresh them. While refresh is running user can still see current data and refresh indicator
* If refresh fails user can still see current data and some error message should be shown in snackbar

App should correctly handle configuration changes (e.g. screen rotation)

* current state (loading, refreshing, error, ...) should be correctly preserved after configuration change
* RecyclerView scroll position should be preserved after configuration change
* there should no unnecessary http requests

Obviously app shouldn't crash, leak memory, get stuck and shouldn't show some nonsensical UI state.
Fix all the issues in the code. Try to make just minimal changes to existing code (try not to rewrite everything from scratch). But please, don't add any other libraries.   

## Task 2: ##

We are loading only 10 cat images in one request. Let's make this app more useful. Instead of hardcoded text we want to show some fun fact to every picture.
This request is already implemented in Api.getCatFact(). But it returns only 1 fact, so we have to call it 10 times to pair it with every cat image. 

* in every call of CatsModel.loadCats() load not only images but also unique fun fact for every image 
* if getCatImages fails keep the same behaviour as in Task 1 
* on UI show in every item fun fact text if request succeeded or "no fact" if particular request failed 

## Task 3: ##

10 cat images is not enough! Let's add paging. When user scrolls to the bottom of the list we need to automatically load next data. For example: we have initial 10 items, user scrolls to the bottom and we load next 10. So now we have 20 items in recycler and so on.
Don't use Paging library from google or any other library. This task is not specifically about paging. It should just demonstrate how you can handle a little more complicated state machine with combination of crazy android lifecycles. You will probably have to make bigger changes to Activity and ViewModel. Focus on correctness, readability, and try to make it foolproof.    

* all requirements from Task 1 and 2 should still work the same
* for every new page you also have to load 10 new fun facts
* if you already have let's say 50 images and now user does pull-to-refresh then loading and failure behavior is same as in Task 1. But if refresh succeeds drop those previous 50 images and keep only these 10 new ones
* while loading of page is running add new item at the end of recycler. This item will show only progressbar.
* when loading of page fails show new item at the end of recycler. This item will contain retry button that tries to load this failed page one more time

When everything is done our UI should handle 3 different loading states and 3 different error states (first page/any other page/refresh)
