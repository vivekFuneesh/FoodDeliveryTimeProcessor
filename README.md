# FoodDeliveryTimeProcessor
A time-calculator/processing API for food delivery under GPL v3 License.

For a given restaurant, input will come in format of JSON as :: <br>
[ orderID, listOfMeals, DistanceFromRest ] <br>

and Response will be :: <br>
[ orderId, message, isConfirmed ] <br>

A sample postman collection is also included, just import this file named as FoodDeliveryTimeProcessor.postman_collection.json inside postman and run the project in eclipse and see the o/p by sending the Post request from postman. <br>

This is just a sample service for calculating feasibility of food delivery. This is non-customized, hard-coded options.. can be customized & user-choiced by using Enums and constants via application.properties.. depends upon mood of developer ;) 
<br> <br>
ENJOY!!
