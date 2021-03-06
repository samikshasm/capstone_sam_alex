# capstone_sam_alex

README Week 3

We got a lot of things accomplished this week. First is the ability to login to the app by searching through Firebase's authentication database. We created two fake users (Alex and myself) and tested to see if the app authenticates correctly - which it does. In terms of the Login screen, the next step that we are working towards is to make sure that the app keeps the local user signed in so that they do not have to log in to the app each time (that would be extremely inconvinient). 

The next feature we accomplished was the ability to write to the Firebase Databse. Firebase uses a non-SQL database and stores data in the form of a JSON tree. There are benefits and challenges to this. The biggest challenge we identified is that we would have to organize the data collection with a high level of granularity. The first step towards this was the ability to organize based on specific user. Every time a new user is created, Firebase assigns that user a unique UserID. We worked on getting that userID into the app and used that as the  nodes of the JSON tree. After multiple attempts, we were able to successfully retrieve and write to the databse.

Now that we were able to create a node in the tree, the next step to the organization was the timestamps. We formatted the current time so that the date and the time would populate as a child node of the userID node. Once this was accomplished, we worked on gathering the "last known location" or the current location of the user associated with the specific user ID. We configured Google Play Services in our app and were able to get the longitude and latitude of the user. Currently, this is displayed in a text field in the MainActivity of the app but we will populate the location to the database soon. This code hasn't been commited yet, but will be at the end of the day today.

This weekend and through the next week before our presentation, the first goal is to put together all the necessary requirements for the presentation. Once we have that ready, we will continue working on the app by first perfecting the Login screen and populating the database every X minutes with the time and the location of the user. 

